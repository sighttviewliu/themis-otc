package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/28.
 * @author huohuo
 */
@Repository
public interface OrderCommentRepo extends CrudRepository<OrderComment,Long>{
    OrderComment findOrderCommentByOrderId(String orderId);
}
