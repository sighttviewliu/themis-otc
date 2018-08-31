package com.oxchains.themis.user.domain;

import lombok.Data;

/**
 * @author ccl
 * @time 2017-11-06 10:32
 * @name UserTrust
 * @desc:
 */
@Data
public class UserTrust {

    private Long fromUserId;
    private Long toUserId;
    private String fromUserName;
    private String toUserName;

    /**
     * 交易次数
     */
    private Integer txNum;
    /**
     * 好评次数
     */
    private Integer goodDesc;
    /**
     * 差评次数
     */
    private Integer badDesc;
    /**
     * 第一次购买时间
     */
    private Long firstBuyTime;
    /**
     * 信任次数
     */
    private Integer believeNum;
    private Double buyAmount;
    private Double sellAmount;

    /**
     * 交易次数1
     */
    private Integer txToNum;

}
