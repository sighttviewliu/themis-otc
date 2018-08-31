package com.oxchains.themis.common.okhttp;

/**
 * @author ccl
 * @time 2018-04-10 15:06
 * @name IResponse
 * @desc:
 */
public interface IResponse {
    int STATE_OK = 200;
    /**
     * response status code
     * @return
     */
    int getCode();

    /**
     * response data
     * @return
     */
    String getData();
}
