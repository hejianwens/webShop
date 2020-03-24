package com.project.webshop.order.feign;

import common.model.Result;
import common.model.order.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "webShop-goods")
@RequestMapping(value = "/goods")
public interface GoodsFeignService {

    @RequestMapping(value = "/orderUpdateStockAmount")
    Result orderUpdateStockAmount(List<OrderItem> orderItems);

}
