package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderPriKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anonymity
 * @create 2018-08-01 11:09
 **/
@Repository
public interface OrderPriKeyRepo extends CrudRepository<OrderPriKey, Long>{

    List<OrderPriKey> findByOrderId(String orderId);
    OrderPriKey findByOrderIdAndUserId(String orderId, Long userId);
}
