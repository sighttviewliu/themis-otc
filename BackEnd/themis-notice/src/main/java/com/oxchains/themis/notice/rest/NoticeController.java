package com.oxchains.themis.notice.rest;

import com.oxchains.themis.common.aop.ControllerLogs;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.domain.ReqParam;
import com.oxchains.themis.notice.service.NoticeService;
import com.oxchains.themis.repo.entity.Notice;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author luoxuri
 * @create 2017-10-25 10:21
 **/
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 发布公告
     */
    @ControllerLogs(description = "发布广告")
    @PostMapping(value = "/broadcast")
    public RestResp broadcastNotice(@RequestBody Notice notice) {
        return noticeService.broadcastNotice(notice);
    }

    /**
     * 随机查询两条购买公告、两条出售公告
     */
    @ControllerLogs(description = "随机查询两条购买广告、两条出售广告")
    @GetMapping(value = "/query/random")
    public RestResp queryRandomNotice() {
        return noticeService.queryRandomNotice();
    }

    /**
     * 查询所有公告
     */
    @ControllerLogs(description = "查询所有广告，未分页")
    @GetMapping(value = "/query/all")
    public RestResp queryAllNotice() {
        return noticeService.queryAllNotice();
    }

    /**
     * 根据交易状态查询自己的公告
     */
    @ControllerLogs(description = "根据交易状态查询自己的广告")
    @GetMapping(value = "/query/me2")
    public RestResp queryMeAllNotice(@RequestParam Long userId, @RequestParam Integer pageNum, @RequestParam Long noticeType, @RequestParam Integer txStatus) {
        return noticeService.queryMeAllNotice(userId, pageNum, noticeType, txStatus);
    }

    /**
     * 实时获取(火币网)BTC价格
     */
    @ControllerLogs(description = "实时获取(火币网)BTC价格")
    @GetMapping(value = "/query/BTCPrice")
    public RestResp queryBTCPrice() {
        return noticeService.queryBTCPrice();
    }

    /**
     * 实时获取(火币网)BTC行情信息
     */
    @ControllerLogs(description = "实时获取(火币网)BTC行情信息")
    @GetMapping(value = "/query/BTCMarket")
    public RestResp queryBTCMarket() {
        return noticeService.queryBTCMarket();
    }

    /**
     * 实时获取BlockChain.info BTC 价格
     */
    @ControllerLogs(description = "实时获取(BlockChain.info)BTC价格")
    @GetMapping(value = "/query/blockchain.info")
    public RestResp queryBlockChainInfo() {
        return noticeService.queryBlockChainInfo();
    }

    /**
     * 搜索购买公告
     */
    @ControllerLogs(description = "搜索购买广告")
    @PostMapping(value = "/search/page/buy")
    public RestResp searchBuyPage(@RequestBody ReqParam notice) {
        if (null == notice.getSearchType()) {
            notice.setSearchType(NoticeConst.SearchType.ONE.getStatus());
        }
        /* 1 默认是搜公告 */
        if (notice.getSearchType().equals(NoticeConst.SearchType.ONE.getStatus())) {
            return noticeService.searchBuyPage(notice);
        } else {
            return noticeService.searchBuyPage(notice);
        }
    }

    /**
     * 搜索出售公告
     */
    @ControllerLogs(description = "搜索出售广告")
    @PostMapping(value = "search/page/sell")
    public RestResp searchSellPage(@RequestBody ReqParam notice) {
        if (null == notice.getSearchType()) {
            notice.setSearchType(NoticeConst.SearchType.ONE.getStatus());
        }
        /* 1 默认是搜公告 */
        if (notice.getSearchType().equals(NoticeConst.SearchType.ONE.getStatus())) {
            return noticeService.searchSellPage(notice);
        } else {
            return noticeService.searchSellPage(notice);
        }
    }

    /**
     * 下架公告
     */
    @ControllerLogs(description = "下架广告")
    @GetMapping(value = "/stop")
    public RestResp stopNotice(@RequestParam Long id) {
        return noticeService.stopNotice(id);
    }

    /**
     * 根据Id查找广告
     */
    @ControllerLogs(description = "根据ID查找广告")
    @GetMapping(value = "/query/noticeId/{id}")
    public String queryNoticeOne(@PathVariable Long id) {
        return JsonUtil.toJson(noticeService.queryNoticeOne(id));
    }

    /**
     * 修改广告交易状态
     */
    @ControllerLogs(description = "修改广告交易状态")
    @GetMapping(value = "/update/txStatus/{id}/{txStatus}")
    public String updateTxStatus(@PathVariable Long id, @PathVariable Integer txStatus) {
        return JsonUtil.toJson(noticeService.updateTxStatus(id, txStatus));
    }

    /**
     * 状态列表
     */
    @ControllerLogs(description = "状态列表")
    @GetMapping(value = "/query/statusKV")
    public RestResp queryStatusKV() {
        return noticeService.queryStatusKV();
    }


    /**
     * 获取所有支持币种信息
     **/
    @ControllerLogs(description = "获取所有支持的币种信息")
    @GetMapping("/query/coinType")
    public RestResp queryAllCoinType(){
        return noticeService.queryAllCoinType();
    }

    /**
     * 对已经下架的公告进行修改
     **/
    @ControllerLogs(description = "对已经下架的广告进行修改")
    @PostMapping("/update")
    public RestResp updateNotice(@RequestBody Notice notice){
        return noticeService.updateNotice(notice);
    }

    /**
     * 对已经下架的公告进行删除
     **/
    @ControllerLogs(description = "对已经下架的广告进行删除")
    @DeleteMapping("/delete/{noticeId}")
    public RestResp deleteNotice(@PathVariable Long noticeId){
        return noticeService.deleteNotice(noticeId);
    }


    /**
     * 重新上架公告
     **/
    @ControllerLogs(description = "重新上架公告")
    @GetMapping(value = "/restart")
    public RestResp restartNotice(@RequestParam Long id) {
        return noticeService.restartNotice(id);
    }


    /**
     * 根据userId查询公告
     **/
    @ControllerLogs(description = "根据userId查询公告")
    @PostMapping(value = "/query/page/userId")
    public RestResp queryNoticeByUserId(@RequestBody com.oxchains.themis.notice.domain.RequestParam requestParam){
        return noticeService.queryNoticeByUserId(requestParam);
    }


}
