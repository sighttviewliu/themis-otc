package com.oxchains.themis.chat.websocket.scanner;

import com.oxchains.themis.chat.entity.HeartBeat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行器管理类
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
public class InvokerManager {

    /**
     * 命令调用器
     */
    private static Map<Short, Map<Short, Invoker>> invokers = new HashMap<Short, Map<Short, Invoker>>();

    /**
     * 增加命令调用器
     *
     * @param module
     * @param cmd
     * @param invoker
     */
    public static void addInvoker(short module, short cmd, Invoker invoker) {
        Map<Short, Invoker> map = invokers.get(module);
        if (null == map) {
            map = new HashMap<Short, Invoker>();
            invokers.put(module, map);
        }
        map.put(cmd, invoker);
    }


    /**
     * 获取命令调度器
     *
     * @param module
     * @param cmd
     * @return invoker
     */
    public static Invoker getInvoker(short module, short cmd) {
        Invoker invoker = null;
        Map<Short, Invoker> map = invokers.get(module);
        if (null != map) {
            invoker = map.get(cmd);
        }
        return invoker;
    }


    /**
     * 封装对象，将map中的数据封装成一个clazz对应的对象并返回
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T change(Map data, Class<T> clazz) {

        Field[] fields = clazz.getDeclaredFields();

        T instance = null;
        try {
            instance = clazz.newInstance();
            for (Field field : fields) {
                Method setMethod = getSetMethod(clazz, field.getName());
                Class<?> type = field.getType();
                if (data.get(field.getName()) == null) {
                    if (field.getType().getName().toLowerCase().endsWith("string")) {
                        data.put(field.getName(), "");
                    } else {
                        data.put(field.getName(), "0");
                    }
                }
                Constructor<?> constructor = type.getDeclaredConstructor(String.class);

                Object value = constructor.newInstance(data.get(field.getName()));

                setMethod.invoke(instance, value);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }


    /**
     * 根据class和字段名获取该字段的set方法
     *
     * @param clazz
     * @param fidleName
     * @return
     */
    public static Method getSetMethod(Class clazz, String fidleName) {

        Field field = null;
        try {
            field = clazz.getDeclaredField(fidleName);
            Class<?> type = field.getType();

            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fidleName.substring(0, 1).toUpperCase());
            sb.append(fidleName.substring(1));
            Method method = clazz.getMethod(sb.toString(), type);
            return method;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


}
