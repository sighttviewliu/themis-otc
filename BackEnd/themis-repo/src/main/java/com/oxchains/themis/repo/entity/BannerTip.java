package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-07-12 11:41
 * @name BannerTip
 * @desc:
 */
@Entity
@Data
@Table(name = "tbl_biz_bannertip")
public class BannerTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createTime;

    /**
     * 1 web
     * 2 app
     */
    @Column(length = 1, columnDefinition = "int(1)")
    private Integer itemId;

    private String image;

    @Column(length = 1, columnDefinition = "int(1)")
    private Integer enabled = 1;
}
