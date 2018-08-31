package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author ccl
 * @time 2018-06-04 13:49
 * @name AddressVO
 * @desc:
 */
@Data
public class AddressVO {
    private Long userId;
    private Set<Map<String,String>> btcAddress;
    private Set<Map<String,String>> ethAddress;

    public AddressVO() {
    }

    public AddressVO(Long userId, Set<Map<String,String>> btcAddress, Set<Map<String,String>> ethAddress) {
        this.userId = userId;
        this.btcAddress = btcAddress;
        this.ethAddress = ethAddress;
    }
}
