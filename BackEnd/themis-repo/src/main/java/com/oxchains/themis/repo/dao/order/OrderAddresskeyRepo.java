package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderAddresskeys;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Repository
public interface OrderAddresskeyRepo extends CrudRepository<OrderAddresskeys,Long> {
    OrderAddresskeys findOrderAddresskeysByOrderId(String id);
}
