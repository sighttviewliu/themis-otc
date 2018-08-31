package contract;

import com.oxchains.themis.common.okhttp.OkHttpClientHelper;
import com.oxchains.themis.common.util.EncryptUtils;
import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JPushRestHelper;
import com.oxchains.themis.common.util.JsonUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anonymity
 * @create 2018-08-16 10:04
 **/
public class JPushHelper {

    private static final String J_PUSH_URL = "https://api.jpush.cn/v3/push";

    public static String push() {
        Map<String, String> header = new HashMap();
        String authorization = "Basic " + EncryptUtils.encodeBase64("24b4e6e841b0589d342e8542:9a935754a447dc83a8986d60");
        header.put("Authorization", authorization);

        PushBody pushBody = new PushBody();
        pushBody.setPlatform("all");

        Map<String, List<String>> audience = new HashMap<>();
        audience.put("alias", Arrays.asList( "103"));
        //pushBody.setAudience(audience);


        Notification notification = new Notification();
        PushAndroidNotification android = new PushAndroidNotification();
        android.setAlert("Hello JPush");
        android.setTitle("通知");
        Map<String, Object> extras = new HashMap<>();
        extras.put("newsid", 321);
        android.setExtras(extras);
        notification.setAndroid(android);
        pushBody.setNotification(notification);

        Message message = new Message();
        message.setMsg_content("HiHiHi, JPush");
        message.setContent_type("text");
        message.setTitle("MSG");
        extras = new HashMap<>();
        extras.put("hell", "world");
        message.setExtras(extras);
        pushBody.setMessage(message);

        String jsonStr = JsonUtil.toJson(pushBody);

        return OkHttpClientHelper.post(J_PUSH_URL, header, jsonStr);

    }

    @Data
    static class PushBody {
        String cid;
        String platform;
        //Map<String, List<String>> audience;
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

    public static void main(String[] args) {
        String result = JPushHelper.push();
        System.out.println(result);

    }
}
