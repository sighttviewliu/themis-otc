package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/7/16  14:19
 **/
@Entity
@Data
@Table(name = "coin_type")
public class CoinType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String coinType;

}
