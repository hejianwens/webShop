package com.project.webshop.order.mapper;

import common.model.order.GoodsEvaluate;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface GoodsEvaluateMapper {
    void insertGoodsEvaluate(GoodsEvaluate goodsEvaluate);

    void delete(BigDecimal id);
}
