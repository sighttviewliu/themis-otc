package com.oxchains.themis.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.oxchains.basicService.files.tfsService.TFSConsumer;
import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.constant.Const;
import com.oxchains.themis.common.constant.Status;
import com.oxchains.themis.common.constant.UserConstants;
import com.oxchains.themis.common.geetest.GeetestConfig;
import com.oxchains.themis.common.geetest.GeetestLib;
import com.oxchains.themis.common.geetest.GeetestPo;
import com.oxchains.themis.common.i18n.I18NConst;
import com.oxchains.themis.common.i18n.MyMessageSource;
import com.oxchains.themis.common.mail.Email;
import com.oxchains.themis.common.model.PageModel;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.common.param.ParamType;
import com.oxchains.themis.common.param.RequestBody;
import com.oxchains.themis.common.param.VerifyCode;
import com.oxchains.themis.common.sms.SmsService;
import com.oxchains.themis.common.util.*;
import com.oxchains.themis.repo.dao.order.OrderRepo;
import com.oxchains.themis.repo.dao.user.*;
import com.oxchains.themis.repo.entity.order.Order;
import com.oxchains.themis.repo.entity.user.*;
import com.oxchains.themis.user.domain.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author ccl
 * @time 2017-10-12 17:24
 * @name UserService
 * @desc:
 */

@Transactional
@Slf4j
@Service
public class UserService extends BaseService {

    @Resource
    private UserDao userDao;

    @Resource
    JwtService jwtService;

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private UserTxDetailDao userTxDetailDao;

    @Resource
    private OrderRepo orderRepo;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    MailService mailService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private TFSConsumer tfsConsumer;

    @Resource
    private SendGridService sendGridService;

    @Resource
    private SmsService1 smsService;

    private String token;

    @Resource
    private MyMessageSource myMessageSource;

    @Resource
    private UResParam uresParam;

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @Resource
    CaptchaService captchaService;

    @Resource
    private UserQICDao userQICDao;

    @Resource
    private UserPaymentRepo userPaymentRepo;


    public RestResp updateUser(UserVO user, ParamType.UpdateUserInfoType uuit) {
        Object res = null;
        if (null == user) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        if (user.getId() == null) {
            return RestResp.fail(getMessage(I18NConst.USER_ID_NOT_EMPTY));
        }
        User u = userDao.findUserById(user.getId());
        if (null == u) {
            return RestResp.fail(getMessage(I18NConst.USER_INFORMATION_INCORRECT));
        }
        String vcode = null;
        switch (uuit) {
            case INFO:
                boolean flag = false;
                if (null != user.getDescription() && !"".equals(user.getDescription().trim())) {
                    u.setDescription(user.getDescription());
                    flag = true;
                }
                if (!flag) {
                    return RestResp.fail(getMessage(I18NConst.NO_INFORMATION_MODIFIED));
                }
                break;
            case PWD:
                if (null == user.getPassword() || "".equals(user.getPassword().trim())) {
                    return RestResp.fail(getMessage(I18NConst.OLD_PASSWORD_NOT_EMPTY));
                }
                String newPassword = user.getNewPassword();
                if (null == newPassword || "".equals(newPassword.trim())) {
                    return RestResp.fail(getMessage(I18NConst.NEW_PASSWORD_NOT_EMPTY));
                }
                if (!RegexUtils.match(newPassword, RegexUtils.REGEX_PWD)) {
                    return RestResp.fail(getMessage("请输入6-16位数字和字母,英文字符符号组合"));
                }
                if (EncryptUtils.encodeSHA256(user.getPassword()).equals(u.getPassword())) {
                    u.setPassword(EncryptUtils.encodeSHA256(newPassword));
                } else {
                    return RestResp.fail(getMessage(I18NConst.OLD_PASSWORD_ERROR));
                }
                break;
            case FPWD:

                break;
            case EMAIL:
                if (null == user.getEmail() || "".equals(user.getEmail().trim()) || !RegexUtils.match(user.getEmail(), RegexUtils.REGEX_EMAIL)) {
                    return RestResp.fail(getMessage(I18NConst.FILL_MAIL_ADDRESS_CORRECTLY));
                }
                if (user.getEmail().equals(u.getEmail())) {
                    return RestResp.fail(getMessage(I18NConst.MAIL_ADDRESS_BEING_USED));
                }
                vcode = getVcodeFromRedis(user.getEmail());
                if (null == user.getVcode() || "".equals(user.getVcode().trim()) || null == vcode || !vcode.equals(user.getVcode())) {
                    return RestResp.fail(getMessage(I18NConst.FILL_VERIFYING_CODE_CORRECTLY));
                }
                /*String password = user.getPassword();
                String encrypt = EncryptUtils.encodeSHA256(password);
                if(null == password || !u.getPassword().equals(encrypt)){
                    return RestResp.fail(getMessage(I18NConst.FILL_LOGIN_PASSWORD_CORRECTLY));
                }*/

                User eu = userDao.findByEmail(user.getEmail());
                if (null != eu) {
                    return RestResp.fail(getMessage(I18NConst.MAIL_ADDRESS_BEEN_USED));
                }
                u.setEmail(user.getEmail());
                break;
            case PHONE:
                if (null == user.getMobilephone() || "".equals(user.getMobilephone().trim()) || !RegexUtils.match(user.getMobilephone(), RegexUtils.REGEX_MOBILEPHONE)) {
                    return RestResp.fail(getMessage(I18NConst.FILL_PHONE_NUMBER_CORRECTLY));
                }
                if (user.getMobilephone().equals(u.getMobilephone())) {
                    return RestResp.fail(getMessage(I18NConst.PHONE_NUMBER_BEING_USED));
                }
                vcode = getVcodeFromRedis(user.getMobilephone());
                if (null == user.getVcode() || "".equals(user.getVcode().trim()) || null == vcode || !vcode.equals(user.getVcode())) {
                    return RestResp.fail(getMessage(I18NConst.FILL_VERIFYING_CODE_CORRECTLY));
                }
                User mu = userDao.findByMobilephone(user.getMobilephone());
                if (null != mu) {
                    return RestResp.fail(getMessage(I18NConst.PHONE_NUMBER_BEEN_USED));
                }
                u.setMobilephone(user.getMobilephone());
                break;
            case NICK_NAME:
                if (null != u.getLoginname() && !"".equals(u.getLoginname())) {
                    return RestResp.fail("用户名只允许修改一次");
                }
                if (null == user.getUsername() || "".equals(user.getUsername().trim())) {
                    return RestResp.fail(getMessage(I18NConst.MODIFY_NICKNAME_NOT_EMPTY));

                }
                if (user.getUsername().equals(u.getUsername())) {
                    return RestResp.fail(getMessage(I18NConst.NOT_MADE_ANY_CHANGES));
                }
                u.setUsername(user.getUsername());
                u.setLoginname(user.getUsername());
                break;
            default:
                break;
        }

        return save(u, res);
    }

