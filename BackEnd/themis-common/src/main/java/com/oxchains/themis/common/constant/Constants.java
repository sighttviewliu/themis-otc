package com.oxchains.themis.common.constant;

/**
 * @author ccl
 * @time 2017-11-13 10:40
 * @name Constants
 * @desc:
 */
public class Constants {
    /**
     * 分割符
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public static final  String GENERIC_USER_PREFIX = "themis_";

    public static final String BTC_P2SH_TXID = "BTC_P2SH_TXID";
    public static final String BTC_P2SH_SIGN_TXID = "BTC_P2SH_SIGN_TXID";

    public static final String POA_TRANS_STATUS_DOING = "POA_TRANS_STATUS_DOING";
    public static final String POA_TRANS_STATUS_DONE = "POA_TRANS_STATUS_DONE";
    public static final String POA_LATEST_BLOCK_NUM = "POA_LATEST_BLOCK_NUM";
    public static final String POA_LATEST_BLOCK_10 = "POA_LATEST_BLOCK_10";
}
