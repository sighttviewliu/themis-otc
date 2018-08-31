package com.oxchains.themis.common.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author ccl
 * @time 2017-12-01 17:46
 * @name KaptchaConfig
 * @desc: 验证码工具类配置
 */
@Component
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();
        defaultKaptcha.setConfig(getConfig(4, null));
        return defaultKaptcha;
    }

    @Bean
    public NumberKaptcha getNumberKaptcha4(){
        NumberKaptcha numberKaptcha = new NumberKaptcha();
        numberKaptcha.setConfig(getConfig(4, "0123456789"));
        return numberKaptcha;
    }

    private Config getConfig(int len, String textproducer){
        return new Config(getProperties(len, textproducer));
    }

    private Properties getProperties(int len, String textproducer){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        properties.setProperty("kaptcha.image.width", "110");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        if(len > 0){
            properties.setProperty("kaptcha.textproducer.char.length", len + "");
        }else {
            properties.setProperty("kaptcha.textproducer.char.length", "4");
        }

        if(null != textproducer && !"".equals(textproducer.trim())){
            properties.setProperty("kaptcha.textproducer.char.string", textproducer);
        }
        return properties;
    }
}
