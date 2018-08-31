package com.oxchains.themis.notice.service;

import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.average.AverageMarketDao;
import com.oxchains.themis.notice.dao.bitfinex.BitfinexMarketDao;
import com.oxchains.themis.notice.dao.bitstamp.BitStampMarketDao;
import com.oxchains.themis.notice.dao.coinmarketcap.*;
import com.oxchains.themis.notice.domain.MarketParam;
import com.oxchains.themis.notice.domain.average.AverageMarket;
import com.oxchains.themis.notice.domain.bitfinex.BitfinexMarket;
import com.oxchains.themis.notice.domain.bitstamp.BitstampMarket;
import com.oxchains.themis.notice.domain.coinmarketcap.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gaoyp
 * @create 2018/7/30  15:46
 **/
@Service
@Slf4j
public class MarketService {

    @Resource
    private AverageMarketDao averageMarketDao;

    @Resource
    private BitStampMarketDao bitStampMarketDao;

    @Resource
    private CNYDetailsDao cnyDetailsDao;

    @Resource
    private HKDDetailsDao hkdDetailsDao;

    @Resource
    private JPYDetailsDao jpyDetailsDao;

    @Resource
    private KRWDetailsDao krwDetailsDao;

    @Resource
    private USDDetailsDao usdDetailsDao;

    @Resource
    BitfinexMarketDao bitfinexMarketDao;

    public RestResp getAverageMarket(String cashName, String coinType) {
        try {
            AverageMarket averageMarket = averageMarketDao.findByCashNameAndCoinType(cashName, coinType);
            if (null != averageMarket) {
                return RestResp.success("获取average行情成功", new MarketParam(averageMarket.getCashName(),averageMarket.getCoinType(),averageMarket.getLast()));
            } else {
                return RestResp.fail("average行情为空，请稍后再试");
            }
        } catch (Exception e) {
            log.error("获取average行情出现异常", e);
            return RestResp.fail("获取average行情出现异常");
        }
    }


    public RestResp getBitstampMarket(String cashName, String coinType) {
        try {
            BitstampMarket bitstampMarket = bitStampMarketDao.findByCashNameAndCoinType(cashName, coinType);
            if (null != bitstampMarket) {
                return RestResp.success("获取bitstamp行情成功", new MarketParam(bitstampMarket.getCashName(),bitstampMarket.getCoinType(),bitstampMarket.getLast()));
            } else {
                return RestResp.fail("bitstamp行情为空，请稍后再试");
            }
        } catch (Exception e) {
            log.error("获取bitstamp行情出现异常", e);
            return RestResp.fail("获取bitstamp行情出现异常");
        }
    }


    public RestResp getCoinmarketcapMarket(String cashName, String coinType) {

        try {
            switch (cashName) {
                case "CNY":
                    CNYDetails cnyDetails = cnyDetailsDao.findByCoinType(coinType);
                    if (null != cnyDetails) {
                        cnyDetails.setCashName(Const.CNY.getAbbreviation());
                        return RestResp.success("获取coinmarketcap cny行情成功", new MarketParam(cnyDetails.getCashName(),cnyDetails.getCoinType(),cnyDetails.getPrice()));
                    } else {
                        return RestResp.fail("coinmarketcap cny行情为空，请稍后再试");
                    }
                case "KRW":
                    KRWDetails krwDetails = krwDetailsDao.findByCoinType(coinType);
                    if (null != krwDetails) {
                        krwDetails.setCashName(Const.KRW.getAbbreviation());
                        return RestResp.success("获取coinmarketcap krw行情成功", new MarketParam(krwDetails.getCashName(),krwDetails.getCoinType(),krwDetails.getPrice()));
                    } else {
                        return RestResp.fail("coinmarketcap krw行情为空，请稍后再试");
                    }
                case "JPY":
                    JPYDetails jpyDetails = jpyDetailsDao.findByCoinType(coinType);
                    if (null != jpyDetails) {
                        jpyDetails.setCashName(Const.JPY.getAbbreviation());
                        return RestResp.success("获取coinmarketcap jpy行情成功", new MarketParam(jpyDetails.getCashName(),jpyDetails.getCoinType(),jpyDetails.getPrice()));
                    } else {
                        return RestResp.fail("coinmarketcap jpy行情为空，请稍后再试");
                    }
                case "HKD":
                    HKDDetails hkdDetails = hkdDetailsDao.findByCoinType(coinType);
                    if (null != hkdDetails) {
                        hkdDetails.setCashName(Const.HKD.getAbbreviation());
                        return RestResp.success("获取coinmarketcap hkd行情成功", new MarketParam(hkdDetails.getCashName(),hkdDetails.getCoinType(),hkdDetails.getPrice()));
                    } else {
                        return RestResp.fail("coinmarketcap hkd行情为空，请稍后再试");
                    }
                case "USD":
                    USDDetails usdDetails = usdDetailsDao.findByCoinType(coinType);
                    if (null != usdDetails) {
                        usdDetails.setCashName(coinType);
                        return RestResp.success("获取coinmarketcap cny行情成功", new MarketParam(usdDetails.getCashName(),usdDetails.getCoinType(),usdDetails.getPrice()));
                    } else {
                        return RestResp.fail("coinmarketcap usd行情为空，请稍后再试");
                    }
                default:
                    return RestResp.fail("不支持该货币");
            }
        } catch (Exception e) {
            log.error("获取coinmarketcap行情异常", e);
            return RestResp.fail("获取coinmarketcap行情异常");
        }

    }

    public RestResp getBitfinexMarket(String cashName, String coinType) {
        try {
            BitfinexMarket bitfinexMarket = bitfinexMarketDao.findByCashNameAndCoinType(cashName, coinType);
            if (null != bitfinexMarket) {
                return RestResp.success("获取bitfinex行情成功", new MarketParam(bitfinexMarket.getCashName(),bitfinexMarket.getCoinType(),bitfinexMarket.getLast_price()));
            } else {
                return RestResp.fail("bitfinex行情为空，请稍后再试");
            }
        } catch (Exception e) {
            log.error("获取bitfinex行情出现异常", e);
            return RestResp.fail("获取bitfinex行情出现异常");
        }
    }
}
