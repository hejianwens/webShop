package com.project.webshop.order.feign;


import common.model.Result;
import common.model.buyCar.BuyCar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "webShop-buyCar")
@RequestMapping(value = "/buyCar")
public interface BuyCarFeignService {

    @RequestMapping(value = "/deleteByInnerService")
    public Result deleteByInnerService(List<BuyCar> buyCars);
}
