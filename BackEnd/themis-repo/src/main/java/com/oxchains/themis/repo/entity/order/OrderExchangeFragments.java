package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2018-08-01 13:26
 **/
@Data
@Entity
@Table(name = "order_exchange_fragments")
public class OrderExchangeFragments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 上传碎片的用户的userId
     */
    private Long userId;

    /**
     * 交易对方的userId
     */
    private Long otherId;

    /**
     * 5份密钥碎片，中间用','(英文的逗号)隔开，数据量大，
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LongText")
    private String fragments;

    /**
     * 是否给交易对方推送了消息
     */
    private boolean push = false;

    public OrderExchangeFragments() {
    }

    public OrderExchangeFragments(String orderId, Long userId, Long otherId, String fragments) {
        this.orderId = orderId;
        this.userId = userId;
        this.otherId = otherId;
        this.fragments = fragments;
    }
}
