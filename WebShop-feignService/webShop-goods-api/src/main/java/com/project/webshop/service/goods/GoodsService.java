package com.project.webshop.service.goods;

import common.model.Result;
import common.model.goods.Goods;
import common.model.order.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;
//这里只是一个规范，需要被远程调用的服务，继承这个接口
public interface GoodsService {
     Result findGoodsInfo(Goods goods);

    Result orderUpdateStockAmount(List<OrderItem> orderItems);

}
