package com.oxchains.themis.chat.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.UploadTxIdPojo;
import com.oxchains.themis.chat.entity.User;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@Service
public class ChatService {
    @Resource
    private MongoRepo mongoRepo;
    @Resource
    private RestTemplate restTemplate;
    //    @Resource
//    private HashOperations hashOperations;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${themis.user.redisInfo.hk}")
    private String userHK;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public List<ChatContent> getChatHistroy(Long senderId, Long receiverId, Integer pageNum) {
        try {
            LOG.info("get chat history userId ：" + senderId + " orderId: " + receiverId);
            //如果发送者id和接受者id都为空或有一方为空，则通过订单号查询出来
//            if (null == senderId || senderId < 1 || null == receiverId || receiverId < 1) {
//                List<ChatContent> chatContentsByOrderId = mongoRepo.findChatContentsByOrderId(orderId);
//                if (chatContentsByOrderId == null || chatContentsByOrderId.size() < 1) {
//                    return null;
//                }
//                ChatContent chatContent = chatContentsByOrderId.get(0);
//                senderId = chatContent.getSenderId();
//                receiverId = chatContent.getReceiverId();
//            }

            String chatId = SessionManager.getIDS(senderId.toString(), receiverId.toString());

            List<ChatContent> chatContents = null;

            //不分页获取聊天记录
//            chatContents = mongoRepo.findChatContentByChatId(chatId);

//            分页获取聊天记录
            Pageable pageable = new PageRequest(pageNum - 1, 10, new Sort(Sort.Direction.DESC, "createTime"));
            Page<ChatContent> list = mongoRepo.findChatContentByChatId(chatId, pageable);
            chatContents = list.getContent();

//            System.out.println("size --> " + list.getSize() + "  number --> " + list.getNumber() + "  totalPages --> " + list.getTotalPages());
//            System.out.println(list.getContent());

            //获取最新的用户名称和头像信息
            for (ChatContent chatContent : chatContents) {
                chatContent.setUserName(this.getLoginNameByUserId(chatContent.getSenderId()));
                chatContent.setUserAvatar(this.getAvatarByUserId(chatContent.getSenderId()));
            }

            LOG.info("chat history ---> " + chatContents);

            return chatContents;
        } catch (Exception e) {
            LOG.error("faild get chat history : {}", e);
        }
        return null;
    }

    /**
     * 根据上一条记录的id，查找该记录的后10条记录
     *
     * @param previousId
     * @return
     */
    public List<ChatContent> getChatHistoryByPreviousId(Long senderId, Long receiverId, String previousId) {
        String chatId = SessionManager.getIDS(senderId.toString(), receiverId.toString());
        String createTime = null;
        List<ChatContent> chatContents = new ArrayList<>();

        if (StringUtils.isNotBlank(previousId)) {
            ChatContent one = mongoRepo.findOne(previousId);
            createTime = null == one ? DateUtil.getPresentDate() : one.getCreateTime();
            //查找当有时间相同的记录时，去除请求的那个，返回未请求的，防止遗漏记录
//            chatContents = mongoRepo.findChatContentByChatIdAndCreateTime(chatId, createTime);
//            for (ChatContent chatContent : chatContents) {
//                if (previousId.equals(chatContent.getId())) {
//                    chatContents.remove(chatContent);
//                    break;
//                }
//            }
//            System.out.println("createTime equal --> " + chatContents.toString());
        } else {
            createTime = DateUtil.getPresentDate();
        }

        //这里的分页是为了截取前10个数据
        Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "createTime"));

        Page<ChatContent> pageInfo = mongoRepo.findChatContentByChatIdAndCreateTimeBefore(chatId, createTime, pageable);

        chatContents.addAll(pageInfo.getContent());

        //获取最新的用户名称和头像信息
        for (ChatContent chatContent : chatContents) {
            chatContent.setUserName(this.getLoginNameByUserId(chatContent.getSenderId()));
            chatContent.setUserAvatar(this.getAvatarByUserId(chatContent.getSenderId()));
        }
        System.out.println("chatContents --> " + chatContents.toString());

        return chatContents;
    }


    /**
     * 获取所有的历史记录
     *
     * @param senderId
     * @param receiverId
     * @return
     */
    public List<ChatContent> getAllChatHistory(Long senderId, Long receiverId) {
        String chatId = SessionManager.getIDS(senderId.toString(), receiverId.toString());

        List<ChatContent> chatContents = mongoRepo.findChatContentByChatId(chatId);


        for (ChatContent chatContent : chatContents) {
            chatContent.setUserName(this.getLoginNameByUserId(chatContent.getSenderId()));
            chatContent.setUserAvatar(this.getAvatarByUserId(chatContent.getSenderId()));
        }
        System.out.println("chatContents --> " + chatContents.toString());

        return chatContents;
    }

    /**
     * 从用户中心 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByIdError")
    public User getUserById(Long userId) {


        try {
            HashOperations hashOperations = redisTemplate.opsForHash();
            String userInfo = (String) hashOperations.get(userHK, userId.toString());

            if (StringUtils.isNotBlank(userInfo)) {
                return JsonUtil.jsonToEntity(userInfo, User.class);
            }
            String str = restTemplate.getForObject(ThemisUserAddress.GET_USER + userId, String.class);
            if (null != str) {
                RestResp restResp = JsonUtil.jsonToEntity(str, RestResp.class);
                if (null != restResp && restResp.status == 1 && null != restResp.data) {
                    hashOperations.put(userHK, userId.toString(), JsonUtil.toJson(restResp.data));
                    Boolean expire = redisTemplate.expire(userHK, 3, TimeUnit.MINUTES);
                    System.out.println("expire --> " + expire);
                    return JsonUtil.objectToEntity(restResp.data, User.class);
                }
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}", e);
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByIdError(Long userId) {
        return null;
    }

    /**
     * 根据用户的id查询用户的登录名
     *
     * @param userId
     * @return
     */
    public String getLoginNameByUserId(Long userId) {
        User userById = this.getUserById(userId);
        return userById != null ? userById.getLoginname() : null;
    }


    /**
     * 根据用户的id查询用户的头像
     *
     * @param userId
     * @return
     */
    public String getAvatarByUserId(Long userId) {
        User userById = this.getUserById(userId);
        return userById != null ? userById.getAvatar() : null;
    }


  /*  public boolean uploadTxInform(UploadTxIdPojo pojo) {
        try {
            ChannelHandler channelHandler = ChatUtil.txChannels.get(pojo.getId());
            if (channelHandler != null) {
                LOG.info("连接存在 :" + pojo.getId());
                String message = JsonUtil.toJson(pojo);
                channelHandler.getChannel().writeAndFlush(new TextWebSocketFrame(message));
                return true;
            }
        } catch (Exception e) {
            LOG.error("upload tx inform faild", e.getMessage());
        }
        return false;
    }*/


    /**
     * 根据userID删除该用户在redis中的用户信息
     *
     * @param userId
     */
    public void removeUserInfoFromRedis(Long userId) {
        Long delete = redisTemplate.opsForHash().delete(userHK, userId.toString());
    }

}
