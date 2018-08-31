package com.oxchains.themis.notice.domain;

import lombok.Data;

/**
 * @author gaoyp
 * @create 2018/8/1  15:44
 **/
@Data
public class RequestParam {

    private Long userId;
    private Integer pageSize;
    private Integer pageNum;

}
