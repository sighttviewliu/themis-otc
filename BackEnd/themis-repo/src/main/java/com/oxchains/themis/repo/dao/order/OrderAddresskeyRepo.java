package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderAddresskey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author ccl
 * @time 2018-05-08 17:34
 * @name OrderAddresskeyRepo
 * @desc:
 */
@Repository
public interface OrderAddresskeyRepo extends CrudRepository<OrderAddresskey,Long> {
    OrderAddresskey findOrderAddresskeysByOrderId(String id);
}
