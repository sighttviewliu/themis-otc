package com.oxchains.themis.common.ethereum;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-16 11:17
 * @name ContractHandler
 * @desc:
 */

@Slf4j
public class ContractHandler extends AbstractContract {

    private TradeContract tradeContract;
    private TrusteeContract trusteeContract;

    public ContractHandler(TradeContract tradeContract) {
        this.tradeContract = tradeContract;
    }

    public ContractHandler(TrusteeContract trusteeContract) {
        this.trusteeContract = trusteeContract;
    }

    public ContractHandler(String path, String password, String contract,int type) {
        switch (type) {
            case 1:
                if(null != contract && !"".equals(contract.trim())){
                    this.tradeContract = new TradeContract(password, path, contract);
                }else {
                    this.tradeContract = new TradeContract(password, path);
                }
                break;
            case 2:
                if(null != contract && !"".equals(contract.trim())){
                    this.trusteeContract = new TrusteeContract(password, path, contract);
                }else {
                    this.trusteeContract = new TrusteeContract(password, path);
                }
                break;
            default:
                break;
        }
    }

    public static ContractHandler getTradeContractHandler(String path, String password) {
        return new ContractHandler(path, password, null,1);
    }
    public static ContractHandler getTradeContractHandler(String path, String password,String contract) {
        return new ContractHandler(path, password, contract,1);
    }

    public static ContractHandler getTrusteeContractHandler(String path, String password) {
        return new ContractHandler(path, password, null,2);
    }

    public static ContractHandler getTrusteeContractHandler(String path, String password, String contract) {
        return new ContractHandler(path, password, contract,2);
    }

    public ContractHandler() {
    }

