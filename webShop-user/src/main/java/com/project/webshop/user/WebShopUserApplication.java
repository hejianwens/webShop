package com.project.webshop.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WebShopUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopUserApplication.class, args);
    }

}
