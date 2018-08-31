package com.oxchains.themis.notice.dao.coinmarketcap;

import com.oxchains.themis.notice.domain.coinmarketcap.JPYDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gaoyp
 * @create 2018/7/11  14:54
 **/
@Repository
public interface JPYDetailsDao extends CrudRepository<JPYDetails,Long> {

    JPYDetails findByCoinType(String coinType);

}
