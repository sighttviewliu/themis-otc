package com.oxchains.themis.notice.domain.coinmarketcap;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/7/11  14:34
 **/
@Data
@Entity
@Table(name = "coinmarketcap_usd_market")
public class USDDetails {
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
