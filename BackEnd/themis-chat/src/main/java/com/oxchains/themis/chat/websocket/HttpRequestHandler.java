package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.entity.User;
import com.oxchains.themis.chat.websocket.scanner.Invoker;
import com.oxchains.themis.chat.websocket.scanner.InvokerManager;
import com.oxchains.themis.common.model.RestResp;

/**
 * create by huohuo
 *
 * @author huohuo
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        String requestUri = httpRequest.getUri().toString();
        if (requestUri.contains(wsUri)) {
            //连接参数
            String message = requestUri.substring(requestUri.lastIndexOf("?") + 1);
            String[] split = message.split("_");
            if (split != null && split.length == 4) {

                short module = Short.valueOf(split[0]);
                short cmd = Short.valueOf(split[1]);
                User user = new User();
                user.setId(Long.valueOf(split[2]));
                user.setPassword((split[3]));
                //根据模块号和命令号获取执行器
                Invoker invoker = InvokerManager.getInvoker(module, cmd);
                RestResp result = (RestResp) invoker.invoker(ctx, user);
                ctx.fireChannelRead(httpRequest.retain());
            }

        } else {
            HttpResponse response = new DefaultHttpResponse(httpRequest.getProtocolVersion(), HttpResponseStatus.OK);
            boolean keepAlive = HttpHeaders.isKeepAlive(httpRequest);
            if (keepAlive) {
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
