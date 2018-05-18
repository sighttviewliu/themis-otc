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
    private UserContract userContract;
    private ExchangeContract exchangeContract;
    private TokenContract tokenContract;

    public ContractHandler(UserContract userContract) {
        this.userContract = userContract;
    }

    public ContractHandler(ExchangeContract exchangeContract) {
        this.exchangeContract = exchangeContract;
    }

    public ContractHandler(TokenContract tokenContract) {
        this.tokenContract = tokenContract;
    }

    public ContractHandler() {}

    /**
     * called by official
     */
    public void addUser(String id, BigInteger fame, String publicKey){
        String hex = userContract.addUser(id, fame, publicKey);
        String hash = sendRawTransaction(hex);
        log.info("addUser()--->Hash: {}", hash);
    }

    /**
     * called by official
     */
    public void updateUser(String id, BigInteger fame, String publicKey){
        String hex = userContract.updateUser(id, fame, publicKey);
        String hash = sendRawTransaction(hex);
        log.info("updateUser()--->Hash: {}", hash);
    }

    /**
     * called by official
     */
    public void removeUser(String id){
        String hex = userContract.removeUser(id);
        String hash = sendRawTransaction(hex);
        log.info("removeUser()--->Hash: {}", hash);
    }

    /**
     * called by anyone
     */
    public boolean isThemisUser(String id) throws IOException {
        return userContract.isThemisUser(id);
    }

    /**
     * called by anyone
     */
    public boolean isHoster(String id) throws IOException {
        return userContract.isHoster(id);
    }

    /**
     * called by official
     */
    public void addHoster(String id, BigInteger fame, BigInteger deposit, String publicKey){
        String hex = userContract.addHoster(id, fame, deposit, publicKey);
        String hash = sendRawTransaction(hex);
        log.info("addHoster()--->Hash: {}", hash);
    }

    /**
     * called by official
     */
    public void  updateNormalUserToHoster(String id, BigInteger deposit){
        String hex = userContract.updateNormalUserToHoster(id, deposit);
        String hash = sendRawTransaction(hex);
        log.info("updateNormalUserToHoster()--->Hash: {}", hash);
    }

    /**
     * called by official
     */
    public void  removeHoster(String id){
        String hex = userContract.removeHoster(id);
        String hash = sendRawTransaction(hex);
        log.info("removeHoster()--->Hash: {}", hash);
    }

    /**
     * called by official
     */
    public void  updateUserFame(String id, BigInteger fame){
        String hex = userContract.updateUserFame(id, fame);
        String hash = sendRawTransaction(hex);
        log.info("updateUserFame()--->Hash: {}", hash);
    }

    /**
     * called by user
     */
    public void  updateUserDeposit(BigInteger deposit){
        String hex = userContract.updateUserDeposit(deposit);
        String hash = sendRawTransaction(hex);
        log.info("updateUserDeposit()--->Hash: {}", hash);
    }

    /**
     * called by anyone
     */
    public void getHosters(BigInteger num) throws IOException {
        String hex = userContract.getHosters(num);
        //String hash = sendRawTransaction(hex);
        log.info("updateUserDeposit()--->Hex: {}", hex);
    }

    /**
     * called by buyer
     */
    public void createNewTradeOrder(BigInteger orderId, String seller, BigInteger hosterNum){
        String hex = exchangeContract.createNewTradeOrder(orderId,seller,hosterNum);
        String hash = sendRawTransaction(hex);
        log.info("createNewTradeOrder()--->Hash: {}", hash);
    }

    /**
     * called by seller
     */
    public void uploadBuyerShardFromSeller(BigInteger orderID, String shard){
        String hex = exchangeContract.uploadBuyerShardFromSeller(orderID,shard);
        String hash = sendRawTransaction(hex);
        log.info("uploadBuyerShardFromSeller()--->Hash: {}", hash);
    }

    /**
     * called by buyer
     */
    public void uploadSellerShardFromBuyer(BigInteger orderID, String shard){
        String hex = exchangeContract.uploadSellerShardFromBuyer(orderID,shard);
        String hash = sendRawTransaction(hex);
        log.info("uploadSellerShardFromBuyer()--->Hash: {}", hash);
    }

    /**
     * called by anyone
     */
    public void getBuyerShardByHosterID(BigInteger orderID, String hosterID) throws IOException {
        String result = exchangeContract.getBuyerShardByHosterID(orderID,hosterID);
        log.info("getBuyerShardByHosterID()--->Result: {}", result);
    }

    /**
     * called by anyone
     */
    public void getSellerShardByHosterID(BigInteger orderID, String hosterID) throws IOException {
        String result = exchangeContract.getSellerShardByHosterID(orderID,hosterID);
        log.info("getSellerShardByHosterID()--->Result: {}", result);
    }

    /**
     * called by hoster
     */
    public void uploadBuyerDecryptedShard(BigInteger orderID, String decryptedShard){
        String hex = exchangeContract.uploadBuyerDecryptedShard(orderID,decryptedShard);
        String hash = sendRawTransaction(hex);
        log.info("uploadBuyerDecryptedShard()--->Hash: {}", hash);
    }

    /**
     * called by hoster
     */
    public void uploadSellerDecryptedShard(BigInteger orderID, String decryptedShard){
        String hex = exchangeContract.uploadSellerDecryptedShard(orderID,decryptedShard);
        String hash = sendRawTransaction(hex);
        log.info("uploadSellerDecryptedShard()--->Hash: {}", hash);
    }

    /**
     * called by winner
     */
    public String getBuyerDecryptedShard(String seller,BigInteger order, String hoster) throws IOException {
        String result = exchangeContract.getBuyerDecryptedShard(seller, order,hoster);
        log.info("getBuyerDecryptedShard()--->Result: {}", result);
        return result;
    }

    /**
     * called by winner
     */
    public String getSellerDecryptedShard(String buyer, BigInteger order, String hoster) throws IOException {
        String result = exchangeContract.getSellerDecryptedShard(buyer, order,hoster);
        log.info("getSellerDecryptedShard()--->Result: {}", result);
        return result;
    }

    /**
     * called by user
     */
    public boolean isUserHoster(String from, BigInteger orderID) throws IOException {
        return exchangeContract.isUserHoster(from, orderID);
        //log.info("getSellerDecryptedShard()--->Result: {}", result);
    }

    /**
     * called by anyone
     */
    public String getBuyer(BigInteger orderID) throws IOException {
        String result = exchangeContract.getBuyer(orderID);
        log.info("getBuyer()--->Result: {}", result);
        return result;
    }

    /**
     * called by anyone
     */
    public String getSeller(BigInteger orderID) throws IOException {
        String result = exchangeContract.getSeller(orderID);
        log.info("getBuyer()--->Result: {}", result);
        return result;
    }
    /**
     * called by anyone
     */
    public List<String> getShardHosters(BigInteger orderID) throws IOException {
        return exchangeContract.getShardHosters(orderID);
    }

    public void approve(String spender, BigInteger value){
        String hex = tokenContract.approve(spender,value);
        String hash = sendRawTransaction(hex);
        log.info("approve()--->Hash: {}", hash);
    }
}
