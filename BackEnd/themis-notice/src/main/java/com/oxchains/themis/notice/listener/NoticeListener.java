package com.oxchains.themis.notice.listener;

import com.oxchains.themis.common.constant.notice.NoticeTxStatus;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.notice.common.NoticeConst;
import com.oxchains.themis.notice.dao.NoticeDao;
import com.oxchains.themis.repo.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author gaoyp
 * @create 2018/7/24  11:44
 * 公告监听器，每当noticeTime被置为0，
 * 当天晚上12点下架该公告
 **/
@Component
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class NoticeListener {

    @Resource
    private NoticeDao noticeDao;



    @Scheduled(cron = "0 0 0 * * ?")
    public void stopNotice(){
        try {

            List<Notice> expiredNotice = noticeDao.findByNoticeTimeAndTxStatus(0L, NoticeTxStatus.UNDONE_TX);

            if (expiredNotice.size() == 0){
                log.info("无过期公告");
            }else {
                for (Notice notice :
                        expiredNotice) {
                    notice.setTxStatus(NoticeTxStatus.DONE_TX);
                    noticeDao.save(notice);
                }
                log.info("下架过期公告成功");
            }
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            log.error("下架过期公告异常");
        }

    }

}
