package com.oxchains.themis.common.util;

import com.oxchains.themis.common.okhttp.OkHttpClientHelper;
import lombok.Data;

/**
 * @author ccl
 * @time 2018-08-09 10:11
 * @name BPushRestHelper
 * @desc:
 */
public class BPushRestHelper {
    private static final String B_PUSH_SINGLE_URL = "https://api.tuisong.baidu.com/rest/3.0/push/single_device";
    private static final String B_PUSH_ALL_URL = "https://api.tuisong.baidu.com/rest/3.0/push/all";

    @Data
    static class MessageSingle{
        String channel_id;
        Integer msg_type;
        String msg;
        Integer msg_expires;
        Integer deploy_status;
    }
    public static String push(){
//        OkHttpClientHelper.post(B_PUSH_SINGLE_URL)
        return null;
    }

}
