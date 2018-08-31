package com.oxchains.themis.chat.entity;

/**
 * 模块id
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
public interface ModuleId {

    /**
     * chat模块号
     */
    short CHAT = 1;

    /**
     * message模块号
     */
    short MESSAGE = 2;

    /**
     * notice模块号
     */
    short NOTICE = 3;

    /**
     * order模块号
     */
    short ORDER = 4;

    /**
     * user模块号
     */
    short USER = 5;

    /**
     * wallet模块号
     */
    short WALLET = 6;

    /**
     * 管道心跳
     */
    short HEART = 7;


}
