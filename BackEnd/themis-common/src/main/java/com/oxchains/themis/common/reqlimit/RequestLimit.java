package com.oxchains.themis.common.reqlimit;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author ccl
 * @time 2018-05-30 10:38
 * @name RequestLimit
 * @desc:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {
    /**
     * 允许访问的最大次数
     */
    int count() default 60;

    /**
     * 时间段，单位为毫秒，默认值30s
     */
    long time() default 30000;
}
