package com.oxchains.themis.notice.dao.coinmarketcap;

import com.oxchains.themis.notice.domain.coinmarketcap.CNYDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gaoyp
 * @create 2018/7/11  14:53
 **/
@Repository
public interface CNYDetailsDao extends CrudRepository<CNYDetails,Long> {

    CNYDetails findByCoinType(String coinType);

}
