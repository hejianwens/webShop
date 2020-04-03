package com.project.webshop.order.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.webshop.order.feign.BuyCarFeignService;
import com.project.webshop.order.feign.GoodsFeignService;
import com.project.webshop.order.mapper.OrderItemMapper;
import com.project.webshop.order.mapper.OrderMapper;
import com.project.webshop.order.util.PageUtils;
import com.project.webshop.order.util.RedisUtil;
import common.enums.OrderStatusEnum;
import common.model.PageResult;
import common.model.QueryParams;
import common.model.Result;
import common.model.buyCar.BuyCar;
import common.model.order.Order;
import common.model.order.OrderItem;
import common.model.order.OrderStatus;
import common.model.order.ReceiveCreateOrderParams;
import common.model.user.Customer;
import common.model.user.Shop;
import common.utils.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class OrderServiceImpl {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    BuyCarFeignService buyCarService;

    @Autowired
    GoodsFeignService goodsService;
    @Autowired
    RedisUtil redisUtil;

    public Result createOrder(ReceiveCreateOrderParams receiveCreateOrderParams, String loginKey) {
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
        boolean checkParamsFlag=checkParams(receiveCreateOrderParams);
        if(!checkParamsFlag){
            result.setMessage("下单失败，参数上送错误");
            result.setCode("500");
            return result;
        }
        //拿到购物内容
        List<BuyCar> buyCars=receiveCreateOrderParams.getBuyCars();
        //查库存
        for (BuyCar buyCar:buyCars){
            BigDecimal stockAmount=orderMapper.selectStockAmount(buyCar.getGoodsId());
            if (buyCar.getAmount().compareTo(stockAmount)==1){
                result.setCode("500");
                result.setMessage("下单失败，商品库存不足");
                return result;
            }
        }

        //开始处理订单
        Order order=new Order();
        BigDecimal total=new BigDecimal("0");
        List<OrderItem>orderItems=new ArrayList<OrderItem>();
        //获取订单号
        String number=getOrderNumber();

        //遍历购物车
        for(BuyCar buyCar:buyCars){
            OrderItem orderItem=new OrderItem();
            total=total.add(buyCar.getSubtotal());
            orderItem.setAmount(buyCar.getAmount());
            orderItem.setGoodsId(buyCar.getGoodsId());
            orderItem.setOrderNumber(number);
            orderItem.setShopId(buyCar.getGoods().getShopId());
            orderItem.setSubtotal(buyCar.getSubtotal());
            orderItem.setOrderItemStatus(OrderStatusEnum.HaveNotApply.getValue());
            orderItems.add(orderItem);
        }
        //设置订单号
        order.setOrderNumber(number);
        //设置对应客户账号id
        order.setCustomerId(customer.getId());
        //设置下单时间

        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
        order.setCreateTime(createTime);
        //设置总数
        order.setTotal(total);
        //设置配送信息
        order.setReceiverName(receiveCreateOrderParams.getReceiverName());
        order.setReceiverAddress(receiveCreateOrderParams.getReceiverAddress());
        order.setReceiverTelephone(receiveCreateOrderParams.getReceiverTelephone());
        order.setEvaluateFlag("n");

        order.setStatus(OrderStatusEnum.HaveNotApply.getValue());

        //创建订单
        orderMapper.insert(order);
        //插入订单子项
        orderItemMapper.insertOrderItemList(orderItems);

        //远程服务删除购物车内容
        Result buyCarServiceRes=buyCarService.deleteByInnerService(buyCars);
        if(!"200".equals(buyCarServiceRes.getCode())){
            result.setMessage(buyCarServiceRes.getMessage());
            result.setCode(buyCarServiceRes.getCode());
            //失败就手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        //远程服务减库存
        Result goodsServiceRes=goodsService.orderUpdateStockAmount(orderItems);
        if(!"200".equals(goodsServiceRes.getCode())){
            result.setMessage(goodsServiceRes.getMessage());
            result.setCode(goodsServiceRes.getCode());
            //失败就手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }


        result.setMessage("下单成功");
        result.setCode("200");
        result.setData(order.getOrderNumber());
        return result;
    }


    private String getOrderNumber() {
        StringBuffer stringBuffer=new StringBuffer();
        BigInteger rowCount=orderMapper.getRowCount();
        stringBuffer.append(getDateNumber());

        if(rowCount.toString().length()==1){
            stringBuffer.append("00000");
        }else if(rowCount.toString().length()==2){
            stringBuffer.append("0000");
        }else if(rowCount.toString().length()==3){
            stringBuffer.append("000");
        }else if(rowCount.toString().length()==4){
            stringBuffer.append("00");
        }else if(rowCount.toString().length()==5){
            stringBuffer.append("0");
        }
        stringBuffer.append(rowCount.toString());
        return stringBuffer.toString();
    }

    private static String getDateNumber() {
        Date date=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }



    public Result apply(String orderNumber,String loginKey) {
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
        if("".equals(orderNumber)||null==orderNumber){
            result.setMessage("支付失败，订单号为空");
            result.setCode("500");
            result.setData(null);
            return result;
        }
        Order order=new Order();
        order.setStatus(OrderStatusEnum.AlreadyApply.getValue());
        order.setOrderNumber(orderNumber);

        result=checkOrderStatus(order,OrderStatusEnum.HaveNotApply.getValue(),result);
        if(!"".equals(result.getCode())){
            return result;
        }

//        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
//        if(selectOrder==null){
//            result.setMessage("支付失败，订单号错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
//        if(!OrderStatusEnum.HaveNotApply.getValue().equals(selectOrder.getOrderNumber())){
//            result.setMessage("支付失败，订单号对应订单状态错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }

        orderMapper.updateBySelect(order);

        OrderItem orderItem=new OrderItem();
        orderItem.setOrderNumber(orderNumber);
        orderItem.setOrderItemStatus(OrderStatusEnum.AlreadyApply.getValue());
        orderItemMapper.updateBySelect(orderItem);

        result.setMessage("成功");
        result.setCode("200");
        result.setData(null);
        return result;
    }

    public Result findOrders(Order order, QueryParams qps, String loginKey) {
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
        order.setCustomerId(customer.getId());
        PageHelper.startPage(qps.getPage(),qps.getRows());
        List<Order> orders=orderMapper.findOrders(order);
        PageInfo<Order> pageInfo = new PageInfo<Order>(orders);
        PageResult pageResult = PageUtils.getPageResult(qps,pageInfo);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(pageResult);
        return result;
    }

    public Result alreadySendGoods(Order order, BigDecimal orderItemId, String loginKey) {
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
        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        if("".equals(order.getOrderNumber())){
            result.setCode("500");
            result.setMessage("失败，订单号为空");
            return result;
        }
        result=checkOrderStatus(order,OrderStatusEnum.AlreadyApply.getValue(),result);
        if(!"".equals(result.getCode())){
            return result;
        }

//        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
//        if(selectOrder==null){
//            result.setMessage("发货失败，订单号错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
//        if(!OrderStatusEnum.AlreadyApply.getValue().equals(selectOrder.getOrderNumber())){
//            result.setMessage("发货失败，订单号对应订单状态错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
        OrderItem orderItem=new OrderItem();
        orderItem.setId(orderItemId);
        orderItem.setOrderItemStatus(OrderStatusEnum.AlreadySend.getValue());
        orderItem.setOrderNumber(order.getOrderNumber());

        orderItemMapper.updateBySelect(orderItem);

        changOrderStatus(order.getOrderNumber(),OrderStatusEnum.AlreadySend.getValue());

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public Result alreadyGetGoods(Order order, String loginKey) {
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
        if("".equals(order.getOrderNumber())){
            result.setCode("500");
            result.setMessage("失败，订单号为空");
            return result;
        }
        result=checkOrderStatus(order,OrderStatusEnum.AlreadySend.getValue(),result);
        if(!"".equals(result.getCode())){
            return result;
        }
//        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
//        if(selectOrder==null){
//            result.setMessage("收货失败，订单号错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
//        if(!OrderStatusEnum.AlreadySend.getValue().equals(selectOrder.getOrderNumber())){
//            result.setMessage("收货失败，订单号对应订单状态错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }

        order.setStatus(OrderStatusEnum.AlreadyReceive.getValue());
        orderMapper.updateBySelect(order);

        OrderItem orderItem=new OrderItem();
        orderItem.setOrderItemStatus(OrderStatusEnum.AlreadyReceive.getValue());
        orderItem.setOrderNumber(order.getOrderNumber());
        orderItemMapper.updateBySelect(orderItem);

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public Result cancelOrder(Order order, String loginKey) {
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
        if("".equals(order.getOrderNumber())){
            result.setCode("500");
            result.setMessage("失败，订单号为空");
            return result;
        }

        result=checkOrderStatus(order,OrderStatusEnum.AlreadyReceive.getValue(),result);
        if(!"".equals(result.getCode())){
            return result;
        }
//        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
//        if(selectOrder==null){
//            result.setMessage("取消订单失败，订单号错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
//        if(!OrderStatusEnum.AlreadyReceive.getValue().equals(selectOrder.getOrderNumber())){
//            result.setMessage("取消订单失败，订单号对应订单状态错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }

        order.setStatus(OrderStatusEnum.WaitRefund.getValue());
        orderMapper.updateBySelect(order);

        OrderItem orderItem=new OrderItem();
        orderItem.setOrderItemStatus(OrderStatusEnum.WaitRefund.getValue());
        orderItem.setOrderNumber(order.getOrderNumber());
        orderItemMapper.updateBySelect(orderItem);

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public Result refund(Order order, BigDecimal orderItemId, String loginKey) {

        Result result;
        Shop shop= (Shop) redisUtil.get(loginKey);
        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        if("".equals(order.getOrderNumber())){
            result.setCode("500");
            result.setMessage("失败，订单号为空");
            return result;
        }

        result=checkOrderStatus(order,OrderStatusEnum.WaitRefund.getValue(),result);
//        if(!"".equals(result.getCode())){
//            return result;
//        }
//        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
//        if(selectOrder==null){
//            result.setMessage("退款失败，订单号错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }
//        if(!OrderStatusEnum.WaitRefund.getValue().equals(selectOrder.getOrderNumber())){
//            result.setMessage("退款失败，订单号对应订单状态错误");
//            result.setCode("500");
//            result.setData(null);
//            return result;
//        }

        OrderItem orderItem=new OrderItem();
        orderItem.setId(orderItemId);
        orderItem.setOrderItemStatus(OrderStatusEnum.AlreadyCancel.getValue());
        orderItem.setOrderNumber(order.getOrderNumber());

        orderItemMapper.updateBySelect(orderItem);
        changOrderStatus(order.getOrderNumber(),OrderStatusEnum.AlreadyCancel.getValue());

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    private Result checkOrderStatus(Order order, String value, Result result) {

        Order selectOrder=orderMapper.findOrderByOrderNumber(order);
        if(selectOrder==null){
            result.setMessage("支付失败，订单号错误");
            result.setCode("500");
            result.setData(null);
            return result;
        }
        if(!value.equals(selectOrder.getOrderNumber())){
            result.setMessage("请求失败，订单号对应订单状态错误，订单号对应状态为:"+selectOrder.getStatus());
            result.setCode("500");
            result.setData(null);
            return result;
        }

        return result;
    }

    private void changOrderStatus(String orderNumber, String value) {
        boolean changOrderFlag=false;
        OrderItem selectOrderItem=new OrderItem();
        selectOrderItem.setOrderNumber(orderNumber);
        List<OrderItem>orderItems=orderItemMapper.findOrderItemStatus(selectOrderItem);
        int i=0;
        for(OrderItem o:orderItems){
            if(o.getOrderItemStatus().equals(value)){
                i++;
            }
        }
        if(i==orderItems.size()){
            changOrderFlag=true;
        }
        if(changOrderFlag){
            Order order=new Order();
            order.setStatus(value);
            order.setOrderNumber(orderNumber);
            orderMapper.updateBySelect(order);
        }
    }


    //入参检验方法，经常被调用，设置static
    private static boolean checkParams(ReceiveCreateOrderParams receiveCreateOrderParams) {
        boolean flag=true;
        List <BuyCar> buyCars=receiveCreateOrderParams.getBuyCars();
        if(null==buyCars||buyCars.size()<=0){
            flag=false;
            return flag;
        }
        for(BuyCar buyCar:buyCars){
            if(null==buyCar.getAmount()){
                flag=false;
                return flag;
            }
            if(null==buyCar.getSubtotal()){
                flag=false;
                return flag;
            }
            if(null==buyCar.getGoodsId()){
                flag=false;
                return flag;
            }
        }
        return flag;
    }


    public Result getOrderStatus() {
        Result result=new Result();
        List<OrderStatusEnum>orderStatusEnums=OrderStatusEnum.getOrderStatusEnums();
        List<OrderStatus>orderStatuses=new ArrayList<OrderStatus>();
        for(OrderStatusEnum orderStatusEnum:orderStatusEnums){
            OrderStatus orderStatus=new OrderStatus();
            orderStatus.setName(orderStatusEnum.getName());
            orderStatus.setValue(orderStatusEnum.getValue());
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(orderStatuses);
        return result;
    }
}
