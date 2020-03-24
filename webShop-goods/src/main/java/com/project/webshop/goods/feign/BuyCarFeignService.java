package com.project.webshop.goods.feign;


import common.model.Result;
import common.model.buyCar.BuyCar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Component
@FeignClient(value = "webShop-buyCar")
@RequestMapping(value = "/buyCar")
public interface  BuyCarFeignService {

    @RequestMapping(value = "/delete")
    public Result delete(List<BuyCar> buyCars);

}
