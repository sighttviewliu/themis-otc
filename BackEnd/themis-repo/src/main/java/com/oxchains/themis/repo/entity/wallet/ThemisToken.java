package com.oxchains.themis.repo.entity.wallet;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-06-28 11:18
 * @name ThemisToken
 * @desc:
 */

@Data
@Entity
@Table(name = "themis_token")
public class ThemisToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long createTime;
    @Column(unique = true)
    private String address;
    private Double amount;
    private String txhash;


}
