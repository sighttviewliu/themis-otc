package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderEvidence;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by huohuo on 2017/10/31.
 *
 * @author huohuo
 */
@Repository
public interface OrderEvidenceRepo extends CrudRepository<OrderEvidence, Long> {
    OrderEvidence findByOrderId(String orderId);
}
