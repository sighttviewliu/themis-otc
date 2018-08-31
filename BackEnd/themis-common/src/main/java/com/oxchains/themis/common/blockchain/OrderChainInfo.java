package com.oxchains.themis.common.blockchain;

import com.oxchains.themis.common.util.ShamirUtil;
import com.oxchains.themis.repo.entity.order.Order;
import lombok.Data;

/**
 * Created by xuqi on 2017/12/11.
 */
@Data
public class OrderChainInfo {
    private String orderId;   //订单id
    private String buyerId;    //买家id
    private String buyerPriKey; //买家私钥
    private String buyerPubKey;
    private String sellerId;      //卖家id
    private String sellerPriKey; //卖家私钥
    private String sellerPubKey;
    private String K;             //门限方案中的K值
    private String N;             //门限方案中的N值
    private String arbitratePubKey; //仲裁者公钥
    private String arbitratePriKey; //仲裁者私钥

    public OrderChainInfo(Order orders, String buyerPriKey, String buyerPubKey, String arbitratePriKey, String arbitratePubKey){
        this.orderId = orders.getId();
        this.sellerId = orders.getSellerId().toString();
        this.buyerId = orders.getBuyerId().toString();
        this.K = ShamirUtil.K.toString();
        this.N = ShamirUtil.N.toString();
        this.buyerPriKey = buyerPriKey;
        this.buyerPubKey = buyerPubKey;
        this.arbitratePriKey =arbitratePriKey;
        this.arbitratePubKey = arbitratePubKey;
    }

    public OrderChainInfo(String orderId, String sellerPriKey, String sellerPubKey,Integer K,Integer N) {
        this.orderId = orderId;
        this.sellerPriKey = sellerPriKey;
        this.sellerPubKey = sellerPubKey;
    }

    public OrderChainInfo() {
    }
}
