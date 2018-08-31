package com.oxchains.themis.common.blockchain;

import com.oxchains.themis.common.util.ShamirUtil;
import com.oxchains.themis.repo.entity.order.OrderArbitrate;
import com.oxchains.themis.repo.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Created by xuqi on 2017/12/11.
 */
@Service
public class ChainHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public static final BigInteger BUYER = new BigInteger("1");
    public static final BigInteger SELLER = new BigInteger("2");

    //添加订单时 在链中存入一条订单信息 包括买家公私钥 仲裁者公私钥 买家id,卖家id 卖家私钥碎片 们现方案的K N值
    public void setOrderInfoToChain(OrderChainInfo orderChainInfo, List<User> list) {
        try {
            Order order = Web3JUtils.getOrder();
            order.RequestHostingService(
                    orderChainInfo.getOrderId(),
                    orderChainInfo.getBuyerId(),
                    orderChainInfo.getBuyerPubKey(),
                    orderChainInfo.getBuyerPriKey(),
                    orderChainInfo.getArbitratePubKey(),
                    orderChainInfo.getArbitratePriKey(),
                    orderChainInfo.getSellerId(),
                    new BigInteger(orderChainInfo.getK()),
                    new BigInteger(orderChainInfo.getN())).send();
            String[] auth = ShamirUtil.splitAuth(orderChainInfo.getBuyerPriKey(), Integer.parseInt(orderChainInfo.getN()), Integer.parseInt(orderChainInfo.getK()));
            for (int i = 0; i < Integer.parseInt(orderChainInfo.getN()); i++) {
                order.UploadShardKeyToTrustee(orderChainInfo.getOrderId(), list.get(i).getId().toString(), auth[i], BUYER).send();
            }
        } catch (Exception e) {
            LOG.error("set order info to chain faild:{}", e.getMessage(), e);
        }
    }

    //上传卖家的公私钥
    public void setSellerPriPubKeyToChain(OrderChainInfo orderChainInfo, List<OrderArbitrate> list) throws Exception {
        Order order = Web3JUtils.getOrder();
        order.UploadSellerKey(orderChainInfo.getOrderId(), orderChainInfo.getSellerPubKey(), orderChainInfo.getBuyerPriKey());
        String[] auth = ShamirUtil.splitAuth(orderChainInfo.getSellerPriKey(), ShamirUtil.N, ShamirUtil.K);
        for (int i = 0; i < ShamirUtil.N; i++) {
            order.UploadShardKeyToTrustee(orderChainInfo.getOrderId(), list.get(i).getUserId().toString(), auth[i], BUYER).send();
        }

    }

    //获取买家私钥
    public String getBuyerPriKeyByOrderid(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        String str = order.GetEncryBuyerPrivKey(orderId).send();
        return str;
    }

    //获取买家公钥
    public String getBuyerPubKeyByOrderId(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        String s = order.GetBuyerPublicKey(orderId).send();
        return s;
    }

    //获取卖家私钥
    public String getSellerPriKeyByOrderid(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        String str = order.GetEncrySellerPrivKey(orderId).send();
        return str;
    }

    //获取卖家公钥
    public String getSellerPubKeyByOrderId(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        String s = order.GetSellerPublicKey(orderId).send();
        return s;
    }

    //获取仲裁者公钥
    public String getArbitratePubKey(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        String s = order.GetVirTrusteePublicKey(orderId).send();
        return s;
    }

    //获取订单的胜利者
    public Integer getArbitrateWin(String orderId) throws Exception {
        Order order = Web3JUtils.getOrder();
        BigInteger send = order.JudgeWhoWin(orderId).send();
        return Optional.of(send).get().intValue();
    }

    //仲裁者 仲裁订单
    public void arbitrateToUser(Long arbitrateUserId, String orderId, Integer type) throws Exception {
        Order order = Web3JUtils.getOrder();
        order.JudgeUserWinByTrustee(orderId, arbitrateUserId.toString(), new BigInteger(type.toString())).send();
    }

    //获取用户的私钥碎片
    public String getUserShardKeyByOrderId(String orderId, Integer type) throws Exception {
        Order order = Web3JUtils.getOrder();
        String str = order.GetWinerShardKey(orderId, new BigInteger(type.toString())).send();
        return str;
    }
}
