package com.oxchains.themis.notice.domain.coinmarketcap;

import lombok.Data;

/**
 * @author gaoyp
 * @create 2018/7/11  14:19
 **/
@Data
public class Quotes {

    private CNYDetails CNY;
    private HKDDetails HKD;
    private USDDetails USD;
    private JPYDetails JPY;
    private KRWDetails KRW;

}
