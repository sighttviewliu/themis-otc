package com.oxchains.themis.common.ethereum;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-24 15:19
 * @name Web3jHandler
 * @desc:
 */
public class Web3jHandler {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private volatile static Web3jHandler instance;
    private static String SERVER_URL = "http://192.168.1.195:8545";
    private static Web3j web3j = null;
    private static final BigInteger GAS_PRICE = new BigInteger("20");
    private static final BigInteger GAS_LIMIT = new BigInteger("4500000");
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

    public TransactionReceipt getTransactionReceipt(String hash) throws IOException {
        return web3j.ethGetTransactionReceipt(hash).send().getResult();
    }

    public BigInteger getGasUsed(String hash) throws IOException {
        return web3j.ethGetTransactionReceipt(hash).send().getResult().getGasUsed();
    }
    public Transaction getTransaction(String hash) throws IOException {
        return web3j.ethGetTransactionByHash(hash).send().getTransaction().map(transaction -> {
            return transaction;
        }).orElse(null);
    }

    public Transaction getTransactionByHash(String hash) throws IOException {
        return web3j.ethGetTransactionByHash(hash).send().getResult();
    }

    public String sendRawTransactions(String hash) throws Exception {
        EthSendTransaction ethSendTransaction = null;
        String transactionHash = null;

        ethSendTransaction = web3j.ethSendRawTransaction(hash).send();
        if (ethSendTransaction.hasError()) {
            LOG.error("Transaction Error: {}", ethSendTransaction.getError().getMessage());
            throw new Exception(ethSendTransaction.getError().getMessage());
        } else {
            transactionHash = ethSendTransaction.getTransactionHash();
            LOG.info("Transactoin Hash: {}", transactionHash);
        }
        return transactionHash;
    }
    public String sendRawTransaction(String hash){
        EthSendTransaction ethSendTransaction = null;
        String transactionHash = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hash).send();
            if(ethSendTransaction.hasError()){
                LOG.error("Transaction Error: {}",ethSendTransaction.getError().getMessage());
            }else {
                transactionHash = ethSendTransaction.getTransactionHash();
                LOG.info("Transactoin Hash: {}", transactionHash);
            }
        } catch (IOException e) {
            LOG.error("Send Raw Transaction Error: {}",e);
        }
        return transactionHash;
    }

    public BigInteger getNonce(String address) throws IOException {
        return getNoncePending(address);
    }

    public BigInteger getNonceLatest(String address) throws IOException {
        EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        return count.getTransactionCount();
    }

    public BigInteger getNoncePending(String address) throws IOException {
        EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        return count.getTransactionCount();
    }

    public BigInteger getLatestBlockNumber() throws IOException {
            return getLatestBlock().getNumber();
    }

    public List<EthBlock.Block> getLatestBlock(int total) throws IOException {
        List<EthBlock.Block> result = new ArrayList<>(total);
        long latestNum = getLatestBlockNumber().longValue();
        long start = latestNum - total + 1L;
        for(; start <= latestNum; start++ ){
            result.add(getBlock(new BigInteger(""+start)));
        }
        return result;
    }

    public String transfer(String source, String password, String to, BigDecimal value) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(password, source);
        return transfer(credentials, to, value);
    }

    public String transfer(Credentials credentials, String to, BigDecimal value) throws Exception {
        value = Convert.toWei(value, Convert.Unit.ETHER);
        BigInteger nonce = getNonce(credentials.getAddress());
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, Convert.toWei("20", Convert.Unit.GWEI).toBigInteger(), GAS_LIMIT, to, value.toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return sendRawTransactions(hexValue);
    }

    public BigInteger validateTransaction(String txhash) throws IOException {
        return getTransactionByHash(txhash).getValue();
    }

    public static String parseAddressFromExtra(EthBlock.Block block) throws SignatureException {
        if(null == block){
            return null;
        }
        RlpString parentHash = RlpString.create(Hex.decode(block.getParentHash().substring(2)));
        RlpString uncleHash = RlpString.create(Hex.decode(block.getSha3Uncles().substring(2)));
        RlpString miner = RlpString.create(Hex.decode(block.getMiner().substring(2)));
        RlpString stateRoot = RlpString.create(Hex.decode(block.getStateRoot().substring(2)));
        RlpString txRoot = RlpString.create(Hex.decode(block.getTransactionsRoot().substring(2)));
        RlpString receiptRoot = RlpString.create(Hex.decode(block.getReceiptsRoot().substring(2)));
        RlpString logsBoom = RlpString.create(Hex.decode(block.getLogsBloom().substring(2)));
        RlpString diff = RlpString.create(block.getDifficulty());
        if (block.getDifficultyRaw().equals("0x1")) {
            diff = RlpString.create(1);
        }
        if (block.getDifficultyRaw().equals("0x2")) {
            diff = RlpString.create(2);
        }
        RlpString number = RlpString.create(block.getNumber());
        RlpString gasLimit = RlpString.create(block.getGasLimit());
        RlpString gasUsed = RlpString.create(block.getGasUsed());
        if (block.getGasUsedRaw().equals("0x0")) {
            gasUsed = RlpString.create(0);
        }
        RlpString timestamp = RlpString.create(block.getTimestamp());
        String extraData = block.getExtraData().substring(2);
        byte[] extraDataByteArray = Hex.decode(extraData);
        byte[] data = new byte[extraDataByteArray.length-65];
        System.arraycopy(extraDataByteArray, 0, data, 0, extraDataByteArray.length-65);
        RlpString extraDataString = RlpString.create(data);

        RlpString mixHash = RlpString.create(Hex.decode(block.getMixHash().substring(2)));
        RlpString nonce = RlpString.create(Hex.decode(block.getNonceRaw().substring(2)));
        RlpList list = new RlpList(
                parentHash,
                uncleHash,
                miner,
                stateRoot,
                txRoot,
                receiptRoot,
                logsBoom,
                diff,
                number,
                gasLimit,
                gasUsed,
                timestamp,
                extraDataString,
                mixHash,
                nonce
        );
        byte[] out = RlpEncoder.encode(list);

        byte[] sig = new byte[65];
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        byte v = 0x0;
        if (extraDataByteArray.length <= 65) {
            return null;
        }

        System.arraycopy(extraDataByteArray, extraDataByteArray.length-65, sig, 0, 65);
        for (int i=0; i<sig.length; i++) {
            if (i <= 31) {
                r[i] = sig[i];
            }

            if (i >= 32 && i < 64) {
                s[i-32] = sig[i];
            }

            if (i == 64) {
                v = (byte)((sig[i] & 0xFF) + 27);
            }
        }
        Sign.SignatureData sigMsg = new Sign.SignatureData(v, r, s);
        BigInteger pubKeyRecovered = pubKeyRecovered = Sign.signedMessageToKey(out, sigMsg);;
        String address = Keys.getAddress(pubKeyRecovered.toString(16));
        if(null != address && !address.startsWith("0x")){
            address = "0x" + address;
         }
        return address;
    }
}
