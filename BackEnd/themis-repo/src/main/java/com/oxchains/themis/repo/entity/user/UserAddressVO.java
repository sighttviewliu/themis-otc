package com.oxchains.themis.repo.entity.user;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-06-04 13:09
 * @name UserAddress
 * @desc:
 */
@Data
public class UserAddressVO extends UserAddress{
        String name;

    public UserAddressVO() {
    }

    public UserAddressVO(UserAddress userAddress) {
        this.setAddress(userAddress.getAddress());
        this.setCreateTime(userAddress.getCreateTime());
        this.setType(userAddress.getType());
        this.setId(userAddress.getId());
        this.setUserId(userAddress.getUserId());
    }
}
