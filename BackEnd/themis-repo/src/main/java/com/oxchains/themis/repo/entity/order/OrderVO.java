package com.oxchains.themis.repo.entity.order;

import com.oxchains.themis.repo.entity.Notice;
import com.oxchains.themis.repo.entity.Payment;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ccl
 * @time 2018-06-04 11:01
 * @name Role
 * @desc:
 */
@Data
public class OrderVO extends Order implements Serializable{
    private String orderStatusName; //订单状态名称
    //private Notice notice;  //相关联的公告信息
    private String payment; //相关联的 支付方式信息
    private List<String> buyerPayment; //相关联的 支付方式信息
    private List<String> sellerPayment; //相关联的 支付方式信息
    private String orderType;  //  交易类型     购买  或 出售
    private String buyerUsername; //买家名称
    private String sellerUsername; //卖家名称
    private String partner;
    //private Integer status;
    private String txId;
    private String vcurrency;
    private String currency;
    private String partnerAvatar;
    private String trusteeAddress;
    public OrderVO(Order order) {
        if(order != null){
            this.setId(order.getId());
            this.setMoney(order.getMoney());
            this.setPrice(order.getPrice());
            this.setAmount(order.getAmount());
            this.setArbitrate(order.getArbitrate());
            this.setBuyerId(order.getBuyerId());
            this.setSellerId(order.getSellerId());
            this.setCreateTime(order.getCreateTime());
            this.setCurrencyId(order.getCurrencyId());
            this.setVcurrencyId(order.getVcurrencyId());
            this.setFinishTime(order.getFinishTime());
            this.setNoticeId(order.getNoticeId());
            this.setPaymentId(order.getPaymentId());
            this.setOrderStatus(order.getOrderStatus());
            this.setUri(order.getUri());
            this.setType(order.getType());
            this.setTrusteeAddress(order.getTrusteeAddress());
        }
    }

}
