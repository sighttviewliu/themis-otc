package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.entity.User;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session会话管理器
 *
 * @author brandon
 * Created by brandon on 2018/7/8.
 */
public class SessionManager {

    /**
     * 存在在线的会话
     */
    private static Map<Long, Session> userChannels = new ConcurrentHashMap<Long, Session>();

    /**
     * 获取聊天号，由senderID和receiveID组成（此为遗留方法，后期会舍弃）
     *
     * @param id
     * @param did
     * @return
     */
    public static String getIDS(String id, String did) {
        return Integer.parseInt(id) > Integer.parseInt(did) ? did + "_" + id : id + "_" + did;
    }

    /**
     * 加入session
     *
     * @param userId
     * @param session
     * @return
     */
    public static boolean putSession(long userId, Session session) {

        if (!userChannels.containsKey(userId)) {
            Session put = userChannels.put(userId, session);
            if (put == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取session
     *
     * @param userId
     * @return
     */
    public static Session getSession(long userId) {
        if (userChannels.containsKey(userId)) {
            return userChannels.get(userId);
        }
        return null;
    }

    /**
     * 移除session
     *
     * @param userId
     * @return
     */
    public static Session removeSession(long userId) {
        return userChannels.remove(userId);
    }

    /**
     * 判断是否在线
     *
     * @param userId
     * @return
     */
    public static boolean isOnline(long userId) {
        return userChannels.containsKey(userId);
    }

    /**
     * 获取所有在线用户
     *
     * @return
     */
    public static Set<Long> getOnlineUsers() {
        return Collections.unmodifiableSet(userChannels.keySet());
    }

}
