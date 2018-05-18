package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
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
 * @time 2018-05-16 10:24
 * @name UserContract
 * @desc:
 */
@Slf4j
public class UserContract extends AbstractContract {

    /**
     * user management contract
     */
    protected static final String HOSTER_CONTRACT_ADDRESS = "0x027A46809E1D8A1336B9E6cdaBC7139Fb0F5850c";

    public UserContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public UserContract(String password, String source) {
        initCredentials(password, source);
    }

    /**
     * 注册普通用户
     * @param id 以太坊合约地址
     * @param fame 初始名声 大于0
     * @param publicKey 用户公钥
     * @param userType 用户角色类型 0 为普通用户
     * @return
     */
    public String addUser(String id, BigInteger fame, String publicKey, BigInteger userType){
        List<Type> inparams = Arrays.asList(
                new Address(id),
                new Uint256(fame),
                new Utf8String(publicKey),
                new Uint8(userType));
        Function function = new Function("addUser",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String addUser(String id, BigInteger fame, String publicKey){
        return addUser(id, fame, publicKey, new BigInteger("0"));
    }

    /**
     *
     * @param id
     * @param fame
     * @param publicKey
     * @param userType
     * @return
     */
    public String updateUser(String id,BigInteger fame, String publicKey, BigInteger userType){
        List<Type> inparams = Arrays.asList(
                new Address(id),
                new Uint256(fame),
                new Utf8String(publicKey),
                new Uint8(userType));
        Function function = new Function("updateUser",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String updateUser(String id,BigInteger fame, String publicKey){
        return updateUser(id, fame, publicKey, new BigInteger("0"));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    public String removeUser(String id){
        Function function = new Function("removeUser",Arrays.asList(new Address(id)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 判断是否为themis用户
     * @param id
     * @return
     */
    public boolean isThemisUser(String id) throws IOException {
        Function function = new Function("isThemisUser",Arrays.asList(new Address(id)), Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(null,HOSTER_CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    /**
     * 判断是否为托管用户
     * @param id
     * @return
     */
    public boolean isHoster(String id) throws IOException {
        Function function = new Function("isHoster",Arrays.asList(new Address(id)),Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(null,HOSTER_CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    /**
     * 添加托管用户
     * @param id
     * @param fame
     * @param deposit 承诺支付的押金
     * @param publicKey
     * @return
     */
    public String addHoster(String id, BigInteger fame, BigInteger deposit, String publicKey){
        deposit = Convert.toWei(deposit.toString(), Convert.Unit.ETHER).toBigInteger();
        List<Type> inparams = Arrays.asList(
                new Address(id),
                new Uint256(fame),
                new Uint256(deposit),
                new Utf8String(publicKey));
        Function function = new Function("addHoster",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 将某个用户更新为托管用户
     * @param id
     * @param deposit
     * @return
     */
    public String updateNormalUserToHoster(String id, BigInteger deposit){
        List<Type> inparams = Arrays.asList(
                new Address(id),
                new Uint256(deposit));
        Function function = new Function("updateNormalUserToHoster",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 删除托管用户
     * @param id
     * @return
     */
    public String removeHoster(String id){
        Function function = new Function("removeHoster",Arrays.asList(new Address(id)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * 更新用户声誉
     * @param id
     * @param fame
     * @return
     */
    public String updateUserFame(String id, BigInteger fame){
        List<Type> inparams = Arrays.asList(
                new Address(id),
                new Uint256(fame));
        Function function = new Function("updateUserFame",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * This method called by every register user
     * 更新用户押金
     * @param deposit
     * @return
     */
    public String updateUserDeposit(BigInteger deposit){
        deposit = Convert.toWei(deposit.toString(), Convert.Unit.ETHER).toBigInteger();
        Function function = new Function("updateUserDeposit",Arrays.asList(new Uint256(deposit)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(HOSTER_CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    /**
     * This method called by every register user
     * 获取托管用户数量
     * @param num
     * @return
     */
    public String getHosters(BigInteger num) throws IOException {
        Function function = new Function("getHosters",Arrays.asList(new Uint256(num)), Arrays.asList(new TypeReference<Utf8String>() {}));
        String result = query(null,HOSTER_CONTRACT_ADDRESS,function);
        return result;
    }

}
