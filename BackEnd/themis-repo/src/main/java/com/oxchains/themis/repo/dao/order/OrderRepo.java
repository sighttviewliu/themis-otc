package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huohuo on 2017/10/23.
 *
 * @author huohuo
 */
@Repository
public interface OrderRepo extends CrudRepository<Order,String>, JpaSpecificationExecutor<Order> {
    @Query(value = " select  s from Order as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus in ( :status) ")
    Page<Order> findOrdersByBuyerIdOrSellerIdAndOrderStatus(@Param("id") Long id, @Param("ids") Long ids, @Param("status") List<Integer> status, Pageable pageable);
    @Query(value = " select  s from Order as s where  (s.buyerId = :id or s.sellerId = :ids ) and s.orderStatus not in ( :status)")
    Page<Order> findOrdersByBuyerIdOrSellerIdAndOrderStatusIsNotIn(@Param("id") Long id, @Param("ids") Long ids, @Param("status") List<Integer> status, Pageable pageable);
    List<Order> findOrdersByOrderStatus(Integer status);
    List<Order> findOrdersByOrderStatusAndPush(Integer status, boolean push);
    List<Order> findOrdersByOrderStatusAndTransactionPush(Integer status, boolean push);
    List<Order> findByArbitrate(Integer status);

    List<Order> findByBuyerIdOrSellerId(Long buyId, Long sellerId);
    @Query(value = "select s from Order as s where (s.buyerId = :buyerId or s.sellerId = :sellerId) and s.vcurrencyId = :vcurrencyId")
    List<Order> findOrdersByUserIdAndVcurrencyId(@Param("buyerId") Long buyerId, @Param("sellerId") Long sellerId, @Param("vcurrencyId") Long vcurrencyId);

    @Query(value = " select  s from Order as s where  (s.buyerId = :id or s.sellerId = :ids )")
    Page<Order> findOrdersByUserId(@Param("id") Long id, @Param("ids") Long ids, Pageable pageable);

    @Query(value = " select  s from Order as s where  (s.buyerId = :buyerId) and s.orderStatus in (:status)")
    Page<Order> findOrdersByBuyerIdAndOrderStatusIn(@Param("buyerId") Long buyerId, @Param("status") List<Integer> status, Pageable pageable);

    @Query(value = " select  s from Order as s where  (s.buyerId = :buyerId) and s.orderStatus not in (:status)")
    Page<Order> findOrdersByBuyerIdAndOrderStatusNotIn(@Param("buyerId") Long buyerId, @Param("status") List<Integer> status, Pageable pageable);

    @Query(value = " select  s from Order as s where  (s.sellerId = :sellerId) and s.orderStatus in (:status)")
    Page<Order> findOrdersBySellerIdAndOrderStatusIn(@Param("sellerId") Long sellerId, @Param("status") List<Integer> status, Pageable pageable);

    @Query(value = " select  s from Order as s where  (s.sellerId = :sellerId) and s.orderStatus not in (:status)")
    Page<Order> findOrdersBySellerIdAndOrderStatusNotIn(@Param("sellerId") Long sellerId, @Param("status") List<Integer> status, Pageable pageable);

    int countByBuyerIdOrSellerId(Long buyId,Long sellerId);

    Order findById(String orderId);
    Order findOrderById(String orderId);

    List<Order> findByOrderStatusAndCreateTimeLessThan(Integer orderStatus, Long time);
    List<Order> findByOrderStatusAndFinishTimeLessThan(Integer orderStatus, Long time);
    List<Order> findByOrderStatusAndOrderLockAndFinishTimeLessThan(Integer orderStatus, Integer lock, Long time);
}
