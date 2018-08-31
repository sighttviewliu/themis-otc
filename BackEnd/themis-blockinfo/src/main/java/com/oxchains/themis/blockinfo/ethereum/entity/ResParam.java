package com.oxchains.themis.blockinfo.ethereum.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ccl
 * @time 2018-06-25 10:49
 * @name ResParam
 * @desc:
 */
@Component
public class ResParam {
    private static String ethBlockinfoServer /*= "http://101.254.183.20:8556"*/;

    private static int ethBlockCheckStep = 100;

    public static String getEthBlockinfoServer() {
        return ethBlockinfoServer;
    }

    @Value("${eth.blockinfo.server}")
    public void setEthBlockinfoServer(String ethBlockinfoServer) {
        ResParam.ethBlockinfoServer = ethBlockinfoServer;
    }

    public static int getEthBlockCheckStep() {
        return ethBlockCheckStep;
    }

    @Value("${eth.block.check.step}")
    public void setEthBlockCheckStep(int ethBlockCheckStep) {
        ResParam.ethBlockCheckStep = ethBlockCheckStep;
    }
}
