package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.common.Coin;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.bitfinex.BitfinexMarketDao;
import com.oxchains.themis.notice.domain.bitfinex.BitfinexMarket;
import com.oxchains.themis.notice.service.RatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gaoyp
 * @create 2018/7/12  11:25
 **/
@Component
@Slf4j
public class BitfinexMarketListener {

    @Value(("${bitfinex_market_url}"))
    private String url;

    @Resource
    private BitfinexMarketDao bitfinexMarketDao;

    @Resource
    private RatesService ratesService;

//        @Scheduled(fixedRate = 1000 * 60)
    public void bitstampListener() {
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("bitstamp market开始监听...");
            String result = HttpUtils.sendGet(url);
            BitfinexMarket bitfinexMarket = (BitfinexMarket) JsonUtil.fromJson(result, BitfinexMarket.class);
            if (null != bitfinexMarket) {
                String last = bitfinexMarket.getLast_price();
                BitfinexMarket btcusd = bitfinexMarketDao.findByCashName(Const.USD.getAbbreviation());
                if (VerifyNumUtil.isNumber(last)) {
                    if (null != btcusd) {
                        btcusd.setCurrentTime(currentTime);
                        btcusd.setLast_price(last);
                        bitfinexMarketDao.save(btcusd);
                    } else {
                        bitfinexMarket.setCurrentTime(currentTime);
                        bitfinexMarket.setCashName(Const.USD.getAbbreviation());
                        bitfinexMarket.setCoinType(Coin.BTC.getCoinType());
                        bitfinexMarketDao.save(bitfinexMarket);
                    }
                    log.info(" bitfinex market 获取成功...");
                } else {
                    log.error("非法字符，本次不更新行情...");
                    return;
                }
            } else {
                log.error(" bitfinex market 获取失败...");
                return;
            }
        } catch (Exception e) {
            log.error(" bitfinex market 获取异常...");
        }
    }


//    @Scheduled(fixedRate = 1000 * 60)
    public void getMarket() {
        getCoinCash(Const.CNY.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.KRW.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.JPY.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.HKD.getAbbreviation(), Coin.BTC.getCoinType());
    }

    public void getCoinCash(String abbreviation, String coinType) {
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            BitfinexMarket btcusd = bitfinexMarketDao.findByCashName(Const.USD.getAbbreviation());
            if (null != btcusd) {
                String last = btcusd.getLast_price();
                String rate = null;
                switch (abbreviation) {
                    case "CNY":
                        rate = ratesService.getCNYRate();
                        break;
                    case "HKD":
                        rate = ratesService.getHKDRate();
                        break;
                    case "KRW":
                        rate = ratesService.getKRWRate();
                        break;
                    case "JPY":
                        rate = ratesService.getJPYRate();
                        break;
                    default:
                        break;
                }

                if (null == rate) {
                    log.error("汇率数据为空...");
                    return;
                } else {
                    BigDecimal coincash = ArithmeticUtils.multiply(rate, last);
                    BitfinexMarket bitfinexMarket = bitfinexMarketDao.findByCashName(abbreviation);
                    if (null != bitfinexMarket) {
                        bitfinexMarket.setLast_price(coincash.toString());
                        bitfinexMarket.setCurrentTime(currentTime);
                        bitfinexMarketDao.save(bitfinexMarket);
                    } else {
                        BitfinexMarket bitfinexMarket1 = new BitfinexMarket();
                        bitfinexMarket1.setLast_price(coincash.toString());
                        bitfinexMarket1.setCoinType(coinType);
                        bitfinexMarket1.setCashName(abbreviation);
                        bitfinexMarket1.setCurrentTime(currentTime);
                        bitfinexMarketDao.save(bitfinexMarket1);
                    }
                }
            } else {
                log.error("获取失败...");
                return;
            }
        } catch (Exception e) {

        }
    }

}
