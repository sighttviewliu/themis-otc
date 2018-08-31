package com.oxchains.themis.chat.websocket.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行器封装类
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
public class Invoker {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 方法
     */
    private Method method;

    private Invoker(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public static Invoker valueOf(Method method, Object target) {
        return new Invoker(method, target);
    }


    /**
     * 执行方法
     *
     * @param parms
     * @return
     */
    public Object invoker(Object... parms) {

        try {
            return method.invoke(target, parms);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