    public RestResp avatar(UserVO user) {
        if (null == user) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        if (user.getId() == null) {
            return RestResp.fail(getMessage(I18NConst.USER_ID_NOT_EMPTY));
        }
        User u = userDao.findUserById(user.getId());
        MultipartFile file = user.getFile();
        if (null != file) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = tfsConsumer.saveTfsFile(file, u.getId());
            if (null == newFileName) {
                log.info("头像上传失败");
                return RestResp.fail("头像上传失败");
            }
            u.setAvatar(newFileName);
            userDao.save(u);
            log.info("头像上传成功");
            return RestResp.success("头像上传成功", newFileName);

        }
        log.info("头像上传失败");
        return RestResp.fail("上传头像失败");
    }

    private RestResp save(User user, Object res) {
        try {
            userDao.save(user);
            return RestResp.success("操作成功", res);
        } catch (Exception e) {
            log.error("保存用户信息异常", e);
            return RestResp.fail("操作失败");
        }
    }

    private void saveRedis(User save, String originToken) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(save.getId().toString(), originToken, 7, TimeUnit.DAYS);
    }

    private void reSaveRedis(User save, String originToken) {
        log.info("重新保存 TOKEN 到 REDIS ");
        redisTemplate.delete(save.getId().toString());
        saveRedis(save, originToken);
    }

    public RestResp logout(User user) {
        User u = userDao.findByLoginname(user.getLoginname());
        if (null != u && u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())) {
            u.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());
            userDao.save(u);
            redisTemplate.delete(u.getId().toString());
            return RestResp.success("退出成功", null);
        } else {
            return RestResp.fail("退出失败");
        }
    }

    public Optional<User> findUser(User user) {
        Optional<User> optional = null;
        if (null != user.getLoginname()) {
            optional = userDao.findByLoginnameAndPassword(user.getLoginname(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getEmail()) {
            optional = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        if (null != user.getMobilephone()) {
            optional = userDao.findByMobilephoneAndPassword(user.getMobilephone(), user.getPassword());
            if (optional.isPresent()) {
                return optional;
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUser(User user) {
        User u = null;
        if (null != user.getLoginname()) {
            u = userDao.findByLoginname(user.getLoginname());
            if (null != u) {
                return Optional.of(u);
            }
        }
        if (null != user.getEmail()) {
            u = userDao.findByEmail(user.getEmail());
            if (null != u) {
                return Optional.of(u);
            }
        }
        if (null != user.getMobilephone()) {
            u = userDao.findByMobilephone(user.getMobilephone());
            if (null != u) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public RestResp findUsers() {
        return RestResp.success(newArrayList(userDao.findAll()));
    }

    /**
     * 信任/屏蔽
     *
     * @return
     */
    public RestResp trustUsers(RequestBody body, Status.TrustStatus status) {
        com.oxchains.themis.common.model.Page<UserTrust> res = new com.oxchains.themis.common.model.Page(body.getPageNo(), body.getPageSize());
        Pageable pager = new PageRequest((body.getPageNo() - 1) * body.getPageSize(), body.getPageSize(), new Sort(Sort.Direction.ASC, "toUserId"));
        Page<UserRelation> page = null;
        if (status.equals(Status.TrustStatus.SHIELD)) {
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.SHIELD.getStatus(), pager);
        } else {
            page = userRelationDao.findByFromUserIdAndStatus(body.getUserId(), Status.TrustStatus.TRUST.getStatus(), pager);
        }

        res.setTotalCount((int) page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()) {
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findUserById(relation.getToUserId());
            int txToNum = orderRepo.countByBuyerIdOrSellerId(body.getUserId(), relation.getToUserId()) + orderRepo.countByBuyerIdOrSellerId(relation.getToUserId(), body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(u.getLoginname());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getToUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());


            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    /**
     * 被信任
     *
     * @return
     */
    public RestResp trustedUsers(RequestBody body) {
        com.oxchains.themis.common.model.Page<UserTrust> res = new com.oxchains.themis.common.model.Page(body.getPageNo(), body.getPageSize());
        Pageable pager = new PageRequest((body.getPageNo() - 1) * body.getPageSize(), body.getPageSize(), new Sort(Sort.Direction.ASC, "fromUserId"));
        Page<UserRelation> page = userRelationDao.findByToUserIdAndStatus(body.getUserId(), Status.TrustStatus.TRUST.getStatus(), pager);
        res.setTotalCount((int) page.getTotalElements());
        res.setTotalPages(page.getTotalPages());
        List<UserTrust> list = new ArrayList<>();
        Iterator<UserRelation> it = page.iterator();
        UserTrust trustu = null;
        while (it.hasNext()) {
            UserRelation relation = it.next();
            trustu = new UserTrust();
            User u = userDao.findUserById(relation.getFromUserId());
            int txToNum = orderRepo.countByBuyerIdOrSellerId(body.getUserId(), relation.getFromUserId()) + orderRepo.countByBuyerIdOrSellerId(relation.getFromUserId(), body.getUserId());
            trustu.setTxToNum(txToNum);
            trustu.setFromUserId(relation.getFromUserId());
            trustu.setFromUserName(u.getLoginname());
            trustu.setToUserId(relation.getToUserId());
            trustu.setToUserName(relation.getToUserName());

            UserTxDetail detail = findUserTxDetailByUserId(relation.getFromUserId());

            trustu.setTxNum(detail.getTxNum());
            trustu.setGoodDesc(detail.getGoodDesc());
            trustu.setBadDesc(detail.getBadDesc());
            trustu.setFirstBuyTime(detail.getFirstBuyTime());
            trustu.setBelieveNum(detail.getBelieveNum());
            trustu.setBuyAmount(detail.getBuyAmount());
            trustu.setSellAmount(detail.getSellAmount());
            list.add(trustu);
        }
        res.setResult(list);

        return RestResp.success(res);
    }

    private UserTxDetail findUserTxDetailByUserId(Long userId) {
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(userId);
        if (null == userTxDetail) {
            return null;
        }
        List<Order> orders = orderRepo.findByBuyerIdOrSellerId(userId, userId);
        double buyAmount = 0d;
        double sellAmount = 0d;
        for (Order order : orders) {
            if (userId.equals(order.getBuyerId())) {
                buyAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
            if (userId.equals(order.getSellerId())) {
                sellAmount += order.getAmount() == null ? 0d : order.getAmount().doubleValue();
            }
        }
        userTxDetail.setBuyAmount(buyAmount);
        userTxDetail.setSellAmount(sellAmount);

        return userTxDetail;
    }

    public RestResp relation(UserRelation relation) {
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(), relation.getToUserId());
        try {
            if (null != ur) {
                ur.setStatus(relation.getStatus());
                userRelationDao.save(ur);
            } else {
                userRelationDao.save(relation);
            }
            return RestResp.success("操作成功", null);
        } catch (Exception e) {
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getRelation(UserRelation relation) {
        UserRelationInfo userRelationInfo = null;
        User user = userDao.findUserById(relation.getToUserId());
        if (null == user) {
            return RestResp.fail("无法查询相关用户信息");
        }
        userRelationInfo = new UserRelationInfo(user);
        UserTxDetail userTxDetail = userTxDetailDao.findByUserId(relation.getToUserId());
        userRelationInfo.setUserTxDetail(userTxDetail);
        UserRelation ur = userRelationDao.findByFromUserIdAndToUserId(relation.getFromUserId(), relation.getToUserId());
        if (null == ur) {
            ur = new UserRelation();
            ur.setFromUserId(relation.getFromUserId());
            ur.setToUserId(relation.getToUserId());
            ur.setStatus(Status.TrustStatus.NONE.getStatus());
        }
        userRelationInfo.setUserRelation(ur);
        return RestResp.success(userRelationInfo);
    }

    public RestResp forgetPwd(RequestBody body) {
        User user = userDao.findByLoginname(body.getLoginname());
        user.setPassword(EncryptUtils.encodeSHA256("123456"));
        userDao.save(user);
        try {
            String[] to = {body.getEmail()};
            mailService.send(new Email(to, "密码重置", "密码重置为:123456,请尽快登录修改!"));
            return RestResp.success("操作成功", null);
        } catch (Exception e) {
            log.error("操作失败: {}", e);
            return RestResp.fail("操作失败");
        }
    }

    public RestResp getArbitration() {
        List<UserRole> userRoles = userRoleDao.findByRoleId(UserConstants.UserRole.ARBITRATION.getRoleId());
        if (null != userRoles && userRoles.size() > 0) {
            final List<Long> ids = new ArrayList<>(userRoles.size());
            userRoles.stream().forEach(userRole -> {
                ids.add(userRole.getUserId());
            });
            List<User> list = userDao.findByIdIn(ids);
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setPassword(null);
                }
                return RestResp.success(list);
            }
        }
        return RestResp.fail();
    }

    public RestResp getArbitrations() {
        List<User> list = userDao.findByRoleId(UserConstants.UserRole.ARBITRATION.getRoleId());
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPassword(null);
            }
            return RestResp.success(list);
        }
        return RestResp.fail();
    }

    public RestResp getUser(Long id) {
        if (null == id) {
            return RestResp.fail("用户id不能为空");
        }
        User vo = null;
        User user = userDao.findUserById(id);
        if (user != null) {
            //user.setPassword(null);
            vo = new UserVO(user);
            vo.setPassword(null);
        }
        return RestResp.success(vo);
    }

    public RestResp getUserById(Long id){
        if (null == id) {
            return RestResp.fail("User Id requied, but null");
        }
        try{
            User user = userDao.findUserById(id);
            if(null == user){
                return RestResp.fail("User not exist");
            }
            return RestResp.success(user);
        }catch (Exception e){
            log.error("", e);
            return RestResp.fail("Find user by Id error: ", e);
        }
    }
    public RestResp getUserByLoginname(String loginname){
        if (null == loginname || "".equals(loginname.trim())) {
            return RestResp.fail("Login name requied, but null");
        }
        try{
            User user = userDao.findByLoginname(loginname);
            if(null == user){
                return RestResp.fail("User not exist");
            }
            return RestResp.success(user);
        }catch (Exception e){
            log.error("", e);
            return RestResp.fail("Find user by login name error: ", e);
        }
    }
    public boolean saveVcode(String key, String vcode) {
        try {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(key, vcode, 5L, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            log.error("Redis 操作异常:", e);
            return false;
        }
    }

    public String getVcodeFromRedis(String key) {
        String val = null;
        try {
            boolean flag = redisTemplate.hasKey(key);
            if (!flag) {
                return val;
            }
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            val = ops.get(key);
            //redisTemplate.delete(key);
            return val;
        } catch (Exception e) {
            log.error("Redis 操作异常", e);
            return null;
        }
    }

    //@Value("${themis.frontend.url}")
    private String frontEndUrl;

    public RestResp sendVmail(VerifyCode vcode) {
        if (null == vcode) {
            return RestResp.fail("参数不能为空");
        }
        if (null == vcode.getKey() || "".equals(vcode.getKey().trim()) || !vcode.getKey().contains("@")) {
            return RestResp.fail("输入的邮箱格式不正确");
        }
        if (null == vcode.getVcode() || "".equals(vcode.getKey().trim())) {
            return RestResp.fail("验证码不能为空");
        }

        String vcodeVal = getVcodeFromRedis(vcode.getKey());
        if (vcodeVal.equals(vcode.getVcode())) {
            String[] to = {vcode.getKey()};
            String url = "http://" + frontEndUrl + "/resetpsw?email=" + vcode.getKey() + "&vcode=" + vcode.getVcode();
            try {
                //mailService.send(new Email(to,"修改密码","请点击以下链接进行密码修改操作：\n" +  url));
                mailService.sendHtmlMail(vcode.getKey(), "修改密码", "请点击以下链接进行密码修改操作：\n" +
                        "<a href='" + url + "'>点击这里</a>");
                return RestResp.success("邮件已发送到：" + vcode.getKey() + "，请尽快修改您的密码", null);
            } catch (Exception e) {
                log.error("邮件发送异常", e);
                return RestResp.fail("邮件发送失败,请重新操作");
            }
        }
        return RestResp.fail("验证码错误");
    }

    public RestResp resetpwd(String resetkey, String password) {
        User u = null;
        if (resetkey == null || "".equals(resetkey.trim())) {
            return RestResp.fail("账号非法");
        }
        if (null == password || "".equals(password.trim())) {
            return RestResp.fail("密码不能为空");
        }
        /*if(redisTemplate.hasKey(resetkey)){
            return RestResp.fail("链接失效");
        }*/
        if (resetkey.contains("@")) {
            u = userDao.findByEmail(resetkey);
        } else {
            u = userDao.findByMobilephone(resetkey);
        }
        if (null == u) {
            return RestResp.fail("重置密码失败");
        }
        if (null != password) {
            u.setPassword(EncryptUtils.encodeSHA256(password));
            userDao.save(u);
            return RestResp.success("重置密码成功!", null);
        }
        return RestResp.fail("重置密码失败");
    }

    public RestResp active(String email) {
        if (email == null || "".equals(email) || !RegexUtils.match(email, RegexUtils.REGEX_EMAIL)) {
            return RestResp.fail("邮箱格式不正确，激活失败");
        }
        User user = userDao.findByEmail(email);
        if (null == user) {
            return RestResp.fail("该邮箱未注册，无法激活");
        }
        if (user.getEnabled().equals(Status.EnableStatus.ENABLED.getStatus())) {
            return RestResp.fail("账号已经激活，请勿重复操作");
        } else {
            user.setEnabled(Status.EnableStatus.ENABLED.getStatus());
            userDao.save(user);
            return RestResp.success("账号激活成功", null);
        }
    }

    public RestResp sendMail(String email, String subject, String content) {
        if (email == null || "".equals(email) || !RegexUtils.match(email, RegexUtils.REGEX_EMAIL)) {
            return RestResp.fail("请正确填写邮箱");
        }
        if (content == null || "".equals(content.trim())) {
            return RestResp.fail("发送内容不能为空");
        }
        try {
            mailService.sendHtmlMail(email, subject, content);
            return RestResp.success("邮件已发送到：" + email + "，请前往查收", null);
        } catch (Exception e) {
            log.error("邮件发送异常", e);
            return RestResp.fail("邮件发送失败,请重新操作");
        }
    }

    public RestResp addBitcoinAddress(String loginname, String firstAddress) {
        if (null == loginname || "".equals(loginname.trim())) {
            return RestResp.fail("用户名不正确");
        }
        if (null == firstAddress || "".equals(firstAddress.trim()) || firstAddress.length() < 26 || firstAddress.length() > 34) {
            return RestResp.fail("未正确填写收款地址,请重新填写");
        }
        firstAddress = firstAddress.trim();
        User user = userDao.findByLoginname(loginname);
        if (null == user) {
            return RestResp.fail("用户名不正确");
        }
/*        if(firstAddress.equals(user.getFirstAddress())){
            return RestResp.fail("您未修改地址");
        }
        user.setFirstAddress(firstAddress);*/
        userDao.save(user);
        return RestResp.success("操作成功", firstAddress);

    }

    public RestResp signup(UserVO vo) throws Exception {
        if (null == vo || null == vo.getStype()) {
            return RestResp.fail(getMessage(I18NConst.SUBMIT_REGISTRATION_INFORMATION_CORRECTLY));
        }
        /**
         * remove loginname
         */
        /*if (null == vo.getLoginname() || !RegexUtils.match(vo.getLoginname(), RegexUtils.REGEX_NAME_LEN15)) {
            return RestResp.fail(getMessage(I18NConst.LOGIN_NAME_FORMAT));
        }
        user = userDao.findByLoginname(vo.getLoginname());
        if (null != user) {
            return RestResp.fail(getMessage(I18NConst.USER_ALREADY_EXIST));
        }*/
        User user = null;
        String vcode = null;
        if (Const.STYPE.WEB_EMAIL.getType() == vo.getStype() || Const.STYPE.APP_EMAIL.getType() == vo.getStype()) {
            if (null == vo.getEmail() || !RegexUtils.match(vo.getEmail(), RegexUtils.REGEX_EMAIL)) {
                return RestResp.fail(getMessage(I18NConst.FILL_MAIL_ADDRESS_CORRECTLY));
            } else {
                vo.setEnabled(Status.EnableStatus.ENABLED.getStatus());
            }
            user = userDao.findByEmail(vo.getEmail());
            if (null != user) {
                return RestResp.fail(getMessage(I18NConst.MAIL_ADDRESS_REGISTERED));
            }

            vo.setUsername(vo.getEmail());
            vcode = getVcodeFromRedis(vo.getEmail());
        } else if (Const.STYPE.WEB_MOBILEPHONE.getType() == vo.getStype() || Const.STYPE.APP_MOBILEPHONE.getType() == vo.getStype()) {
            if (null == vo.getMobilephone() || !RegexUtils.match(vo.getMobilephone(), RegexUtils.REGEX_MOBILEPHONE)) {
                return RestResp.fail(getMessage(I18NConst.FILL_PHONE_NUMBER_CORRECTLY));
            } else {
                vo.setEnabled(Status.EnableStatus.ENABLED.getStatus());
            }
            user = userDao.findByMobilephone(vo.getMobilephone());
            if (null != user) {
                return RestResp.fail(getMessage(I18NConst.PHONE_NUMBER_BEEN_USED));
            }
            vo.setUsername(vo.getMobilephone());
            vcode = getVcodeFromRedis(vo.getMobilephone());
        } else {
            return RestResp.fail(getMessage(I18NConst.REGISTER_FAILURE));
        }
        if (null == vo.getPassword() || "".equals(vo.getPassword().trim())) {
            return RestResp.fail(getMessage(I18NConst.FILL_LOGIN_PASSWORD_CORRECTLY));
        }
        String password = vo.getPassword().trim();
        if (!RegexUtils.match(password, RegexUtils.REGEX_PWD)) {
            return RestResp.fail(getMessage("请输入6-16位数字和字母,英文字符符号组合"));
        }
        vo.setCreateTime(System.currentTimeMillis());
        vo.setLoginStatus(Status.LoginStatus.LOGOUT.getStatus());

        vo.setPassword(EncryptUtils.encodeSHA256(password));

        if (null == vcode || "".equals(vcode.trim()) || !vcode.equals(vo.getVcode().trim())) {
            return RestResp.fail(getMessage(I18NConst.FILL_VERIFYING_CODE_CORRECTLY));
        }
        vo.setRoleId(Const.ROLE.USER.getId());

        //vo.setUsername(vo.getLoginname());
        vo.setAvatar(/*defaultUserAvatar*/uresParam.getDefaultUserAvatar());

        User u = userDao.save(vo.userVO2User());
        if (u == null) {
            return RestResp.fail(getMessage(I18NConst.ACTION_FAILURE));
        }
        addUserRole(u.getId(), Const.ROLE.USER.getId());
        UserVO userInfo = new UserVO(u);
        userInfo.setRoles(getRoles(u.getId()));


        if (uresParam.isAutomaticSign()) {
            String originToken = jwtService.generate(userInfo);
            token = "Bearer " + originToken;
            log.info("automaitc login token = " + token);
            userInfo.setToken(token);
        }

        return RestResp.success(getMessage(I18NConst.REGISTER_SUCCESS), userInfo);
    }

    public RestResp signin(UserVO vo) {
        if (null == vo || null == vo.getStype()) {
            return RestResp.fail(getMessage(I18NConst.FAILURE));
        }
        if (vo.getStype() == Const.STYPE.WEB_LOGNAME.getType() || vo.getStype() == Const.STYPE.WEB_EMAIL.getType() || vo.getStype() == Const.STYPE.WEB_MOBILEPHONE.getType()) {
            GeetestPo geetestPo = new GeetestPo();
            geetestPo.setGeetestChallenge(vo.getGeetestChallenge());
            geetestPo.setGeetestSeccode(vo.getGeetestSeccode());
            geetestPo.setGeetestValidate(vo.getGeetestValidate());
            geetestPo.setGtServerStatus(vo.getGtServerStatus());
            int gtStatus = getGeetestSecond(geetestPo);
            if (1 != gtStatus) {
                return RestResp.fail("校验失败");
            }
        }
        if (Const.STYPE.WEB_EMAIL.getType() == vo.getStype() || Const.STYPE.APP_EMAIL.getType() == vo.getStype()) {
            if (null == vo.getEmail() || !RegexUtils.match(vo.getEmail(), RegexUtils.REGEX_EMAIL)) {
                return RestResp.fail(getMessage(I18NConst.FILL_MAIL_ADDRESS_CORRECTLY));
            }
        } else if (Const.STYPE.WEB_MOBILEPHONE.getType() == vo.getStype() || Const.STYPE.APP_MOBILEPHONE.getType() == vo.getStype()) {
            if (null == vo.getMobilephone() || !RegexUtils.match(vo.getMobilephone(), RegexUtils.REGEX_MOBILEPHONE)) {
                return RestResp.fail(getMessage(I18NConst.FILL_PHONE_NUMBER_CORRECTLY));
            }
        } else {
            return RestResp.fail(getMessage(I18NConst.ACCOUNT_ERROR));
        }
        String password = vo.getPassword();
        vo.setPassword(EncryptUtils.encodeSHA256(password));
        try {
            Optional<User> optional = findUser(vo);
            return optional.map(u -> {
                if (u.getEnabled().equals(Status.EnableStatus.UNENABLED.getStatus())) {
                    return RestResp.fail(getMessage(I18NConst.ACCOUNT_NOT_ACTICATED));
                }
                if (u.getLoginStatus().equals(Status.LoginStatus.LOGIN.getStatus())) {
                    return RestResp.fail(getMessage(I18NConst.USER_BEEN_LOGIN));
                }

                UserTxDetail userTxDetail = getUserTxDetail(u.getId());
                UserVO userInfo = new UserVO(u);
                /**
                 * 实名检查
                 */
                UserQIC userQIC = userQICDao.findByUserId(u.getId());
                if (null != userQIC) {
                    userInfo.setQic(1);
                }
                userInfo.setUserTxDetail(userTxDetail);
                userInfo.setRoles(getRoles(userInfo.getId()));
                String originToken = jwtService.generate(userInfo);
                token = "Bearer " + originToken;
                log.info("token = " + token);
                userInfo.setToken(token);
                return RestResp.success(getMessage(I18NConst.LOGIN_SUCCESS), userInfo);
            }).orElse(RestResp.fail(getMessage(I18NConst.ACCOUNT_OR_PASSWORD_ERROR)));
        } catch (Exception e) {
            log.error("用户信息异常", e);
            return RestResp.fail(getMessage(I18NConst.USER_INFORMATION_EXECPTION));
        }
    }

    /**
     * 获取邮箱验证码
     *
     * @param email
     * @return
     */
    public RestResp sendVmailCode(String email) {

        if (null == email || "".equals(email.trim()) || !RegexUtils.match(email, RegexUtils.REGEX_EMAIL)) {
            return RestResp.fail(getMessage(I18NConst.MAIL_ADDRESS_FORMAT_INCORRECT));

        }
        //String code = defaultKaptcha.createText();
        String code = captchaService.getNumberKaptchaText();
        saveVcode(email, code);
        String content = String.format(/*signupStr*/uresParam.getSignupStr(), code);
        log.info(email + ": " + content);
        try {

            sendGridService.sendHtmlMail(email, "验证码", content);
            return RestResp.success(getMessage(I18NConst.MAIL_BEEN_SEND) + email + getMessage(I18NConst.PLEAST_LOG_IN), null);
        } catch (Exception e) {
            log.error("邮件发送异常", e);
            return RestResp.fail(getMessage(I18NConst.SEND_MAIL_FAILED));
        }

    }

    /**
     * 获取手机验证码
     *
     * @param mobilephone
     * @return
     */
    public RestResp sendPhoneCode(String mobilephone) {
        if (null == mobilephone || "".equals(mobilephone.trim()) || !RegexUtils.match(mobilephone, RegexUtils.REGEX_MOBILEPHONE)) {
            return RestResp.fail(getMessage(I18NConst.FILL_PHONE_NUMBER_CORRECTLY));
        }
        //String code = defaultKaptcha.createText();
        String code = captchaService.getNumberKaptchaText();
        saveVcode(mobilephone, code);
        String content = String.format(/*signupSmsStr*/uresParam.getSignupSmsStr(), code);
        log.info(mobilephone + ": " + content);
        try {
            boolean flag = smsService.sendSingleSms(mobilephone, content, /*smsCampaignId*/uresParam.getSmsCampaignId(), null);
            if (flag) {
                return RestResp.success(getMessage(I18NConst.SMS_BEEN_SEND) + mobilephone + getMessage(I18NConst.PLEASE_CHECK), null);
            }
            return RestResp.fail(getMessage(I18NConst.SMS_SENDING_FAILURE));
        } catch (Exception e) {
            log.error("短信发送异常", e);
            return RestResp.fail(getMessage(I18NConst.SMS_SENDING_FAILURE));
        }
    }

    /**
     * 验证验证码
     *
     * @param vcode
     * @return
     */
    public RestResp verifytKaptchaCode(VerifyCode vcode) {
        if (null == vcode || null == vcode.getKey() || null == vcode.getVcode() || "".equals(vcode.getKey().trim()) || "".equals(vcode.getVcode().trim())) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        String vcodeVal = getVcodeFromRedis(vcode.getKey());
        if (null != vcodeVal && vcodeVal.equals(vcode.getVcode())) {
            return RestResp.success(getMessage(I18NConst.VERIFICATION_CODE_CORRECT));
        }
        return RestResp.fail(getMessage(I18NConst.VERIFICATION_CODE_ERROR));
    }

    /**
     * 检查登录名是否使用
     *
     * @param loginname
     * @return
     */
    public RestResp checkLoginname(String loginname) {
        if (null == loginname || !RegexUtils.match(loginname, RegexUtils.REGEX_NAME_LEN15)) {
            return RestResp.fail(getMessage(I18NConst.LOGIN_NAME_FORMAT));
        }
        User user = userDao.findByLoginname(loginname);
        if (null != user) {
            return RestResp.fail(getMessage(I18NConst.USER_ALREADY_EXIST));
        }
        return RestResp.success(getMessage(I18NConst.VERIFICATION_SUCCESS));
    }

    /**
     * 检查邮箱是否使用
     *
     * @param email
     * @return
     */
    public RestResp checkEmail(String email) {
        if (null == email || !RegexUtils.match(email, RegexUtils.REGEX_EMAIL)) {
            return RestResp.fail(getMessage(I18NConst.FILL_MAIL_ADDRESS_CORRECTLY));
        }
        User user = userDao.findByEmail(email);
        if (null != user) {
            return RestResp.fail(getMessage(I18NConst.MAIL_ADDRESS_BEEN_USED));
        }
        return RestResp.success(getMessage(I18NConst.VERIFICATION_SUCCESS));
    }

    /**
     * 检查手机是否使用
     *
     * @param mobilephone
     * @return
     */
    public RestResp checkMobilephone(String mobilephone) {
        if (null == mobilephone || !RegexUtils.match(mobilephone, RegexUtils.REGEX_MOBILEPHONE)) {
            return RestResp.fail(getMessage(I18NConst.FILL_PHONE_NUMBER_CORRECTLY));
        }
        User user = userDao.findByMobilephone(mobilephone);
        if (null != user) {
            return RestResp.fail(getMessage(I18NConst.PHONE_NUMBER_BEEN_USED));
        }
        return RestResp.success(getMessage(I18NConst.VERIFICATION_SUCCESS));
    }

    public RestResp validatePwd(ReqParam param) {
        if (null == param) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        String resetkey = param.getResetkey();
        if (StringUtils.isEmpty(resetkey)) {
            return RestResp.fail("Name is Empty");
        }
        String pwd = param.getPassword();
        if (null == pwd || "".equals(pwd)) {
            return RestResp.fail(getMessage(I18NConst.PASSWORD_NOT_EMPTY));
        }
        UserVO vo = new UserVO();
        vo.setPassword(EncryptUtils.encodeSHA256(pwd));

        if (RegexUtils.match(resetkey, RegexUtils.REGEX_EMAIL)) {
            vo.setEmail(resetkey);
        } else if (RegexUtils.match(resetkey, RegexUtils.REGEX_MOBILEPHONE)) {
            vo.setMobilephone(resetkey);
        } else {
            return RestResp.fail();
        }
        try {
            Optional<User> optional = findUser(vo);
            if (optional.isPresent()) {
                return RestResp.success("验证通过");
            } else {
                return RestResp.fail("验证失败");
            }
        } catch (Exception e) {
            log.error("用户信息异常", e);
            return RestResp.fail(getMessage(I18NConst.USER_INFORMATION_EXECPTION));
        }

    }

    public RestResp checkQIC(Long userId) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            UserQIC userQIC = userQICDao.findByUserId(userId);
            if (null != userQIC) {
                return RestResp.success("已验证", userQIC);
            }
        } catch (Exception e) {
            log.error("check QIC error", e);
        }
        return RestResp.fail();
    }

    public RestResp userQIC(UserQIC userQIC) {
        log.info("实名认证信息：{}", userQIC);
        if (null == userQIC) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        if (null == userQIC.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        if (null == userQIC.getIdNo() || !RegexUtils.match(userQIC.getIdNo(), RegexUtils.REGEX_ID_NUMBER)) {
            return RestResp.fail("请正确填写身份证号");
        }
        if (userQIC.getRealName() == null || !RegexUtils.match(userQIC.getRealName(), RegexUtils.REGEX_REAL_NAME)) {
            return RestResp.fail("请正确填写姓名");
        }
        try {
            UserQIC userQIC1 = userQICDao.findByUserId(userQIC.getUserId());
            if (null != userQIC1) {
                return RestResp.fail("用户已认证", userQIC1);
            }
            userQIC1 = userQICDao.findByIdNo(userQIC.getIdNo());
            if (null != userQIC1) {
                return RestResp.fail("身份证已被使用", userQIC1);
            }
            log.info("开始请求实名认证...");
            String returnMessage = sendQICRequest(userQIC);
            log.info("结束请求实名认证...");
            if (null == returnMessage || "".equals(returnMessage.trim())) {
                log.error("实名认证失败：{}", returnMessage);
                return RestResp.fail("认证失败");
            }
            JSONObject returnJson = JSON.parseObject(returnMessage);
            int code = returnJson.getInteger("code");
            int res = returnJson.getJSONObject("result").getInteger("res");
            if (code != 0 || returnJson.getJSONObject("result").getInteger("res") != 1) {
                log.error("实名认证失败：{}", returnJson);
                return RestResp.fail("认证失败", userQIC);
            }
            //userQIC.setPhoto(returnJson.getJSONObject("result").getString("photo"));
            userQIC.setCreateTime(System.currentTimeMillis());
            userQIC1 = userQICDao.save(userQIC);
            return RestResp.success("认证成功", userQIC1);
        } catch (Exception e) {
            log.error("实名认证失败：{}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return RestResp.fail("认证失败");
    }

    public RestResp getQIC(Long userId) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            UserQIC userQIC = userQICDao.findByUserId(userId);
            if (null != userQIC) {
                return RestResp.success(userQIC);
            }
        } catch (Exception e) {
            log.error("check QIC error", e);
        }
        return RestResp.fail();
    }

    /**
     *
     */
    public RestResp getUserPayment(Long userId, Integer ptype) {
        if (null == userId) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            User user = userDao.findUserById(userId);
            if (null == user) {
                return RestResp.fail("用户信息异常");
            }
            List<UserPayment> list = userPaymentRepo.findByUserIdAndPtype(userId, ptype);
            return RestResp.success(list);

        } catch (Exception e) {
            log.error("查询出错", e);
            return RestResp.fail("查询出错");
        }
    }

    public RestResp getUserPrimaryEnabledPayment(Long userId) {
        if (null == userId) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            List<UserPayment> list = userPaymentRepo.findByUserIdAndEnabled(userId, 1);
            return RestResp.success(list);
        } catch (Exception e) {
            log.error("查询出错", e);
            return RestResp.fail("查询出错");
        }
    }

    public RestResp getUserPayment(Long userId) {
        if (null == userId) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            User user = userDao.findUserById(userId);
            if (null == user) {
                return RestResp.fail("用户信息异常");
            }
            List<UserPayment> list = userPaymentRepo.findByUserIdAndEnabled(userId, 1);
            UserPaymentVO payment = payment2VO(list, userId, user);
            return RestResp.success(payment);

        } catch (Exception e) {
            log.error("查询出错", e);
            return RestResp.fail("查询出错");
        }
    }

    public RestResp bindPayment(UserPaymentVO vo) {
        if (null == vo || null == vo.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            User user = userDao.findUserById(vo.getUserId());
            if (null == user) {
                log.error("用户信息异常");
                return RestResp.fail("用户信息异常");
            }
            UserQIC userQIC = userQICDao.findByUserId(vo.getUserId());
            if (null == userQIC) {
                log.error("未实名认证");
                return RestResp.fail("未实名认证");
            }
            List<UserPayment> list = null;
            UserPayment payment = new UserPayment();
            int type = -1;
            if (null != vo.getBankCard() && !"".equals(vo.getBankCard())) {
                type = (int) Const.PAYMENT.BANK_CARD.getType();
                list = userPaymentRepo.findByUserIdAndPtype(vo.getUserId(), type);
                if (null != list && list.size() >= 10) {
                    return RestResp.fail("最多绑定10张银行卡");
                    //payment = list.get(0);
                }
                for (UserPayment payment1 : list) {
                    if (payment1.getPayment().equals(vo.getBankCard())) {
                        return RestResp.fail("该账号已被绑定");
                    }
                }
                payment.setPayment(vo.getBankCard());
                if (null == vo.getBankName() || "".equals(vo.getBankName().trim())) {
                    String bakName = BankUtil.getNameOfBank(vo.getBankCard());
                    if (null == bakName) {
                        return RestResp.fail("无法获取到银行名称, 请手动填写");
                    }
                    vo.setBankName(bakName);
                }
                payment.setPaymentName(vo.getBankName());
            } else if (null != vo.getAliPay()) {
                type = (int) Const.PAYMENT.ALI_PAY.getType();
                list = userPaymentRepo.findByUserIdAndPtype(vo.getUserId(), type);
                if (null != list && list.size() >= 10) {
                    return RestResp.fail("最多绑定10个支付宝账号");
                    //payment = list.get(0);
                }
                for (UserPayment payment1 : list) {
                    if ((null != vo.getUsername() || !"".equals(vo.getUsername().trim())) && !payment1.getUsername().equals(vo.getUsername())) {
                        return RestResp.fail("绑定支付宝名称不一致");
                    }
                    if (payment1.getPayment().equals(vo.getAliPay())) {
                        return RestResp.fail("该账号已被绑定");
                    }
                }
                payment.setPayment(vo.getAliPay());
                payment.setPaymentName("支付宝");
                MultipartFile file = vo.getFile();
                if (null != file) {
                    String aliPayQr = uploadFile(file, RandomUtil.getRandomNumber(6));
                    if (null == aliPayQr) {
                        return RestResp.fail("二维码上传失败");
                    }
                    payment.setPaymentQr(aliPayQr);
                }
            } else {
                return RestResp.fail();
            }
            payment.setPtype(type);
            payment.setCreateTime(System.currentTimeMillis());
            payment.setUserId(vo.getUserId());
            payment.setUsername(vo.getUsername());
            String username = payment.getUsername() == null ? userQIC.getRealName() : payment.getUsername();
            payment.setUsername(username);
            payment = userPaymentRepo.save(payment);
            if (null == payment) {
                return RestResp.fail();
            }
            return RestResp.success(Arrays.asList(payment));
        } catch (Exception e) {

        }
        return RestResp.fail();
    }

    public RestResp bindPayment(UserPayment vo) {
        if (null == vo || null == vo.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            User user = userDao.findUserById(vo.getUserId());
            if (null == user) {
                log.error("用户不存在");
                return RestResp.fail("用户不存在");
            }
            UserQIC userQIC = userQICDao.findByUserId(vo.getUserId());
            if (null == userQIC) {
                log.error("未实名认证");
                return RestResp.fail("未实名认证");
            }
            List<UserPayment> list = null;
            UserPayment payment = new UserPayment();
            if (vo.getId() != null) {
                payment = userPaymentRepo.findUserPaymentById(vo.getId());
                if (null != payment) {
                    vo.setCreateTime(payment.getCreateTime());
                    //vo.setUpdateTime(System.currentTimeMillis());
                    payment = userPaymentRepo.save(vo);
                    return RestResp.success("修改成功", payment);
                } else {
                    return RestResp.fail("修改失败");
                }
            }
            int ptype = vo.getPtype();
            list = userPaymentRepo.findByUserIdAndPtype(vo.getUserId(), ptype);
            if (null != list && list.size() > 0) {
                if (list.size() >= 10) {
                    return RestResp.fail("支付方式最多允许10个账号");
                } else {
                    for (UserPayment payment1 : list) {
                        if (payment1.getPayment().equals(vo.getPayment())) {
                            return RestResp.fail("该账号已被绑定");
                        }
                        if ((null != vo.getUsername() || !"".equals(vo.getUsername().trim())) && !payment1.getUsername().equals(vo.getUsername())) {
                            return RestResp.fail("绑定支付方式姓名不一致");
                        }
                    }
                }
            }

            if (Const.PAYMENT.BANK_CARD.getType() == ptype) {

            } else if (Const.PAYMENT.ALI_PAY.getType() == ptype) {
                MultipartFile file = null;
                if (null != file) {
                    String aliPayQr = uploadFile(file, RandomUtil.getRandomNumber(6));
                    if (null == aliPayQr) {
                        return RestResp.fail("二维码上传失败");
                    }
                    payment.setPaymentQr(aliPayQr);
                }
            } else {
                return RestResp.fail();
            }

            payment.setPtype(ptype);
            payment.setPayment(vo.getPayment());
            String paymentName = vo.getPaymentName() == null ? Const.PAYMENT.getName((long) ptype) : vo.getPaymentName();
            payment.setPaymentName(paymentName);
            payment.setPaymentQr(vo.getPaymentQr());
            payment.setCreateTime(System.currentTimeMillis());
            payment.setUserId(vo.getUserId());
            String username = payment.getUsername() == null ? userQIC.getRealName() : payment.getUsername();
            payment.setUsername(username);
            payment = userPaymentRepo.save(payment);
            if (null == payment) {
                return RestResp.fail();
            }
            return RestResp.success(payment);
        } catch (Exception e) {

        }
        return RestResp.fail();
    }

    public RestResp unbindPayment(UserPaymentVO vo) {
        if (null == vo || null == vo.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            UserPayment payment = null;
            if (null != vo.getBankCard()) {
                payment = userPaymentRepo.findByUserIdAndPayment(vo.getUserId(), vo.getBankCard());
                if (null == payment) {
                    return RestResp.fail("未找到绑定银行卡信息");
                }
            } else if (null != vo.getAliPay()) {
                payment = userPaymentRepo.findByUserIdAndPayment(vo.getUserId(), vo.getAliPay());
                if (null == payment) {
                    return RestResp.fail("未找到绑定支付宝信息");
                }
            } else {
                return RestResp.fail();
            }
            userPaymentRepo.delete(payment.getId());
            return RestResp.success("解除绑定成功");
        } catch (Exception e) {
            return RestResp.fail("解绑支付信息异常");
        }
    }

    public RestResp unbindPayment(UserPayment vo) {
        if (null == vo || null == vo.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        try {
            UserPayment payment = userPaymentRepo.findByUserIdAndPayment(vo.getUserId(), vo.getPayment());
            if (null == payment) {
                return RestResp.fail("未找到绑定信息");
            }
            userPaymentRepo.delete(payment.getId());
            return RestResp.success("解除绑定成功");
        } catch (Exception e) {
            return RestResp.fail("解绑支付信息异常");
        }
    }

    public RestResp paymentActive(UserPayment vo) {
        if (null == vo || null == vo.getUserId()) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        if (null == vo.getEnabled()) {
            return RestResp.fail("enabled = null");
        }
        try {
            UserPayment userPayment = userPaymentRepo.findByUserIdAndPayment(vo.getUserId(), vo.getPayment());
            if (null == userPayment) {
                return RestResp.fail("该账号不存在");
            }
            if (vo.getEnabled().equals(userPayment.getEnabled())) {
                return RestResp.success("状态未改变: " + vo.getEnabled());
            }
            userPayment.setEnabled(vo.getEnabled());
            userPayment = userPaymentRepo.save(userPayment);
            return RestResp.success("修改成功", userPayment);
        } catch (Exception e) {
            return RestResp.fail("修改支付信息异常");
        }
    }

    @Resource
    private UserAddressRepo userAddressRepo;

    public RestResp addAddress(UserAddress userAddress) {
        if (null == userAddress || null == userAddress.getUserId() || null == userAddress.getAddress() || "".equals(userAddress.getAddress().trim())) {
            return RestResp.fail();
        }
        try {
            boolean flag = false;
            if(null != userAddress.getType() && Const.DIGICCY.BTC.getType() == userAddress.getType()){
                flag = CoinAddressValidateUtil.bitcoinAddressValidate(userAddress.getAddress());
            } else {
                flag =CoinAddressValidateUtil.isETHValidAddress(userAddress.getAddress());
            }
            if(!flag){
                return RestResp.fail("输入地址不合法");
            }
            UserAddress userAddress1 = userAddressRepo.findByUserIdAndAddress(userAddress.getUserId(), userAddress.getAddress());
            if (null != userAddress1) {
                return RestResp.fail("地址已存在");
            }
            userAddress.setCreateTime(System.currentTimeMillis());
            userAddress1 = userAddressRepo.save(userAddress);

            /**
             * 只有当前地址时设为默认地址
             */
            List<UserAddress> list = userAddressRepo.findByUserIdAndType(userAddress.getUserId(), userAddress.getType());
            if(null != list && list.size() == 1 && list.get(0).getAddress().equals(userAddress1.getAddress())){
                userAddress1.setDefaulted(1);
                userAddress1 = userAddressRepo.save(userAddress1);
            }
            return RestResp.success(userAddress1);
        } catch (Exception e) {
            log.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return RestResp.fail();
    }

    public RestResp removeAddress(UserAddress userAddress) {
        if (null == userAddress || null == userAddress.getUserId() || null == userAddress.getAddress() || "".equals(userAddress.getAddress().trim())) {
            return RestResp.fail("Parameters NULL");
        }
        try {
            String[] addresses = userAddress.getAddress().split(Const.SEPARATOR_COMMA);
            List<UserAddress> list = userAddressRepo.findByAddressIn(Arrays.asList(addresses));
            for (UserAddress address : list) {
                userAddressRepo.delete(address);
            }
            return RestResp.success();
        } catch (Exception e) {
            log.error("", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return RestResp.fail();
    }

    public RestResp getAddress(Long userId) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            List<UserAddress> userAddress = userAddressRepo.findByUserId(userId);
            AddressVO vo = null;
            if (null != userAddress && userAddress.size() > 0) {
                vo = new AddressVO(userId, new HashSet<>(), new HashSet<>());
                Map<String, String> map = null;
                for (UserAddress uaddr : userAddress) {
                    Integer type = uaddr.getType();
                    String address = uaddr.getAddress();
                    map = new HashMap<>(1);
                    map.put("address", address);
                    if (Const.VCURR.BTC.getType() == type) {
                        vo.getBtcAddress().add(map);
                    } else if (Const.VCURR.ETH.getType() == type) {
                        vo.getEthAddress().add(map);
                    } else {

                    }
                }
            }
            return RestResp.success("", vo);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    public RestResp getAddresses(Long userId, Integer addtype) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            List<UserAddress> userAddress = new ArrayList<>();
            if (null == addtype) {
                userAddress = userAddressRepo.findByUserIdOrderByTypeAscCreateTimeDesc(userId);
            } else {
                userAddress = userAddressRepo.findByUserIdAndTypeOrderByCreateTimeDesc(userId, addtype);
            }

            UserAddrVO vo = new UserAddrVO(userId);
            vo.setAddresses(new ArrayList<>());
            Address get = new Address("GET", new ArrayList<>());
            Address btc = new Address("BTC", new ArrayList<>());
            Address eth = new Address("ETH", new ArrayList<>());
            if (null != userAddress && userAddress.size() > 0) {
                Map<String, String> map = null;
                for (UserAddress uaddr : userAddress) {
                    Integer type = uaddr.getType();
                    String address = uaddr.getAddress();
                    map = new HashMap<>(1);
                    map.put("addr", address);
                    map.put("remark", uaddr.getRemark());
                    if(Const.VCURR.GET.getType() == type){
                        get.getAddress().add(map);
                    }else if (Const.VCURR.BTC.getType() == type) {
                        btc.getAddress().add(map);
                    } else if (Const.VCURR.ETH.getType() == type) {
                        eth.getAddress().add(map);
                    } else {

                    }
                }
            }
            vo.getAddresses().add(btc);
            vo.getAddresses().add(get);
            return RestResp.success("", vo);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    public RestResp setDefaultAddresses(UserAddressVO vo) {
        if (null == vo) {
            return RestResp.fail();
        }
        try {
            List<UserAddress> userAddresses = userAddressRepo.findByUserIdAndType(vo.getUserId(), vo.getType());
            if(null == userAddresses || userAddresses.size() < 1){
                return RestResp.fail("Addresses Not Exist");
            }
            UserAddress def = null;
            if(null != vo.getAddress()){
                UserAddress voaddr = null;
                for(UserAddress userAddress : userAddresses){
                    if(userAddress.getAddress().equals(vo.getAddress())){
                        voaddr = userAddress;
                    }
                    if(userAddress.getDefaulted() == 1){
                        def = userAddress;
                    }
                }
                if(null == voaddr){
                    return RestResp.fail("Assigned Address Not Exist");
                }
                if(null == def){
                    voaddr.setDefaulted(1);
                    def = userAddressRepo.save(voaddr);
                }else if(!voaddr.getAddress().equals(def.getAddress())){
                    def.setDefaulted(0);
                    def = userAddressRepo.save(def);
                    voaddr.setDefaulted(1);
                    def = userAddressRepo.save(voaddr);
                }
            }else {
                for(UserAddress userAddress : userAddresses){
                    if(userAddress.getDefaulted() == 1){
                        def = userAddress;
                    }
                }
                if(null == def){
                    Collections.sort(userAddresses, new Comparator<UserAddress>() {
                        @Override
                        public int compare(UserAddress o1, UserAddress o2) {
                            return (int) (o1.getCreateTime() - o2.getCreateTime());
                        }
                    });
                    def = userAddresses.get(0);
                    def.setDefaulted(1);
                    def = userAddressRepo.save(def);
                }
            }
           return RestResp.success("", def);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    public RestResp getDefaultAddresses(Long userId, Integer addtype) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            UserAddress userAddress = userAddressRepo.findUserAddressByUserIdAndTypeAndDefaulted(userId, addtype, 1);
            return RestResp.success("", null != userAddress ? userAddress.getAddress() : null);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    public RestResp getUserTxDetails(Long userId) {
        if (null == userId) {
            return RestResp.fail(getMessage(I18NConst.PARAMETERS_NOT_EMPTY));
        }
        User user = userDao.findUserById(userId);
        if (null == user) {
            return RestResp.fail(getMessage(I18NConst.USER_NOT_EXIST));
        }
        UserTxDetail userTxDetail = getUserTxDetail(userId);
        if (null == userTxDetail) {
            userTxDetail = new UserTxDetail(true);
            userTxDetail.setUserId(userId);
        }
        UserTxDetailVO vo = new UserTxDetailVO(userTxDetail);
        if (null != user.getLoginname() && !"".equals(user.getLoginname().trim())) {
            vo.setUnstatus(0);
        }
        vo.setUsername(user.getUsername());
        vo.setAvatar(user.getAvatar());
        return RestResp.success(vo);
    }

    public RestResp getGeetestFirst() {
        HashMap<String, String> optionalParam = null;
        HashMap<String, Object> param = null;
        try {
            //geetest SDK实例
            GeetestLib geetestLib = new GeetestLib(GeetestConfig.getGeetestId(), GeetestConfig.getGeetestKey(), GeetestConfig.isnewfailback());
            //参数列表
            optionalParam = new HashMap<>();

            int gtServerStatus = geetestLib.preProcess(optionalParam);
            param = new HashMap<>();
            param.put("gt", GeetestConfig.getGeetestId());
            JSONObject jsonObject = JSON.parseObject(geetestLib.getResponseStr());
            param.put("challenge", jsonObject.getString("challenge"));

            if (gtServerStatus != 1) {//宕机
                param.put("offline", true);
            } else {//正常
                param.put("offline", false);
            }

            param.put("new_captcha", false);
        } catch (Exception e) {
            log.error("首次验证异常:", e.getMessage(), e);
            return RestResp.fail("首次验证异常" + e.getMessage(), e);
        }

        return RestResp.success(param);
    }

    /**
     * 极验第二步验证
     *
     * @param geetestPo
     * @return
     */
    public int getGeetestSecond(GeetestPo geetestPo) {
        Integer gtResult = 0;
        try {
            GeetestLib geetestLib = new GeetestLib(GeetestConfig.getGeetestId(), GeetestConfig.getGeetestKey(), GeetestConfig.isnewfailback());
            HashMap<String, String> param = new HashMap<>();
            if (geetestPo.getGtServerStatus() == 1) {
                //gt-server正常， 向服务器发送二次验证
                gtResult = geetestLib.enhencedValidateRequest(geetestPo.getGeetestChallenge(),
                        geetestPo.getGeetestValidate(),
                        geetestPo.getGeetestSeccode(), param);
            } else {
                // gt-server非正常情况下，进行failback模式验证
                gtResult = geetestLib.failbackValidateRequest(geetestPo.getGeetestChallenge(),
                        geetestPo.getGeetestChallenge(),
                        geetestPo.getGeetestSeccode());
            }
        } catch (Exception e) {
            log.error("二次验证异常:", e.getMessage(), e);
        }
        return gtResult;
    }


    public String uploadFile(MultipartFile file, Long no) {
        String newFileName = null;
        if (null != file) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            newFileName = tfsConsumer.saveTfsFile(file, no);
        }
        return null;
    }

    private void addUserRole(Long userId, Long roleId) {
        if (null == userId || null == roleId) {
            return;
        }
        UserRole userRole = userRoleDao.findByUserIdAndRoleId(userId, roleId);
        if (null == userRole) {
            userRole = new UserRole(userId, roleId);
            userRoleDao.save(userRole);
        }
    }

    public Set<String> getRoles(Long userId) {
        if (null == userId) {
            return null;
        }
        Set<String> set = new HashSet<>();
        List<UserRole> list = userRoleDao.findByUserId(userId);
        if (null == list || list.size() < 1) {
            set.add("user");
            return set;
        }
        List<Long> ll = new ArrayList<>();
        list.stream().forEach(userRole -> {
            ll.add(userRole.getRoleId());
        });
        List<Role> roles = roleDao.findByIdIn(ll);
        for (Role role : roles) {
            set.add(role.getRoleSign());
        }
        return set;
    }

    private UserPaymentVO payment2VO(List<UserPayment> list, Long userId, User user) {
        UserPaymentVO vo = new UserPaymentVO(userId);
        if (null != list && list.size() > 0) {
//            Collections.sort(list, new Comparator<UserPayment>() {
//                @Override
//                public int compare(UserPayment o1, UserPayment o2) {
//                    return  -o1.getCreateTime().compareTo(o2.getCreateTime());
//                }
//            });
            String username = null;
            String bankCard = null;
            String bankName = null;
            String aliPay = null;
            for (UserPayment payment : list) {
                username = payment.getUsername() != null ? payment.getUsername() : user != null ? user.getUsername() : null;
                if (Const.PAYMENT.CASH.getType() == payment.getPtype().intValue()) {

                } else if (Const.PAYMENT.BANK_CARD.getType() == payment.getPtype().intValue()) {
//                    bankCard = bankCard != null ? bankCard + Const.SEPARATOR_COMMA + payment.getPayment() : payment.getPayment();
//                    bankName = bankName != null ? bankName + Const.SEPARATOR_COMMA + payment.getPaymentName() : payment.getPaymentName();
                    bankCard = bankCard != null ? bankCard : payment.getPayment();
                    bankName = bankName != null ? bankName : payment.getPaymentName();

                } else if (Const.PAYMENT.ALI_PAY.getType() == payment.getPtype().intValue()) {
//                    aliPay = aliPay != null ? aliPay + Const.SEPARATOR_COMMA + payment.getPayment() : payment.getPayment();
                    aliPay = aliPay != null ? aliPay : payment.getPayment();
                } else if (Const.PAYMENT.WECHAT.getType() == payment.getPtype().intValue()) {

                } else if (Const.PAYMENT.APPLE_PAY.getType() == payment.getPtype().intValue()) {

                } else {

                }
            }
            vo.setUsername(username);
            vo.setBankCard(bankCard);
            vo.setBankName(bankName);
            vo.setAliPay(aliPay);
        }
        return vo;
    }

    private Role getRole(Long roleId) {
        Role role = null;
        if (null != role) {
            try {
                role = roleDao.findById(roleId);
            } catch (Exception e) {
                log.error("角色查询异常：", e);
            }
        }
        return role;
    }

    private UserTxDetail getUserTxDetail(Long userId) {
        UserTxDetail userTxDetail = null;
        if (null != userId) {
            try {
                userTxDetail = findUserTxDetailByUserId(userId);
                if (userTxDetail == null) {
                    userTxDetail = new UserTxDetail(true);
                    userTxDetail.setUserId(userId);
                    userTxDetailDao.save(userTxDetail);
                }
            } catch (Exception e) {
                log.error("用户交易信息查询失败：", e);
            }
        }
        return userTxDetail;
    }

    private String getMessage(String code) {
        return myMessageSource.getMessage(code);
    }

    @Value("${QIC.appcode}")
    private String appcode;

    private String sendQICRequest(UserQIC userQIC) throws Exception {
        String host = "https://eidimage.shumaidata.com";
        String path = "/eid_image/get";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("idcard", userQIC.getIdNo());
        bodys.put("name", userQIC.getRealName());
        return AliBaBaHttpUtils.doPost(host, path, headers, querys, bodys);

    }

    public RestResp list(Integer pageSize, Integer pageNo, String search, Integer status) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, new Sort(Sort.Direction.DESC, "createTime"));
        try {
            Page<User> page = userDao.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    Predicate predicate = null;
                    Predicate predicate1 = null;
                    Predicate predicate2 = null;
                    if (null != status) {
                        predicates.add(criteriaBuilder.equal(root.get("enabled").as(Integer.class), status));
                        predicate1 = criteriaBuilder.and(criteriaBuilder.equal(root.get("enabled").as(Integer.class), status));
                        predicate = criteriaBuilder.and(predicate1);
                        //predicates.add(predicate1);
                    }
                    if (null != search && !"".equals(search.trim())) {
                        predicates.add(criteriaBuilder.like(root.get("email").as(String.class), "%" + search + "%"));
                        predicates.add(criteriaBuilder.like(root.get("loginname").as(String.class), "%" + search + "%"));
                        predicates.add(criteriaBuilder.like(root.get("mobilephone").as(String.class), "%" + search + "%"));
                        predicate2 = criteriaBuilder.or(criteriaBuilder.like(root.get("email").as(String.class), "%" + search + "%"),
                                criteriaBuilder.like(root.get("loginname").as(String.class), "%" + search + "%"),
                                criteriaBuilder.like(root.get("mobilephone").as(String.class), "%" + search + "%"));
                        if (null != predicate1) {
                            predicate = criteriaBuilder.and(predicate1, predicate2);
                        } else {
                            predicate = criteriaBuilder.and(predicate2);
                        }
                        //predicates.add(predicate2);
                    }
                    Predicate[] p = new Predicate[predicates.size()];
//                    predicate = criteriaBuilder.and(predicate1, predicate2);
                    if (null != predicate) {
                        return predicate;
                    }
                    return criteriaBuilder.and(predicates.toArray(p));
                }
            }, pageable);
            List<UserRVO> rvos = new ArrayList<>(page.getContent().size());
            for (User user : page.getContent()) {
                UserRVO rvo = user2RVO(user, true, true, false);
                rvos.add(rvo);
            }
            return RestResp.success(new PageModel(page.getTotalPages(), page.getTotalElements(), rvos));
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    private UserRVO user2RVO(User user, boolean isqic, boolean ispay, boolean isaddress) {
        if (null != user) {
            UserRVO rvo = new UserRVO(user);
            if (isqic) {
                UserQIC qic = userQICDao.findByUserId(user.getId());
                if (null != qic) {
                    rvo.setQic(1);
                    rvo.setUserQIC(qic);
                } else {
                    rvo.setQic(0);
                }
            }
            if (ispay) {
                List<UserPayment> userPayments = userPaymentRepo.findByUserId(user.getId());
                if (null != userPayments && userPayments.size() > 0) {
                    Collections.sort(userPayments, new Comparator<UserPayment>() {
                        @Override
                        public int compare(UserPayment o1, UserPayment o2) {
                            return o1.getPtype() - o2.getPtype();
                        }
                    });
                    rvo.setPayments(userPayments);
                }
            }
            if (isaddress) {
                List<UserAddress> userAddresses = userAddressRepo.findByUserId(user.getId());
                if (null != userAddresses && userAddresses.size() > 0) {
                    Collections.sort(userAddresses, new Comparator<UserAddress>() {
                        @Override
                        public int compare(UserAddress o1, UserAddress o2) {
                            return o1.getType() - o2.getType();
                        }
                    });
                    rvo.setAddresses(userAddresses);
                }
            }

            return rvo;
        }
        return null;
    }

    public RestResp detail(Long userId) {
        if (null == userId) {
            return RestResp.fail();
        }
        try {
            User user = userDao.findUserById(userId);
            if (null == user) {
                return RestResp.fail("找不到用户");
            }
            UserRVO rvo = user2RVO(user, true, true, true);
            return RestResp.success(rvo);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail();
    }

    public RestResp adminUser(ReqParam param) {
        if (null == param || null == param.getUserId()) {
            return RestResp.fail("参数不全");
        }
        try {
            User u = userDao.findUserById(param.getUserId());
            if (null != u) {
                if (null != param.getEnabled()) {
                    u.setEnabled(param.getEnabled());
                } else if (null != param.getMobilephone() && !"".equals(param.getMobilephone().trim())) {
                    if (!RegexUtils.match(param.getMobilephone(), RegexUtils.REGEX_MOBILEPHONE)) {
                        return RestResp.fail("手机格式不正确");
                    }
                    u.setMobilephone(param.getMobilephone());
                } else if (null != param.getPassword() && !"".equals(param.getPassword())) {
                    u.setPassword(EncryptUtils.encodeSHA256(param.getPassword()));
                } else {

                }
            }
            userDao.save(u);
            return RestResp.success("操作成功", null);
        } catch (Exception e) {
            log.error("", e);
        }
        return RestResp.fail("操作失败");
    }
}
