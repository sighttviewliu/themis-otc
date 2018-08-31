package com.oxchains.themis.blockinfo.ethereum.repo;

import com.oxchains.themis.blockinfo.ethereum.entity.EthTransactionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

/**
 * @author ccl
 * @time 2018-06-28 16:42
 * @name EthTransactionInfoRepo
 * @desc:
 */
public interface EthTransactionInfoRepo extends CrudRepository<EthTransactionInfo, String> {
    @Query(value = " select  s from EthTransactionInfo as s where  (s.fromAddr = :address or s.toAddr = :address )")
    Page<EthTransactionInfo> findByTransactionRecordByAddress(@Param("address") String address, Pageable pageable);

    Page<EthTransactionInfo> findAll(Pageable pageable);

    @Query(value = " select *  from eth_transaction_info as b order by b.timestamp desc limit ?, ? ", nativeQuery = true)
    List<EthTransactionInfo> findEthTransactionInfosByPage(Integer pageNo, Integer pageSize);

    Page<EthTransactionInfo> findByBlockNumber(BigInteger blockNumber, Pageable pageable);
    List<EthTransactionInfo> findByBlockNumber(BigInteger blockNumber);
    List<EthTransactionInfo> findByBlockNumberIn(List<BigInteger> blockNumbers);
    Page<EthTransactionInfo> findByBlockHash(String blockHash, Pageable pageable);


}
