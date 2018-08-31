package com.oxchains.themis.common.util;

import com.oxchains.themis.common.okhttp.OkHttpClientHelper;
import lombok.Data;

import java.util.*;

/**
 * @author ccl
 * @time 2018-08-08 13:34
 * @name JPushRestHelper
 * @desc:
 */
public class JPushRestHelper {
    private static final String J_PUSH_URL = "https://api.jpush.cn/v3/push";
    private static String apiKey = "417a6b02004f430f4e877185";
    private static String masterSecret = "4a627493d73023c74b0353c4";

    private static final String ALL = "all";
    private static final String ANDROID = "android";
    private static final String MESSAGE_CONTENT_TYPE = "text";
    private static final String MESSAGE_TITLE = "MSG";

    private static Map<String, String> initAuth() {
        Map<String, String> header = new HashMap(1);
        String authorization = "Basic " + EncryptUtils.encodeBase64(apiKey + ":" + masterSecret);
        header.put("Authorization", authorization);
        return header;
    }

    public static String pushAll(String title, String summary, String message, Map<String, Object> extras) {
        return push(ALL, title, summary, message, extras, null);
    }

    /**
     * 无别名的通知和自定义消息
     */
    public static String pushAndroidAll(String title, String summary, String message, Map<String, Object> extras) {
        return push(ANDROID, title, summary, message, extras, null);
    }

    /**
     * 有别名的通知和自定义消息
     */
    public static String pushAndroidTag(String title, String summary, String message, Map<String, Object> extras, List<String> tag) {
        return push(ANDROID, title, summary, message, extras, tag);
    }

    /**
     * 只有 有别名的自定义消息
     */
    public static String pushAndroidTagCustom(String message, Map<String, Object> extras, List<String> tag) {
        return push(ANDROID, null, null, message, extras, tag);
    }

    /**
     * 有别名的通知消息
     */
    public static String pushAndroidTagNotification(String title, String summary, Map<String, Object> extras, List<String> tag) {
        return push(ANDROID, title, summary, null, extras, tag);
    }

    private static void setTag(PushBody pushBody, List<String> tag) {
        if (tag != null) {
            Map<String, List<String>> audience = new HashMap<>();
            audience.put("alias", tag);
            pushBody.setAudience(audience);
        }
    }

    private static void setNotification(PushBody pushBody, String title, String summary, Map<String, Object> extras) {
        if (title != null) {
            Notification notification = new Notification();
            PushAndroidNotification android = new PushAndroidNotification();
            android.setTitle(title);
            if (summary != null) {
                android.setAlert(summary);
            }
            if (extras != null) {
                android.setExtras(extras);
            }
            notification.setAndroid(android);
            pushBody.setNotification(notification);
        }
    }

    private static void setMessage(PushBody pushBody, String msg, Map<String, Object> extras) {
        if (msg != null) {
            Message message = new Message();
            message.setMsg_content(msg);
            message.setContent_type(MESSAGE_CONTENT_TYPE);
            message.setTitle(MESSAGE_TITLE);
            if (extras != null) {
                message.setExtras(extras);
            }
            pushBody.setMessage(message);
        }
    }

    /**
     * @param platform    设备平台
     * @param title       大标题
     * @param summary     简要
     * @param message     消息主体
     * @param extras      透传字段
     * @param tag         标签
     * @return
     */
    private static String push(String platform, String title, String summary, String message, Map<String, Object> extras, List<String> tag) {
        PushBody pushBody = new PushBody();
        // 设置设备平台
        pushBody.setPlatform(platform);
        // 设置别名
        setTag(pushBody, tag);
        // 设置通知信息和透传字段
        setNotification(pushBody, title, summary, extras);
        // 设置自定义消息和透传字段
        setMessage(pushBody, message, extras);


        String jsonStr = JsonUtil.toJson(pushBody);

        return OkHttpClientHelper.post(J_PUSH_URL, initAuth(), jsonStr);
    }

    @Data
    static class PushBody {
        String cid;
        String platform;
        Object audience = "all";
        Notification notification;
        Message message;
    }

    @Data
    static class Notification {
        PushAndroidNotification android;
        PushIOSNotification ios;
    }

    @Data
    static class PushAndroidNotification {
        String alert;
        String title;
        Integer builer_id;
        Map<String, Object> extras;
    }

    @Data
    static class PushIOSNotification {
        String alert;
        String sound;
        String badge;
        Map<String, Object> extras;
    }

    @Data
    static class Message {
        String msg_content;
        String content_type;
        String title;
        Map<String, Object> extras;
    }

    /*public static void main(String[] args) {
        List<String> tags = new ArrayList<>();
        tags.add("22");

        String title = "您的订单被拍下";
        String summary = "订单ID: 52013三生三世十里桃花kjkj14";
        String message = "哈哈，你被骗了";

        Map<String, Object> extras = new HashMap<>();
        extras.put("type", 0);
        extras.put("orderId", "5201314");

        *//*String result = JPushRestHelper.pushAndroidTagNotification(
                "您的广告已被拍下",
                "您的广告已被[罗太帅]拍下，订单编号：[2276886]，正在做订单上链的托管准备，完成托管准备后会激活托管按钮！",
                extras,
                tags
        );*//*
        String result = JPushRestHelper.pushAndroidTagCustom("s", extras, tags);
        if (result != null){
            System.out.println(result);
        } else {
            System.out.println(1111);
        }
    }*/
}
