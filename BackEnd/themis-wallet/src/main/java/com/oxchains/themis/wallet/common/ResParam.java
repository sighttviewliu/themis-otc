package com.oxchains.themis.wallet.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ccl
 * @time 2018-06-07 13:16
 * @name ResParam
 * @desc:
 */
@Component
public class ResParam {
    private static String ethPoaServerUrl;

    public static String getEthPoaServerUrl() {
        return ethPoaServerUrl;
    }

    @Value("${eth.poa.server-url}")
    public void setEthPoaServerUrl(String ethPoaServerUrl) {
        ResParam.ethPoaServerUrl = ethPoaServerUrl;
    }
}
