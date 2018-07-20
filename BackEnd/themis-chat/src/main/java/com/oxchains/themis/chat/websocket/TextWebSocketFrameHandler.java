package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.entity.*;
import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.websocket.scanner.Invoker;
import com.oxchains.themis.chat.websocket.scanner.InvokerManager;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * create by huohuo
 *
 * @author huohuo
 */
public class TextWebSocketFrameHandler extends
        SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private KafkaService kafkaService;
    private MessageService messageService;

    private ChatService chatService;

    public TextWebSocketFrameHandler(KafkaService kafkaService, MessageService messageService, ChatService chatService) {
        this.kafkaService = kafkaService;
        this.messageService = messageService;
        this.chatService = chatService;
    }

//    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                TextWebSocketFrame msg) throws Exception {

        System.out.println("msg text --> " + msg.text());
        Request request = (Request) JsonUtil.fromJson(msg.text(), Request.class);

        short module = request.getModule();
        short cmd = request.getCmd();

        //根据模块号和命令号获取执行器
        Invoker invoker = InvokerManager.getInvoker(module, cmd);

        invoker.invoker(ctx, request.getData());

    }

    //    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
//            ctx.pipeline().remove(HttpRequestHandler.class);
//            channels.add(ctx.channel());
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }
//
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        Channel incoming = ctx.channel();
//        channels.add(incoming);
//    }
//


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionImpl session = new SessionImpl(ctx.channel());
        Object attachment = session.getAttachment();
        if (null != attachment) {
            User user = (User) attachment;
            chatService.removeUserInfoFromRedis(((User) session.getAttachment()).getId());
            session.removeAttachment();
            SessionManager.removeSession(user.getId());
            session.close();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelInactive(ctx);
    }


}
