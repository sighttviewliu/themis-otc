package com.oxchains.themis.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2018-08-01 11:06
 **/
@Data
@Entity
@Table(name = "order_pri_key")
public class OrderPriKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID
     */
    private Long userId;
    /**
     * 订单
     */
    private String orderId;
    /**
     * 私钥/未处理
     */
    private String priKey;

    /**
     * 私钥/最终的
     */
    private String finalPriKey;

    public OrderPriKey() {
    }

    public OrderPriKey(Long userId, String orderId, String priKey) {
        this.userId = userId;
        this.orderId = orderId;
        this.priKey = priKey;
    }
}
