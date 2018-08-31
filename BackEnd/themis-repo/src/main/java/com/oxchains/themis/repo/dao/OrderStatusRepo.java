package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.order.OrderStatus;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccl
 * @time 2018-08-23 13:31
 * @name OrderStatusRepo
 * @desc:
 */
public interface OrderStatusRepo extends CrudRepository<OrderStatus, Long>{
}
