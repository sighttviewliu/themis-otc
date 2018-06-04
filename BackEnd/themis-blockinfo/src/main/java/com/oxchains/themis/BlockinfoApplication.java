package com.oxchains.themis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@ServletComponentScan
@EnableEurekaClient
@SpringBootApplication
public class BlockinfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockinfoApplication.class, args);
    }
}
