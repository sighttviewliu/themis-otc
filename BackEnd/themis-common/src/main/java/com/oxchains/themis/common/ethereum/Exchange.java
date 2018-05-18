package com.oxchains.themis.common.ethereum;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-10 14:05
 * @name Exchange
 * @desc:
 */
public class Exchange extends Contract{
    protected static final String CONTRACT_BINARY = "";
    protected static final String CONTRACT_ADDRESS = "";
    private Exchange(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    private Exchange(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(contractBinary, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private Exchange(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(CONTRACT_BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    private Exchange(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(CONTRACT_BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Exchange init(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Exchange(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Exchange init(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Exchange(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RemoteCall<Exchange> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Exchange.class, web3j, credentials, gasPrice, gasLimit, CONTRACT_BINARY, "");
    }

    public static RemoteCall<Exchange> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Exchange.class, web3j, transactionManager, gasPrice, gasLimit, CONTRACT_BINARY, "");
    }


    /**
     * 创建交易
     * @return
     */
    public RemoteCall<String> createNewTradeOrder() {
        Function function = new Function("createNewTradeOrder",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 发起仲裁请求
     * @return
     */
    public RemoteCall<String> uploadSellerShardFromBuyer() {
        Function function = new Function("uploadSellerShardFromBuyer",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getSellerShardByHosterID() {
        Function function = new Function("getSellerShardByHosterID",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> uploadSellerDecryptedShard(String orderId) {
        Function function = new Function("uploadSellerDecryptedShard",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    public RemoteCall<String> getSellerDecryptedShard() {
        Function function = new Function("getSellerDecryptedShard",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    public RemoteCall<String> getBuyerDecryptedShard() {
        Function function = new Function("getBuyerDecryptedShard",
                null,
                null);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 注册用户
     * @return
     */
    public RemoteCall<String> addUser(String id, String fname, String pubKey, String userType) {
        userType = "0";
        List<Type> inparam = Arrays.asList(new Utf8String(id),
                new Utf8String(fname),
                new Utf8String(pubKey),
                new Utf8String(userType));
        Function function = new Function("addUser",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    /**
     * 更新用户
     * @return
     */
    public RemoteCall<String> updateUser(String id, String fname, String pubKey, String userType) {
        userType = "0";
        List<Type> inparam = Arrays.asList(new Utf8String(id),
                new Utf8String(fname),
                new Utf8String(pubKey),
                new Utf8String(userType));
        Function function = new Function("updateUser",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 移除用户
     * @return
     */
    public RemoteCall<String> removeUser(String id) {
        Function function = new Function("removeUser",
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 是否为themis用户
     * @return
     */
    public RemoteCall<String> isThemisUser(String id) {
        Function function = new Function("isThemisUser",
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 是否为托管用户
     * @return
     */
    public RemoteCall<String> isHoster(String id) {
        Function function = new Function("isHoster",
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }


    /**
     * 添加托管用户
     * @return
     */
    public RemoteCall<String> addHoster(String id, String fame, String deposit, String pubKey) {
        List<Type> inparam = Arrays.asList(new Utf8String(id),
                new Utf8String(fame),
                new Utf8String(deposit),
                new Utf8String(pubKey));
        Function function = new Function("addHoster",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 将普通用户更新为托管用户
     * @return
     */
    public RemoteCall<String> updateNormalUserToHoster(String id, String deposit) {
        List<Type> inparam = Arrays.asList(new Utf8String(id),
                new Utf8String(deposit));
        Function function = new Function("updateNormalUserToHoster",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 移除托管用户
     * @return
     */
    public RemoteCall<String> removeHoster(String id) {
        Function function = new Function("removeHoster",
                Arrays.<Type>asList(new Utf8String(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 更新用户声誉
     * @return
     */
    public RemoteCall<String> updateUserFame(String id, String fame) {
        List<Type> inparam = Arrays.asList(new Utf8String(id),
                new Utf8String(fame));
        Function function = new Function("updateUserFame",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取托管用户
     * @return
     */
    public RemoteCall<String> getHosters(String num) {
        List<Type> inparam = Arrays.asList(new Utf8String(num));
        Function function = new Function("getHosters",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 交易合约-创建交易
     * @return
     */
    public RemoteCall<String> createNewTradeOrder(String orderId, String seller, String hosterNum) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(seller),
                new Utf8String(hosterNum));
        Function function = new Function("createNewTradeOrder",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 上传买家私钥碎片
     * @return
     */
    public RemoteCall<String> uploadBuyerShardFromSeller(String orderId, String shard) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(shard));
        Function function = new Function("uploadBuyerShardFromSeller",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 上传卖家私钥碎片
     * @return
     */
    public RemoteCall<String> uploadSellerShardFromBuyer(String orderId, String shard) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(shard));
        Function function = new Function("uploadSellerShardFromBuyer",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取买家托管加密碎片
     * @return
     */
    public RemoteCall<String> getBuyerShardByHosterID(String orderId, String hosterId) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(hosterId));
        Function function = new Function("getBuyerShardByHosterID",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取卖家托管加密碎片
     * @return
     */
    public RemoteCall<String> getSellerShardByHosterID(String orderId, String hosterId) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(hosterId));
        Function function = new Function("getSellerShardByHosterID",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 上传买家解密碎片
     * @return
     */
    public RemoteCall<String> uploadBuyerDecryptedShard(String orderId, String decryptedShard) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(decryptedShard));
        Function function = new Function("uploadBuyerDecryptedShard",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 上传卖家解密碎片
     * @return
     */
    public RemoteCall<String> uploadSellerDecryptedShard(String orderId, String decryptedShard) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(decryptedShard));
        Function function = new Function("uploadSellerDecryptedShard",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取买家解密碎片
     * @return
     */
    public RemoteCall<String> getBuyerDecryptedShard(String orderId, String hoster) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(hoster));
        Function function = new Function("getBuyerDecryptedShard",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取卖家解密碎片
     * @return
     */
    public RemoteCall<String> getSellerDecryptedShard(String orderId, String hoster) {
        List<Type> inparam = Arrays.asList(new Utf8String(orderId),
                new Utf8String(hoster));
        Function function = new Function("getSellerDecryptedShard",
                inparam,
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 是否为订单的托管用户
     * @return
     */
    public RemoteCall<String> isUserHoster(String orderId) {
        Function function = new Function("isUserHoster",
                Arrays.asList(new Utf8String(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取订单的买方
     * @return
     */
    public RemoteCall<String> getBuyer(String orderId) {
        Function function = new Function("getBuyer",
                Arrays.asList(new Utf8String(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取订单的卖方
     * @return
     */
    public RemoteCall<String> getSeller(String orderId) {
        Function function = new Function("getSeller",
                Arrays.asList(new Utf8String(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    /**
     * 获取订单的托管用户
     * @return
     */
    public RemoteCall<String> getShardHoster(String orderId) {
        Function function = new Function("getShardHoster",
                Arrays.asList(new Utf8String(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
}
