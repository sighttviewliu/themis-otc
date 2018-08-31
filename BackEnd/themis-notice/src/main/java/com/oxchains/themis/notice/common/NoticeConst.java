package com.oxchains.themis.notice.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @author luoxuri
 * @create 2017-11-03 19:11
 **/
public interface NoticeConst {
    /**
     * 公告类型
     */
    enum NoticeType{
        BUY(1L, "购买"), SELL(2L, "出售");
        private Long status;
        private String name;
        NoticeType(Long status, String name){
            this.status = status;
            this.name = name;
        }
        public Long getStatus() {
            return status;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * 搜索类型
     */
    enum SearchType{
        ONE(1L, "搜公告"), TWO(2L, "搜用户");
        private Long status;
        private String name;

        SearchType(Long status, String name) {
            this.status = status;
            this.name = name;
        }

        public Long getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 支持的交易币种
     * 比特币、以太坊、Themis
     */

    enum CoinType{
        BTC(1L,"BTC"),ETH(2L,"ETH"),GET(3L,"GET");

        private Long status;
        private String name;

        CoinType(Long status, String name) {
            this.status = status;
            this.name = name;
        }

        public Long getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }

    //下架公告是否已经被删除，0未删除，1已删除
    enum IsDelete{
        EXIST(0L),NOTEXIST(1L);


        private Long status;

        IsDelete(Long status) {
            this.status = status;
        }

        public Long getStatus() {
            return status;
        }
    }

    //交易所
    enum Exchange{
        AVERAGE(1L),COINMARKETCAP(2L),BITSTAMP(3L),BITFINEX(4L);

        private Long status;

        Exchange(Long status) {
            this.status = status;
        }

        public Long getStatus() {
            return status;
        }
    }
}
