package com.oxchains.themis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author luoxuri
 * @create 2017-10-20 19:06
 **/
@EnableSwagger2
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@EnableEurekaClient
@EnableTransactionManagement
public class NoticeApplication {
    public static void main(String[] args){
        SpringApplication.run(NoticeApplication.class, args);
    }

}
