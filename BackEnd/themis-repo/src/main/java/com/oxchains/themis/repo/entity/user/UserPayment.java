package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-05-31 11:36
 * @name UserPayment
 * @desc:
 */
@Data
@Entity
@Table(name = "user_payment")
public class UserPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long createTime;
    //private Long updateTime;

    @Column(length = 32)
    private String username;

    @Column (length = 32)
    private String payment;
    @Column(length = 32)
    private String paymentName;

    @Column(length = 64)
    private String paymentQr;

    private Integer ptype;

    private Integer enabled = 1;
}
