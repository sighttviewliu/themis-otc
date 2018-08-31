package com.oxchains.themis.notice.domain.bitfinex;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/7/12  11:20
 **/
@Data
@Table(name = "bitfinex_market")
@Entity
public class BitfinexMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cashName;
    @Column(name = "lastprice")
    private String last_price;
    @Column(name = "savetime")
    private String currentTime;
    @Column(name = "cointype")
    private String coinType;
}
