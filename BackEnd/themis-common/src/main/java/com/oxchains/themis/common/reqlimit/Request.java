package com.oxchains.themis.common.reqlimit;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-05-30 10:38
 * @name Request
 * @desc:
 */
@Data
public class Request {
    private Integer count;
    private Long time;
    private Long limit;

    public Request(Integer count, Long time, Long limit) {
        this.count = count;
        this.time = time;
        this.limit = limit;
    }

    public Request() {
    }
    public void increase(){
        if (count != null) {
            this.count += 1;
        }
    }
}
