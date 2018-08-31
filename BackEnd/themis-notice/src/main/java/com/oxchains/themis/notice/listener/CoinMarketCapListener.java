package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.common.Coin;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.coinmarketcap.*;
import com.oxchains.themis.notice.domain.coinmarketcap.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoyp
 * @create 2018/7/11  14:52
 **/
@Component
@Slf4j
public class CoinMarketCapListener {

    @Value("${coinmarketcap_market_url}")
    private String url;

    @Resource
    private CNYDetailsDao cnyDetailsDao;

    @Resource
    private HKDDetailsDao hkdDetailsDao;

    @Resource
    private USDDetailsDao usdDetailsDao;

    @Resource
    private JPYDetailsDao jpyDetailsDao;

    @Resource
    private KRWDetailsDao krwDetailsDao;


    @Scheduled(fixedRate = 1000 * 60)
    public void getRates(){
        getCNYMarket();
        getHKDMarket();
        getJPYMarket();
        getKRWMarket();
        getUSDMarket();
    }

    public CoinMarketCap getJson(String cashType){
        String result = HttpUtils.sendGet(url + cashType);
        CoinMarketCap coinMarketCap = (CoinMarketCap) JsonUtil.fromJson(result, CoinMarketCap.class);
        return coinMarketCap;
    }


    public void getCNYMarket(){

        try {
            CoinMarketCap coinMarketCap = getJson(Const.CNY.getAbbreviation());
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("coinmarketcap "+ Const.CNY.getAbbreviation() +" 监听启动");
            if(null == coinMarketCap){
                log.error("coinmarketcap获取 "+ Const.CNY.getAbbreviation() +" 行情失败");
                return;
            }else {

                String price = coinMarketCap.getData().getQuotes().getCNY().getPrice();
                //保留小数点后两位
                String formatPrice = String.format("%.2f",Double.valueOf(price));

                CNYDetails cny = cnyDetailsDao.findByCoinType(Coin.BTC.getCoinType());
                if (VerifyNumUtil.isNumber(formatPrice)){
                    if(null != cny){
                        cny.setSaveTime(currentTime);
                        cny.setPrice(formatPrice);
                        cnyDetailsDao.save(cny);
                    }else {
                        coinMarketCap.getData().getQuotes().getCNY().setCoinType(Coin.BTC.getCoinType());
                        coinMarketCap.getData().getQuotes().getCNY().setSaveTime(currentTime);
                        coinMarketCap.getData().getQuotes().getCNY().setPrice(formatPrice);
                        cnyDetailsDao.save(coinMarketCap.getData().getQuotes().getCNY());
                    }
                }else {
                    log.error("含有非法字符，本次行情不更新...");
                    return;
                }
            }
            log.info("coinmarketcap获取 "+ Const.CNY.getAbbreviation() +" 行情正常...");
        }catch (Exception e){
            log.error(Const.CNY.getAbbreviation() + " 获取失败");
            return;
        }

    }

    public void getJPYMarket(){
        try {
            CoinMarketCap coinMarketCap = getJson(Const.JPY.getAbbreviation());
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("coinmarketcap "+ Const.JPY.getAbbreviation() +" 监听启动");
            if(null == coinMarketCap){
                log.error("coinmarketcap获取 "+ Const.JPY.getAbbreviation() +" 行情失败");
                return;
            }else {

                String price = coinMarketCap.getData().getQuotes().getJPY().getPrice();
                //保留小数点后两位
                String formatPrice = String.format("%.2f",Double.valueOf(price));

                JPYDetails jpy = jpyDetailsDao.findByCoinType(Coin.BTC.getCoinType());
                if (VerifyNumUtil.isNumber(formatPrice)){
                    if(null != jpy){
                        jpy.setSaveTime(currentTime);
                        jpy.setPrice(formatPrice);
                        jpyDetailsDao.save(jpy);
                    }else {
                        coinMarketCap.getData().getQuotes().getJPY().setCoinType(Coin.BTC.getCoinType());
                        coinMarketCap.getData().getQuotes().getJPY().setSaveTime(currentTime);
                        coinMarketCap.getData().getQuotes().getJPY().setPrice(formatPrice);
                        jpyDetailsDao.save(coinMarketCap.getData().getQuotes().getJPY());
                    }
                }else {
                    log.error("含有非法字符，本次行情不更新...");
                    return;
                }
            }
            log.info("coinmarketcap获取 "+ Const.JPY.getAbbreviation() +" 行情正常..." + currentTime);
        }catch (Exception e){
            log.error(Const.JPY.getAbbreviation() + " 获取失败");
            return;
        }

    }

