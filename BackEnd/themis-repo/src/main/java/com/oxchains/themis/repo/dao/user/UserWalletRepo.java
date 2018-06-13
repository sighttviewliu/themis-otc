package com.oxchains.themis.repo.dao.user;


import com.oxchains.themis.repo.entity.user.UserWallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-06-07 10:46
 * @name UserWalletRepo
 * @desc:
 */
@Repository
public interface UserWalletRepo extends CrudRepository<UserWallet,Long> {
    List<UserWallet> findByUserId(Long userId);
    UserWallet findByUserIdAndType(Long userId, Integer type);
}