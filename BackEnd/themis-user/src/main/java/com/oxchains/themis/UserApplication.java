package com.oxchains.themis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ccl
 * @time 2017-10-12 11:24
 * @name UserApplication
 * @desc:
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
@ComponentScan(value = {"com.oxchains.themis", "com.oxchains.basicService.files"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
