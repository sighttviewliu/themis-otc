package com.oxchains.themis.common.okhttp;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ccl
 * @time 2018-04-10 15:22
 * @name BaseRequest
 * @desc:
 */
public class BaseRequest implements IRequest{
    private String method = POST;
    private String url;
    private Map<String, String> header;
    private Map<String, Object> body;

    public BaseRequest(String url) {
        this.url = url;
        header = new HashMap<>();
        body = new HashMap<>();
    }

    public BaseRequest() {
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setHeader(String key, String value) {
        header.put(key, value);
    }

    @Override
    public void setHeader(Map<String, String> params){
        header.putAll(params);
    }

    @Override
    public void setBody(String key, String value) {
        body.put(key, value);
    }

    @Override
    public void setBody(Map<String, Object> params){
        body.putAll(params);
    }
    @Override
    public String getUrl() {
        if(GET.equals(method)){
            if(url.contains("?")){
                if(url.contains("${")){
                    for(String key : body.keySet()){
                        url = url.replace("${" + key + "}", body.get(key).toString());
                    }
                }
            }else {
                url = url +"?";
                for (String key : body.keySet()){
                    url += (key + "="+ body.get(key)+ "&");
                }
            }
        }
        return url;
    }

    @Override
    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public Object getBody() {
        if(null != body){
            return new Gson().toJson(body, HashMap.class);
        }else {
            return "{}";
        }
    }
}
