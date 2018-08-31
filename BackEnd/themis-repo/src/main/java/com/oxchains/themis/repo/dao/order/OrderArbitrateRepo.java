package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderArbitrate;
import org.hibernate.boot.model.source.spi.Orderable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/25.
 * @author huohuo
 */
@Repository
public interface OrderArbitrateRepo extends CrudRepository<OrderArbitrate,Long>, JpaSpecificationExecutor<OrderArbitrate> {
    Page<OrderArbitrate> findByUserIdAndStatusIsNot(Long userId, Integer status, Pageable pageable);
    OrderArbitrate findByUserIdAndOrderId(Long id, String orderId);
    List<OrderArbitrate> findByOrderId(String orderId);
    List<OrderArbitrate> findByOrOrderIdAndStatus(String id, Integer status);
    Page<OrderArbitrate> findByOrderId(String orderId, Pageable pageable);


    OrderArbitrate findOrderArbitrateById(Long id);

    Page<OrderArbitrate> findAll(Pageable pageable);
    Page<OrderArbitrate> findByStatus(Integer orderId, Pageable pageable);
    Page<OrderArbitrate> findByUsernameLike(String username, Pageable pageable);
    Page<OrderArbitrate> findByStatusAndUsernameLike(Integer status, String username, Pageable pageable);
    Page<OrderArbitrate> findByStatusAndUserIdIn(Integer status, List<Long> userIds, Pageable pageable);
    Page<OrderArbitrate> findByOrderIdAndStatus(String orderId, Integer status, Pageable pageable);

    List<OrderArbitrate> findAll(Sort sort);
}
