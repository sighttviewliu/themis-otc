package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author luoxuri
 * @create 2017-10-24 19:06
 **/
@Entity
@Data
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userid")
    private Long userId;       // user表的id

    private String loginname;   // 其实这个loginname是user表的username

    @Column(name = "noticetype")
    private Long noticeType;      // 购买BTC/出售BTC

    private Long location;        // 所在地

    private Long coinType;        //交易的数字货币币种选择 1、BTC 2、ETH 3、GET

    private Long currency;        // 货币类型   1、CNY 2、USD 3、JPY 4、KRW 5、HKD

    private Double premium = 0D;         // 溢价

    private BigDecimal price;           // 价格

    @Column(name = "minprice")
    private BigDecimal minPrice;        // 最低价

    @Column(name = "mintxlimit")
    private BigDecimal minTxLimit;      // 最小交易限额

    @Column(name = "maxtxlimit")
    private BigDecimal maxTxLimit;      // 最大交易限额

    @Column(name = "paytype")
    private String payType;         // 支付方式/付款方式

    @Column(name = "noticecontent")
    private String noticeContent;   // 公告内容

    @Column(name = "validpaytime")
    private Long validPayTime = 1800000L;      // 付款期限，默认30分钟的毫秒值1800000，字段废弃

    @Column(name = "noticetime")
    private Long noticeTime =  604800000L;

    @Column(name = "isdelete")
    private Long isDelete = 0L;                   //公告是否被删除，0存在  1删除  只有下架公告才能被删除

    @Transient
    private Long searchType;

    @Column(name = "txstatus")
    private Integer txStatus = 0;           // 交易状态，默认0:非交易和交易进行,2:交易完成

    @Column(name = "createtime")
    private Long createTime;

    private Long exchange;           //交易所  1、average  2、coinmarketcap  3、bitstamp   4、bitfinex

    // 以下是暂时展示的数据，实际数据在从对应的表中获取
    @Transient
    private Integer txNum;

    @Transient
    private Integer trustNum;

    @Transient
    private Integer trustPercent;

    @Transient
    private Integer goodPercent;

    @Transient
    private String searchName;

    @Transient
    private String imageName;

    @Transient
    private double txAmount;

    @Transient
    private BigDecimal exPrice;       //交易所价格

    public Notice(){}
}
