package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-30 19:00
 * @nameUserTxDetail
 * @desc:
 */
@Data
public class UserTxDetailVO extends UserTxDetail{

    private String avatar;
    private String username;
    private int unstatus = 1;
    public UserTxDetailVO() {
    }

    public UserTxDetailVO(UserTxDetail detail) {
        this.setId(detail.getId());
        this.setUserId(detail.getUserId());
        this.setTxNum(detail.getTxNum());
        this.setSuccessCount(detail.getSuccessCount());
        this.setGoodDesc(detail.getGoodDesc());
        this.setBadDesc(detail.getBadDesc());
        this.setBelieveNum(detail.getBelieveNum());
        this.setBuyAmount(detail.getBuyAmount());
        this.setSellAmount(detail.getSellAmount());
        this.setFirstBuyTime(detail.getFirstBuyTime());
    }

}
