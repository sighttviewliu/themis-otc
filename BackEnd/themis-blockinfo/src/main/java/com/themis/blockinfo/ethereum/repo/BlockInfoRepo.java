package com.themis.blockinfo.ethereum.repo;

import com.themis.blockinfo.ethereum.entity.BlockInfo;
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
public interface BlockInfoRepo extends CrudRepository<BlockInfo,Long> {
    BlockInfo findByHash(String hash);
    BlockInfo findByNumber(BigInteger number);
    Page<BlockInfo> findAll(Pageable pageable);
}
