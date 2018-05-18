package com.oxchains.themis.common.ethereum;

/**
 * @author ccl
 * @time 2018-05-14 11:37
 * @name ExchangeRpcMethod
 * @desc:
 */
public interface ExchangeRpcMethod {
    enum METHOD {
        GET_TRANSACTION_RECEIPT("getTransactionReceipt"),
        ADD_USER("addUser"),
        UPDATE_USER("updateUser"),
        REMOVE_USER("removeUser"),
        IS_THEMIS_USER("isThemisUser"),
        IS_HOSTER("isHoster"),
        ADD_HOSTER("addHoster"),
        UPDATE_NORMAL_USER_TO_HOSTER("updateNormalUserToHoster"),
        REMOVE_HOSTER("removeHoster"),
        UPDATE_USER_FAME("updateUserFame"),
        UPDATE_USER_DEPOSIT("updateUserDeposit"),
        GET_HOSTERS("getHosters"),
        CREATE_NEW_TRADE_ORDER("createNewTradeOrder"),
        UPLOAD_BUYER_SHARD_FROM_SELLER("uploadBuyerShardFromSeller"),
        UPLOAD_SELLER_SHARD_FROM_BUYER("uploadSellerShardFromBuyer"),
        GET_BUYER_SHARD_BY_HOSTER_ID("getBuyerShardByHosterID"),
        GET_SELLER_SHARD_BY_HOSTER_ID("getSellerShardByHosterID"),
        UPLOAD_BUYER_DECRYPTED_SHARD("uploadBuyerDecryptedShard"),
        UPLOAD_SELLER_DECRYPTED_SHARD("uploadSellerDecryptedShard"),
        GET_BUYER_DECRYPTED_SHARD("getBuyerDecryptedShard"),
        GET_SELLER_DECRYPTED_SHARD("getSellerDecryptedShard"),
        IS_USER_HOSTER("isUserHoster"),
        GET_BUYER("getBuyer"),
        GET_SELLER("getSeller"),
        GET_SHARD_HOSTER("getShardHoster");
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        METHOD(String name) {
            this.name = name;
        }
    }
}
