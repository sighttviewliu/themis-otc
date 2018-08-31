package com.oxchains.themis.repo.dao.wallet;


import com.oxchains.themis.repo.entity.wallet.UserWallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<UserWallet> findByUserId(Long userId, Pageable pageable);
    UserWallet findByUserIdAndType(Long userId, Integer type);
    UserWallet findByAddress(String address);
}