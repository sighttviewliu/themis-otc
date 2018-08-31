package com.oxchains.themis.notice.dao.average;

import com.oxchains.themis.notice.domain.average.AverageMarket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gaoyp
 * @create 2018/7/10  13:47
 **/
@Repository
public interface AverageMarketDao extends CrudRepository<AverageMarket,Long> {

    AverageMarket findByCashName(String cashName);

    AverageMarket findByCashNameAndCoinType(String cashName,String coinType);


}
