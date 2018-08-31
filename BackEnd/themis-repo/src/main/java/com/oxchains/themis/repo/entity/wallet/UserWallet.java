package com.oxchains.themis.repo.entity.wallet;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ccl
 * @time 2018-06-07 10:33
 * @name UserWallet
 * @desc:
 */
@Data
@Entity
@Table(name = "user_wallet")
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long userId;
    /**
     * default 1 GET
     */
    private Integer type = 1;
    private String address;
    private Double balance;;
}
