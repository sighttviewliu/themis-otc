package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.CoinType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gaoyp
 * @create 2018/7/16  14:21
 **/
@Repository
public interface CoinTypeDao extends CrudRepository <CoinType,Long> {

    List<CoinType> findAll();
    CoinType findById(Long id);
}
