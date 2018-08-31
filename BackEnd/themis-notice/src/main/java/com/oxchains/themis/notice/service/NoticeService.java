package com.oxchains.themis.notice.service;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.constant.Const;
import com.oxchains.themis.common.constant.notice.NoticeConstants;
import com.oxchains.themis.common.constant.notice.NoticeTxStatus;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.ArithmeticUtils;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.dao.*;
import com.oxchains.themis.notice.dao.average.AverageMarketDao;
import com.oxchains.themis.notice.dao.bitfinex.BitfinexMarketDao;
import com.oxchains.themis.notice.dao.bitstamp.BitStampMarketDao;
import com.oxchains.themis.notice.dao.coinmarketcap.*;
import com.oxchains.themis.notice.domain.*;
import com.oxchains.themis.notice.domain.Currency;
import com.oxchains.themis.notice.domain.average.AverageMarket;
import com.oxchains.themis.notice.domain.bitfinex.BitfinexMarket;
import com.oxchains.themis.notice.domain.bitstamp.BitstampMarket;
import com.oxchains.themis.notice.domain.coinmarketcap.*;
import com.oxchains.themis.notice.rest.dto.PageDTO;
import com.oxchains.themis.notice.rest.dto.StatusDTO;
import com.oxchains.themis.repo.dao.PaymentRepo;
import com.oxchains.themis.repo.dao.user.UserDao;
import com.oxchains.themis.repo.dao.user.UserTxDetailDao;
import com.oxchains.themis.repo.entity.*;
import com.oxchains.themis.repo.entity.user.UserTxDetail;
import com.oxchains.themis.repo.entity.user.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.web3j.utils.Collection;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class NoticeService {

    private final Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Resource
    private NoticeDao noticeDao;
    @Resource
    private BTCTickerDao btcTickerDao;
    @Resource
    private BTCResultDao btcResultDao;
    @Resource
    private BTCMarketDao btcMarketDao;
    //    @Resource
//    private CNYDetailDao cnyDetailDao;
    @Resource
    private CountryDao countryDao;
    @Resource
    private CurrencyDao currencyDao;
    @Resource
    private PaymentRepo paymentDao;
    @Resource
    private SearchTypeDao searchTypeDao;
    @Resource
    private UserTxDetailDao userTxDetailDao;
    @Resource
    private UserDao userDao;
    @Resource
    private CoinTypeDao coinTypeDao;
    @Value("${themis.user.default}")
    private String userDefaultImage;
    @Resource
    private AverageMarketDao averageMarketDao;
    @Resource
    private BitStampMarketDao bitStampMarketDao;
    @Resource
    private BitfinexMarketDao bitfinexMarketDao;
    @Resource
    private CNYDetailsDao cnyDetailsDao;
    @Resource
    private JPYDetailsDao jpyDetailsDao;
    @Resource
    private HKDDetailsDao hkdDetailsDao;
    @Resource
    private KRWDetailsDao krwDetailsDao;
    @Resource
    private USDDetailsDao usdDetailsDao;
    @Resource
    UserFeign userFeign;
    @Resource
    OrderFeign orderFeign;
    @Resource
    private ExchangeDao exchangeDao;

    //七天的毫秒值
    private Long SEVEN_DAY = 604800000L;

    public RestResp broadcastNotice(Notice notice) {
        try {
            // 判断用于信息是否完善（收货地址）

            Long userId = notice.getUserId();
//            User user = userDao.findOne(userId);
//            String firstAddress = user.getFirstAddress();
//            if (StringUtils.isBlank(firstAddress)) {
//                return RestResp.fail("请完善用户信息：收货地址");
//            }

            JSONObject o = userFeign.checkQIC(userId);
            String status = o.get("status").toString();
            if ("-1".equals(status)) {
                return RestResp.fail("发布广告需要实名认证");
            }
            // 必填项判断
            if (null == notice.getNoticeType()) {
                return RestResp.fail("请选择广告类型");
            }
            if (null == notice.getLocation()) {
                return RestResp.fail("请选择所在地");
            }
            if (null == notice.getCurrency()) {
                return RestResp.fail("请选择货币类型");
            }
            if (null == notice.getPremium()) {
                return RestResp.fail("请填写溢价");
            }
            if (null == notice.getPrice()) {
                return RestResp.fail("比特币价格获取失败，请联系管理员！");
            }
            if (null == notice.getMinTxLimit()) {
                return RestResp.fail("请填写最小限额");
            }
            if (null == notice.getMaxTxLimit()) {
                return RestResp.fail("请填写最大限额");
            }
            if (null == notice.getPayType()) {
                return RestResp.fail("请选择收款/付款方式");
            }
//            if (notice.getPremium() < 0) {
//                return RestResp.fail("溢价比例最小为：0");
//            }
            if (null == notice.getMinTxLimit()){
                return RestResp.fail("请输入最小限额");
            }
            if (null == notice.getMaxTxLimit()){
                return RestResp.fail("请输入最大限额");
            }
            if (notice.getMinTxLimit().doubleValue() < 0) {
                return RestResp.fail("最小交易限额：0");
            }
            if (notice.getMaxTxLimit().doubleValue() > NoticeConstants.ONE_HUNDRED_MILLION) {
                return RestResp.fail("最大交易限额：1亿");
            }
            if (ArithmeticUtils.minus(notice.getMaxTxLimit().doubleValue(), notice.getMinTxLimit().doubleValue()) < 0) {
                return RestResp.fail("最大限额不能低于最小限额");
            }
            if (notice.getPrice().doubleValue() <= 0) {
                return RestResp.fail("价格异常，请联系管理员");
            }
            if (null == notice.getExchange()) {
                return RestResp.fail("请返回交易所名称");
            }
//            if (notice.getNoticeContent().length() > 2000){
//                return RestResp.fail("公告超出2000字限制");
//            }

            //判断溢价小数点
//            String formatPreium = String.format("%.2f", notice.getPremium());
//            notice.setPremium(Double.valueOf(formatPreium));

            //设置公告有效期为7天
            notice.setNoticeTime(SEVEN_DAY);

            // 选填项（最低价判断）
            if (notice.getMinPrice() != null) {
                if (notice.getMinPrice().doubleValue() < 0) {
                    return RestResp.fail("最低价最小为：0");
                }
                Double marketLow = notice.getPrice().doubleValue();
                Double minPrice = notice.getMinPrice().doubleValue();
                if (ArithmeticUtils.minus(marketLow, minPrice) < 0) {
                    notice.setPrice(notice.getMinPrice());
                }
            }


            /**
             * 最小值默认赋值
             * 如果溢价为正及0
             * minPrice = price - (premium/100 * price)
             * 如果溢价为负数
             * minPrice = price + (premium/100 * price)
             **/
//            double premium = ArithmeticUtils.divide(notice.getPremium(), 100);
//            double multiply = ArithmeticUtils.multiply(premium, notice.getPrice().doubleValue(), 2);
//            double minPrice;
//            if (notice.getPremium() >= 0){
//                minPrice = ArithmeticUtils.minus(notice.getPrice().doubleValue(),multiply);
//            }else {
//                minPrice = ArithmeticUtils.plus(notice.getPrice().doubleValue(),multiply);
//            }
//            notice.setMinPrice(BigDecimal.valueOf(minPrice));


            // 不能发布公告的判断
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatusAndCoinType(notice.getUserId(), notice.getNoticeType(), NoticeTxStatus.UNDONE_TX, notice.getCoinType());
            if (noticeListUnDone.size() >= 2) {
                notice.setTxStatus(NoticeTxStatus.DONE_TX);
                Long createTime = Calendar.getInstance().getTimeInMillis();
                notice.setCreateTime(createTime);
                noticeDao.save(notice);
                return RestResp.fail("已有两条相同类型广告正在上架中，请先下架后再发布！");
            }

            Long createTime = Calendar.getInstance().getTimeInMillis();
            notice.setCreateTime(createTime);
            Notice n = noticeDao.save(notice);
            return RestResp.success("操作成功", n);
        } catch (Exception e) {
            LOG.error("发布公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.NEVER)
    public RestResp queryRandomNotice() {
        try {
            List<Notice> partList = new ArrayList<>();
            List<Notice> buyNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.BUY.getStatus(), NoticeTxStatus.UNDONE_TX);
            List<Notice> sellNoticeList = noticeDao.findByNoticeTypeAndTxStatus(NoticeConst.NoticeType.SELL.getStatus(), NoticeTxStatus.UNDONE_TX);

            if (buyNoticeList.size() == 0 && sellNoticeList.size() == 0) {
                return RestResp.success("没有数据", new ArrayList<>());
            }
            if (buyNoticeList.size() > NoticeConstants.TWO && sellNoticeList.size() > NoticeConstants.TWO) {
                int buySize = getRandom(buyNoticeList);
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < subBuyList.size(); i++) {
                    setUserTxDetail(subBuyList, i);
                }
                for (int i = 0; i < subSellList.size(); i++) {
                    setUserTxDetail(subSellList, i);
                }
                partList.addAll(subBuyList);
                partList.addAll(subSellList);
            } else if (buyNoticeList.size() <= NoticeConstants.TWO && sellNoticeList.size() > NoticeConstants.TWO) {
                int sellSize = getRandom(sellNoticeList);
                List<Notice> subSellList = getSubList(sellNoticeList, sellSize);
                for (int i = 0; i < buyNoticeList.size(); i++) {
                    setUserTxDetail(buyNoticeList, i);
                }
                for (int i = 0; i < subSellList.size(); i++) {
                    setUserTxDetail(subSellList, i);
                }
                partList.addAll(buyNoticeList);
                partList.addAll(subSellList);
            } else if (buyNoticeList.size() > NoticeConstants.TWO && sellNoticeList.size() <= NoticeConstants.TWO) {
                int buySize = getRandom(buyNoticeList);
                List<Notice> subBuyList = getSubList(buyNoticeList, buySize);
                for (int i = 0; i < subBuyList.size(); i++) {
                    setUserTxDetail(subBuyList, i);
                }
                for (int i = 0; i < sellNoticeList.size(); i++) {
                    setUserTxDetail(sellNoticeList, i);
                }
                partList.addAll(subBuyList);
                partList.addAll(sellNoticeList);
            } else {
                for (int i = 0; i < buyNoticeList.size(); i++) {
                    setUserTxDetail(buyNoticeList, i);
                }
                for (int i = 0; i < sellNoticeList.size(); i++) {
                    setUserTxDetail(sellNoticeList, i);
                }
                partList.addAll(buyNoticeList);
                partList.addAll(sellNoticeList);
            }

            partList.forEach(list -> {
                if (list.getPayType().length() > 1){
                    list.setPayType(list.getPayType().replaceAll(",", ""));
                }
            });

            return RestResp.success("操作成功", partList);
        } catch (Exception e) {
            LOG.error("获取4条随机公告异常", e);

        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryAllNotice() {
        try {
            Iterable<Notice> it = noticeDao.findAll();
            if (it.iterator().hasNext()) {
                return RestResp.success("操作成功", it);
            } else {
                return RestResp.fail("操作失败");
            }
        } catch (Exception e) {
            LOG.error("查询所有公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryMeAllNotice(Long userId, Integer pageNum, Long noticeType, Integer txStatus) {
        try {
            List<Notice> resultList = new ArrayList<>();
            Pageable pageable = new PageRequest(pageNum - 1, 5, new Sort(Sort.Direction.ASC, "createTime"));
            Page<Notice> page = null;
            if (txStatus.equals(NoticeTxStatus.DONE_TX)) {
                page = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeTxStatus.DONE_TX, pageable);
                Iterator<Notice> it = page.iterator();
                while (it.hasNext()) {
                    resultList.add(it.next());
                }
            } else {
                List<Notice> unDoneNoticeList = noticeDao.findByUserIdAndNoticeTypeAndTxStatus(userId, noticeType, NoticeTxStatus.UNDONE_TX);
                resultList.addAll(unDoneNoticeList);
            }
            PageDTO<Notice> pageDTO = new PageDTO<>();
            if (page == null) {
                pageDTO.setCurrentPage(1);
                pageDTO.setPageSize(5);
                pageDTO.setRowCount((long) resultList.size());
                pageDTO.setTotalPage(1);
                pageDTO.setPageList(resultList);
            } else {
                pageDTO.setCurrentPage(pageNum);
                pageDTO.setPageSize(5);
                pageDTO.setRowCount(page.getTotalElements());
                pageDTO.setTotalPage(page.getTotalPages());
                pageDTO.setPageList(resultList);
            }
            return RestResp.success("操作成功", pageDTO);
        } catch (Exception e) {
            LOG.error("查询我的公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
    public RestResp queryBTCPrice() {
        try {
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            if (!btcTickerList.isEmpty()) {
                return RestResp.success("操作成功", btcTickerList);
            } else {
                return RestResp.fail("操作失败");
            }
        } catch (Exception e) {
            LOG.error("查询BTC价格异常", e);
        }
        return RestResp.fail("操作失败");
    }

    /**
     * 火币网API接口停止服务，获取行情失败
     */
    @Deprecated
    public RestResp queryBTCMarket() {
        try {
            List<BTCResult> btcResultList = btcResultDao.findByIsSuc("true");
            List<BTCMarket> btcMarketList = btcMarketDao.findBySymbol("huobibtccny");
            List<BTCTicker> btcTickerList = btcTickerDao.findBySymbol("btccny");
            BTCResult btcResult = null;
            BTCMarket btcMarket = null;
            BTCTicker btcTicker = null;
            for (int i = 0; i < btcResultList.size(); i++) {
                btcResult = btcResultList.get(i);
            }
            for (int i = 0; i < btcMarketList.size(); i++) {
                btcMarket = btcMarketList.get(i);
            }
            for (int i = 0; i < btcTickerList.size(); i++) {
                btcTicker = btcTickerList.get(i);
            }
            btcMarket.setTicker(btcTicker);
            btcResult.setDatas(btcMarket);
            return RestResp.success("操作成功", btcResultList);
        } catch (Exception e) {
            LOG.error("查询BTC深度行情异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @Deprecated
    public RestResp queryBlockChainInfo() {
        try {
//            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
//            if (cnyDetail != null) {
//                return RestResp.success("操作成功", cnyDetail);
//            } else {
//                return RestResp.fail("操作失败");
//            }
        } catch (Exception e) {
            LOG.error("获取BTC价格异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.NEVER)
    public RestResp searchBuyPage(/*Notice*/ReqParam notice) {
        try {
            Long coinType = notice.getCoinType();
            CoinType ct = new CoinType();
            if (null != coinType) {
                ct = coinTypeDao.findById(coinType);
                if (null != ct) {
                    LOG.info("需要展示的币种广告是 ：" + ct.getId());
                } else {
                    LOG.error("不支持该币种，默认搜索BTC");
                    ct = new CoinType();
                    ct.setId(1L);
                }
            } else {
                LOG.error("币种为空，默认搜索BTC");
                ct.setId(1L);
            }
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            String payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.SELL.getStatus();
            Integer pageNum = notice.getPageNum();
            Integer pageSize = notice.getPageSize();
            if (null == pageSize) {
                pageSize = 10;
            }
            if (null == pageNum) {
                pageNum = 1;
            }

            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.ASC, "price"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createTime"));

            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(orders));

            // 对所在地，货币类型，支付方式判断，可为null
            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(location, currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatusAndCoinType(location, currency, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(location, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatusAndCoinType(location, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null == currency && null != payType) {
             //   page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatusAndCoinType(payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
//                page = noticeDao.findByPayTypeLikeAndNoticeTypeAndTxStatusAndCoinType("%"+ payType +"%", noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null != currency && null == payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatusAndCoinType(currency, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatusAndCoinType(noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
/*
//TODO 多一次循环？
            List<Notice> resultList = new ArrayList<>();
            Iterator<Notice> it = page.iterator();
            while (it.hasNext()) {
                resultList.add(it.next());
            }
*/

            List<Notice> resultList = parseList(page.getContent());

            for (int i = 0; i < resultList.size(); i++) {
                Notice not = resultList.get(i);
                countdown(not);
                getPrice(not);
                noticeDao.save(not);
            }

            for (int i = 0; i < resultList.size(); i++){
                resultList.get(i).setPayType(resultList.get(i).getPayType().replaceAll(",", ""));
            }

            // 将page相关参数设置到DTO中返回
            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        } catch (Exception e) {
            LOG.error("搜索购买公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.NEVER)
    public RestResp searchSellPage(/*Notice*/ReqParam notice) {
        try {
            Long coinType = notice.getCoinType();
            CoinType ct = new CoinType();
            if (null != coinType) {
                ct = coinTypeDao.findById(coinType);
                if (null != ct) {
                    LOG.info("需要展示的币种广告是 ：" + ct.getId());
                } else {
                    LOG.error("不支持该币种，默认搜索BTC");
                    ct = new CoinType();
                    ct.setId(1L);
                }
            } else {
                LOG.error("币种为空，默认搜索BTC");
                ct.setId(1L);
            }
            Long location = notice.getLocation();
            Long currency = notice.getCurrency();
            String payType = notice.getPayType();
            Long noticeType = NoticeConst.NoticeType.BUY.getStatus();
            Integer pageNum = notice.getPageNum();
            Integer pageSize = notice.getPageSize();
            if (null == pageSize) {
                pageSize = 10;
            }
            if (null == pageNum) {
                pageNum = 1;
            }

            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC, "price"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createTime"));

            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(orders));

            Page<Notice> page = null;
            if (null != location && null != currency && null != payType) {
                page = noticeDao.findByLocationAndCurrencyAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(location, currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null != currency && null == payType) {
                page = noticeDao.findByLocationAndCurrencyAndNoticeTypeAndTxStatusAndCoinType(location, currency, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null == currency && null != payType) {
                page = noticeDao.findByLocationAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(location, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null != currency && null != payType) {
                page = noticeDao.findByCurrencyAndPayTypeAndNoticeTypeAndTxStatusAndCoinType(currency, payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null != location && null == currency && null == payType) {
                page = noticeDao.findByLocationAndNoticeTypeAndTxStatusAndCoinType(location, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null == currency && null != payType) {
                page = noticeDao.findByPayTypeAndNoticeTypeAndTxStatusAndCoinType(payType, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null != currency && null == payType) {
                page = noticeDao.findByCurrencyAndNoticeTypeAndTxStatusAndCoinType(currency, noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }
            if (null == location && null == currency && null == payType) {
                page = noticeDao.findByNoticeTypeAndTxStatusAndCoinType(noticeType, NoticeTxStatus.UNDONE_TX, ct.getId(), pageable);
            }

            //optimize
            List<Notice> resultList = parseList(page.getContent());


            for (int i = 0; i < resultList.size(); i++) {
                Notice not = resultList.get(i);
                countdown(not);
                getPrice(not);
                noticeDao.save(not);
            }

            for (int i = 0; i < resultList.size(); i++){
                resultList.get(i).setPayType(resultList.get(i).getPayType().replaceAll(",", ""));
            }

            PageDTO<Notice> pageDTO = new PageDTO<>();
            pageDTO.setCurrentPage(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setRowCount(page.getTotalElements());
            pageDTO.setTotalPage(page.getTotalPages());
            pageDTO.setPageList(resultList);
            return RestResp.success("操作成功", pageDTO);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            LOG.error("搜索出售公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    private List<Notice> parseList(List<Notice> resultList) {
        if (null != resultList && resultList.size() > 0) {
            // 将好评度等值添加到list中返回
            for (int i = 0; i < resultList.size(); i++) {
                Long userId = resultList.get(i).getUserId();
                if (null == userId) {
                    continue;
                }
                User user = userDao.findOne(userId);
                if (null != user) {
                    resultList.get(i).setImageName(user.getAvatar());
                    //如果notice表的loginname和user表的username不等，显示username
                    if (null == resultList.get(i).getLoginname()){
                        resultList.get(i).setLoginname(user.getUsername());
                    }
                    if (!resultList.get(i).getLoginname().equals(user.getUsername())) {
                        resultList.get(i).setLoginname(user.getUsername());
                    }
                } else {
                    resultList.get(i).setImageName(userDefaultImage);
                }
                UserTxDetail utdInfo = userTxDetailDao.findByUserId(userId);
                if (utdInfo == null) {
                    resultList.get(i).setTxNum(0);
                    resultList.get(i).setTrustNum(0);
                    resultList.get(i).setGoodPercent(0);
                } else {
                    resultList.get(i).setTxNum(utdInfo.getTxNum());
                    resultList.get(i).setTrustNum(utdInfo.getBelieveNum());
                    double descTotal = ArithmeticUtils.plus(utdInfo.getGoodDesc(), utdInfo.getBadDesc());
                    double goodP;
                    if (descTotal == 0) {
                        goodP = 0;
                    } else {
                        goodP = ArithmeticUtils.divide(utdInfo.getGoodDesc(), descTotal, 2);
                    }

                    resultList.get(i).setGoodPercent((int) (goodP * 100));
                }

            }
        }
        return resultList;
    }

    public RestResp stopNotice(Long id) {
        try {
            Notice noticeInfo = noticeDao.findOne(id);
            if (null == noticeInfo) {
                return RestResp.fail("操作失败");
            }
            if (noticeInfo.getTxStatus().equals(NoticeTxStatus.DONE_TX)) {
                return RestResp.fail("公告已下架");
            }else {
                noticeInfo.setTxStatus(NoticeTxStatus.DONE_TX);
                return RestResp.success("公告下架成功");
            }
        } catch (Exception e) {
            LOG.error("下架公告异常", e);
        }
        return RestResp.fail("操作失败");
    }

    @javax.transaction.Transactional(javax.transaction.Transactional.TxType.NEVER)
    public RestResp queryNoticeOne(Long id) {
        try {
            Notice notice = noticeDao.findOne(id);
            User user = userDao.findOne(notice.getUserId());
            JSONObject o = orderFeign.getDealHistory(notice.getUserId(), notice.getCoinType());
            UserTxDetail userTxDetail = userTxDetailDao.findByUserId(notice.getUserId());
            if (null == user.getAvatar()) {
                notice.setImageName(userDefaultImage);
            } else {
                notice.setImageName(user.getAvatar());
            }
            //如果notice表的loginname和user表的username不等，显示username

            if (null == notice.getLoginname()){
                notice.setLoginname(user.getUsername());
            }

            if (!notice.getLoginname().equals(user.getUsername())) {
                notice.setLoginname(user.getUsername());
            }

            if (null == userTxDetail) {
                notice.setTxNum(0);
                notice.setGoodPercent(0);
                notice.setTxAmount(0);
            } else {
                notice.setTxNum(userTxDetail.getTxNum());
                double descTotal = ArithmeticUtils.plus(userTxDetail.getGoodDesc(), userTxDetail.getBadDesc());
                if (descTotal != 0) {
                    double goodPercent = ArithmeticUtils.divide(userTxDetail.getGoodDesc(), descTotal);
                    notice.setGoodPercent((int) (goodPercent * 100));
                } else {
                    notice.setGoodPercent(0);
                }
                String data = o.get("data").toString();
                String buyAmount = StringUtils.substringBetween(data, "buyAmout=", ",");
                String sellAmount = StringUtils.substringBetween(data, "sellAmount=", ",");
//                LOG.info(data + buyAmount + sellAmount +"-----------------------------------------------------------------------");
                String txAmount = ArithmeticUtils.plus(buyAmount, sellAmount, 3);
                notice.setTxAmount(Double.parseDouble(txAmount));
            }
            countdown(notice);
            getPrice(notice);
            if (null == notice.getNoticeContent()){
                notice.setNoticeContent("1");
            }

            noticeDao.save(notice);
            notice.setPayType(notice.getPayType().replaceAll(",", ""));
            return RestResp.success("操作成功", notice);
        } catch (Exception e) {
            LOG.error("根据公告ID查找异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp updateTxStatus(Long id, Integer txStatus) {
        try {
            Notice notice = noticeDao.findOne(id);
            notice.setTxStatus(txStatus);
            Notice n = noticeDao.save(notice);
            return RestResp.success("操作成功", n);
        } catch (Exception e) {
            LOG.error("修改公告交易状态异常", e);
        }
        return RestResp.fail("操作失败");
    }

    public RestResp queryStatusKV() {
        try {
            Iterable<Country> location = countryDao.findAll();
            Iterable<Currency> currency = currencyDao.findAll();
            Iterable<Payment> payment = paymentDao.findAll();
            Iterable<SearchType> searchType = searchTypeDao.findAll();
//            Iterable<BTCTicker> btcTiker = btcTickerDao.findBTCTickerBySymbol("btccny");
//            CNYDetail cnyDetail = cnyDetailDao.findBySymbol("¥");
            Iterable<Exchange> exchanges = exchangeDao.findAll();


            if (location.iterator().hasNext() && currency.iterator().hasNext() && payment.iterator().hasNext() && searchType.iterator().hasNext() && exchanges.iterator().hasNext()) {
                StatusDTO statusDTO = new StatusDTO<>();
                statusDTO.setLocationList(location);
                statusDTO.setCurrencyList(currency);
                statusDTO.setPaymentList(payment);
                statusDTO.setSearchTypeList(searchType);
                statusDTO.setBTCMarketList(exchanges);
//                statusDTO.setCnyDetailList(cnyDetail);
                return RestResp.success("操作成功", statusDTO);
            } else {
                return RestResp.fail("操作失败");
            }
        } catch (Exception e) {
            LOG.error("查询状态异常", e);
        }
        return RestResp.fail("操作失败");
    }

    // =================================================================

    /**
     * 根据list大小-2获取一个随机数
     */
    private int getRandom(List list) {
        return new Random().nextInt(list.size() - 2);
    }

    /**
     * 对list截取，获取size为2的新list
     */
    private List getSubList(List list, int size) {
        return list.subList(size, size + 2);
    }

    /**
     * 设置用户交易详情数据
     */
    private void setUserTxDetail(List<Notice> subList, int i) {
        Long userId = subList.get(i).getUserId();
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(userId);
        // 查找头像名
        User user = userDao.findOne(userId);
        String imageName = user.getAvatar();

        if (null == imageName) {
            subList.get(i).setImageName(userDefaultImage);
        } else {
            subList.get(i).setImageName(imageName);
        }

        if (null == userTxDetail) {
            subList.get(i).setTxNum(0);
            subList.get(i).setTrustNum(0);
            subList.get(i).setTrustPercent(0);
        } else {
            subList.get(i).setTxNum(userTxDetail.getTxNum());
            subList.get(i).setTrustNum(userTxDetail.getBelieveNum());
            double trustP;
            if (userTxDetail.getTxNum() == 0) {
                trustP = 0;
            } else {
                trustP = ArithmeticUtils.divide(userTxDetail.getBelieveNum(), userTxDetail.getTxNum(), 2);
            }
            subList.get(i).setTrustPercent((int) ArithmeticUtils.multiply(trustP, 100, 0));
        }

    }


    /**
     * 查询所有支持的数字货币币种
     **/
    public RestResp queryAllCoinType() {

        try {
            List<CoinType> coinTypes = coinTypeDao.findAll();
            if (coinTypes != null) {
                return RestResp.success("获取全部币种成功", coinTypes);
            } else {
                return RestResp.fail("支持币种表为空...");
            }
        } catch (Exception e) {
            LOG.error("获取所有币种信息异常...");
            return RestResp.fail("获取币种信息异常...");
        }

    }


    //对下架的公告进行修改，未下架不能修改
    public RestResp updateNotice(Notice notice) {

        try {
            Long noticeId = notice.getId();
            Notice currentNotice = noticeDao.findOne(noticeId);
            if (null == currentNotice) {
                return RestResp.fail("公告不存在");
            }
            if (NoticeTxStatus.DONE_TX.equals(currentNotice.getTxStatus())) {
                currentNotice.setLoginname(notice.getLoginname());
                currentNotice.setNoticeType(notice.getNoticeType());
                currentNotice.setLocation(notice.getLocation());
                currentNotice.setCoinType(notice.getCoinType());
                currentNotice.setCurrency(notice.getCurrency());
                currentNotice.setPremium(notice.getPremium());
                currentNotice.setPrice(notice.getPrice());
                currentNotice.setMinPrice(notice.getMinPrice());
                currentNotice.setMinTxLimit(notice.getMinTxLimit());
                currentNotice.setMaxTxLimit(notice.getMaxTxLimit());
                if (notice.getPayType().length() == 2){
                    StringBuffer payType = new StringBuffer(notice.getPayType());
                    StringBuffer insert = payType.insert(1, ",");
                    currentNotice.setPayType(String.valueOf(insert));
                }
                currentNotice.setNoticeContent(notice.getNoticeContent());
                currentNotice.setExchange(notice.getExchange());
                List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatusAndCoinType(currentNotice.getUserId(), currentNotice.getNoticeType(), NoticeTxStatus.UNDONE_TX, currentNotice.getCoinType());
                if (noticeListUnDone.size() >= 2) {
                    Long createTime = Calendar.getInstance().getTimeInMillis();
                    notice.setCreateTime(createTime);
                    noticeDao.save(currentNotice);
                    return RestResp.fail("已有两条相同类型广告正在上架中，请先下架后再发布！");
                }else {
                    currentNotice.setTxStatus(NoticeTxStatus.UNDONE_TX);
                    currentNotice.setNoticeTime(SEVEN_DAY);
                    currentNotice.setCreateTime(Calendar.getInstance().getTimeInMillis());
                    noticeDao.save(currentNotice);
                    return RestResp.success("重新上架公告成功");
                }
            } else {
                return RestResp.fail("此公告未下架，不能修改");
            }
        } catch (Exception e) {
            LOG.error("修改下架公告异常");
            return RestResp.fail("修改下架公告异常");
        }
    }

    //对下架公告进行删除操作，用isDelete字段进行判断
    public RestResp deleteNotice(Long noticeId) {
        try {
            Notice notice = noticeDao.findOne(noticeId);
            if (NoticeTxStatus.DONE_TX.equals(notice.getTxStatus())) {
                if (NoticeConst.IsDelete.NOTEXIST.getStatus().equals(notice.getIsDelete())) {
                    return RestResp.fail("公告已删除");
                } else {
                    notice.setIsDelete(NoticeConst.IsDelete.NOTEXIST.getStatus());
                    noticeDao.save(notice);
                    return RestResp.success("删除下架公告成功");
                }
            } else {
                return RestResp.fail("公告未下架，不能删除");
            }
        } catch (Exception e) {
            LOG.error("删除下架公告出现异常");
            return RestResp.fail("删除下架公告出现异常");
        }

    }


    public RestResp restartNotice(Long id) {
        try {
            Notice noticeInfo = noticeDao.findOne(id);
            if (null == noticeInfo) {
                return RestResp.fail("公告不存在");
            }
            if (noticeInfo.getTxStatus().equals(NoticeTxStatus.UNDONE_TX)) {
                return RestResp.fail("公告已上架");
            }
            List<Notice> noticeListUnDone = noticeDao.findByUserIdAndNoticeTypeAndTxStatusAndCoinType(noticeInfo.getUserId(), noticeInfo.getNoticeType(), NoticeTxStatus.UNDONE_TX, noticeInfo.getCoinType());
            if (noticeListUnDone.size() >= 2) {
                return RestResp.fail("该币种已经有两条此类型公告");
            }else {
                noticeInfo.setTxStatus(NoticeTxStatus.UNDONE_TX);
                noticeInfo.setNoticeTime(SEVEN_DAY);
                noticeInfo.setCreateTime(Calendar.getInstance().getTimeInMillis());
                return RestResp.success("重新上架公告成功");
            }
        } catch (Exception e) {
            LOG.error("重新上架公告异常", e);
            return RestResp.fail("重新上架公告异常");
        }
    }


    public RestResp queryNoticeByUserId(RequestParam requestParam){
        try {

            Integer pageNum = requestParam.getPageNum();
            Integer pageSize = requestParam.getPageSize();

            pageNum = pageNum == null ? 1 : pageNum;
            pageSize = pageSize == null ? 10 : pageSize;
            List<Sort.Order> orders = new ArrayList<>();

            orders.add(new Sort.Order(Sort.Direction.ASC, "txStatus"));
            orders.add(new Sort.Order(Sort.Direction.DESC, "createTime"));

            Pageable pageable = new PageRequest(pageNum - 1, pageSize, new Sort(orders));

            Page<Notice> page = noticeDao.findByUserIdAndIsDelete(requestParam.getUserId(), NoticeConst.IsDelete.EXIST.getStatus(), pageable);

            List<Notice> notices = page.getContent();

            if (null == notices){
                return RestResp.fail("此用户公告数量为空...");
            }else {
                PageDTO<Notice> pageDTO = new PageDTO<>();
                pageDTO.setCurrentPage(pageNum);
                pageDTO.setPageSize(pageSize);
                pageDTO.setTotalPage(page.getTotalPages());
                pageDTO.setRowCount(page.getTotalElements());
                pageDTO.setPageList(notices);
                return RestResp.success("根据用户名查询成功" , pageDTO);
            }

        }catch (Exception e){
            LOG.error("根据userId获取公告异常",e);
            return RestResp.fail("根据userId获取公告异常");
        }
    }


    //公告有效期倒计时
    public void countdown(Notice notice) {
        if (null == notice.getNoticeTime()) {
            notice.setNoticeTime(SEVEN_DAY);
        } else {
            Long currentTime = Calendar.getInstance().getTimeInMillis();
            double passedTime = ArithmeticUtils.minus(currentTime, notice.getCreateTime());
            if (ArithmeticUtils.minus(SEVEN_DAY, passedTime) <= 0) {
                notice.setNoticeTime(0L);
            } else {
                notice.setNoticeTime(new Double(ArithmeticUtils.minus(SEVEN_DAY, passedTime)).longValue());
            }
        }
    }

    //计算动态价格
    public void getPrice(Notice notice) {

        double premium = ArithmeticUtils.divide(notice.getPremium(), 100);
        double multiply = 0;
        double price = 0;
        switch (notice.getExchange().intValue()) {
            case 1:
                AverageMarket averageMarket = averageMarketDao.findByCashNameAndCoinType(getCashName(notice.getCurrency()), getCoinType(notice.getCoinType()));
                multiply = ArithmeticUtils.multiply(premium, Double.valueOf(averageMarket.getLast()), 2);
                price = ArithmeticUtils.plus(multiply, Double.valueOf(averageMarket.getLast()));
                notice.setPrice(BigDecimal.valueOf(price));
                notice.setExPrice(BigDecimal.valueOf(Double.valueOf(averageMarket.getLast())));
                break;
            case 2:
                switch (notice.getCurrency().intValue()) {
                    case 1:
                        CNYDetails cnyDetails = cnyDetailsDao.findByCoinType(getCoinType(notice.getCoinType()));
                        multiply = ArithmeticUtils.multiply(premium, Double.valueOf(cnyDetails.getPrice()), 2);
                        price = ArithmeticUtils.plus(multiply, Double.valueOf(cnyDetails.getPrice()));
                        notice.setPrice(BigDecimal.valueOf(price));
                        notice.setExPrice(BigDecimal.valueOf(Double.valueOf(cnyDetails.getPrice())));
                        break;
                    case 2:
                        USDDetails usdDetails = usdDetailsDao.findByCoinType(getCoinType(notice.getCoinType()));
                        multiply = ArithmeticUtils.multiply(premium, Double.valueOf(usdDetails.getPrice()), 2);
                        price = ArithmeticUtils.plus(multiply, Double.valueOf(usdDetails.getPrice()));
                        notice.setPrice(BigDecimal.valueOf(price));
                        notice.setExPrice(BigDecimal.valueOf(Double.valueOf(usdDetails.getPrice())));
                        break;
                    case 3:
                        JPYDetails jpyDetails = jpyDetailsDao.findByCoinType(getCoinType(notice.getCoinType()));
                        multiply = ArithmeticUtils.multiply(premium, Double.valueOf(jpyDetails.getPrice()), 2);
                        price = ArithmeticUtils.plus(multiply, Double.valueOf(jpyDetails.getPrice()));
                        notice.setPrice(BigDecimal.valueOf(price));
                        notice.setExPrice(BigDecimal.valueOf(Double.valueOf(jpyDetails.getPrice())));
                        break;
                    case 4:
                        KRWDetails krwDetails = krwDetailsDao.findByCoinType(getCoinType(notice.getCoinType()));
                        multiply = ArithmeticUtils.multiply(premium, Double.valueOf(krwDetails.getPrice()), 2);
                        price = ArithmeticUtils.plus(multiply, Double.valueOf(krwDetails.getPrice()));
                        notice.setPrice(BigDecimal.valueOf(price));
                        notice.setExPrice(BigDecimal.valueOf(Double.valueOf(krwDetails.getPrice())));
                        break;
                    case 5:
                        HKDDetails hkdDetails = hkdDetailsDao.findByCoinType(getCoinType(notice.getCoinType()));
                        multiply = ArithmeticUtils.multiply(premium, Double.valueOf(hkdDetails.getPrice()), 2);
                        price = ArithmeticUtils.plus(multiply, Double.valueOf(hkdDetails.getPrice()));
                        notice.setPrice(BigDecimal.valueOf(price));
                        notice.setExPrice(BigDecimal.valueOf(Double.valueOf(hkdDetails.getPrice())));
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                BitstampMarket bitstampMarket = bitStampMarketDao.findByCashNameAndCoinType(getCashName(notice.getCurrency()), getCoinType(notice.getCoinType()));
                multiply = ArithmeticUtils.multiply(premium, Double.valueOf(bitstampMarket.getLast()), 2);
                price = ArithmeticUtils.plus(multiply, Double.valueOf(bitstampMarket.getLast()));
                double price1 =new BigDecimal(price).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                notice.setPrice(BigDecimal.valueOf(price1));
                notice.setExPrice(BigDecimal.valueOf(Double.valueOf(bitstampMarket.getLast())));
                break;
            case 4:
                BitfinexMarket bitfinexMarket = bitfinexMarketDao.findByCashNameAndCoinType(getCashName(notice.getCurrency()), getCoinType(notice.getCoinType()));
                multiply = ArithmeticUtils.multiply(premium, Double.valueOf(bitfinexMarket.getLast_price()), 2);
                price = ArithmeticUtils.plus(multiply, Double.valueOf(bitfinexMarket.getLast_price()));
                notice.setPrice(BigDecimal.valueOf(price));
                notice.setExPrice(BigDecimal.valueOf(Double.valueOf(bitfinexMarket.getLast_price())));
            default:
                break;
        }

    }



    /**
     * long型currency转String型
     **/
    public String getCashName(Long currency) {

        String cashName;
        switch (currency.intValue()) {
            case 1:
                cashName = com.oxchains.themis.notice.common.Const.CNY.getAbbreviation();
                return cashName;
            case 2:
                cashName = com.oxchains.themis.notice.common.Const.USD.getAbbreviation();
                return cashName;
            case 3:
                cashName = com.oxchains.themis.notice.common.Const.JPY.getAbbreviation();
                return cashName;
            case 4:
                cashName = com.oxchains.themis.notice.common.Const.KRW.getAbbreviation();
                return cashName;
            case 5:
                cashName = com.oxchains.themis.notice.common.Const.HKD.getAbbreviation();
                return cashName;
            default:
                return null;
        }
    }

    /**
     * long型coinType转String型
     **/
    public String getCoinType(Long coinType) {
        String coinName;
        switch (coinType.intValue()) {
            case 1:
                coinName = NoticeConst.CoinType.BTC.getName();
                return coinName;
            case 2:
                coinName = NoticeConst.CoinType.ETH.getName();
                return coinName;
            case 3:
                coinName = NoticeConst.CoinType.GET.getName();
                return coinName;
            default:
                return null;
        }
    }



}
