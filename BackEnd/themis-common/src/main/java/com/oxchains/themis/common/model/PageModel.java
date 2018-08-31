package com.oxchains.themis.common.model;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-06-11 16:55
 * @name PageModel
 * @desc:
 */
@Data
public class PageModel {
    private int pageCount = 0;
    private long totalCount = 0L;
    private Object data;

    public PageModel(int pageCount, Object data) {
        this.pageCount = pageCount;
        this.data = data;
    }

    public PageModel(int pageCount, long totalCount, Object data) {
        this.pageCount = pageCount;
        this.totalCount = totalCount;
        this.data = data;
    }
}
