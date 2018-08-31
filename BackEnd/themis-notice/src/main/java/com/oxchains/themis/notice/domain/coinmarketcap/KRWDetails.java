package com.oxchains.themis.notice.domain.coinmarketcap;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/7/11  14:35
 **/
@Data
@Entity
@Table(name = "coinmarketcap_krw_market")
public class KRWDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String price;
    @Column(name = "savetime")
    private String saveTime;
    @Column(name = "cointype")
    private String coinType;

    @Transient
    private String cashName;
}
