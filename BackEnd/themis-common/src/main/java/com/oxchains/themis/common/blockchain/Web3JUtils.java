package com.oxchains.themis.common.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;

/**
 * Created by xuqi on 2017/12/12.
 */
public class Web3JUtils {
    private static final Logger LOG = LoggerFactory.getLogger(Web3JUtils.class);
    private static final BigInteger gasPrice = new BigInteger("0");
    private static final BigInteger gasLimit = new BigInteger("4500000");
    private static final String url = "http://192.168.1.205:8545";
    private static final String contractAddress = "0x836ca1852a40a2b7facc0db3215397bf7d98b3e7";
    private String DeployContract(Web3j web3j){
        String contractAddress = null;
        try {
            Credentials credentials = WalletUtils.loadCredentials("123", "wallet.json");
            Order contract = Order.deploy(web3j, credentials, gasPrice, gasLimit).send();
            contractAddress = contract.getContractAddress();
        } catch (Exception e) {
            LOG.error("deploy contract faild:{}",e.getMessage(),e);
        }
        return contractAddress;
    }
    public static Order getOrder(){
        Order contract = null;
        try {
                Web3j web3j = web3j = Web3j.build(new HttpService(url));
                //File file = new File("classpath:wallet.json");
                File file = org.springframework.util.ResourceUtils.getFile("classpath:wallet.json");
                LOG.warn(file.exists()+"-------");
                Credentials credentials = WalletUtils.loadCredentials("123", file);
                contract = Order.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        } catch (Exception e) {
            LOG.error("get Order faild:{}",e.getMessage(),e);
        }
        return contract;
    }
}
