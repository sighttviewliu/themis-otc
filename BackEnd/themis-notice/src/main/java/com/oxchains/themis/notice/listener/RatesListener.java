package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.util.HttpUtils;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyNumUtil;
import com.oxchains.themis.notice.common.Const;
import com.oxchains.themis.notice.dao.average.CNYDetailDao;
import com.oxchains.themis.notice.dao.average.HKDDetailDao;
import com.oxchains.themis.notice.dao.average.JPYDetailDao;
import com.oxchains.themis.notice.dao.average.KRWDetailDao;
import com.oxchains.themis.notice.domain.rate.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Gaoyp
 * @Description:
 * @Date: Create in 下午2:43 2018/7/7
 * @Modified By:
 */
@Component
@Slf4j
public class RatesListener {

    @Resource
    private CNYDetailDao cnyDetailDao;
    @Resource
    private KRWDetailDao krwDetailDao;
    @Resource
    private JPYDetailDao jpyDetailDao;
    @Resource
    private HKDDetailDao hkdDetailDao;


    @Value("${global_rate_url}")
    private String url;


    @Scheduled(cron = "0 0 0 * * ?")
    public void getRates(){
        getCNYRate();
        getHKDRate();
        getJPYRate();
        getKRWRate();
    }

    public ExchangeRate getJson(){
        String result = HttpUtils.sendGet(url);
        ExchangeRate exchangeRate = (ExchangeRate) JsonUtil.fromJson(result, ExchangeRate.class);
        return exchangeRate;
    }

    /**
     * 获取CNY全球汇率
     **/
    public void getCNYRate(){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ExchangeRate exchangeRate = getJson();
            log.info("人民币汇率开始监听...");
            if (null == exchangeRate){
                log.error("人民币汇率失败...");
                return;
            }else {

                String cnyName = exchangeRate.getRates().getCNY().getName();
                String cnyRate = exchangeRate.getRates().getCNY().getRate();

                CNYDetail cny = cnyDetailDao.findByName(Const.CNY.getFullName());
                if (VerifyNumUtil.isNumber(cnyRate)){
                    if (null != cny ){
                        cny.setDate(currentTime);
                        cny.setName(cnyName);
                        cny.setRate(cnyRate);
                        cnyDetailDao.save(cny);
                    }else {
                        exchangeRate.getRates().getCNY().setDate(currentTime);

                        cnyDetailDao.save(exchangeRate.getRates().getCNY());
                    }
                }else {
                    log.error("获取人民币汇率含有异常字符，本次不更新");
                    return;
                }
                log.info("人民币汇率获取正常...");
            }
        }catch (Exception e){
            log.error("获取人民币汇率异常...", e);
            return;
        }

    }


    /**
     * 获取HKD汇率
     **/
    public void getHKDRate(){
        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ExchangeRate exchangeRate = getJson();
            log.info("港币汇率开始监听...");
            if (null == exchangeRate){
                log.error("港币汇率失败...");
                return;
            }else {

                String hkdName = exchangeRate.getRates().getHKD().getName();
                String hkdRate = exchangeRate.getRates().getHKD().getRate();

                HKDDetail hkd = hkdDetailDao.findByName(Const.HKD.getFullName());
                if (VerifyNumUtil.isNumber(hkdRate)){
                    if (null != hkd ){
                        hkd.setDate(currentTime);
                        hkd.setName(hkdName);
                        hkd.setRate(hkdRate);
                        hkdDetailDao.save(hkd);
                    }else {
                        exchangeRate.getRates().getHKD().setDate(currentTime);
                        hkdDetailDao.save(exchangeRate.getRates().getHKD());
                    }
                }else {
                    log.error("获取港币汇率含有异常字符，本次不更新");
                    return;
                }
                log.info("港币汇率获取正常...");
            }
        }catch (Exception e){
            log.error("获取港币汇率异常...", e);
            return;
        }

    }

    /**
     * 获取KRW汇率
     **/
    public void getKRWRate(){

        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ExchangeRate exchangeRate = getJson();
            log.info("韩元汇率开始监听...");
            if (null == exchangeRate){
                log.error("韩元汇率失败...");
                return;
            }else {

                String krwName = exchangeRate.getRates().getKRW().getName();
                String krwRate = exchangeRate.getRates().getKRW().getRate();

                KRWDetail krw = krwDetailDao.findByName(Const.KRW.getFullName());
                if (VerifyNumUtil.isNumber(krwRate)){
                    if (null != krw ){
                        krw.setDate(currentTime);
                        krw.setName(krwName);
                        krw.setRate(krwRate);
                        krwDetailDao.save(krw);
                    }else {
                        exchangeRate.getRates().getKRW().setDate(currentTime);
                        krwDetailDao.save(exchangeRate.getRates().getKRW());
                    }
                }else {
                    log.error("获取韩元汇率含有异常字符，本次不更新");
                    return;
                }
                log.info("韩元汇率获取正常...");
            }
        }catch (Exception e){
            log.error("获取韩元汇率异常...", e);
            return;
        }

    }

    /**
     * 获取JPY汇率
     **/
    public void getJPYRate(){

        try {
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ExchangeRate exchangeRate = getJson();
            log.info("日元汇率开始监听...");
            if (null == exchangeRate){
                log.error("日元汇率失败...");
                return;
            }else {

                String jpyName = exchangeRate.getRates().getJPY().getName();
                String jpyRate = exchangeRate.getRates().getJPY().getRate();

                JPYDetail jpy = jpyDetailDao.findByName(Const.HKD.getFullName());
                if (VerifyNumUtil.isNumber(jpyRate)){
                    if (null != jpy ){
                        jpy.setDate(currentTime);
                        jpy.setName(jpyName);
                        jpy.setRate(jpyRate);
                        jpyDetailDao.save(jpy);
                    }else {
                        exchangeRate.getRates().getJPY().setDate(currentTime);
                        jpyDetailDao.save(exchangeRate.getRates().getJPY());
                    }
                }else {
                    log.error("获取日元汇率含有异常字符，本次不更新");
                    return;
                }
                log.info("日元汇率获取正常...");
            }
        }catch (Exception e){
            log.error("获取日元汇率异常...", e);
            return;
        }
    }
}
