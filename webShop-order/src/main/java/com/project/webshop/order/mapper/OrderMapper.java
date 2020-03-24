package com.project.webshop.order.mapper;

import common.model.buyCar.BuyCar;
import common.model.goods.Goods;
import common.model.order.Order;
import common.model.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Mapper
public interface OrderMapper {
    BigInteger getRowCount();

    void insert(Order order);

    BigDecimal selectStockAmount(@Param("id") BigDecimal id);

    void updateBySelect(Order order);

    List<Order> findOrders(Order order);




}
