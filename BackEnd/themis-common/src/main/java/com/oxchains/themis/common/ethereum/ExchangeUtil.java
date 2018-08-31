package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;

/**
 * @author ccl
 * @time 2018-05-10 14:24
 * @name ExchangeUtil
 * @desc:
 */
@Slf4j
public class ExchangeUtil {
    protected static final String CONTRACT_BINARY = "";
    protected static final String CONTRACT_ADDRESS = "";
    protected static final BigInteger GAS_PRICE = new BigInteger("0");
    protected static final BigInteger GAS_LIMIT = new BigInteger("4500000");
    protected static final String SERVER_URL = "http://192.168.1.195:8545";
    protected static final String WALLET_JSON_FILE = "D:\\temp\\ethereum\\admin.json";
    protected static final String WALLET_JSON_PASSWD = "123456";
    private String deployContract(Web3j web3j){
        String contractAddress = null;
        try {
            Credentials credentials = WalletUtils.loadCredentials("123", "wallet.json");
            Exchange contract = Exchange.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
            contractAddress = contract.getContractAddress();
        } catch (Exception e) {
            log.error("deploy contract faild:{}",e.getMessage(),e);
        }
        return contractAddress;
    }

    public static Exchange getExchange(){
        Exchange exchange = null;
        try {
            Web3j web3j = Web3j.build(new HttpService(SERVER_URL));
            Credentials credentials = WalletUtils.loadCredentials(WALLET_JSON_PASSWD, new File(WALLET_JSON_FILE));
            exchange = Exchange.init(CONTRACT_ADDRESS, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        } catch (Exception e) {
            log.error("get Exchange faild:{}",e.getMessage(),e);
        }
        return exchange;
    }


}
