package com.oxchains.themis.blockinfo.ethereum.repo;

import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author ccl
 * @time 2018-05-22 15:31
 * @name BlockInfoRepo
 * @desc:
 */
@Repository
public interface EthBlockInfoRepo extends CrudRepository<EthBlockInfo,Long> {
    EthBlockInfo findByHash(String hash);
    EthBlockInfo findByNumber(BigInteger number);
    Page<EthBlockInfo> findAll(Pageable pageable);
}
