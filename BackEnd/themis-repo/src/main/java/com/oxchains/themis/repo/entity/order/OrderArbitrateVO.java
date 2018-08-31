package com.oxchains.themis.repo.entity.order;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-08-13 14:02
 * @name OrderArbitrateVO
 * @desc:
 */
@Data
public class OrderArbitrateVO extends OrderArbitrate {
    private OrderVO order;

    public OrderArbitrateVO() {
    }

    public OrderArbitrateVO(OrderArbitrate arbitrate) {
        if (null != arbitrate) {
            this.setId(arbitrate.getId());
            this.setOrderId(arbitrate.getOrderId());
            this.setUserId(arbitrate.getUserId());
            this.setUsername(arbitrate.getUsername());

            this.setCreateTime(arbitrate.getCreateTime());
            this.setUpdateTime(arbitrate.getUpdateTime());

            this.setReason(arbitrate.getReason());
            this.setAttachment(arbitrate.getAttachment());
            this.setWinnerId(arbitrate.getWinnerId());

            this.setStatus(arbitrate.getStatus());

            this.setDealer(arbitrate.getDealer());
            this.setIdentity(arbitrate.getIdentity());
        }
    }

    public OrderArbitrateVO(OrderArbitrate arbitrate, OrderVO order) {
        this(arbitrate);
        if (null != order) {
            this.setOrder(order);
        }
    }
}
