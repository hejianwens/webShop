package com.project.webshop.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//开启远程服务，扫描Feign板块所有远程服务加载，要用哪个直接当本地服务使用
@EnableFeignClients(basePackages = "com.project.webshop.order.feign")
public class WebShopOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopOrderApplication.class, args);
    }

}
