package com.oxchains.themis.notice.domain.rate;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: Gaoyp
 * @Description:
 * @Date: Create in 下午2:33 2018/7/7
 * @Modified By:
 */
@Data
@Entity
@Table(name = "cny_usd_rate")
public class CNYDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String rate;
    private String date;

}
