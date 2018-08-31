package com.oxchains.themis.common.geetest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ccl
 * @time 2018-06-19 16:12
 * @name GeetestPo
 * @desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeetestPo {

    private String geetestChallenge;

    private String geetestValidate;

    private String geetestSeccode;

    private Integer gtServerStatus;

}
