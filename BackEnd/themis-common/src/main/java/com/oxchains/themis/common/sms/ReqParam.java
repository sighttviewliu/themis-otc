package com.oxchains.themis.common.sms;

import lombok.Data;

/**
 * @author gaoyp
 * @create 2018/8/28  18:29
 **/
@Data
public class ReqParam {

    private String mobile;
    private String content;
    private String realUrl;

}
