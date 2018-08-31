package com.oxchains.themis.chat.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块号的注解
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocketModule {

    /**
     * 模块号
     *
     * @return
     */
    short module();

}
