package com.oxchains.themis.chat.websocket.function;

import com.oxchains.themis.chat.entity.*;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.chat.websocket.scanner.InvokerManager;
import com.oxchains.themis.common.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author brandon
 * Created by brandon on 2018/7/10.
 */
@Component
@SocketModule(module = ModuleId.HEART)
public class HeartBeatFunction {

    /**
     * 心跳
     *
     * @param ctx
     * @param data data参数中必须包含通道的使用者id
     */
    @SocketCommand(cmd = CommandId.HEARTBEAT)
    public void heartBeat(ChannelHandlerContext ctx, Map data) {


        HeartBeat heartBeat = InvokerManager.change(data, HeartBeat.class);

        long userId = heartBeat.getUserId();

        System.out.println("id --> " + userId + " 上一次通道使用时间 " + new Date(SessionManager.getSession(userId).getLastUseTime()));

        if (SessionManager.isOnline(userId)) {
            SessionManager.getSession(userId).updateLastUseTime(System.currentTimeMillis());
            heartBeat.setStatus("success");
            SessionManager.getSession(userId).write(new TextWebSocketFrame(JsonUtil.toJson(new Response(ModuleId.HEART, CommandId.HEARTBEAT, heartBeat))));
        } else {
            heartBeat.setStatus("error");
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(new Response(ModuleId.HEART, CommandId.HEARTBEAT, heartBeat))));
            ctx.channel().close();
        }
    }


}
