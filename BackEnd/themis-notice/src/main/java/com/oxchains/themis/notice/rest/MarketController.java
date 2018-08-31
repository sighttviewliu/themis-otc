package com.oxchains.themis.notice.rest;

import com.oxchains.themis.common.aop.ControllerLogs;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.service.MarketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author gaoyp
 * @create 2018/7/30  15:46
 **/
@RestController
@RequestMapping("/market")
public class MarketController {


    @Resource
    private MarketService marketService;

    /**
     * 获取average交易所数据
     **/
    @ControllerLogs(description = "获取average交易所数据")
    @GetMapping("/average/{cashName}/{coinType}")
    public RestResp getAverageMarket(@PathVariable("cashName") String cashName,
                                     @PathVariable("coinType") String coinType){
        return marketService.getAverageMarket(cashName,coinType);
    }

    /**
     * 获取bitstamp交易所数据
     **/
    @ControllerLogs(description = "获取bitstamp交易所数据")
    @GetMapping("/bitstamp/{cashName}/{coinType}")
    public RestResp getBitstampMarket(@PathVariable("cashName") String cashName,
                                     @PathVariable("coinType") String coinType){
        return marketService.getBitstampMarket(cashName,coinType);
    }

    /**
     * 获取coinmarketcap港币行情
     **/
    @ControllerLogs(description = "获取coinmarketcap港币行情")
    @GetMapping("/coinmarketcap/{cashName}/{coinType}")
    public RestResp getCoinmarketcapMarket(@PathVariable("cashName") String cashName,
                                        @PathVariable("coinType") String coinType){
        return marketService.getCoinmarketcapMarket(cashName,coinType);
    }


    /**
     * 巴比特交易所数据
     **/
    @ControllerLogs(description = "巴比特交易所数据")
    @GetMapping("/bitfinex/{cashName}/{coinType}")
    public RestResp getBitfinexMarket(@PathVariable("cashName") String cashName,
                                      @PathVariable("coinType") String coinType){
        return marketService.getBitfinexMarket(cashName,coinType);
    }

}
