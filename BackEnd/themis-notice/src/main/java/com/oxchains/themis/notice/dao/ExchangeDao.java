package com.oxchains.themis.notice.dao;

import com.oxchains.themis.notice.domain.Exchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gaoyp
 * @create 2018/8/22  15:40
 **/
@Repository
public interface ExchangeDao extends CrudRepository<Exchange,Long>{
}
