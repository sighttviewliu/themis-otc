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

    @Column(unique = true)
    private Long userId;

    @Column(length = 19)
    private String bankCard;

    @Column(length = 32)
    private String aliPay;
    private String aliPayQr;


}
