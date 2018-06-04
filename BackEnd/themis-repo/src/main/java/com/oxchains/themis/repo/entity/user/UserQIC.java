package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-05-29 18:31
 * @name UserQIC
 * @desc:
 */
@Data
@Entity
@Table(name = "user_qic")
public class UserQIC {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private String realName;

    @Column(unique = true)
    private String idNo;

    //身份证照片正面的 图片url
    private String photoFront;

    //身份证照片背面的 图片url
    private String photoBack;

    //查询时返回的头像照片base64编码
    @Column(length = 20480)
    private String photo;
}
