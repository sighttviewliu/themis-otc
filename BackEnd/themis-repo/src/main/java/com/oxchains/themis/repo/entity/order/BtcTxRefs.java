package com.oxchains.themis.repo.entity.order;

import lombok.Data;

/**
 * @author anonymity
 * @create 2018-08-22 17:33
 **/
@Data
public class BtcTxRefs {
    private String tx_hash;
    private Integer block_height;
    private String tx_input_n;
    private String tx_output_n;
    private String value;
    private String ref_balance;
    private String confirmations;
    private String confirmed;
    private String double_spend;

}
