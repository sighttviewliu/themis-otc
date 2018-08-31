package com.oxchains.themis.notice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gaoyp
 * @create 2018/7/10  11:42
 **/
@Getter
@AllArgsConstructor
public enum Coin {

    BTC(1L,"BTC"),ETH(2L,"ETH");

    private Long status;
    private String coinType;

}
