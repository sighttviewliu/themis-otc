package com.oxchains.themis.user.domain;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author ccl
 * @time 2018-06-28 13:49
 * @name UserAddressVO
 * @desc:
 */

@Data
public class UserAddrVO {
    private Long userId;
    private List<Address> addresses;

    public UserAddrVO() {
    }

    public UserAddrVO(Long userId) {
        this.userId = userId;
    }

    public UserAddrVO(Long userId, List<Address> addresses) {
        this.userId = userId;
        this.addresses = addresses;
    }
}
