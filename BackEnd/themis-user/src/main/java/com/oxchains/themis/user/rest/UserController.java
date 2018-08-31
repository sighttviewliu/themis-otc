package com.oxchains.themis.user.rest;

import com.google.common.net.HttpHeaders;
import com.oxchains.themis.common.aop.ControllerLogs;
import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.VerifyCode;
import com.oxchains.themis.common.reqlimit.RequestLimit;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.VerifyCodeUtils;

import com.oxchains.themis.repo.entity.user.*;
import com.oxchains.themis.user.domain.ReqParam;
import com.oxchains.themis.user.service.CaptchaService;
import com.oxchains.themis.user.service.UserService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;


import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ccl
 * @time 2017-10-12 18:19
 * @name UserController
 * @desc:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    CaptchaService captchaService;

    //@Value("${user.info.image}")
    private String imageUrl;

    @GetMapping(value = "/queryRedis/{key}")
    public String exist(@PathVariable String key) {
        return JsonUtil.toJson(userService._queryRedisValue(key));
    }


    /**
     * Verification Code
     *
     * @return
     */
    @GetMapping(value = "/verifyCode")
    public RestResp verifyCode() {
        return RestResp.success(VerifyCodeUtils.getRandCode(6));
    }

    /*
     *下载图片
     * */
    @RequestMapping(value = "/image")
    public void downloadImage(String fileName, HttpServletResponse response) {
        try {
            File file = new File(imageUrl + fileName);
            if (file.exists()) {
                Path filePath = file.toPath();
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                response.setContentType(HttpURLConnection.guessContentTypeFromName(file.getName()));
                response.setContentLengthLong(file.length());
                Files.copy(filePath, response.getOutputStream());
            } else {
                try {
                    response.setStatus(SC_NOT_FOUND);
                    response.getWriter().write("file not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置资金密码
     * @param user
     * @return
     */
    @PostMapping(value = "/fpassword")
    public RestResp fpassword(@RequestBody UserVO user) {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.FPWD);
    }

    @GetMapping(value = "/trust")
    public RestResp trust(com.oxchains.themis.common.param.RequestBody body) {
        if (body.getType() == ParamType.TrustTabType.TRUSTED.getType()) {
            return userService.trustedUsers(body);
        } else if (body.getType() == ParamType.TrustTabType.TRUST.getType()) {
            return userService.trustUsers(body, Status.TrustStatus.TRUST);
        } else {
            return userService.trustUsers(body, Status.TrustStatus.SHIELD);
        }
    }

    @PostMapping(value = "/trust")
    public RestResp relation(UserRelation relation) {
        return userService.relation(relation);
    }

    @GetMapping(value = "/relation")
    public RestResp getRelation(UserRelation relation) {
        return userService.getRelation(relation);
    }

    @PostMapping(value = "/forget")
    public RestResp forgetPwd(com.oxchains.themis.common.param.RequestBody body) {
        return userService.forgetPwd(body);
    }

    @GetMapping(value = "/arbitrations")
    public String getArbitrations() {
        return JsonUtil.toJson(userService.getArbitrations());
    }

    @GetMapping(value = "/findOne")
    public String getUser(Long id) {
        return JsonUtil.toJson(userService.getUser(id));
    }

    /**
     * 图片验证码
     */
    @RequestMapping(value = "/imgVcode")
    public void defaultKaptcha(VerifyCode vcode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        captchaService.defaultKaptcha(vcode,request,response);
    }

    /**
     * 发送邮件
     *
     * @return
     */
    @RequestMapping(value = "/sendVmail")
    public RestResp sendVerifyMail(VerifyCode vcode) {
        return userService.sendVmail(vcode);
    }

    @GetMapping(value = "/active")
    public RestResp activeUser(String email) {
        return userService.active(email);
    }

    @PostMapping(value = "/mail")
    public RestResp sendMail(String email, String subject, String content) {
        return userService.sendMail(email, subject, content);
    }

    @PostMapping(value = "/btcaddress")
    public RestResp addBitcoinAddress(String loginname, String firstAddress) {
        return userService.addBitcoinAddress(loginname, firstAddress);
    }

    /**********************************************************/

    @GetMapping(value = "/list")
    public RestResp list() {
        return userService.findUsers();
    }

    @ControllerLogs(description = "注册")
    @PostMapping(value = "/signup")
    public RestResp signup(@RequestBody UserVO user) throws Exception {
        return userService.signup(user);
    }

    @ControllerLogs(description = "登录")
    @PostMapping(value = "/signin")
    public RestResp signin(@RequestBody UserVO user) {
        return userService.signin(user);
    }

    /**
     * 获取手机验证码
     */
    @RequestLimit(count = 6, time = 60000)
    @ControllerLogs(description = "获取手机验证码")
    @RequestMapping(value = "/phoneVcode/{mobilephone}")
    public RestResp phoneVcode(@PathVariable String mobilephone, HttpServletRequest request) {
        return userService.sendPhoneCode(mobilephone);
    }

    /**
     * 获取邮箱验证码
     */
    @RequestLimit(count = 6, time = 60000)
    @ControllerLogs(description = "获取邮箱验证码")
    @RequestMapping(value = "/mailVcode")
    public RestResp sendVmailCode(String email) {
        return userService.sendVmailCode(email);
    }

    /**
     * 验证验证码
     */
    @ControllerLogs(description = "验证验证码")
    @RequestMapping("/verifyICode")
    public RestResp verifytKaptchaCode(VerifyCode vcode, HttpServletRequest request, HttpServletResponse response) {
       return userService.verifytKaptchaCode(vcode);
    }

    /**
     * 检查登录名
     */
    @ControllerLogs(description = "检查登录名")
    @GetMapping(value = "/exist/loginname/{loginname}")
    public RestResp existName(@PathVariable String loginname) {
        return userService.checkLoginname(loginname);
    }

    /**
     * 检查邮箱
     */
    @ControllerLogs(description = "检查邮箱")
    @GetMapping(value = "/exist/email")
    public RestResp existEmail(String email) {
        return userService.checkEmail(email);
    }

    /**
     * 检查手机
     */
    @ControllerLogs(description = "检查手机")
    @GetMapping(value = "/exist/mobilephone/{mobilephone}")
    public RestResp existPhone(@PathVariable String mobilephone) {
        return userService.checkMobilephone(mobilephone);
    }

    @ControllerLogs(description = "验证密码")
    @PostMapping(value = "/validate/passwd")
    public RestResp validatePwd(@RequestBody ReqParam param){
        return userService.validatePwd(param);
    }

    /**
     * 修改电子邮箱
     */
    @ControllerLogs(description = "修改电子邮箱")
    @PostMapping(value = "/bind/email")
    public RestResp email(@RequestBody UserVO user) {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.EMAIL);
    }

    /**
     * 修改手机号
     */
    @ControllerLogs(description = "修改手机号")
    @PostMapping(value = "/bind/mobilephone")
    public RestResp phone(@RequestBody UserVO user) {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.PHONE);
    }

    /**
     * 修改密码
     */
    @ControllerLogs(description = "修改密码")
    @PostMapping(value = "/password")
    public RestResp password(@RequestBody UserVO user) {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.PWD);
    }

    /**
     * 重置密码
     */
    @ControllerLogs(description = "重置密码")
    @PostMapping(value = "/resetpwd")
    public RestResp resetpwd(@RequestBody ReqParam reqParam) {
        return userService.resetpwd(reqParam.getResetkey(), reqParam.getPassword());
    }

    /**
     * 上传头像
     */
    @ControllerLogs(description = "上传头像")
    @RequestMapping(value = "/avatar")
    public RestResp vatar(@ModelAttribute UserVO user) throws Exception {
        return userService.avatar(user);
    }

    /**
     * 修改信息
     */
    @ControllerLogs(description = "修改信息")
    @PostMapping(value = "/info")
    public RestResp info(@RequestBody UserVO user) throws Exception {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.INFO);
    }

    /**
     * 修改昵称
     */
    @ControllerLogs(description = "修改昵称")
    @PostMapping(value = "/nick")
    public RestResp nick(@RequestBody UserVO user) throws Exception {
        return userService.updateUser(user, ParamType.UpdateUserInfoType.NICK_NAME);
    }

    /**
     * 检查实名认证
     */
    @ControllerLogs(description = "检查实名认证")
    @GetMapping(value = "/check/qic/{userId}")
    public RestResp checkQIC(@PathVariable Long userId){
        return userService.checkQIC(userId);
    }

    /**
     * 实名认证
     */
    @ControllerLogs(description = "实名认证")
    @RequestLimit(count = 2, time = 30000)
    @PostMapping(value = "/qic")
    public RestResp qic(@RequestBody UserQIC userQIC, HttpServletRequest request){
        return userService.userQIC(userQIC);
    }

    /**
     * 获取实名认证信息
     */
    @ControllerLogs(description = "获取实名认证信息")
    @GetMapping(value = "/qic/{userId}")
    public RestResp getQIC(@PathVariable Long userId){
        return userService.getQIC(userId);
    }

    @ControllerLogs(description = "获取用户支付信息")
    @GetMapping(value = "/payment/{userId}")
    public RestResp getUserPayment(@PathVariable Long userId){
        return userService.getUserPayment(userId);
    }

    @ControllerLogs(description = "获取用户支付信息")
    @GetMapping(value = "/payment/{userId}/{ptype}")
    public RestResp getUserPayment(@PathVariable Long userId, @PathVariable Integer ptype){
        return userService.getUserPayment(userId, ptype);
    }

    @ControllerLogs(description = "获取用户支付信息")
    @GetMapping(value = "/payment/primary/{userId}")
    public RestResp getUserPrimaryPayment(@PathVariable Long userId){
        return userService.getUserPrimaryEnabledPayment(userId);
    }

    /**
     * 2018-07-31
     * 绑定支付信息
     */
    @ControllerLogs(description = "绑定支付信息")
    @PostMapping(value = "/payment/bind")
    public RestResp bindPayment(@RequestBody UserPayment vo){
        return userService.bindPayment(vo);
    }

    /**
     * 2018-07-31
     * 解除绑定支付信息
     */
    @ControllerLogs(description = "解绑支付信息")
    @PostMapping(value = "/payment/unbind")
    public RestResp unbindPayment(@RequestBody UserPayment vo){
        return userService.unbindPayment(vo);
    }

    /**
     * 激活支付信息
     * @param vo
     * @return
     */
    @ControllerLogs(description = "激活|禁用支付信息")
    @PostMapping(value = "/payment/active")
    public RestResp paymentActive(@RequestBody UserPayment vo){
        return userService.paymentActive(vo);
    }

    /**
     * 绑定银行卡
     */
    @ControllerLogs(description = "绑定银行卡")
    @PostMapping(value = "/bind/bankcard")
    public RestResp bankcard(@RequestBody UserPaymentVO vo){
        return userService.bindPayment(vo);
    }

    /**
     * 解除绑定银行卡
     */
    @ControllerLogs(description = "解除绑定银行卡")
    @DeleteMapping(value = "/unbind/bankcard")
    public RestResp unbankcard(@RequestBody UserPaymentVO vo){
        return userService.unbindPayment(vo);
    }

    /**
     * 绑定支付宝
     */
    @ControllerLogs(description = "绑定支付宝")
    @RequestMapping(value = "/bind/alipay")
    public RestResp alipay(@ModelAttribute UserPaymentVO vo){
        return userService.bindPayment(vo);
    }

    /**
     * 解除绑定支付宝
     */
    @ControllerLogs(description = "解除绑定支付宝")
    @DeleteMapping(value = "/unbind/alipay")
    public RestResp unalipay(@RequestBody UserPaymentVO vo){
        return userService.unbindPayment(vo);
    }

    /**
     * 添加地址
     */
    @ControllerLogs(description = "添加地址")
    @PostMapping(value = "/vaddress/add")
    public RestResp addAddress(@RequestBody UserAddress userAddress){
        return userService.addAddress(userAddress);
    }

    /**
     * 删除地址
     */
    @ControllerLogs(description = "删除地址")
    @DeleteMapping(value = "/vaddress/delete")
    public RestResp removeAddress(@RequestBody(required = false) UserAddress userAddress){
        return userService.removeAddress(userAddress);
    }

    /**
     * 获取地址
     */
    @ControllerLogs(description = "获取地址")
    @GetMapping(value = "/vaddress/{userId}")
    public RestResp getAddress(@PathVariable Long userId){
        return userService.getAddresses(userId, null);
    }

    @GetMapping(value = "/vaddress/{userId}/{type}")
    public RestResp getAddress(@PathVariable Long userId, @PathVariable Integer type){
        return userService.getAddresses(userId, type);
    }

    /**
     * 极验第一步验证
     * @return
     */
    @ControllerLogs(description = "极验第一步验证")
    @GetMapping(value = "/geetest")
    public RestResp getGeetestFirst() {
       return userService.getGeetestFirst();
    }

    @ControllerLogs(description = "获取用户交易详情")
    @GetMapping(value = "/utx/{userId}")
    public RestResp getUserTxDetail(@PathVariable Long userId){
        return userService.getUserTxDetails(userId);
    }

    /**
     * rms api
     */
    @GetMapping(value = "/rms/list/{pageSize}/{pageNo}")
    public RestResp list(@PathVariable Integer pageSize, @PathVariable Integer pageNo, String search, Integer status){
        return userService.list(pageSize, pageNo, search, status);
    }

    @GetMapping(value = "/rms/detail/{userId}")
    public RestResp detail(@PathVariable Long userId){
        return userService.detail(userId);
    }

    @PostMapping(value = "/rms/enabled")
    public RestResp enabled(@RequestBody ReqParam param){
        return userService.adminUser(param);
    }

    @PostMapping(value = "/rms/reset/password")
    public RestResp setpwd(@RequestBody ReqParam param){
        return userService.adminUser(param);
    }

    @PostMapping(value = "/rms/reset/mobilephone")
    public RestResp setphone(@RequestBody ReqParam param){
        return userService.adminUser(param);
    }
}
