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
@Table(name = "tbl_biz_newstip")
public class NewsTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 1 web
     * 2 app
     */
    @Column(length = 1, columnDefinition = "int(1) COMMENT '1 web, 2 app'")
    private Integer itemId;

    private Long createTime;

    @Column(length = 1, columnDefinition = "int(1) COMMENT '1 news, 2 notice'")
    private Integer type;

    @Column(columnDefinition = "varchar(64) COMMENT '标题'")
    private String title;

    @Column(columnDefinition = "varchar(2048) COMMENT '内容'")
    private String content;

    @Column(columnDefinition = "varchar(64) COMMENT '英文标题'")
    private String titleEn;

    @Column(columnDefinition = "varchar(2048) COMMENT '英文内容'")
    private String contentEn;

    @Column(columnDefinition = "varchar(128) COMMENT '链接'")
    private String url;

    @Column(columnDefinition = "varchar(32) COMMENT '来源'")
    private String source;

    @Column(length = 1, columnDefinition = "int(1) COMMENT '1-6 为新闻的位置，7表示为不首页展示新闻'")
    private Integer position = 7;

    @Column(columnDefinition = "varchar(128) COMMENT '图片,多图用英文字符,分隔'")
    private String image;

    @Column(length = 1, columnDefinition = "int(1) COMMENT '1 展示中, 0 下架'")
    private Integer enabled = 1;
}
