package com.oxchains.themis.notice.domain;

import lombok.Data;

/**
 * @author gaoyp
 * @create 2018/8/1  22:03
 **/
@Data
public class MarketParam {

    private String currencyType;
    private String coinType;
    private String price;

    public MarketParam(String currencyType, String coinType, String price) {
        this.currencyType = currencyType;
        this.coinType = coinType;
        this.price = price;
    }
}
