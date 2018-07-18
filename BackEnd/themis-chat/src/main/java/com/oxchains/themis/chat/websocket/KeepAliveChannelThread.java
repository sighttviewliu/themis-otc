package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.entity.HeartBeat;
import com.oxchains.themis.chat.entity.Request;
import com.oxchains.themis.chat.entity.User;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * create by huohuo
 *
 * @author huohuo
 */
public class KeepAliveChannelThread implements Runnable {
    private ScheduledExecutorService keepAliveScheduler;
    private long keepTime;
    private static final Logger LOG = LoggerFactory.getLogger(KeepAliveChannelThread.class);

    public KeepAliveChannelThread(ScheduledExecutorService keepAliveScheduler, long keepTime) {
        this.keepAliveScheduler = keepAliveScheduler;
        this.keepTime = keepTime;
    }

    @Override
    public void run() {
        try {
            for (Long s : SessionManager.getOnlineUsers()) {
                if (!SessionManager.getSession(s).isAlive()) {
                    Session session = SessionManager.getSession(s);
                    System.out.println("removeSession --> " + ((User) session.getAttachment()).getId());
                    session.removeAttachment();
                    SessionManager.removeSession(s);
                    session.close();
                }
            }
        } catch (Exception e) {
            LOG.error("Keep Alive websocket channel faild : {}", e);
        }
        this.keepAliveScheduler.schedule(this, keepTime, TimeUnit.SECONDS);


    }
}
