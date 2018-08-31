package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.common.Coin;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.average.AverageMarketDao;
import com.oxchains.themis.notice.domain.average.AverageMarket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoyp
 * @create 2018/7/10  11:13
 **/
@Component
@Slf4j
public class AverageListener {

    @Value("${average_market_url}")
    private String url;

    @Resource
    private AverageMarketDao averageMarketDao;

//    @Scheduled(fixedRate = 1000 * 60)
    public void getBTCCash(){
        averageListener(Const.CNY.getAbbreviation(),Coin.BTC.getCoinType());
        averageListener(Const.HKD.getAbbreviation(),Coin.BTC.getCoinType());
        averageListener(Const.JPY.getAbbreviation(),Coin.BTC.getCoinType());
        averageListener(Const.KRW.getAbbreviation(),Coin.BTC.getCoinType());
        averageListener(Const.USD.getAbbreviation(),Coin.BTC.getCoinType());
    }

    public void averageListener(String abbreviation,String coinType){

        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("average market  " + coinType + abbreviation + "  开始监听...");
            String result = HttpUtils.sendGet(url + coinType + abbreviation);
             AverageMarket averageMarket= (AverageMarket) JsonUtil.fromJson(result, AverageMarket.class);
             if (null != averageMarket){
                 String last = averageMarket.getLast();
                 AverageMarket coincash = averageMarketDao.findByCashName(abbreviation);
                 if (VerifyNumUtil.isNumber(last)){
                     if (null != coincash){
                         coincash.setLast(last);
                         coincash.setCurrentTime(currentTime);
                         coincash.setCoinType(coinType);
                         averageMarketDao.save(coincash);
                     }else {
                         averageMarket.setCurrentTime(currentTime);
                         averageMarket.setCashName(abbreviation);
                         averageMarket.setCoinType(coinType);
                         averageMarketDao.save(averageMarket);
                     }
                     log.info("average market  " + coinType + abbreviation +"  获取成功...");
                 }else {
                     log.error("非法字符，本次不更新行情...");
                     return;
                 }
             }else {
                 log.error("average market  " + coinType + abbreviation +"  获取失败...");
                 return;
             }
        }catch (Exception e){
            log.error("average market  " + coinType + abbreviation +"  获取异常...");
        }
    }
}
