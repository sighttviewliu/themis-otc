package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import java.util.Set;

/**
 * @author oxchains
 * @time 2018-06-04 13:49
 * @name UserAddressVO
 * @desc:
 */
@Data
public class AddressVO {
    private Long userId;
    private Set<String> btcAddress;
    private Set<String> ethAddress;

    public AddressVO() {
    }

    public AddressVO(Long userId, Set<String> btcAddress, Set<String> ethAddress) {
        this.userId = userId;
        this.btcAddress = btcAddress;
        this.ethAddress = ethAddress;
    }
}
