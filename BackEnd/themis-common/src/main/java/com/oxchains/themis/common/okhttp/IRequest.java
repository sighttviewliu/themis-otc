package com.oxchains.themis.common.okhttp;

import java.util.Map;

/**
 * @author ccl
 * @time 2018-04-10 15:16
 * @name IRequest
 * @desc:
 */
public interface IRequest {
    String POST = "POST";
    String GET = "GET";

    /**
     * set request method
     * @param method
     */
    void setMethod(String method);

    /**
     * set request header
     * @param key
     * @param value
     */
    void setHeader(String key, String value);

    void setHeader(Map<String, String> params);

    /**
     * set request params
     * @param key
     * @param value
     */
    void setBody(String key, String value);

    void setBody(Map<String, Object> params);

    /**
     * url
     * @return
     */
    String getUrl();

    /**
     * header
     * @return
     */
    Map<String, String> getHeader();

    /**
     * body
     * @return
     */
    Object getBody();

}
