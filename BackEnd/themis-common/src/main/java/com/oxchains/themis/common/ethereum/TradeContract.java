package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.generated.Uint80;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
    protected String CONTRACT_ADDRESS = "0x86740c477747251757a42070f591da9b4a2bf412";

    public TradeContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public TradeContract(String password, String source) {
        initCredentials(password, source);
    }
    public TradeContract(String password, String source, String contract) {
        initCredentials(password, source);
        this.CONTRACT_ADDRESS = contract;
    }

    /**
     * 创建订单
     *
     * @param orderId  订单id
     * @param userId   创建订单用户id
     * @param userType 用户类型
     * @param amount   GET 币
     * @return
     */
    public String createNewTradeOrder(BigInteger orderId, Long userId, Integer userType, BigInteger amount) {
        List<Type> inparams = Arrays.asList(
                new Uint80(orderId),
                new Uint32(userId),
                new Uint8(userType));
        Function function = new Function("createNewTradeOrder", inparams, Collections.<TypeReference<?>>emptyList());
        amount = new BigInteger(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toString());
        String result = transaction(CONTRACT_ADDRESS, function, credentials, amount);
        return result;
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param userId  创建订单用户id
     * @return
     */
    public String cancelTrade(BigInteger orderId, Long userId) {
        Function function = new Function("cancelTrade", Arrays.asList(new Uint80(orderId), new Uint32(userId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 确认订单
     *
     * @param orderId
     * @param userId  确认订单用户id, 与创建用户不一样
     * @return
     */
    public String confirmTradeOrder(BigInteger orderId, Long userId, BigInteger amount) {
        Function function = new Function("confirmTradeOrder", Arrays.asList(new Uint80(orderId), new Uint32(userId)), Collections.<TypeReference<?>>emptyList());
        amount = new BigInteger(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toString());
        String result = transaction(CONTRACT_ADDRESS, function, credentials, amount);
        return result;
    }

    /**
     * 完成订单
     *
     * @param orderId
     * @return
     */
    public String finishOrder(BigInteger orderId) {
        Function function = new Function("finishOrder", Arrays.asList(new Uint80(orderId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 获取托管费用
     *
     * @param amount
     * @return
     */
    public String withdrawFee(BigInteger amount) {
        Function function = new Function("withdrawFee", Arrays.asList(), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials, amount);
        return result;
    }

    /**
     * 上传私钥碎片
     *
     * @param orderId
     * @param secret
     * @return
     */
    public String uploadSecret(BigInteger orderId, String secret, Long userId, String verifyData) {
        List<Type> inparams = Arrays.asList(
                new Uint80(orderId),
                new Utf8String(secret),
                new Uint32(userId),
                new Utf8String(verifyData));
        Function function = new Function("uploadSecret", inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 获取私钥碎片
     *
     * @param orderId
     * @param trusteeId
     * @param userId
     * @return
     * @throws IOException
     */
    public String getSecret(BigInteger orderId, String trusteeId, Long userId) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint80(orderId),
                new Address(trusteeId),
                new Uint32(userId));
        Function function = new Function("getSecret", inparams, Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (null != types && types.size() > 0) {
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取verifyData
     */
    public String getVerifyData(BigInteger orderId, Long userId) throws IOException {
        List<Type> inparams = Arrays.asList(
                new Uint80(orderId),
                new Uint32(userId));
        Function function = new Function("getVerifyData", inparams, Arrays.asList(new TypeReference<Utf8String>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (null != types && types.size() > 0) {
            result = types.get(0).getValue().toString();
        }
        return result;
    }

    /**
     * 获取订单买方
     *
     * @param orderId
     * @return
     */
    public Long getOrderBuyer(BigInteger orderId) throws IOException {
        Function function = new Function("getOrderBuyer", Arrays.asList(new Uint80(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {
                })
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null, CONTRACT_ADDRESS, function);
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (null != types && types.size() > 0) {
            result = types.get(0).getValue().toString();
        }
        return Long.valueOf(result);
    }

    /**
     * 获取订单卖方
     *
     * @param orderId
     * @return
     */
    public Long getOrderSeller(BigInteger orderId) throws IOException {
        Function function = new Function("getOrderSeller", Arrays.asList(new Uint80(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {
                })
                /* Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null, CONTRACT_ADDRESS, function);
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (null != types && types.size() > 0) {
            result = types.get(0).getValue().toString();
        }
        return Long.valueOf(result);
    }

    /**
     * 获取订单托管用户
     *
     * @param orderId
     * @return
     */
    public List<String> getOrderTrustees(BigInteger orderId) throws IOException {
        List<String> addresses = null;
        Function function = new Function("getOrderTrustees", Arrays.asList(new Uint80(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {
                })
                /*Arrays.asList(new TypeReference<Utf8String>() {})*/);
        String result = query(null, CONTRACT_ADDRESS, function);
        List<Type> types = FunctionReturnDecoder.decode(result, function.getOutputParameters());
        if (null != types && types.size() > 0) {
            List<Address> addrs = (List<Address>) types.get(0).getValue();
            if(null != addrs) {
                addresses = new ArrayList<>(addrs.size());
                for (Address address : addrs) {
                    addresses.add(address.getValue());
                }
            }
//            addresses = (List<String>) types.get(0).getValue();
        }
        return addresses;
    }

    /**
     * 判断用户是否为托管用户
     *
     * @param orderId
     * @return
     */
    public boolean isOrderTrustee(BigInteger orderId, String user) throws IOException {
        Function function = new Function("isOrderTrustee", Arrays.asList(
                new Uint80(orderId),
                new Address(user)), Arrays.asList(new TypeReference<Bool>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        return hexToBoolean(result);
    }

    /**
     * 添加仲裁用户
     *
     * @param who
     * @return
     */
    public String addArbitrator(String who) {
        Function function = new Function("addArbitrator", Arrays.asList(new Address(who)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 添加仲裁用户
     *
     * @param who
     * @return
     */
    public String removeArbitrator(String who) {
        Function function = new Function("removeArbitrator", Arrays.asList(new Address(who)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 判断用户是否为仲裁用户
     *
     * @param who
     * @return
     */
    public boolean isArbitrator(String who) throws IOException {
        Function function = new Function("isArbitrator", Arrays.asList(new Address(who)), Arrays.asList(new TypeReference<Bool>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        return hexToBoolean(result);
    }

    /**
     * 申请仲裁
     *
     * @param orderId
     * @return
     */
    public String arbitrate(BigInteger orderId, Long userId) {
        Function function = new Function("arbitrate", Arrays.asList(new Uint80(orderId), new Uint32(userId)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 提交仲裁结果
     *
     * @param orderId
     * @param winner
     * @return
     */
    public String judge(BigInteger orderId, Long winner) {
        Function function = new Function("judge", Arrays.asList(new Uint80(orderId), new Uint32(winner)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 获取申请仲裁的用户
     *
     * @param orderId
     * @return
     * @throws IOException
     */
    public Long getRequester(BigInteger orderId) throws IOException {
        Function function = new Function("getRequester", Arrays.asList(
                new Uint80(orderId)), Arrays.asList(new TypeReference<Uint32>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        return hexToLong(result);
    }

    /**
     * 获取获胜方
     *
     * @param orderId
     * @return
     * @throws IOException
     */
    public Long getWinner(BigInteger orderId) throws IOException {
        Function function = new Function("getWinner", Arrays.asList(
                new Uint80(orderId)), Arrays.asList(new TypeReference<Uint32>() {
        }));
        String result = query(null, CONTRACT_ADDRESS, function);
        return hexToLong(result);
    }

    /**
     * 更新默认托管节点个数
     *
     * @param trusteeNum
     * @return
     */
    public String updateDefaultTrusteeNumber(Integer trusteeNum) {
        Function function = new Function("updateDefaultTrusteeNumber", Arrays.asList(new Uint8(trusteeNum)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 更新托管用户合约地址
     *
     * @param trustee
     * @return
     */
    public String updateTrusteeContract(String trustee) {
        Function function = new Function("updateTrusteeContract", Arrays.asList(new Address(trustee)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    /**
     * 获取订单状态
     * Created：0
     * Canceled：1
     * Confirmed：2
     * SecretUploaded：3
     * VerifiedSuccess：4
     * InArbitration：5
     * Finished：6
     *
     * @param orderId
     * @return
     */
    public Long getOrderStatus(BigInteger orderId) throws Exception {
        Function function = new Function("getOrderStatus", Arrays.asList(new Uint80(orderId)), Arrays.asList(new TypeReference<Uint32>() {}));
        String result = query(null, CONTRACT_ADDRESS, function);
        return hexToLong(result);
    }

    /**
     * 托管节点发送验证
     */
    public String sendVerifyResult(BigInteger orderId, boolean buyerResult, boolean sellerResult) {
        List<Type> inparams = Arrays.asList(
                new Uint80(orderId),
                new Bool(buyerResult),
                new Bool(sellerResult));
        Function function = new Function("sendVerifyResult", inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    public String transferOwnership(String newOwner) {
        Function function = new Function("transferOwnership", Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }
}
