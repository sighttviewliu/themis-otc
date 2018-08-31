package com.oxchains.themis.message.service;

import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.message.common.MessageConst;
import com.oxchains.themis.message.dao.MessageDao;
import com.oxchains.themis.message.dao.MessageTextDao;
import com.oxchains.themis.message.rest.dto.MessageDTO;
import com.oxchains.themis.message.rest.dto.PageDTO;
import com.oxchains.themis.message.rest.dto.UnReadSizeDTO;
import com.oxchains.themis.repo.dao.order.OrderRepo;
import com.oxchains.themis.repo.dao.user.UserDao;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.repo.entity.order.Order;
import com.oxchains.themis.repo.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author luoxuri
 * @create 2017-11-06 15:02
 **/
@Service
public class MessageService {

private final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    // 所有未读消息map
    private final Map<Long, Integer> countMap = new HashMap<>();
    // 公告未读消息map
    private final Map<Long, Integer> countNoticeMap = new HashMap<>();
    // 私信未读消息map
    private final Map<Long, Integer> countPrivateMap = new HashMap<>();
    // 系统未读消息map
    private final Map<Long, Integer> countGlobalMap = new HashMap<>();

    private final UnReadSizeDTO unReadSizeDTO = new UnReadSizeDTO();

    // 所有公告
    private final Set<Long> set = new HashSet<>();

    @Resource private MessageDao messageDao;
    @Resource private MessageTextDao messageTextDao;
    @Resource private OrderRepo orderDao;
    @Resource private UserDao userDao;

    @Value("${themis.user.default}") private String userDefaultImage;
    @Value("${themis.system.default}") private String systemDefaultImage;

    /**
     * 测试，快速添加数据
     * @param messageText
     * @return
     */
    @Deprecated
    public RestResp sendNoticeMessage(MessageText messageText){
        try {
            if (messageText.getMessage() == null){
                return RestResp.fail("请填写内容");
            }
            if (messageText.getUserGroup() == null){
                // 默认将公告发送给所有人
                messageText.setUserGroup(4L);
            }
            if (messageText.getOrderId() == null){
                messageText.setOrderId("");
            }

            // 保存messageText
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            messageText.setPostDate(currentTime);
            messageText.setSenderId(0L);
            messageText.setMessageType(MessageType.PUBLIC);
            messageText.setUserGroup(messageText.getUserGroup());
            MessageText mt = messageTextDao.save(messageText);

            return RestResp.success("操作成功", mt);
        }catch (Exception e){
            LOG.error("站内信：发送系统消息异常", e);
        }
        return RestResp.success("操作失败");
    }

    /**
     * 查询系统信息
     */
    public RestResp queryGlobalMsg(Long userId, Integer pageNum, Integer pageSize){
        return queryMessage(userId, pageNum, pageSize, MessageType.GLOBAL, true);
    }

    /**
     * 查询私信
     */
    public RestResp queryPrivateMsg(Long userId, Integer pageNum, Integer pageSize){
        return queryMessage(userId, pageNum, pageSize, MessageType.PRIVATE_LETTET, true);
    }

    /**
     * 查询公告信息
     */
    public RestResp queryNoticeMsg(Long userId, Integer pageNum, Integer pageSize){
        return queryMessage(userId, pageNum, pageSize, MessageType.PUBLIC, false);
    }

    public RestResp queryUnReadCount(Long userId, Integer tip){
        try {
            int count = 1;
            UnReadSizeDTO unReadSizeDTO = invoke(userId, tip, count);
            return RestResp.success("操作成功", unReadSizeDTO);
        } catch (Exception e){
            LOG.error("站内信：获取所以未读信息数量异常", e);
        }
        return RestResp.fail("操作失败");
    }

