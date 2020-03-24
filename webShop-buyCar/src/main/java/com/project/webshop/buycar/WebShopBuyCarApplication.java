package com.project.webshop.buycar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients("com.project.webshop.buycar.feign")
public class WebShopBuyCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopBuyCarApplication.class, args);
    }

}
