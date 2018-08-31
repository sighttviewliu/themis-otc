package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/25.
 *
 * @author huohuo
 */

@Data
@Entity
@Table(name = "order_arbitrate")
public class OrderArbitrate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Long createTime;
    private Long updateTime;
    private Long userId;
    @Column(length = 32)
    private String username;
    private Long winnerId;
    private String reason;
    private String attachment;
    private Integer status;
    private Integer identity;// 1.买家，2,卖家
    private Long dealer;

    public OrderArbitrate() {
    }

    public OrderArbitrate(String orderId, Long userId, Long winnerId, Integer status, String reason, String attachment, Integer identity) {
        this.orderId = orderId;
        this.userId = userId;
        this.winnerId = winnerId;
        this.status = status;
        this.reason = reason;
        this.attachment = attachment;
        this.identity = identity;
    }
}
