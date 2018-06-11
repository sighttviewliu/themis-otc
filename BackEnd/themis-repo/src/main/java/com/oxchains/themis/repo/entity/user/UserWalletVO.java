package com.oxchains.themis.repo.entity.user;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-06-07 10:33
 * @name UserWallet
 * @desc:
 */
@Data
public class UserWalletVO extends UserWallet {
    private String walletName;

    public UserWalletVO() {
    }

    public UserWalletVO(UserWallet userWallet) {
        this.setId(userWallet.getId());
        this.setUserId(userWallet.getUserId());
        this.setAddress(userWallet.getAddress());
        this.setCreateTime(userWallet.getCreateTime());
        this.setType(userWallet.getType());
        this.setBalance(userWallet.getBalance());
    }
    public UserWallet toUserWallet(){
        UserWallet userWallet = new UserWallet();
        userWallet.setId(this.getId());
        userWallet.setAddress(this.getAddress());
        userWallet.setBalance(this.getBalance());
        userWallet.setCreateTime(this.getCreateTime());
        userWallet.setType(this.getType());
        userWallet.setUserId(this.getUserId());
        return userWallet;
    }
}
