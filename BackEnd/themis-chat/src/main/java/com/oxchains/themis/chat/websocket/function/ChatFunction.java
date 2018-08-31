package com.oxchains.themis.chat.websocket.function;

import com.oxchains.themis.chat.entity.*;
import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.service.SensitiveWordFilter;
import com.oxchains.themis.chat.websocket.Session;
import com.oxchains.themis.chat.websocket.SessionImpl;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.chat.websocket.scanner.InvokerManager;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 聊天业务的接口
 *
 * @author brandon
 * Created by brandon on 2018/7/9.
 */
@Service
@SocketModule(module = ModuleId.CHAT)
public class ChatFunction {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private SensitiveWordFilter sensitiveWordFilter;


    /**
     * 连接管道
     *
     * @param ctx
     * @param user
     * @return
     */
    @SocketCommand(cmd = CommandId.CONNECT)
    public RestResp connect(ChannelHandlerContext ctx, User user) {
        System.out.println("com.netty4.server.function.ChatService.connect --> " + user.toString());
        //TODO 验证获取到的用户参数，用户信息错误的不允许连接


        //判断是否登录过
        SessionImpl session = new SessionImpl(ctx.channel());
        boolean online = SessionManager.isOnline(user.getId());
        if (online) {
            //从会话管理器移除会话并解除会话绑定
            Session oldSession = SessionManager.removeSession(user.getId());
            oldSession.removeAttachment();
            oldSession.close();
        }
        //绑定新会话
        if (SessionManager.putSession(user.getId(), session)) {
            session.setAttachment(user);

            System.out.println(user.getId() + " 是否在线 --> " + SessionManager.isOnline(user.getId()));


            //删除用户遗留在redis中的用户数据
            chatService.removeUserInfoFromRedis(user.getId());

            //登录后将该用户的未读消息推送到客户端
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Session userSession = SessionManager.getSession(user.getId());
                    if (null != userSession) {
                        int i = 0;
                        do {
                            try {
                                Thread.sleep(3000);
                                i++;
                                if (i == 10)
                                    return;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (!userSession.isConnected());
                        List<ChatContent> chatContentList = messageService.postUnreadChatMessage(user.getId(), MessageType.PRIVATE_LETTET);
//                    System.out.println("unread message --> " + chatContentList.toString());
                        for (ChatContent chatContent : chatContentList) {
                            chatContent.setUserName(chatService.getLoginNameByUserId(chatContent.getSenderId()));
                            chatContent.setUserAvatar(chatService.getAvatarByUserId(chatContent.getSenderId()));
                            Response response = new Response(ModuleId.CHAT, CommandId.PRIVATE_CHAT, chatContent);
                            if (SessionManager.isOnline(user.getId())) {
                                SessionManager.getSession(user.getId()).write(new TextWebSocketFrame(JsonUtil.toJson(response)));
                            }
                        }
                    }

                }
            }).start();

        } else {
            return RestResp.fail("login failure");
        }
        return RestResp.success();
    }


    /**
     * 聊天接口
     *
     * @param ctx
     * @param data
     */
    @SocketCommand(cmd = CommandId.PRIVATE_CHAT)
    public void privateChat(ChannelHandlerContext ctx, Map data) {

        ChatContent chatContent = InvokerManager.change(data, ChatContent.class);

        //发送消息的字数限制,字数在1-200之间可以发送，否则消息舍弃
        int length = chatContent.getChatContent().toCharArray().length;
        if (length == 0 || length > 200) {
            return;
        }

        //敏感词过滤
        String word = sensitiveWordFilter.replaceSensitiveWord(chatContent.getChatContent(), 1, "*");
        chatContent.setChatContent(word);


        try {
            //接收到消息后  先给自己转发一份 在给对方转法一份 如果对方不在线则 发到私信里面 然后将消息存到kafka队列里 由kafka存到mongo里
            String keyIDs = SessionManager.getIDS(chatContent.getSenderId().toString(), chatContent.getReceiverId().toString());
            chatContent.setCreateTime(DateUtil.getPresentDate());
            chatContent.setChatId(keyIDs);

            //判断用户自己是否在线,不在线直接关闭通道
            if (!SessionManager.isOnline(chatContent.getSenderId())) {
                ctx.channel().close();
                System.out.println("用户自己不在线。消息发送失败  发送者id --> " + chatContent.getSenderId());
                return;
            }

            //设置发送者用户的名称和头像
            chatContent.setUserName(chatService.getLoginNameByUserId(chatContent.getSenderId()));
            chatContent.setUserAvatar(chatService.getAvatarByUserId(chatContent.getSenderId()));

            //先给对方转发，可以判断对方是否在线来鉴定此消息是已读还是未读
            //封装发送者用户的名称和头像
            String message = null;
            Session receiveSession = SessionManager.getSession(chatContent.getReceiverId());
            if (receiveSession != null && receiveSession.isConnected()) {//如果用户再在线，则此消息标记为已读
                chatContent.setStatus(MessageReadStatus.READ + "");
                message = JsonUtil.toJson(new Response(ModuleId.CHAT, CommandId.PRIVATE_CHAT, chatContent));
                receiveSession.write(new TextWebSocketFrame(message));
            } else {//如果用户不在线，则此消息标记为未读
                chatContent.setStatus(MessageReadStatus.UN_READ + "");
                messageService.postPriChatMessage(chatContent);
            }
            //给自己转发
            message = JsonUtil.toJson(new Response(ModuleId.CHAT, CommandId.PRIVATE_CHAT, chatContent));
            SessionManager.getSession(chatContent.getSenderId()).write(new TextWebSocketFrame(message));

            kafkaService.send(JsonUtil.toJson(chatContent));
        } catch (Exception e) {
            LOG.error("chat privateChat faild : {}", e);
        }
    }

}
