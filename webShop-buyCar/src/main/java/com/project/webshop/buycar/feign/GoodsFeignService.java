package com.project.webshop.buycar.feign;


import common.model.Result;
import common.model.buyCar.BuyCar;
import common.model.goods.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(value = "webShop-goods")
@RequestMapping(value = "/goods")
public interface GoodsFeignService {

    @RequestMapping(value = "/findGoodsInfoByFeign",method = RequestMethod.POST)
    public Result findGoodsInfoByFeign(@RequestBody Goods selectGoods);

}
