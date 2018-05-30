package com.oxchains.themis.common.ethereum;

import groovy.util.logging.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author ccl
 * @time 2018-05-24 15:19
 * @name Web3jHandler
 * @desc:
 */
@Slf4j
public class Web3jHandler {
    private volatile static Web3jHandler instance;
    private static String SERVER_URL = "http://192.168.1.205:8545";
    private static Web3j web3j = null;
    private Web3jHandler(){
        web3j = web3j = Web3j.build(new HttpService(SERVER_URL));

    }
    private Web3jHandler(String serverUrl){
        SERVER_URL = serverUrl;
        web3j = Web3j.build(new HttpService(SERVER_URL));
    }
    private static Web3jHandler getInstance(){
       return getInstance(SERVER_URL);
    }
    public static Web3jHandler getInstance(String serverUrl){
        if(null == instance){
            synchronized (Web3jHandler.class){
                if(null == instance){
                    instance = new Web3jHandler(serverUrl);
                }
            }
        }
        return instance;
    }


    public BigDecimal getBalance(String address) throws IOException {
       EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
       BigInteger b = ethGetBalance.getBalance();
       BigDecimal balance = Convert.fromWei(new BigDecimal(b), Convert.Unit.ETHER);
       return balance;
    }

    public EthBlock.Block getLatestBlock() throws IOException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
    }
    public EthBlock.Block getBlock(String hash) throws IOException {
       return web3j.ethGetBlockByHash(hash, false).send().getBlock();
    }
    public EthBlock.Block getBlock(BigInteger number) throws IOException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(number), false).send().getBlock();
    }

    public Transaction getTransaction(String hash) throws IOException {
        return web3j.ethGetTransactionByHash(hash).send().getTransaction().map(transaction -> {
            return transaction;
        }).orElse(null);
    }
}
