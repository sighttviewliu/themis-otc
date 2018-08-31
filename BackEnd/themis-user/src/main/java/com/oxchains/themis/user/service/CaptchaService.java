package com.oxchains.themis.user.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.oxchains.themis.common.captcha.NumberKaptcha;
import com.oxchains.themis.common.param.VerifyCode;
import com.oxchains.themis.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author ccl
 * @time 2018-05-06 10:39
 * @name CaptchaService
 * @desc:
 */
@Slf4j
@Service
public class CaptchaService {
    @Resource
    DefaultKaptcha defaultKaptcha;

    @Resource
    NumberKaptcha numberKaptcha;

    @Resource
    RedisService redisService;

    public void defaultKaptcha(VerifyCode vcode, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (null == vcode || vcode.getKey() == null || "".equals(vcode.getKey().trim())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            redisService.set(vcode.getKey(),createText,300L);

            //request.getSession().setAttribute(vcode.getKey(), createText);

            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    public String getDefaultKaptchaText(){
        return defaultKaptcha.createText();
    }

    public String getNumberKaptchaText(){
        return numberKaptcha.createText();
    }
    public String getNumberKaptchaText(int len){
        numberKaptcha.setLen(len);
        return numberKaptcha.createText();
    }
}
