package com.oxchains.themis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class ChatApplication {
    public static void main(String[] args) {
<<<<<<< HEAD:BackEnd/themis-chat/src/main/java/com/oxchains/themis/Application.java
        SpringApplication.run(Application.class, args);
=======
        SpringApplication.run(ChatApplication.class,args);
>>>>>>> ca02506b9b12374588cf34069c931058a95e9531:BackEnd/themis-chat/src/main/java/com/oxchains/themis/ChatApplication.java
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
