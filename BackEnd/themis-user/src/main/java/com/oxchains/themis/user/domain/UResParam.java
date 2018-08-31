package com.oxchains.themis.user.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author ccl
 * @time 2018-04-23 14:08
 * @name UserService
 * @desc:
 */
@Component
@Data
public class UResParam {

    /**
     * 注册邮件发送信息
     */
    @Value("${signup.mail.message}")
    private String signupStr;

    /**
     * 注册短信发送信息
     */
    @Value("${signup.sms.message}")
    private String signupSmsStr;

    /**
     * 发送SMS的授权码
     */
    @Value("${web.power.string}")
    private String authorization;

    /**
     * 是否自动登录
     */
    @Value("${automatic.signin}")
    private boolean automaticSign;

    /**
     * 短信活动id
     */
    @Value("${sms.campaign.id}")
    private int smsCampaignId;

    /**
     * sendgrid api key
     */
    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    /**
     * mail address
     */
    @Value("${spring.mail.username}")
    private String fromAddr;

    /**
     * mail user
     */
    @Value("${spring.mail.username}")
    private String fromUsername;


    /**
     * 用户默认头像
     */
    @Value("${default.user.avatar}")
    private String defaultUserAvatar;
}
