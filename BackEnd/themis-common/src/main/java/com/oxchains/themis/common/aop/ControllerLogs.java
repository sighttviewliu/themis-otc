package com.oxchains.themis.common.aop;

import java.lang.annotation.*;

/**
 * Controller 日志记录
 *
 * @author anonymity
 * @create 2018-07-24 18:30
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLogs {
    /**
     * 描述
     */
    String description() default "";
}
