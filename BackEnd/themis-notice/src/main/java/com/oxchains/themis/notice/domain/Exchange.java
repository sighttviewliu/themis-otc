package com.oxchains.themis.notice.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author gaoyp
 * @create 2018/8/22  15:31
 * 记录支持的交易所数据
 **/
@Data
@Entity
@Table(name = "tbl_exchange")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String exchangeName;


}
