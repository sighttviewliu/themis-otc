package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-16 10:43
 * @name ExchangeContract
 * @desc:
 */

@Slf4j
public class ExchangeContract extends AbstractContract{
    /**
     * trade contract
     */
    protected static final String TRADE_CONTRACT_ADDRESS = "0x7C564Ad23683172086A6D88266A42F250B547Ba2";

    public ExchangeContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public ExchangeContract(String password, String source) {
        initCredentials(password, source);
    }
    /**
     * 创建交易
     * @param orderId 订单id
     * @param seller 卖方地址
     * @param hosterNum 托管数量
     * @return
     */
    public String createNewTradeOrder(BigInteger orderId, String seller, BigInteger hosterNum){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderId),
                new Address(seller),
                new Uint256(hosterNum));
        Function function = new Function("createNewTradeOrder",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(TRADE_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 上传买方密钥碎片
     * @param orderID
     * @param shard
     * @return
     */
    public String uploadBuyerShardFromSeller(BigInteger orderID, String shard){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Utf8String(shard));
        Function function = new Function("uploadBuyerShardFromSeller",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(TRADE_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 上传卖方密钥碎片
     * @param orderID
     * @param shard
     * @return
     */
    public String uploadSellerShardFromBuyer(BigInteger orderID, String shard){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Utf8String(shard));
        Function function = new Function("uploadSellerShardFromBuyer",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(TRADE_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 获取买方密钥碎片
     * @param orderID
     * @param hosterID
     * @return
     */
    public String getBuyerShardByHosterID(BigInteger orderID, String hosterID) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Address(hosterID));
        Function function = new Function("getBuyerShardByHosterID",inparams, Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(null,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取卖方密钥碎片
     * @param orderID
     * @param hosterID
     * @return
     */
    public String getSellerShardByHosterID(BigInteger orderID, String hosterID) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Address(hosterID));
        Function function = new Function("getSellerShardByHosterID",inparams, Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(null,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 托管用户上传买方解密碎片
     * @param orderID
     * @param decryptedShard
     * @return
     */
    public String uploadBuyerDecryptedShard(BigInteger orderID, String decryptedShard){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Utf8String(decryptedShard));
        Function function = new Function("uploadBuyerDecryptedShard",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(TRADE_CONTRACT_ADDRESS,function,credentials);
        return result;
    }

    /**
     * 托管用户上传卖方解密碎片
     * @param orderID
     * @param decryptedShard
     * @return
     */
    public String uploadSellerDecryptedShard(BigInteger orderID, String decryptedShard){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderID),
                new Utf8String(decryptedShard));
        Function function = new Function("uploadBuyerDecryptedShard",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(TRADE_CONTRACT_ADDRESS,function,credentials);
        return result;
    }

    /**
     * 获取买家解密碎片
     * @param order
     * @param hoster
     * @return
     */
    public String getBuyerDecryptedShard(String seller,BigInteger order, String hoster) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint256(order),
                new Address(hoster));
        Function function = new Function("getBuyerDecryptedShard",inparams, Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(seller,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取卖家解密碎片
     * @param order
     * @param hoster
     * @return
     */
    public String getSellerDecryptedShard(String buyer, BigInteger order, String hoster) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint256(order),
                new Address(hoster));
        Function function = new Function("getSellerDecryptedShard",inparams, Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(buyer,TRADE_CONTRACT_ADDRESS,function);
        return result;
    }

    /**
     * 判断用户是否为托管用户
     * @param orderID
     * @return
     */
    public boolean isUserHoster(String from, BigInteger orderID) throws IOException {
        Function function = new Function("isUserHoster",Arrays.asList(new Uint256(orderID)), Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(from,TRADE_CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    /**
     * 获取订单买方
     * @param orderID
     * @return
     */
    public String getBuyer(BigInteger orderID) throws IOException {
        Function function = new Function("getBuyer",Arrays.asList(new Uint256(orderID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {})
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取订单卖方
     * @param orderID
     * @return
     */
    public String getSeller(BigInteger orderID) throws IOException {
        Function function = new Function("getSeller",Arrays.asList(new Uint256(orderID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {})
               /* Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取订单托管用户
     * @param orderID
     * @return
     */
    public List<String> getShardHosters(BigInteger orderID) throws IOException {
        List<String> addresses = null;
        Function function = new Function("getShardHosters",Arrays.asList(new Uint256(orderID)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {})
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,TRADE_CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            addresses =  (List<String>) types.get(0).getValue();
        }
        return addresses;
    }
}
