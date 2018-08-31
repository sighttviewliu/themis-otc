package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.OrderTranStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-06-06 11:38
 * @name OrderTranStatusRepo
 * @desc:
 */
public interface OrderTranStatusRepo extends CrudRepository<OrderTranStatus,Long> {
    OrderTranStatus findByOrderId(String orderId);
    List<OrderTranStatus> findByOrderIdIn(List<String> orderIds);

    List<OrderTranStatus> findByTstatusNotIn(List<Integer> status);

    List<OrderTranStatus> findByCreatePushAndTstatus(boolean createPush, Integer txStatus);
    List<OrderTranStatus> findByCreatePushAndConfirmedPushAndUploadPushAndTstatus(boolean createPush, boolean confirmedPush, boolean uploadPush, Integer txStatus);
    List<OrderTranStatus> findByCreatePushAndConfirmedPushAndUploadPushAndTstatusIn(boolean createPush, boolean confirmedPush, boolean uploadPush, List<Integer> txStatus);
    List<OrderTranStatus> findByCreatePushAndConfirmedPushAndTstatus(boolean createPush, boolean confirmedPush, Integer txStatus);

    List<OrderTranStatus> findByTstatusAndCreateTimeLessThan(Integer status, long time);
    List<OrderTranStatus> findByTstatusAndArbitratePush(Integer status, boolean push);
    List<OrderTranStatus> findByOrderIdAndCreatePushAndConfirmedPushAndTstatus(String orderId, boolean createPush, boolean confirmedPush, Integer status);
}
