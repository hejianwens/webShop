package com.project.webshop.order.controller;


import com.project.webshop.order.service.impl.OrderServiceImpl;
import common.model.QueryParams;
import common.model.Result;

import common.model.goods.Goods;
import common.model.order.Order;
import common.model.order.OrderItem;
import common.model.order.ReceiveCreateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderServiceImpl orderServiceImpl;

    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    public Result createOrder(@RequestBody ReceiveCreateOrderParams receiveCreateOrderParams,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.createOrder(receiveCreateOrderParams,loginKey);;
        return result;

    }

    @RequestMapping(value = "/apply",method = RequestMethod.GET)
    public Result apply(Order order,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.apply(order.getOrderNumber(),loginKey);;
        return result;

    }

    @RequestMapping(value = "/findOrders",method = RequestMethod.GET)
    public Result findOrders(Order order, QueryParams qps,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.findOrders(order,qps,loginKey);;
        return result;
    }

    @RequestMapping(value = "/alreadySendGoods",method = RequestMethod.GET)
    public Result alreadySendGoods(Order order, BigDecimal orderItemId,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.alreadySendGoods(order,orderItemId,loginKey);;
        return result;
    }

    @RequestMapping(value = "/alreadyGetGoods",method = RequestMethod.GET)
    public Result alreadyGetGoods(Order order,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.alreadyGetGoods(order,loginKey);;
        return result;
    }

    @RequestMapping(value = "/refund",method = RequestMethod.GET)
    public Result refund(Order order,BigDecimal orderItemId,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.refund(order,orderItemId,loginKey);;
        return result;
    }


    @RequestMapping(value = "/cancelOrder",method = RequestMethod.GET)
    public Result cancelOrder(Order order,@CookieValue("loginKey") String loginKey){
        Result result= orderServiceImpl.cancelOrder(order,loginKey);;
        return result;
    }

    @RequestMapping(value = "/getOrderStatus",method = RequestMethod.GET)
    public Result getOrderStatus(){
        Result result= orderServiceImpl.getOrderStatus();;
        return result;
    }



}
