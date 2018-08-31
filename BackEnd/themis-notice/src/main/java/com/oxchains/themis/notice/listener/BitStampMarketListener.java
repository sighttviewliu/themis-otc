package com.oxchains.themis.notice.listener;


import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.common.Coin;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.average.CNYDetailDao;
import com.oxchains.themis.notice.dao.bitstamp.BitStampMarketDao;
import com.oxchains.themis.notice.domain.bitstamp.BitstampMarket;
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
 * @create 2018/7/11  18:05
 **/
@Component
@Slf4j
public class BitStampMarketListener {

    @Value("${bitstamp_market_url}")
    private String url;

    @Resource
    private BitStampMarketDao bitStampMarketDao;

    @Resource
    private CNYDetailDao cnyDetailDao;

    @Resource
    private RatesService ratesService;

    @Scheduled(fixedRate = 1000 * 60)
    public void bitstampListener() {
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            log.info("bitstamp market开始监听...");
            String result = HttpUtils.sendGet(url);
            BitstampMarket bitstampMarket = (BitstampMarket) JsonUtil.fromJson(result, BitstampMarket.class);
            if (null != bitstampMarket) {
                String last = bitstampMarket.getLast();
                BitstampMarket btcusd = bitStampMarketDao.findByCashName(Const.USD.getAbbreviation());
                if (VerifyNumUtil.isNumber(last)) {
                    if (null != btcusd) {
                        btcusd.setCurrentTime(currentTime);
                        btcusd.setLast(last);
                        bitStampMarketDao.save(btcusd);
                    } else {
                        bitstampMarket.setCurrentTime(currentTime);
                        bitstampMarket.setCashName(Const.USD.getAbbreviation());
                        bitstampMarket.setCoinType(Coin.BTC.getCoinType());
                        bitStampMarketDao.save(bitstampMarket);
                    }
                    log.info("bitstamp market 获取成功...");
                } else {
                    log.error("非法字符，本次不更新行情...");
                    return;
                }
            } else {
                log.error("bitstamp market 获取失败...");
                return;
            }
        } catch (Exception e) {
            log.error("bitstamp market 获取异常...");
        }
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void getMarket() {
        getCoinCash(Const.CNY.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.KRW.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.JPY.getAbbreviation(), Coin.BTC.getCoinType());
        getCoinCash(Const.HKD.getAbbreviation(), Coin.BTC.getCoinType());
    }


    public void getCoinCash(String abbreviation, String coinType) {
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            BitstampMarket btcusd = bitStampMarketDao.findByCashName(Const.USD.getAbbreviation());
            if (null != btcusd) {
                String last = btcusd.getLast();
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
                    String formatPrice = String.format("%.2f", coincash.doubleValue());
                    BitstampMarket bitstampMarket = bitStampMarketDao.findByCashName(abbreviation);
                    if (null != bitstampMarket) {
                        bitstampMarket.setLast(formatPrice.toString());
                        bitstampMarket.setCurrentTime(currentTime);
                        bitStampMarketDao.save(bitstampMarket);
                    } else {
                        BitstampMarket bitstampMarket1 = new BitstampMarket();
                        bitstampMarket1.setLast(formatPrice.toString());
                        bitstampMarket1.setCoinType(coinType);
                        bitstampMarket1.setCashName(abbreviation);
                        bitstampMarket1.setCurrentTime(currentTime);
                        bitStampMarketDao.save(bitstampMarket1);
                    }
                    log.info("bitstamp 行情计算成功");
                }
            } else {
                log.error("获取失败...");
                return;
            }
        } catch (Exception e) {

        }
    }


}
