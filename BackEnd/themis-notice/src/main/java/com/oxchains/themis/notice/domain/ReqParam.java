package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.Transient;

/**
 * @author ccl
 * @time 2018-07-07 15:52
 * @name ReqParam
 * @desc:
 */

@Data
public class ReqParam {
    private Long location;
    private Long currency;
    private String payType;
    private Long searchType;
    private Integer pageSize;
    private Integer pageNum;

    private Long coinType;
}
