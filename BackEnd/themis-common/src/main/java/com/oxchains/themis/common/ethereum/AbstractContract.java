package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author ccl
 * @time 2018-05-16 10:11
 * @name Abstract
 * @desc:
 */
@Slf4j
public abstract class AbstractContract{
    protected Credentials credentials;

    /**
     * fees management contract
     */
    protected static final String FEE_CONTRACT_ADDRESS = "0xDd27Ae8821fddD0c5dCbCFf3469184FcE0417199";


    protected static final BigInteger GAS_PRICE = new BigInteger("0");
    protected static final BigInteger GAS_LIMIT = new BigInteger("4500000");
    protected static final String SERVER_URL = "http://192.168.1.213:8555";

    /**
     * 参数说明
     * String id 用户id（必须是以太坊的地址）
     * BigInteger fame：用户初始名声（必须大于0）
     * String publicKey:用户公钥
     * BigInteger userType：用户角色类型（必须为0， 0代表为普通用户，不允许直接将用户添加为托管用户，否则逻辑会有问题）
     * BigInteger deposit：承诺支付的押金，以wei为单位
     *
     */

    protected final Web3j web3j = Web3j.build(new HttpService(SERVER_URL));

    public AbstractContract() {
    }

    protected void initCredentials(String password, String source){
        try {
            credentials = WalletUtils.loadCredentials(password, source);
        } catch (IOException e) {
            log.error("load credential IO error: {}", e);
        } catch (CipherException e) {
            log.error("load credential cipher error: {}", e);
        }
    }

    /**
     * trade
     * @param to Contract Address
     * @param function input/out parameters
     * @param credentials call method's user
     * @return
     */
    public String transaction(String to, Function function, Credentials credentials){
        return transaction(to, function, credentials, BigInteger.ZERO);
    }

    public String transaction(String to, Function function, Credentials credentials, BigInteger value){
        String encodedFunction = FunctionEncoder.encode(function);
        BigInteger nonce = getNonce(credentials.getAddress());
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, to/*HOSTER_CONTRACT_ADDRESS*/, value, encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        log.info("signed message hex: {}",hexValue);
        return hexValue;
    }

    /**
     * query
     * @param from
     * @param to
     * @param function input/out parameters
     * @return
     * @throws IOException
     */
    public String query(String from, String to,Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(from, to, encodedFunction);
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String result = send.getResult();
        log.info("query result: {}",result.toString());
        return result;
    }

    /**
     * get nonce
     * @param address Contract Address
     * @return
     */
    public BigInteger getNonce(String address){
        BigInteger nonce = null;
        try {
            EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nonce = count.getTransactionCount();
            log.info("address: {} ---> nonce: {}",address, nonce);
        } catch (IOException e) {
            log.error("Get Address: {} ---> Nonce Error: {}",address, e);
        }
        return nonce;
    }

    /**
     * 后端方法
     * @param hex
     * @return
     */
    public String  sendAsyncRawTransaction(String hex){
        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hex).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    public String  sendRawTransaction(String hex){
        EthSendTransaction ethSendTransaction = null;
        String transactionHash = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hex).send();
            if(ethSendTransaction.hasError()){
                log.error("Transaction Error: {}",ethSendTransaction.getError().getMessage());
            }else {
                transactionHash = ethSendTransaction.getTransactionHash();
                log.info("Transactoin Hash: {}", transactionHash);
            }
        } catch (IOException e) {
            log.error("Send Raw Transaction Error: {}",e);
        }
        return transactionHash;
    }

    public boolean getTransactionStatus(String hash){
        boolean result = false;
        try {
            EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hash).send();
            String status = receipt.getTransactionReceipt().get().getStatus();
            log.info("get transaction: {}---> status: {}",hash, status);
            result = status.equals("0x1")?true:false;
        } catch (IOException e) {
            log.error("get transaction status error: {}",e);
        }
        return result;
    }

    public boolean hexToBoolean(String hex){
        boolean flag = false;
        if(hex.contains("0x")){
            hex = hex.replace("0x","");
        }
        flag = new BigInteger(hex, 16).intValue() == 1 ? true : false;
        return flag;
    }
    public String hexToString(String hex){
        String result = null;
        if(hex.contains("0x")){
            hex = hex.replace("0x","");
        }
        return result;
    }

    public Long hexToLong(String hex){
        if(hex.contains("0x")){
            hex = hex.replace("0x","");
        }
        Long result = Long.valueOf(hex, 16);
        return result;
    }
    public void Test() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.115:8545"));
        Function function = new Function("getFundAddr",
                Arrays.<Type>asList(new Uint256(1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        String data = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(null, "0x2fb95baf9fdc9b145f9561a097bcfd5c206bad4f", data);
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String address = send.getResult();
        System.out.println(address);
    }
}
