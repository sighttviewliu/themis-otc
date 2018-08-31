package com.oxchains.themis.common.okhttp;

import com.oxchains.themis.common.util.JsonUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author ccl
 * @time 2018-04-10 15:32
 * @name BaseOkHttpClient
 * @desc:
 */
public class BaseOkHttpClient {
    OkHttpClient mOkHttpClient = new OkHttpClient.Builder().build();

    public IResponse get(String url, Map<String, String> header, Map<String, Object> body) {
        IRequest request = new BaseRequest(url);
        request.setMethod(IRequest.GET);
        request.setHeader(header);
        request.setBody(body);
        IResponse response = get(request, false);
        return response;
    }

    public IResponse post(String url, Map<String, String> header, Map<String, Object> body) {
        IRequest request = new BaseRequest(url);
        request.setMethod(IRequest.POST);
        request.setHeader(header);
        request.setBody(body);
        IResponse response = post(request, false);
        return response;
    }


    public IResponse get(IRequest request, boolean foreCache) {
        // 指定请求方式
        request.setMethod(IRequest.GET);
        // 解析头部
        Request.Builder builder = new Request.Builder();
        Map<String, String> header = request.getHeader();
        for (String key : header.keySet()) {
            // 组装成 OkHttp 的 Header
            builder.header(key, header.get(key));
        }
        //获取url
        String url = request.getUrl();
        builder.url(url).get();
        Request okRequest = builder.build();
        // 执行 oKRequest
        return execute(okRequest);
    }

    public IResponse post(IRequest request, boolean foreCache) {
        // 指定请求方式
        request.setMethod(IRequest.POST);
        Request.Builder builder = new Request.Builder();
        MediaType mediaType = MediaType
                .parse("application/json; charset=utf-8");
        String json = JsonUtil.toJson(request.getBody());
        RequestBody body = RequestBody.create(mediaType, json);
        Map<String, String> header = request.getHeader();
        for (String key : header.keySet()) {
            builder.header(key, header.get(key));
        }
        builder.url(request.getUrl()).post(body);
        Request okRequest = builder.build();
        return execute(okRequest);
    }

    private IResponse execute(Request request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            // 设置状态码
            baseResponse.setCode(response.code());
            String body = response.body().string();
            // 设置响应数据
            baseResponse.setData(body);

        } catch (IOException e) {
            e.printStackTrace();
            baseResponse.setCode(baseResponse.STATE_UNKNOWN_ERROR);
            baseResponse.setData(e.getMessage());
        }
        return baseResponse;
    }

}
