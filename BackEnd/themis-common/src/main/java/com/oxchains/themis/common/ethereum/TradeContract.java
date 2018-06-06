package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-16 10:43
 * @name TradeContract
 * @desc:
 */

@Slf4j
public class TradeContract extends AbstractContract {
    /**
     * trade contract
     */
    protected static final String CONTRACT_ADDRESS = "0xAAa91587531b304B117e367bBAb75ecD9B77cE15";

    public TradeContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public TradeContract(String password, String source) {
        initCredentials(password, source);
    }
    /**
     * 创建订单
     * @param orderId 订单id
     * @param userId 创建订单用户id
     * @param userType 用户类型
     * @param amount GET 币
     * @return
     */
    public String createNewTradeOrder(BigInteger orderId, BigInteger userId,BigInteger userType, BigInteger amount){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderId),
                new Uint256(userId),
                new Uint8(userType));
        Function function = new Function("createNewTradeOrder",inparams, Collections.<TypeReference<?>>emptyList());
        amount = new BigInteger(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toString());
        String result = transaction(CONTRACT_ADDRESS,function, credentials, amount);
        return result;
    }

    /**
     * 取消订单
     * @param orderId
     * @param userId 创建订单用户id
     * @return
     */
    public String cancelTrade(BigInteger orderId, BigInteger userId){
        Function function = new Function("cancelTrade", Arrays.asList(new Uint256(orderId), new Uint256(userId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 确认订单
     * @param orderId
     * @param userId 确认订单用户id, 与创建用户不一样
     * @return
     */
    public String confirmTradeOrder(BigInteger orderId, BigInteger userId, BigInteger amount){
        Function function = new Function("confirmTradeOrder", Arrays.asList(new Uint256(orderId), new Uint256(userId)), Collections.<TypeReference<?>>emptyList());
        amount = new BigInteger(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toString());
        String result = transaction(CONTRACT_ADDRESS,function, credentials, amount);
        return result;
    }

    /**
     * 完成订单
     * @param orderId
     * @return
     */
    public String finishOrder(BigInteger orderId){
        Function function = new Function("finishOrder", Arrays.asList(new Uint256(orderId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 获取托管费用
     * @param amount
     * @return
     */
    public String withdrawFee(BigInteger amount){
        Function function = new Function("withdrawFee", Arrays.asList(), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials, amount);
        return result;
    }

    /**
     * 上传私钥碎片
     * @param orderId
     * @param secret
     * @return
     */
    public String uploadSecret(BigInteger orderId, String secret, BigInteger userId){
        List<Type> inparams = Arrays.asList(
                new Uint256(orderId),
                new Utf8String(secret),
                new Uint256(userId));
        Function function = new Function("uploadSecret",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 获取私钥碎片
     * @param orderId
     * @param trusteeId
     * @param userId
     * @return
     * @throws IOException
     */
    public String getSecret(BigInteger orderId, String trusteeId, BigInteger userId) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint256(orderId),
                new Address(trusteeId),
                new Uint256(userId));
        Function function = new Function("getSecret",inparams, Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取订单买方
     * @param orderId
     * @return
     */
    public Long getOrderBuyer(BigInteger orderId) throws IOException {
        Function function = new Function("getOrderBuyer",Arrays.asList(new Uint256(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {})
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return hexToLong(result);
    }

    /**
     * 获取订单卖方
     * @param orderId
     * @return
     */
    public Long getOrderSeller(BigInteger orderId) throws IOException {
        Function function = new Function("getOrderSeller",Arrays.asList(new Uint256(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {})
               /* Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            result = types.get(0).getValue().toString();
        }
        return hexToLong(result);
    }

    /**
     * 获取订单托管用户
     * @param orderId
     * @return
     */
    public List<String> getOrderTrustees(BigInteger orderId) throws IOException {
        List<String> addresses = null;
        Function function = new Function("getOrderTrustees",Arrays.asList(new Uint256(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {})
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null,CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());
        if(null != types && types.size()>0){
            addresses =  (List<String>) types.get(0).getValue();
        }
        return addresses;
    }

    /**
     * 判断用户是否为托管用户
     * @param orderId
     * @return
     */
    public boolean isOrderTrustee(BigInteger orderId, String user) throws IOException {
        Function function = new Function("isOrderTrustee",Arrays.asList(
                new Uint256(orderId),
                new Address(user)),Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    /**
     * 添加仲裁用户
     * @param who
     * @return
     */
    public String addArbitrator(String who){
        Function function = new Function("addArbitrator",Arrays.asList(new Address(who)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 添加仲裁用户
     * @param who
     * @return
     */
    public String removeArbitrator(String who){
        Function function = new Function("removeArbitrator",Arrays.asList(new Address(who)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 判断用户是否为仲裁用户
     * @param who
     * @return
     */
    public boolean isArbitrator(String who) throws IOException {
        Function function = new Function("isArbitrator",Arrays.asList(new Address(who)),Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    /**
     * 申请仲裁
     * @param orderId
     * @return
     */
    public String arbitrate(BigInteger orderId, BigInteger userId){
        Function function = new Function("arbitrate",Arrays.asList(new Uint256(orderId), new Uint256(userId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 提交仲裁结果
     * @param orderId
     * @param winner
     * @return
     */
    public String judge(BigInteger orderId, BigInteger winner){
        Function function = new Function("judge",Arrays.asList(new Uint256(orderId), new Uint256(winner)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 获取申请仲裁的用户
     * @param orderId
     * @return
     * @throws IOException
     */
    public Long getRequester(BigInteger orderId) throws IOException {
        Function function = new Function("getRequester",Arrays.asList(
                new Uint256(orderId)),Arrays.asList(new TypeReference<Uint256>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        return hexToLong(result);
    }

    /**
     * 获取获胜方
     * @param orderId
     * @return
     * @throws IOException
     */
    public Long getWinner(BigInteger orderId) throws IOException {
        Function function = new Function("getWinner",Arrays.asList(
                new Uint256(orderId)),Arrays.asList(new TypeReference<Uint256>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        return hexToLong(result);
    }

    /**
     * 更新默认托管节点个数
     * @param trusteeNum
     * @return
     */
    public String updateDefaultTrusteeNumber(BigInteger trusteeNum){
        Function function = new Function("updateDefaultTrusteeNumber",Arrays.asList(new Uint256(trusteeNum)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 更新托管用户合约地址
     * @param trustee
     * @return
     */
    public String updateTrusteeContract(String trustee){
        Function function = new Function("updateTrusteeContract",Arrays.asList(new Utf8String(trustee)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

}
