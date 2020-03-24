package com.project.webshop.order.mapper;


import common.model.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void insertOrderItemList(List<OrderItem> orderItems);

   void updateBySelect(OrderItem orderItem);

    List<OrderItem> findOrderItemsByOrder(OrderItem orderItem);

    List<OrderItem> findOrderItems(OrderItem orderItem);

    List<OrderItem> findOrderItemStatus(OrderItem selectOrderItem);
}
