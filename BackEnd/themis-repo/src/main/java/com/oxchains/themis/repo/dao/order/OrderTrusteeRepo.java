package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderTrustee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-06-14 16:32
 * @name OrderTrusteeRepo
 * @desc:
 */
public interface OrderTrusteeRepo extends CrudRepository<OrderTrustee,String> {
    OrderTrustee findByOrderId(String orderId);
}
