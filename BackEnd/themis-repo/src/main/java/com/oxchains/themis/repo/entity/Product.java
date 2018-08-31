package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-07-12 15:35
 * @name Product
 * @desc:
 */
@Entity
@Data
@Table(name = "tbl_biz_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String code;

    private Long organId;
    private String name;
    private String author;
    private String telephone;
    private String mobilephone;
    private String qq;
    private String wechat;
}
