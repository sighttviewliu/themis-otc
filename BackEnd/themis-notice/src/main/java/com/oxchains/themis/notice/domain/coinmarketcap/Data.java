package com.oxchains.themis.notice.domain.coinmarketcap;

/**
 * @author gaoyp
 * @create 2018/7/11  14:16
 **/
@lombok.Data
public class Data {
    private Quotes quotes;
    private String name;    //数字货币全名
    private String symbol;  //数字货币缩写
}
