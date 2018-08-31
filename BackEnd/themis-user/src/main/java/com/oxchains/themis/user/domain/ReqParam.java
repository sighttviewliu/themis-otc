package com.oxchains.themis.user.domain;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-06-22 15:11
 * @name ReqParam
 * @desc:
 */
@Data
public class ReqParam {
    private Long id;
    private Long userId;
    private String mobilephone;
    private Integer enabled;

    private String resetkey;
    private String password;
}
