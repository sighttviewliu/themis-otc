package com.oxchains.themis.repo.entity.order;

import lombok.Data;

/**
 * @author anonymity
 * @create 2018-08-20 14:22
 **/
@Data
public class ShareData {
    private String encrypted_share_data;
    private String paillier_proof;
    private String publicKey;
    private String user_id;
}