    /**
     * call by admin
     */
    public String addTrustee(String id, BigInteger fame, String publicKey) {
        String hex = trusteeContract.addTrustee(id, fame, publicKey);
        String hash = sendRawTransaction(hex);
        log.info("addTrustee()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by admin
     */
    public String removeTrustee(String id) {
        String hex = trusteeContract.removeTrustee(id);
        String hash = sendRawTransaction(hex);
        log.info("removeTrustee()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by admin
     */
    public String updateTrusteeFame(String id, BigInteger newFame) {
        String hex = trusteeContract.updateTrusteeFame(id, newFame);
        String hash = sendRawTransaction(hex);
        log.info("updateTrusteeFame()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by trustee
     */
    public String increaseDeposit(BigInteger amount) {
        String hex = trusteeContract.increaseDeposit(amount);
        String hash = sendRawTransaction(hex);
        log.info("increaseDeposit()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by trustee
     */
    public String decreaseDeposit(BigInteger amount) {
        String hex = trusteeContract.decreaseDeposit(amount);
        String hash = sendRawTransaction(hex);
        log.info("decreaseDeposit()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by trustee
     */
    public String updatePublicKey(String newKey) {
        String hex = trusteeContract.updatePublicKey(newKey);
        String hash = sendRawTransaction(hex);
        log.info("updatePublicKey()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by
     */
    public String getTrusteeInfo(String who) throws IOException {
        return trusteeContract.getTrusteeInfo(who);
    }

    /**
     * call by contract
     */
    public String getTrustees(Integer num){
        String hex = trusteeContract.getTrustees(num);
        String hash = sendRawTransaction(hex);
        log.info("getTrustees()--->Hash: {}", hash);
        return hash;
    }

    /**
     * call by
     */
    public boolean isTrustee(String who) throws IOException {
        return trusteeContract.isTrustee(who);
    }

    /**
     * called by admin
     */
    public String createNewTradeOrder(String orderId, Long userId, Integer userType, BigInteger value) {
        String hex = tradeContract.createNewTradeOrder(new BigInteger(orderId), userId, userType, value);
        String hash = sendRawTransaction(hex);
        log.info("createNewTradeOrder()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by admin
     */
    public String cancelTrade(String orderId, Long userId) {
        String hex = tradeContract.cancelTrade(new BigInteger(orderId), userId);
        String hash = sendRawTransaction(hex);
        log.info("cancelTrade()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by admin
     */
    public String confirmTradeOrder(String orderId, Long userId, BigInteger amount) {
        String hex = tradeContract.confirmTradeOrder(new BigInteger(orderId), userId, amount);
        String hash = sendRawTransaction(hex);
        log.info("confirmTradeOrder()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by admin
     */
    public String finishOrder(String orderId) {
        String hex = tradeContract.finishOrder(new BigInteger(orderId));
        String hash = sendRawTransaction(hex);
        log.info("finishOrder()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by admin
     */
    public String withdrawFee(BigInteger amount) {
        String hex = tradeContract.withdrawFee(amount);
        String hash = sendRawTransaction(hex);
        log.info("withdrawFee()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by admin
     */
    public String uploadSecret(String orderId, String secret, Long userId, String verifyData) {
        String hex = tradeContract.uploadSecret(new BigInteger(orderId), secret, userId, verifyData);
        String hash = sendRawTransaction(hex);
        log.info("uploadSecret()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by anyone
     */
    public String getSecret(String orderId, String trusteeId, Long userId) throws IOException {
        String result = tradeContract.getSecret(new BigInteger(orderId), trusteeId, userId);
        log.info("getSecret()--->Result: {}", result);
        return result;
    }

    /**
     * called by anyone
     */
    public Long getOrderBuyer(String orderId) throws IOException {
        Long result = tradeContract.getOrderBuyer(new BigInteger(orderId));
        log.info("getOrderBuyer()--->Result: {}", result);
        return result;
    }


    /**
     * called by anyone
     */
    public Long getOrderSeller(String orderId) throws IOException {
        Long result = tradeContract.getOrderSeller(new BigInteger(orderId));
        log.info("getOrderSeller()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public List<String> getOrderTrustees(String orderId) throws IOException {
        List<String> result = tradeContract.getOrderTrustees(new BigInteger(orderId));
        log.info("getOrderTrustees()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public boolean isOrderTrustee(String orderId, String user) throws IOException {
        boolean result = tradeContract.isOrderTrustee(new BigInteger(orderId), user);
        log.info("isOrderTrustee()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public String addArbitrator(String who) {
        String hex = tradeContract.addArbitrator(who);
        String hash = sendRawTransaction(hex);
        log.info("addArbitrator()--->Result: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public String removeArbitrator(String who) {
        String hex = tradeContract.removeArbitrator(who);
        String hash = sendRawTransaction(hex);
        log.info("addArbitrator()--->Result: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public boolean isArbitrator(String who) throws IOException {
        boolean result = tradeContract.isArbitrator(who);
        log.info("isArbitrator()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public String arbitrate(String orderId, Long userId) {
        String hex = tradeContract.arbitrate(new BigInteger(orderId), userId);
        String hash = sendRawTransaction(hex);
        log.info("arbitrate()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public String judge(String orderId, Long winner) {
        String hex = tradeContract.judge(new BigInteger(orderId), winner);
        String hash = sendRawTransaction(hex);
        log.info("judge()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public Long getRequester(String orderId) throws IOException {
        Long result = tradeContract.getRequester(new BigInteger(orderId));
        log.info("getRequester()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public Long getWinner(String orderId) throws IOException {
        Long result = tradeContract.getWinner(new BigInteger(orderId));
        log.info("getRequester()--->Result: {}", result);
        return result;
    }

    /**
     * called by owner
     */
    public String updateDefaultTrusteeNumber(Integer hosterNum) {
        String hex = tradeContract.updateDefaultTrusteeNumber(hosterNum);
        String hash = sendRawTransaction(hex);
        log.info("updateDefaultTrusteeNumber()--->Hash: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public String updateTrusteeContract(String hoster) {
        String hex = tradeContract.updateTrusteeContract(hoster);
        String hash = sendRawTransaction(hex);
        log.info("updateTrusteeContract()--->Hash: {}", hash);
        return hash;
    }

    public int getOrderStatus(String orderId) throws Exception {
        Long result = tradeContract.getOrderStatus(new BigInteger(orderId));
        log.info("getOrderStatus()--->Result: {}", result);
        return result.intValue();
    }

    public String getVerifyData(String orderId, Long userId) throws Exception{
        String result = tradeContract.getVerifyData(new BigInteger(orderId), userId);
        log.info("getVerifyData()--->Result: {}", result);
        return result;
    }

    public String sendVerifyResult(String orderId, boolean buyerResult, boolean sellerResult){
        String hex = tradeContract.sendVerifyResult(new BigInteger(orderId), buyerResult, sellerResult);
        String hash = sendRawTransaction(hex);
        log.info("sendVerifyResult()--->Hash: {}", hash);
        return hash;
    }
}