    public void getKRWMarket(){
        try {
            CoinMarketCap coinMarketCap = getJson(Const.KRW.getAbbreviation());
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("coinmarketcap "+ Const.KRW.getAbbreviation() +" 监听启动");
            if(null == coinMarketCap){
                log.error("coinmarketcap获取 "+ Const.KRW.getAbbreviation() +" 行情失败");
                return;
            }else {

                String price = coinMarketCap.getData().getQuotes().getKRW().getPrice();
                //保留小数点后两位
                String formatPrice = String.format("%.2f",Double.valueOf(price));

                KRWDetails krw = krwDetailsDao.findByCoinType(Coin.BTC.getCoinType());
                if (VerifyNumUtil.isNumber(formatPrice)){
                    if(null != krw){
                        krw.setSaveTime(currentTime);
                        krw.setPrice(formatPrice);
                        krwDetailsDao.save(krw);
                    }else {
                        coinMarketCap.getData().getQuotes().getKRW().setCoinType(Coin.BTC.getCoinType());
                        coinMarketCap.getData().getQuotes().getKRW().setSaveTime(currentTime);
                        coinMarketCap.getData().getQuotes().getKRW().setPrice(formatPrice);
                        krwDetailsDao.save(coinMarketCap.getData().getQuotes().getKRW());
                    }
                }else {
                    log.error("含有非法字符，本次行情不更新...");
                    return;
                }
            }
            log.info("coinmarketcap "+ Const.KRW.getAbbreviation() +" 行情获取正常..." + currentTime);
        }catch (Exception e){
            log.error("获取失败");
            return;
        }
    }

    public void getUSDMarket(){
        try {
            CoinMarketCap coinMarketCap = getJson(Const.USD.getAbbreviation());
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("coinmarketcap "+ Const.USD.getAbbreviation() +" 监听启动");
            if(null == coinMarketCap){
                log.error("coinmarketcap获取 "+ Const.USD.getAbbreviation() +" 行情失败");
                return;
            }else {

                String price = coinMarketCap.getData().getQuotes().getUSD().getPrice();
                //保留小数点后两位
                String formatPrice = String.format("%.2f",Double.valueOf(price));

                USDDetails usd = usdDetailsDao.findByCoinType(Coin.BTC.getCoinType());
                if (VerifyNumUtil.isNumber(formatPrice)){
                    if(null != usd){
                        usd.setSaveTime(currentTime);
                        usd.setPrice(formatPrice);
                        usdDetailsDao.save(usd);
                    }else {
                        coinMarketCap.getData().getQuotes().getUSD().setCoinType(Coin.BTC.getCoinType());
                        coinMarketCap.getData().getQuotes().getUSD().setSaveTime(currentTime);
                        coinMarketCap.getData().getQuotes().getUSD().setPrice(formatPrice);
                        usdDetailsDao.save(coinMarketCap.getData().getQuotes().getUSD());
                    }
                }else {
                    log.error("含有非法字符，本次行情不更新...");
                    return;
                }
            }
            log.info("coinmarketcap "+Const.USD.getAbbreviation()+" 行情获取正常..." + currentTime);
        }catch (Exception e){
            log.error("获取失败");
            return;
        }
    }


    public void getHKDMarket(){
        try {
            CoinMarketCap coinMarketCap = getJson(Const.HKD.getAbbreviation());
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("coinmarketcap "+ Const.HKD.getAbbreviation() +" 监听启动");
            if(null == coinMarketCap){
                log.error("coinmarketcap获取 "+Const.HKD.getAbbreviation()+" 行情失败");
                return;
            }else {

                String price = coinMarketCap.getData().getQuotes().getHKD().getPrice();
                //保留小数点后两位
                String formatPrice = String.format("%.2f",Double.valueOf(price));

                HKDDetails hkd = hkdDetailsDao.findByCoinType(Coin.BTC.getCoinType());
                if (VerifyNumUtil.isNumber(formatPrice)){
                    if(null != hkd){
                        hkd.setSaveTime(currentTime);
                        hkd.setPrice(formatPrice);
                        hkdDetailsDao.save(hkd);
                    }else {
                        coinMarketCap.getData().getQuotes().getHKD().setCoinType(Coin.BTC.getCoinType());
                        coinMarketCap.getData().getQuotes().getHKD().setSaveTime(currentTime);
                        coinMarketCap.getData().getQuotes().getHKD().setPrice(formatPrice);
                        hkdDetailsDao.save(coinMarketCap.getData().getQuotes().getHKD());
                    }
                }else {
                    log.error("含有非法字符，本次行情不更新...");
                    return;
                }
            }
            log.info("coinmarketcap "+ Const.HKD.getAbbreviation() +" 行情获取正常..." + currentTime);
        }catch (Exception e){
            log.error("获取失败");
            return;
        }
    }
}
