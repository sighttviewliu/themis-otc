package com.oxchains.themis.notice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Gaoyp
 * @Description:
 * @Date: Create in 下午2:45 2018/7/7
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum Const {

    CNY(1L,"CNY","Chinese Yuan"),USD(2L,"USD","United States Dollar"),JPY(3L,"JPY","Japanese Yen"),
    KRW(4L,"KRW","South Korean Won"),HKD(5L,"HKD","Hong Kong Dollar");

    private Long status;
    private String abbreviation;
    private String fullName;




}
