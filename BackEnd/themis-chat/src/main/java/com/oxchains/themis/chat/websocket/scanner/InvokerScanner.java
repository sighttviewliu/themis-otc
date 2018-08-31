package com.oxchains.themis.chat.websocket.scanner;

import com.oxchains.themis.chat.entity.SocketCommand;
import com.oxchains.themis.chat.entity.SocketModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 执行器扫描类
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
@Component
public class InvokerScanner implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> clazz = bean.getClass();

        SocketModule annotation = clazz.getAnnotation(SocketModule.class);

        //判断是否为handler接口
        if (null == annotation) {
            return bean;
        }
        //获取所有方法
        Method[] methods = clazz.getMethods();
        if (null != methods && methods.length > 0) {
            for (Method method : methods) {
                SocketCommand command = method.getAnnotation(SocketCommand.class);
                //判断是否为命令方法
                if (null == command) {
                    continue;
                }
                //获取模块号和命令号
                short module = annotation.module();
                short cmd = command.cmd();

                Invoker invoker = InvokerManager.getInvoker(module, cmd);
                if (null != invoker) {
                    System.out.println("重复命令:" + "module:" + module + " " + "cmd：" + cmd);
                    continue;
                }
                //封装invoker对象
                InvokerManager.addInvoker(module, cmd, Invoker.valueOf(method, bean));
            }
        }
        return bean;
    }
}
