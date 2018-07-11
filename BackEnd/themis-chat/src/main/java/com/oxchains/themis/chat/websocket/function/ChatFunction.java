package com.oxchains.themis.chat.websocket.function;

import com.oxchains.themis.chat.entity.*;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.websocket.Session;
import com.oxchains.themis.chat.websocket.SessionImpl;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.chat.websocket.scanner.InvokerManager;
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

import java.util.List;
import java.util.Map;

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


    /**
     * 连接管道
     *
     * @param ctx
     * @param user
     * @return
     */
    @SocketCommand(cmd = CommandId.CONNECT)
    public RestResp connect(ChannelHandlerContext ctx, User user) {
//        System.out.println("com.netty4.server.function.ChatService.connect --> " + user.toString());
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

            //登录后将该用户的未读消息推送到客户端
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<ChatContent> chatContentList = messageService.postUnreadChatMessage(user.getId(), MessageType.PRIVATE_LETTET);
//                    System.out.println("unread message --> " + chatContentList.toString());
                    for (ChatContent chatContent : chatContentList) {
                        Response response = new Response(ModuleId.CHAT, CommandId.PRIVATE_CHAT, chatContent);
                        if (SessionManager.isOnline(user.getId())) {
                            SessionManager.getSession(user.getId()).write(new TextWebSocketFrame(JsonUtil.toJson(response)));
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


        try {
            //接收到消息后  先给自己转发一份 在给对方转法一份 如果对方不在线则 发到私信里面 然后将消息存到kafka队列里 由kafka存到mongo里
            String keyIDs = SessionManager.getIDS(chatContent.getSenderId().toString(), chatContent.getReceiverId().toString());
            chatContent.setCreateTime(DateUtil.getPresentDate());
            chatContent.setChatId(keyIDs);

            String message = JsonUtil.toJson(new Response(ModuleId.CHAT, CommandId.PRIVATE_CHAT, chatContent));
            SessionManager.getSession(chatContent.getSenderId()).write(new TextWebSocketFrame(message));
            //再给对方转发
            Session receiveSession = SessionManager.getSession(chatContent.getReceiverId());
            if (receiveSession != null && receiveSession.isConnected()) {
                receiveSession.write(new TextWebSocketFrame(message));
            } else {
                messageService.postPriChatMessage(chatContent);
            }
            kafkaService.send(JsonUtil.toJson(chatContent));
        } catch (Exception e) {
            LOG.error("caht disposeInfo faild : {}", e);
        }
    }


}
