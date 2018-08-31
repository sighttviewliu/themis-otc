package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-06-04 13:09
 * @name UserAddress
 * @desc:
 */
@Data
@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long userId;
    private Integer type;//BTC ETH
    private String address;
    private String remark;
}
