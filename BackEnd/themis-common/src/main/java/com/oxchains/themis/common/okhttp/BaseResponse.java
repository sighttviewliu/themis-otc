package com.oxchains.themis.common.okhttp;

/**
 * @author ccl
 * @time 2018-04-10 15:08
 * @name BaseResponse
 * @desc:
 */
public class BaseResponse implements IResponse{
    public final int STATE_UNKNOWN_ERROR = 100001;

    /**
     * status code
     */
    private int code;

    /**
     * response data
     */
    private String data;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }
}
