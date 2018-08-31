package com.oxchains.themis.chat.entity;

/**
 * 命令号
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
public interface CommandId {

    /**
     * 连接命令号
     */
    short CONNECT = 10;

    /**
     * 私聊消息命令号
     */
    short PRIVATE_CHAT = 11;

    /**
     * 群聊消息命令号
     */
    short PUBLIC_CHAT = 12;

    /**
     * 广播消息命令号
     */
    short REDIO = 13;


    /**
     * 心跳命令号
     */
    short HEARTBEAT = 71;

}
