package com.project.webshop.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.webshop.order.mapper.GoodsEvaluateMapper;
import com.project.webshop.order.mapper.OrderItemMapper;
import com.project.webshop.order.mapper.OrderMapper;
import com.project.webshop.order.util.PageUtils;
import com.project.webshop.order.util.RedisUtil;
import common.model.PageResult;
import common.model.QueryParams;
import common.model.Result;
import common.model.order.GoodsEvaluate;
import common.model.order.Order;
import common.model.order.OrderItem;
import common.model.user.Customer;
import common.model.user.Shop;
import common.utils.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Transactional
public class OrderItemServiceImpl {
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    GoodsEvaluateMapper goodsEvaluateMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RedisUtil redisUtil;

    public Result findOrderItems(OrderItem orderItem, QueryParams qps, String loginKey) {
        Result result;
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        orderItem.setShopId(shop.getId());
        PageHelper.startPage(qps.getPage(),qps.getRows());
        List<OrderItem> orders=orderItemMapper.findOrderItems(orderItem);
        PageInfo<OrderItem> pageInfo = new PageInfo<OrderItem>(orders);
        PageResult pageResult = PageUtils.getPageResult(qps,pageInfo);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(pageResult);
        return result;
    }

    public Result insertEvaluate(List<GoodsEvaluate> goodsEvaluates, String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        if(goodsEvaluates==null||goodsEvaluates.size()<=0){
            result.setCode("500");
            result.setMessage("评价内容参数为空");
            result.setData(null);
            return result;
        }
        Order order=new Order();
        order.setOrderNumber(goodsEvaluates.get(0).getOrderNumber());
        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
        if(selectOrder==null){
            result.setMessage("参数错误，订单号不正确");
            result.setCode("500");
            result.setData(null);
            return result;
        }
        if("y".equals(selectOrder.getEvaluateFlag())){
            result.setCode("500");
            result.setMessage("参数错误，订单已经被评价");
            result.setData(null);
            return result;
        }
        for(GoodsEvaluate goodsEvaluate:goodsEvaluates){
            goodsEvaluate.setCustomerId(customer.getId());
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
            goodsEvaluate.setCreateTime(createTime);
            goodsEvaluateMapper.insertGoodsEvaluate(goodsEvaluate);
        }
        order.setEvaluateFlag("y");
        orderMapper.updateBySelect(order);

        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }
}
