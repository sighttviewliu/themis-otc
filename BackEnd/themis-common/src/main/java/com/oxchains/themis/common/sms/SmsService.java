package com.oxchains.themis.common.sms;

import com.alibaba.fastjson.JSONObject;

import com.oxchains.themis.common.okhttp.OkHttpClientHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author gaoyp
 * @create 2018/8/28  14:44
 * SmsService
 **/
@Slf4j
@Service
public class SmsService {


    /**
     * 发送单条短信
     * @param mobile 手机号
     * @param content 短信内容
     **/
    public boolean sendSingleSms(String mobile, String content) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("mobile", mobile);
            params.put("content", content);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_SINGLE_SMS, header, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.get("status").toString();
            if (SmsConst.STATUS_STR.equals(status)) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            log.error("发送单条短信异常", e);
            return false;
        }
    }

    /**
     * 发送单条短信（短链）
     * @param mobile 手机号
     * @param content 短信内容
     * @param realUrl 真实的链接
     *
     * 信息内容中的短链标识必须为${url}
     * realUrl字段不能为空
     **/
    public boolean sendSingleIndividuationSms(String mobile, String content, String realUrl) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("mobile", mobile);
            params.put("content", content);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);
            params.put("realUrl", realUrl);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_SINGLE_INDIVIDUATION_SMS, header, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.get("status").toString();
            if (SmsConst.STATUS_STR.equals(status)) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            log.error("发送单条短信（短链）异常", e);
            return false;
        }
    }

    /**
     * 批量发送短信
     * @param items 手机号列表（对象数组）
     * 对象变量{String tid;   String mobile;   String content;}
     *
     *批量给多个用户发送短信，全部发送成功返回true
     *部分发送成功/全部发送失败返回false
     **/
    public boolean sendBatchSms(Object[] items) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("items", items);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_BATCH_SMS, header, params);

            System.out.println(result);

            boolean b = formatString(result,flag);

            flag = b == flag?false:true;

            return flag;
        } catch (Exception e) {
            log.error("批量发送短信异常", e);
            return false;
        }
    }

    /**
     * 批量发送短信（短链）
     * @param items 手机号列表（对象数组）
     * 对象变量{String tid;   String mobile;   String content;   String realUrl}
     *
     *批量给多个用户发送短信，全部发送成功返回true
     *部分发送成功/全部发送失败返回false
     **/
    public boolean sendBatchIndividuationSms(Object[] items) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("items", items);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_BATCH_INDIVIDUATION_SMS, header, params);

            boolean b = formatString(result,flag);

            flag = b == flag?false:true;
            return flag;
        } catch (Exception e) {
            log.error("批量发送短信（短链）异常", e);
            return false;
        }
    }

    /**
     * 彩信上传模板
     * @param subject 彩信素材主题
     * @param items 彩信素材项（对象数组）
     **/
    public boolean postMmsMessage(String subject, Object[] items) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("items", items);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);
            params.put("subject", subject);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_POST_MMS_MESSAGE, header, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.get("status").toString();
            if (SmsConst.STATUS_STR.equals(status)) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            log.error("彩信上传模板异常", e);
            return false;
        }
    }


    /**
     * 个性化彩信发送
     * @param templateId 素材模板号
     * @param parameterCount 个性化字段的个数
     * @param items 发送数据（对象数组）
     **/
    public boolean batchMmsVariable(String templateId, int parameterCount, Object[] items) {
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("items", items);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);
            params.put("templateId", templateId);
            params.put("parameterCount", parameterCount);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_BATCH_MMS_VARIABLE, header, params);

            boolean b = formatString(result,flag);

            flag = b == flag?false:true;

            return flag;
        } catch (Exception e) {
            log.error("个性化彩信发送异常", e);
            return false;
        }
    }

    /**
     * 普通彩信发送
     * @param mobiles 手机号以 "," 分隔
     * @param templateId 素材模板号
     **/
    public boolean batchMmsCommon(String templateId, String mobiles){
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            params.put("templateId", templateId);
            params.put("campaignID", SmsConst.SMS_CAMPAIGN_ID);
            params.put("mobiles", mobiles);

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_BATCH_MMS_COMMON, header, params);

            boolean b = formatString(result,flag);

            flag = b == flag?false:true;

            return flag;

        }catch (Exception e){
            log.error("普通彩信发送异常", e);
            return false;
        }
    }

    /**
     * 获取单条短信发送状态
     * @param messageID 短息ID
     **/
    public String getReport(int messageID){
        try {

            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            String url = String.format(SmsConst.DMACLOUD_REPORT,SmsConst.SMS_CAMPAIGN_ID,messageID);

            String result = OkHttpClientHelper.get(url, header, params);

            if (null != result){
                return result;
            }
            return null;
        }catch (Exception e){
            log.error("获取单条短信发送状态异常", e);
            return null;
        }
    }


    /**
     * 获取批量短信发送状态
     * @param nextID 起始短信ID
     **/
    public String getReports(int nextID){
        try {

            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            String url = String.format(SmsConst.DMACLOUD_REPORTS,SmsConst.SMS_CAMPAIGN_ID,nextID);

            String result = OkHttpClientHelper.get(url, header, params);

            JSONObject jsonObject = JSONObject.parseObject(result);

            String resultList = jsonObject.get("resultList").toString();

            if (null != resultList){
                return result;
            }
            return null;
        }catch (Exception e){
            log.error("批量获取短信发送状态异常", e);
            return null;
        }
    }

    /**
     * 获取批量彩信发送状态
     * @param nextID 起始短信ID
     **/
    public String getMmsReports(int nextID){
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            String url = String.format(SmsConst.DMACLOUD_MMS_REPORTS, nextID);

            String result = OkHttpClientHelper.get(url, header, params);

            JSONObject jsonObject = JSONObject.parseObject(result);

            String resultList = jsonObject.get("resultList").toString();

            if (null != resultList){
                return result;
            }
            return null;
        }catch (Exception e){
            log.error("批量获取彩信发送状态异常", e);
            return null;
        }
    }

    /**
     * 获取上行短信（用户回复）
     * @param nextID 上行短信的responseID
     **/
    public String getResponses(int nextID){
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            String url = String.format(SmsConst.DMACLOUD_RESPONSES,SmsConst.SMS_CAMPAIGN_ID,nextID);

            String result = OkHttpClientHelper.get(url, header, params);

            JSONObject jsonObject = JSONObject.parseObject(result);

            String resultList = jsonObject.get("resultList").toString();

            if (null != resultList){
                return result;
            }
            return null;
        }catch (Exception e){
            log.error("获取上行短信（用户回复)", e);
            return null;
        }
    }

    /**
     * (新)获取上行短信（用户回复）
     * @param nextID 上行短信的responseID
     **/
    public String getResponseRecord(int nextID){
        try {
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();

            header.put("Authorization", SmsConst.WEB_POWER_STRING);

            String url = String.format(SmsConst.DMACLOUD_RESPONSE_RECORD,SmsConst.SMS_CAMPAIGN_ID,nextID);

            String result = OkHttpClientHelper.get(url, header, params);

            JSONObject jsonObject = JSONObject.parseObject(result);

            String resultList = jsonObject.get("resultList").toString();

            if (null != resultList){
                return result;
            }
            return null;
        }catch (Exception e){
            log.error("(新)获取上行短信（用户回复)", e);
            return null;
        }
    }

    /**
     * 发送单条海外短信、含有短链的海外短信
     **/
    public boolean sendOverseaSms(ReqParam reqParam){
        try{
            Map<String, String> header = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            boolean flag = false;

            header.put("Authorization", SmsConst.WEB_POWER_STRING);


            params.put("mobile", reqParam.getMobile());
            params.put("content", reqParam.getContent());
            params.put("campaignID",SmsConst.SMS_CAMPAIGN_ID);

            if (null != reqParam.getRealUrl()){
                params.put("realUrl", reqParam.getRealUrl());
            }

            String result = OkHttpClientHelper.post(SmsConst.DMACLOUD_OVERSEASMS, header, params);

            JSONObject jsonObject = JSONObject.parseObject(result);
            String status = jsonObject.get("status").toString();
            if (SmsConst.STATUS_STR.equals(status)){
                flag = true;
            }
            return flag;
        }catch (Exception e){
            log.error("发送单条海外短信异常", e);
            return false;
        }
    }


    private boolean formatString(String result, boolean flag){

        String[] split = result.split("\\}");

        List<String> statusList = new ArrayList<>();
        Arrays.asList(split).forEach(s -> {
            if (s.contains("status")){
                statusList.add(s);
            }
        });

        for (String s : statusList) {
            int beginIndex = s.lastIndexOf(":") + 1;
            int endIndex = s.length();
            String status = s.substring(beginIndex, endIndex);
            if (SmsConst.STATUS_STR1.equals(status)){
                flag = true;
            }else {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
