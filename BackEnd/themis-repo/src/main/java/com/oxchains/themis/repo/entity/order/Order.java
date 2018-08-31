package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/23.
 *
 * @author huohuo
 */

@Entity
@Table(name = "tbl_biz_order")
@Data
public class Order implements Serializable {
    @Id
    @Column(name = "id", length = 10)
    private String id;         //订单编号
    private BigDecimal money;  //订单金额
    private Long createTime;  //下单时间
    private Long finishTime;//完成时间
    private BigDecimal amount; //交易数量
    private BigDecimal price; //交易价格
    private String paymentId; //支付方式编号  1 现金 2 转账 3 支付宝 4 微信 5 Apple Pay
    private Long vcurrencyId; //数字货币币种 1 比特币
    private Long currencyId;  //纸币币种    1  人民币 2  美元
    private Long buyerId;     // 买家id
    private String buyerAddr; //买家收币地址
    private Long sellerId;    //卖家id
    private String sellerAddr; //卖家收币地址
    private Integer orderStatus; // 订单状态    1  待确认 2 待付款  3 待收货 4  待评价 5 完成 6  已取消 7 等待卖家退款 8 仲裁中
    private Long noticeId;
    private int arbitrate;   //是否在仲裁中 默认 0： 不在仲裁中 1： 在仲裁中 2:仲裁结束
    private String uri;
    private Integer type;
    private String trusteeAddress;// 托管地址
    private boolean push = false;// 已托管，通知买家付款的推送
    private boolean transactionPush = false;// 已自动转账的推送
    private Integer orderLock = 0;// 锁定订单，只能走仲裁流程

    public Order(String id, BigDecimal money, Long createTime, BigDecimal amount, String paymentId, Long vcurrencyId, Long currencyId, Long buyerId, Long sellerId, Integer orderStatus, Long noticeId, int arbitrate) {
        this.id = id;
        this.money = money;
        this.createTime = createTime;
        this.amount = amount;
        this.paymentId = paymentId;
        this.vcurrencyId = vcurrencyId;
        this.currencyId = currencyId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.orderStatus = orderStatus;
        this.noticeId = noticeId;
        this.arbitrate = arbitrate;
    }

    public Order(String id, BigDecimal money, Long createTime, BigDecimal amount, String paymentId, Long vcurrencyId, Long currencyId, Long buyerId,
                 Long sellerId, Integer orderStatus, Long noticeId, int arbitrate, String buyerAddr, String sellerAddr, Integer type) {
        this.id = id;
        this.money = money;
        this.createTime = createTime;
        this.amount = amount;
        this.paymentId = paymentId;
        this.vcurrencyId = vcurrencyId;
        this.currencyId = currencyId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.orderStatus = orderStatus;
        this.noticeId = noticeId;
        this.arbitrate = arbitrate;
        this.buyerAddr = buyerAddr;
        this.sellerAddr = sellerAddr;
        this.type = type;
    }

    public Order() {
    }


}
