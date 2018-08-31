package com.oxchains.themis.wallet.common;

import com.oxchains.themis.common.util.EncryptUtils;
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
    private static String themisTokenAdminSource;
    private static String themisTokenAdminPassword;
    private static Double themisTokenAdminAmount = 100D;

    public static String getEthPoaServerUrl() {
        return ethPoaServerUrl;
    }

    @Value("${eth.poa.server-url}")
    public void setEthPoaServerUrl(String ethPoaServerUrl) {
        ResParam.ethPoaServerUrl = ethPoaServerUrl;
    }

    public static String getThemisTokenAdminSource() {
        return themisTokenAdminSource;
    }

    @Value("${themis.token.admin.source}")
    public void setThemisTokenAdminSource(String themisTokenAdminSource) {
        ResParam.themisTokenAdminSource = themisTokenAdminSource;
    }

    public static String getThemisTokenAdminPassword() {
        if(null != themisTokenAdminPassword){
            return EncryptUtils.decodeBase64(themisTokenAdminPassword);
        }
        return themisTokenAdminPassword;
    }

    @Value("${themis.token.admin.password}")
    public void setThemisTokenAdminPassword(String themisTokenAdminPassword) {
        ResParam.themisTokenAdminPassword = themisTokenAdminPassword;
    }

    public static Double getThemisTokenAdminAmount() {
        return themisTokenAdminAmount;
    }

    @Value("${themis.token.admin.amount}")
    public void setThemisTokenAdminAmount(Double themisTokenAdminAmount) {
        ResParam.themisTokenAdminAmount = themisTokenAdminAmount;
    }
}
