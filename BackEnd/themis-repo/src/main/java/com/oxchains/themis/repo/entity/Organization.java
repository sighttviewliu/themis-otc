package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-07-12 11:19
 * @name OrganInfo
 * @desc:
 */
@Entity
@Data
@Table(name = "tbl_biz_organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long pid;
    private String name;
    private String address;
    //private String profile;
    private Integer enabled = 1;
    @Column(length = 2048)
    private String description;

}
