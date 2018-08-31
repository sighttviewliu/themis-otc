package com.oxchains.themis.repo.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-05-15 10:43
 * @name NationRegion
 * @desc:
 */
@Data
@Entity
@Table(name = "nation_region")
public class NationRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String ename;
    private String abbreviation;//简写
    private Short areaCode;

}
