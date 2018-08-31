package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderExchangeFragments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anonymity
 * @create 2018-08-01 13:31
 **/
@Repository
public interface OrderExchangeFragmentsRepo extends CrudRepository<OrderExchangeFragments, Long> {
    List<OrderExchangeFragments> findByOrderId(String orderId);
    OrderExchangeFragments findByOrderIdAndUserId(String orderId, Long userId);
    List<OrderExchangeFragments> findByPush(boolean push);
    List<OrderExchangeFragments> findByOrderIdAndPush(String orderId, boolean push);
}
