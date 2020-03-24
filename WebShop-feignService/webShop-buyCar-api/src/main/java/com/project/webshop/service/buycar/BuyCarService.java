package com.project.webshop.service.buycar;


import common.model.Result;
import common.model.buyCar.BuyCar;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
//这里只是一个规范，需要被远程调用的服务，继承这个接口
public interface BuyCarService {

    public Result delete(List<BuyCar> buyCars,String loginKey);
}
