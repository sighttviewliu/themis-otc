package com.oxchains.themis.common.sms;

/**
 * @author gaoyp
 * @create 2018/8/28  14:09
 **/
public class SmsConst {

    //成功状态
    public static final String STATUS_STR = "OK";

    public static final String STATUS_STR1 = "\"OK\"";
    //单条短信
    public static final String DMACLOUD_SINGLE_SMS = "https://sms.dmacloud.com/rest/v2/single_sms";
    //单条短信（短链）
    public static final String DMACLOUD_SINGLE_INDIVIDUATION_SMS = "https://sms.dmacloud.com/rest/v2/single_individuation_sms";
    //批量发送短信
    public static final String DMACLOUD_BATCH_SMS = "https://sms.dmacloud.com/rest/v2/batch_sms";
    //批量发送短信（短链）
    public static final String DMACLOUD_BATCH_INDIVIDUATION_SMS = "https://sms.dmacloud.com/rest/v2/batch_individuation_sms";
    //彩信上传模板
    public static final String DMACLOUD_POST_MMS_MESSAGE = "https://sms.dmacloud.com/rest/v2/post_mms_message";
    //个性化彩信发送
    public static final String DMACLOUD_BATCH_MMS_VARIABLE = "https://sms.dmacloud.com/rest/v2/batch_mms_variable";
    //普通彩信发送
    public static final String DMACLOUD_BATCH_MMS_COMMON = "https://sms.dmacloud.com/rest/v2/batch_mms_common";
    //获取单条短信发送状态
    public static final String DMACLOUD_REPORT = "https://sms.dmacloud.com/rest/v2/report?campaignID=%d&messageID=%d";
    //获取批量短信发送状态
    public static final String DMACLOUD_REPORTS = "https://sms.dmacloud.com/rest/v2/reports?campaignID=%d&nextID=%d";
    //获取批量彩信发送状态
    public static final String DMACLOUD_MMS_REPORTS = "https://sms.dmacloud.com/rest/v2/mms_reports?nextID=%d";
    //获取上行短信（用户回复）
    public static final String DMACLOUD_RESPONSES = "https://sms.dmacloud.com/rest/v2/responses?campaignID=%d&nextID=%d";
    //（新）获取上行短信（用户回复）
    public static final String DMACLOUD_RESPONSE_RECORD = "https://sms.dmacloud.com/rest/v2/responseRecord?campaignID=%d&nextID=%d";
    //发送单条海外短信、短链
    public static final String DMACLOUD_OVERSEASMS = "https://sms.dmacloud.com/rest/v2/overseasms";


    /**
     * SMS授权码
     **/
    public static final String WEB_POWER_STRING = "Nzk2Mzc1OTdAcXEuY29tOldlYnAwd2Vy";

    /**
     * 短信活动id
     **/
    public static final int SMS_CAMPAIGN_ID=100405;




}
