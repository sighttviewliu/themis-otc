package com.oxchains.themis.notice.domain.bitstamp;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/7/11  17:58
 **/
@Data
@Table(name = "bitstamp_market")
@Entity
public class BitstampMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cashName;
    private String last;
    @Column(name = "savetime")
    private String currentTime;
    @Column(name = "cointype")
    private String coinType;
}
