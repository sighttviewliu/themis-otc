package com.oxchains.themis.notice.dao.bitfinex;

import com.oxchains.themis.notice.domain.bitfinex.BitfinexMarket;
import com.oxchains.themis.notice.domain.bitstamp.BitstampMarket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gaoyp
 * @create 2018/7/12  11:23
 **/
@Repository
public interface BitfinexMarketDao extends CrudRepository<BitfinexMarket,Long> {

    BitfinexMarket findByCashName(String cashName);

    BitfinexMarket findByCashNameAndCoinType(String cashName,String coinType);

}
