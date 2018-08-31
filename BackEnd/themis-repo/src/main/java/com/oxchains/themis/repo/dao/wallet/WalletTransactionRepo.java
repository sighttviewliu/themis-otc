package com.oxchains.themis.repo.dao.wallet;


import com.oxchains.themis.repo.entity.wallet.UserWallet;
import com.oxchains.themis.repo.entity.wallet.WalletTransaction;
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
public interface WalletTransactionRepo extends CrudRepository<WalletTransaction,Long> {
    List<WalletTransaction> findByUserId(Long userId);
    Page<WalletTransaction> findByUserId(Long userId, Pageable pageable);
    List<WalletTransaction> findByUserIdAndFromAddr(Long userId, String from);

    List<WalletTransaction> findByFromAddrIsNullAndHashIsNotNull();

}