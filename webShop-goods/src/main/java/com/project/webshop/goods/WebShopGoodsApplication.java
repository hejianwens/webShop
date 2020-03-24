package com.project.webshop.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//开启远程服务，扫描Feign板块所有远程服务加载，要用哪个直接当本地服务使用
@EnableEurekaClient
//@EnableFeignClients("com.project.webshop.goods.feign")

public class WebShopGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopGoodsApplication.class, args);
    }

}
