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
public class ContractHandler extends AbstractContract{

    private TradeContract tradeContract;
    private TrusteeContract trusteeContract;

    public ContractHandler(TradeContract tradeContract) {
        this.tradeContract = tradeContract;
    }

    public ContractHandler(TrusteeContract trusteeContract) {
        this.trusteeContract = trusteeContract;
    }

    public ContractHandler(String path, String password, int type){
        switch (type){
            case 1:
                this.tradeContract = new TradeContract(password, path);
                break;
            case 2:
                this.trusteeContract = new TrusteeContract(password, path);
                break;
            default:
                break;
        }
    }

    public ContractHandler() {}

    /**
     * call by admin
     */
    public void addTrustee(String id, BigInteger fame, String publicKey){
        String hex = trusteeContract.addTrustee(id, fame, publicKey);
        String hash = sendRawTransaction(hex);
        log.info("addTrustee()--->Hash: {}", hash);
    }

    /**
     * call by admin
     */
    public void removeTrustee(String id){
        String hex = trusteeContract.removeTrustee(id);
        String hash = sendRawTransaction(hex);
        log.info("removeTrustee()--->Hash: {}", hash);
    }

    /**
     * call by admin
     */
    public void updateTrusteeFame(String id, BigInteger newFame){
        String hex = trusteeContract.updateTrusteeFame(id, newFame);
        String hash = sendRawTransaction(hex);
        log.info("updateTrusteeFame()--->Hash: {}", hash);
    }

    /**
     * call by trustee
     */
    public void increaseDeposit(BigInteger amount){
        String hex = trusteeContract.increaseDeposit(amount);
        String hash = sendRawTransaction(hex);
        log.info("increaseDeposit()--->Hash: {}", hash);
    }

    /**
     * call by trustee
     */
    public void decreaseDeposit(BigInteger amount){
        String hex = trusteeContract.decreaseDeposit(amount);
        String hash = sendRawTransaction(hex);
        log.info("decreaseDeposit()--->Hash: {}", hash);
    }

    /**
     * call by trustee
     */
    public void updatePublicKey(String newKey){
        String hex = trusteeContract.updatePublicKey(newKey);
        String hash = sendRawTransaction(hex);
        log.info("updatePublicKey()--->Hash: {}", hash);
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
    public void getTrustees(BigInteger num) throws IOException {
        String hex = trusteeContract.getTrustees(num);
        String hash = sendRawTransaction(hex);
        log.info("getTrustees()--->Hash: {}", hash);
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
    public void createNewTradeOrder(BigInteger orderId, BigInteger userId,BigInteger userType, BigInteger value){
        String hex = tradeContract.createNewTradeOrder(orderId, userId,userType, value);
        String hash = sendRawTransaction(hex);
        log.info("createNewTradeOrder()--->Hash: {}", hash);
    }

    /**
     * called by admin
     */
    public void cancelTrade(BigInteger orderId, BigInteger userId){
        String hex = tradeContract.cancelTrade(orderId, userId);
        String hash = sendRawTransaction(hex);
        log.info("cancelTrade()--->Hash: {}", hash);
    }

    /**
     * called by admin
     */
    public void confirmTradeOrder(BigInteger orderId, BigInteger userId, BigInteger amount){
        String hex = tradeContract.confirmTradeOrder(orderId, userId, amount);
        String hash = sendRawTransaction(hex);
        log.info("confirmTradeOrder()--->Hash: {}", hash);
    }

    /**
     * called by admin
     */
    public void finishOrder(BigInteger orderId){
        String hex = tradeContract.finishOrder(orderId);
        String hash = sendRawTransaction(hex);
        log.info("finishOrder()--->Hash: {}", hash);
    }

    /**
     * called by admin
     */
    public void withdrawFee(BigInteger amount){
        String hex = tradeContract.withdrawFee(amount);
        String hash = sendRawTransaction(hex);
        log.info("withdrawFee()--->Hash: {}", hash);
    }

    /**
     * called by admin
     */
    public void uploadSecret(BigInteger orderId, String secret, BigInteger userId){
        String hex = tradeContract.uploadSecret(orderId,secret, userId);
        String hash = sendRawTransaction(hex);
        log.info("uploadSecret()--->Hash: {}", hash);
    }

    /**
     * called by anyone
     */
    public String getSecret(BigInteger orderId, String trusteeId, BigInteger userId) throws IOException {
        String result = tradeContract.getSecret(orderId,trusteeId, userId);
        log.info("getSecret()--->Result: {}", result);
        return result;
    }

    /**
     * called by anyone
     */
    public Long getOrderBuyer(BigInteger orderId) throws IOException {
        Long result = tradeContract.getOrderBuyer(orderId);
        log.info("getOrderBuyer()--->Result: {}", result);
        return result;
    }


    /**
     * called by anyone
     */
    public Long getOrderSeller(BigInteger orderId) throws IOException {
        Long result = tradeContract.getOrderSeller(orderId);
        log.info("getOrderSeller()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public List<String> getOrderTrustees(BigInteger orderId) throws IOException {
        List<String> result = tradeContract.getOrderTrustees(orderId);
        log.info("getOrderTrustees()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public boolean isOrderTrustee(BigInteger orderId, String user) throws IOException {
        boolean result = tradeContract.isOrderTrustee(orderId, user);
        log.info("isOrderTrustee()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public String addArbitrator(String who){
        String hex = tradeContract.addArbitrator(who);
        String hash = sendRawTransaction(hex);
        log.info("addArbitrator()--->Result: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public String removeArbitrator(String who){
        String hex = tradeContract.removeArbitrator(who);
        String hash = sendRawTransaction(hex);
        log.info("addArbitrator()--->Result: {}", hash);
        return hash;
    }

    /**
     * called by
     */
    public boolean isArbitrator(String who) throws IOException {
        boolean result =tradeContract.isArbitrator(who);
        log.info("isArbitrator()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public void arbitrate(BigInteger orderId, BigInteger userId){
        String hex = tradeContract.arbitrate(orderId, userId);
        String hash = sendRawTransaction(hex);
        log.info("arbitrate()--->Hash: {}", hash);
    }

    /**
     * called by
     */
    public void judge(BigInteger orderId, BigInteger winner){
        String hex = tradeContract.judge(orderId, winner);
        String hash = sendRawTransaction(hex);
        log.info("judge()--->Hash: {}", hash);
    }

    /**
     * called by
     */
    public Long getRequester(BigInteger orderId) throws IOException {
        Long result = tradeContract.getRequester(orderId);
        log.info("getRequester()--->Result: {}", result);
        return result;
    }

    /**
     * called by
     */
    public Long getWinner(BigInteger orderId) throws IOException {
        Long result = tradeContract.getWinner(orderId);
        log.info("getRequester()--->Result: {}", result);
        return result;
    }

    /**
     * called by owner
     */
    public void updateDefaultTrusteeNumber(BigInteger hosterNum){
        String hex = tradeContract.updateDefaultTrusteeNumber(hosterNum);
        String hash = sendRawTransaction(hex);
        log.info("updateDefaultTrusteeNumber()--->Hash: {}", hash);
    }

    /**
     * called by
     */
    public void updateTrusteeContract(String hoster){
        String hex = tradeContract.updateTrusteeContract(hoster);
        String hash = sendRawTransaction(hex);
        log.info("updateTrusteeContract()--->Hash: {}", hash);
    }

}
