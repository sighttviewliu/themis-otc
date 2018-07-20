package com.oxchains.themis.chat.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.UploadTxIdPojo;
import com.oxchains.themis.chat.entity.User;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.common.constant.ThemisUserAddress;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    public List<ChatContent> getChatHistroy(Long senderId, Long receiverId) {
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

//            String keyIDs = SessionManager.getIDS(senderId.toString(), receiverId.toString());
            String chatId = SessionManager.getIDS(senderId + "", receiverId + "");

            List<ChatContent> list = mongoRepo.findChatContentByChatId(chatId);

            LOG.info("chat history ---> " + list);

            return list;
        } catch (Exception e) {
            LOG.error("faild get chat history : {}", e);
        }
        return null;
    }

    /**
     * 从用户中心 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    @HystrixCommand(fallbackMethod = "getUserByIdError")
    public User getUserById(Long userId) {

        HashOperations hashOperations = redisTemplate.opsForHash();

        try {
            String userInfo = (String) hashOperations.get(userHK, userId.toString());
            //因为有的值为null，所以要替换一下
//            userInfo = userInfo.replaceAll("null", "");
//            System.out.println("userInfo --> " + userInfo + "  " + userInfo.length());

            if (StringUtils.isNotBlank(userInfo)) {
                return JsonUtil.jsonToEntity(userInfo, User.class);
            }
            String str = restTemplate.getForObject(ThemisUserAddress.GET_USER + userId, String.class);
            if (null != str) {
                RestResp restResp = JsonUtil.jsonToEntity(str, RestResp.class);
                if (null != restResp && restResp.status == 1) {
                    hashOperations.put(userHK, userId.toString(), JsonUtil.toJson(restResp.data));
                    Boolean expire = redisTemplate.expire(userHK, 3, TimeUnit.MINUTES);
                    System.out.println("expire --> " + expire);
                    return JsonUtil.objectToEntity(restResp.data, User.class);
                }
            }
        } catch (Exception e) {
            LOG.error("get user by id from themis-user faild : {}", e);
            throw e;
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
    private String getLoginNameByUserId(Long userId) {
        User userById = this.getUserById(userId);
        return userById != null ? userById.getLoginname() : null;
    }


    /**
     * 根据用户的id查询用户的头像
     *
     * @param userId
     * @return
     */
    private String getAvatarByUserId(Long userId) {
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
