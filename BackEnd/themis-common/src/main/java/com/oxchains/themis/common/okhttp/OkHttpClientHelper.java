package com.oxchains.themis.common.okhttp;

import com.oxchains.themis.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ccl
 * @time 2018-04-10 17:03
 * @name OkHttpClientHelper
 * @desc:
 */
@Slf4j
public class OkHttpClientHelper {
    private static final int STATE_OK = 200;
    private static OkHttpClient mOkHttpClient = new OkHttpClient.Builder().build();

    public static String get(String url, Map<String, String> header, Map<String, Object> params) {
        // 解析头部
        Request.Builder builder = new Request.Builder();
        if (null != header) {
            for (String key : header.keySet()) {
                // 组装成 OkHttp 的 Header
                builder.header(key, header.get(key));
            }
        }

        if (!url.contains("?") && null != params) {
            url = url + "?";
            for (String key : params.keySet()) {
                url += (key + "=" + params.get(key) + "&");
            }
            url = url.substring(0, url.length() - 1);
        }

        builder.url(url).get();
        Request okRequest = builder.build();
        return execute(okRequest);
    }

    public static String post(String url, Map<String, Object> params) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json");
        return post(url, header, params);
    }

    public static String post(String url, Map<String, String> header, Map<String, Object> params) {

        Request.Builder builder = new Request.Builder();
        MediaType mediaType = MediaType
                .parse("application/json; charset=utf-8");
        String bodyStr = JsonUtil.toJson(params);
        RequestBody body = RequestBody.create(mediaType, bodyStr);
        for (String key : header.keySet()) {
            builder.header(key, header.get(key));
        }
        builder.url(url).post(body);
        Request okRequest = builder.build();
        return execute(okRequest);
    }

    public static String post(String url, Map<String, String> header, String jsonStr) {

        Request.Builder builder = new Request.Builder();
        if (null != header && header.containsKey("Content-Type")) {

        } else {
            builder.header("Content-Type", "application/json");
        }
        MediaType mediaType = MediaType
                .parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, jsonStr);
        for (String key : header.keySet()) {
            builder.header(key, header.get(key));
        }
        builder.url(url).post(body);
        Request okRequest = builder.build();
        return execute(okRequest);
    }

    private static String execute(Request request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (STATE_OK == response.code()) {
                String body = response.body().string();
                return body;
            } else {
                throw new Exception("status code is not 200");
            }
        } catch (IOException e) {
            log.error("OkHttp Request Error: ", e);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
