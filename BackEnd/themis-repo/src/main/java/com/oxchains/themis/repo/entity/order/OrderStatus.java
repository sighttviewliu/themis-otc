package com.oxchains.themis.repo.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-08-23 13:28
 * @name OrderStatus
 * @desc:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 21)
    private String orderId;

    private Long userId;

    private Integer ostatus;

    private Long createTime;

}