    private UnReadSizeDTO invoke(Long userId, Integer tip, int count) throws Exception {
        // 未读信息添加到message表中
        addUnReadMsg(userId);

        // 获取个种类未读消息数量
        Integer unReadNoticeSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PUBLIC);
        Integer unReadGolbalSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.GLOBAL);
        Integer unReadPrivateSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PRIVATE_LETTET);
        if (unReadNoticeSize == null){ unReadNoticeSize = 0; }
        if (unReadGolbalSize == null){ unReadGolbalSize = 0; }
        if (unReadPrivateSize == null){ unReadPrivateSize = 0; }
        Integer unReadAll = unReadNoticeSize + unReadGolbalSize + unReadPrivateSize;

        int cacheNoticeCount = countNoticeMap.getOrDefault(userId, 0);
        int cacheGolbalCount = countGlobalMap.getOrDefault(userId, 0);
        int cachePrivateCount = countPrivateMap.getOrDefault(userId, 0);

        // 第一次请求获取未读消息
        if (tip == 1){
            putMapAndSetValues(userId, unReadNoticeSize, unReadGolbalSize, unReadPrivateSize, unReadAll);
            return unReadSizeDTO;
        }

        while (unReadNoticeSize.equals(cacheNoticeCount) && unReadGolbalSize.equals(cacheGolbalCount) && unReadPrivateSize.equals(cachePrivateCount)) {
            // 未读信息添加到message表中
            addUnReadMsg(userId);
            if (count <= 10) {
                Thread.sleep(2000);
            } else if (count > 10 && count <= 15) {
                Thread.sleep(3000);
            } else {
                Thread.sleep(3000);
                putMapAndSetValues(userId, unReadNoticeSize, unReadGolbalSize, unReadPrivateSize, unReadAll);
                return unReadSizeDTO;
            }

            unReadNoticeSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PUBLIC);
            unReadGolbalSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.GLOBAL);
            unReadPrivateSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PRIVATE_LETTET);
            unReadAll = unReadNoticeSize + unReadGolbalSize + unReadPrivateSize;
            count++;
        }

        // 未读信息添加到message表中
        addUnReadMsg(userId);
        putMapAndSetValues(userId, unReadNoticeSize, unReadGolbalSize, unReadPrivateSize, unReadAll);
        return unReadSizeDTO;
    }

    private void putMapAndSetValues(Long userId, Integer unReadNoticeSize, Integer unReadGolbalSize, Integer unReadPrivateSize, Integer unReadAll) {
        putMap(userId, unReadNoticeSize, unReadGolbalSize, unReadPrivateSize, unReadAll);
        setValues(unReadNoticeSize, unReadGolbalSize, unReadPrivateSize, unReadAll);
    }

    private void putMap(Long userId, Integer unReadNoticeSize, Integer unReadGolbalSize, Integer unReadPrivateSize, Integer unReadAll) {
        countMap.put(userId, unReadAll);
        countNoticeMap.put(userId, unReadNoticeSize);
        countGlobalMap.put(userId, unReadGolbalSize);
        countPrivateMap.put(userId, unReadPrivateSize);
    }

    private void setValues(Integer unReadNoticeSize, Integer unReadGolbalSize, Integer unReadPrivateSize, Integer unReadAll) {
        unReadSizeDTO.setAllUnRead(unReadAll);
        unReadSizeDTO.setNoticeUnRead(unReadNoticeSize);
        unReadSizeDTO.setGlobalUnRead(unReadGolbalSize);
        unReadSizeDTO.setPrivateUnRead(unReadPrivateSize);
    }

    /**
     * 查询所有未读信息
     */
    @Deprecated
    public RestResp unReadCount(Long userId, Integer tip){
        try {
            int count = 0;
            UnReadSizeDTO result = invokeDb(userId, tip, count);
            return RestResp.success("操作成功", result);
        }catch (Exception e){
            LOG.error("站内信：获取所有未读信息数量异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @Deprecated
    public UnReadSizeDTO invokeDb(Long userId, Integer tip, Integer count) throws Exception {
        // 用户登录后，将所在用户组未读公告信息添加到message表中
        addUnReadMsg(userId);

        // 公告未读信息数量
        Integer noticeUnReadSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PUBLIC);
        int noticeCacheCount = countNoticeMap.getOrDefault(userId, 0);
        // 系统未读信息数量
        Integer globalUnReadSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.GLOBAL);
        int globalCacheCount = countGlobalMap.getOrDefault(userId, 0);
        // 私信未读信息数量
        Integer privateUnReadSize = messageDao.countByReceiverIdAndReadStatusAndMessageType(userId, MessageReadStatus.UN_READ, MessageType.PRIVATE_LETTET);
        int privateCacheCount = countPrivateMap.getOrDefault(userId, 0);

        // 所有未读信息数量
        // Integer unReadSize = messageDao.countByReceiverIdAndReadStatus(userId, MessageReadStatus.UN_READ);
        Integer unReadSize = noticeUnReadSize + globalUnReadSize + privateUnReadSize;
        int cacheCount = countMap.getOrDefault(userId, 0);

        unReadSizeDTO.setAllUnRead(0);
        unReadSizeDTO.setNoticeUnRead(0);
        unReadSizeDTO.setGlobalUnRead(0);
        unReadSizeDTO.setPrivateUnRead(0);

        if (tip == 1) {
            // 第一次请求获取未读消息
            countMap.put(userId, unReadSize);
            countNoticeMap.put(userId, noticeUnReadSize);
            countGlobalMap.put(userId, globalUnReadSize);
            countPrivateMap.put(userId, privateUnReadSize);

            unReadSizeDTO.setAllUnRead(unReadSize);
            unReadSizeDTO.setNoticeUnRead(noticeUnReadSize);
            unReadSizeDTO.setGlobalUnRead(globalUnReadSize);
            unReadSizeDTO.setPrivateUnRead(privateUnReadSize);

            return unReadSizeDTO;
        }
        // 旧值和新值一样，则不返回结果
        if (noticeCacheCount == noticeUnReadSize && globalCacheCount == globalUnReadSize && privateCacheCount == privateUnReadSize) {
            Thread.sleep(2000);
            // 前台请求15以上，返回的结果还是一样，就返回之前的数量，不走递归
            if (count >= MessageConst.Constant.FIFTEEN.getValue()) {
                unReadSizeDTO.setAllUnRead(unReadSize);
                unReadSizeDTO.setNoticeUnRead(noticeUnReadSize);
                unReadSizeDTO.setGlobalUnRead(globalUnReadSize);
                unReadSizeDTO.setPrivateUnRead(privateUnReadSize);
                return unReadSizeDTO;
            }
            return invokeDb(userId, tip, ++count);
        }
        // 旧值和新值，则更新缓存，返回结果
        countMap.put(userId, unReadSize);
        countNoticeMap.put(userId, noticeUnReadSize);
        countGlobalMap.put(userId, globalUnReadSize);
        countPrivateMap.put(userId, privateUnReadSize);

        unReadSizeDTO.setAllUnRead(unReadSize);
        unReadSizeDTO.setNoticeUnRead(noticeUnReadSize);
        unReadSizeDTO.setGlobalUnRead(globalUnReadSize);
        unReadSizeDTO.setPrivateUnRead(privateUnReadSize);
        return unReadSizeDTO;

    }

    @Transactional(Transactional.TxType.NEVER)
    private void addUnReadMsg(Long userId) throws Exception{
        // 先找到roleId，然后得到角色userGroup，然后根据msgType和userGroup得到id
        User user = userDao.findOne(userId);
        if (user != null) {
            Long userGroup = user.getRoleId();
            List<MessageText> messageTextList = messageTextDao.findByMessageTypeAndUserGroup(MessageType.PUBLIC, userGroup, 4L);
            //System.out.println("messageTextDao ---------------> " + messageTextDao);
            if (messageTextList.size() != 0) {
                for (MessageText mt : messageTextList) {
                    set.add(mt.getId());
                }

                // 移除已读公告的mtId
                List<Message> allPublic = messageDao.findByReceiverIdAndMessageType(userId, MessageType.PUBLIC);
                for (Message m : allPublic) {
                    set.remove(m.getMessageTextId());
                }

                // 添加剩余没有的公告
                Iterator<Long> it = set.iterator();
                Message message = new Message();
                while (it.hasNext()) {
                    message.setMessageTextId(it.next().longValue());
                    message.setReadStatus(MessageReadStatus.UN_READ);
                    message.setReceiverId(userId);
                    message.setMessageType(MessageType.PUBLIC);
                    messageDao.save(message);
                }
            }
        }
    }

    /**
     * 获取订单相关信息和头像
     */
    private boolean getOrderInfoAndImage(Long userId, MessageText messageText) {
        // 获取头像
        Long sendId = messageText.getSenderId();
        if (null == sendId){
            sendId = 0L;
        }
        if (sendId == 0){
            // 设置系统头像
            messageText.setImageName(systemDefaultImage);
        } else {
            User user = userDao.findOne(sendId);
            String imageName = user.getAvatar();
            if (null == imageName){
                // 设置默认用户头像
                messageText.setImageName(userDefaultImage);
            } else {
                messageText.setImageName(imageName);
            }
        }

        // 获取
        String orderId = messageText.getOrderId();
        Order orders = orderDao.findOne(orderId);
        Long buyerId = orders.getBuyerId();
        Long sellerId = orders.getSellerId();
        if (userId.equals(buyerId)){
            messageText.setPartnerId(sellerId);
            User user = userDao.findOne(sellerId);
            messageText.setFriendUsername(user.getLoginname());
            return true;
        }else if (userId.equals(sellerId)){
            messageText.setPartnerId(buyerId);
            User user = userDao.findOne(buyerId);
            messageText.setFriendUsername(user.getLoginname());
            return true;
        }else {
            return false;
        }
    }

    private RestResp queryMessage(Long userId, Integer pageNum, Integer pageSize, int messageType, boolean needOrder) {
        try {
            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(Sort.Direction.DESC, "id"));
            Page<Message> page = messageDao.findByReceiverIdAndMessageType(userId, messageType, pageable);
            Iterator<Message> it = page.iterator();
            List<MessageDTO> mList = new ArrayList<>();
            while (it.hasNext()){
                Message message = it.next();
                MessageText messageText = messageTextDao.findByIdAndMessageType(message.getMessageTextId(), messageType);

                if (needOrder){
                    boolean isSuccess = getOrderInfoAndImage(userId, messageText);
                    if (!isSuccess){
                        return RestResp.fail("站内信：获取订单信息失败");
                    }
                }

                message.setMessageText(messageText);
                mList.add(new MessageDTO(message));

                // 点击私信按钮，将所有返回数据的状态修改为已读，接受者id修改为自己的id
                message.setReadStatus(MessageReadStatus.READ);
                message.setReceiverId(userId);
                messageDao.save(message);
            }

            PageDTO<MessageDTO> pageDTO = new PageDTO<>();
            pageDTO.setPageList(mList);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);

            return RestResp.success("操作成功", pageDTO);
        }catch (Exception e){
            LOG.error("站内信：获取私信信息异常");
        }
        return RestResp.fail("操作失败");
    }
    public String select(boolean flag)throws  Exception{
        while (flag){
            System.out.println(this.selects());
            Thread.sleep(1000);
            if(1 == 2){
                return "s";
            }
        }
        String s = "sss";
        return s;
    }

    public Integer selects(){
        try {
            addUnReadMsg(22L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageDao.countByReceiverIdAndReadStatus(22L,1);
    }
}
