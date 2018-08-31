package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import java.util.List;

/**
 * @author anonymity
 * @create 2018-08-22 16:35
 **/
@Data
public class BtcAddressInfo {
    private String address;
    private String total_received;
    private String total_sent;
    private String balance;
    private String unconfirmed_balance;
    private String final_balance;
    private String n_tx;
    private String unconfirmed_n_tx;
    private String final_n_tx;
    private List<BtcTxRefs> txrefs;
    private String tx_url;
}
