package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.TrusteeTransactionHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anonymity
 * @create 2018-08-23 16:55
 **/
@Repository
public interface TrusteeTransactionHashRepo extends CrudRepository<TrusteeTransactionHash, Long> {

    TrusteeTransactionHash findByTxHash(String txHash);
}
