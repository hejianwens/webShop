package com.project.webshop.discover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WebShopDiscoverApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopDiscoverApplication.class, args);
    }

}
