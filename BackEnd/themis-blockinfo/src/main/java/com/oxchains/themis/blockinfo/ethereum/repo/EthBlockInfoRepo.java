package com.oxchains.themis.blockinfo.ethereum.repo;

import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-22 15:31
 * @name BlockInfoRepo
 * @desc:
 */
@Repository
public interface EthBlockInfoRepo extends CrudRepository<EthBlockInfo, BigInteger> {
    EthBlockInfo findByHash(String hash);

    EthBlockInfo findByNumber(BigInteger number);

    Page<EthBlockInfo> findAll(Pageable pageable);


    @Query(value = " select *  from eth_block_info as b order by b.number desc limit ?, ? ", nativeQuery = true)
    List<EthBlockInfo> findEthBlockInfosByPage(Integer pageNo, Integer pageSize);

    @Query(value = " select b.number  from EthBlockInfo as b where  (b.number >= :startNum or b.number <= :endNum )")
    List<BigInteger> findEthBlockNumber(@Param("startNum") BigInteger startNum, @Param("endNum") BigInteger endNum);

    @Query(value = " select b  from EthBlockInfo as b where  (b.number >= :startNum or b.number <= :endNum )")
    List<EthBlockInfo> findEthBlockInfoByNumber(@Param("startNum") BigInteger startNum, @Param("endNum") BigInteger endNum);

    @Query(value = " delete from EthBlockInfo as b where (b.number >= :startNum and b.number < :endNum )")
    void deleteByNumber(@Param("startNum") BigInteger startNum, @Param("endNum") BigInteger endNum);


}
