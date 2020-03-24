package com.project.webshop.order.controller;


import com.project.webshop.order.service.impl.OrderItemServiceImpl;
import com.project.webshop.order.service.impl.OrderServiceImpl;
import common.model.QueryParams;
import common.model.Result;
import common.model.order.GoodsEvaluate;
import common.model.order.Order;
import common.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {
    @Autowired
    OrderItemServiceImpl orderServiceImpl;

    @RequestMapping(value = "/findOrderItems",method = RequestMethod.GET)
    public Result findOrderItems(OrderItem orderItem, QueryParams qps,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.findOrderItems(orderItem,qps,loginKey);
        return result;
    }

    @RequestMapping(value = "/insertGoodsEvaluate",method = RequestMethod.POST)
    public Result insertEvaluate(@RequestBody List<GoodsEvaluate>goodsEvaluates,@CookieValue("loginKey") String loginKey) {
        Result result= orderServiceImpl.insertEvaluate(goodsEvaluates,loginKey);;
        return result;
    }



}
