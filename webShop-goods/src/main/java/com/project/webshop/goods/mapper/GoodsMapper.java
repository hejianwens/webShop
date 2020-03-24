package com.project.webshop.goods.mapper;

import common.model.goods.Goods;
import common.model.order.GoodsEvaluate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoodsMapper {

    //goodsKind位置是留给智能处理商品名称的
    public List<Goods>findGoodsList(@Param("goods") Goods goods, @Param("goodsKind") List<String>goodsKind);

    public Goods findGoodsInfo(Goods goods);

    public void  delete(Goods goods);

    public Goods selectStockAmountAndSoldAmount(Goods goods);

    public void insert(Goods goods);

    public BigDecimal getGoodsMaxId();

    void updateGoodsInfo(Goods goods);

    BigDecimal selectOrderItemCount();

    BigDecimal selectUnSameGoodsEvaluationCount();

    BigDecimal selectUnSameCustomerEvaluationCount();

    List<BigDecimal> getAllCustomerId();

    List<BigDecimal> getCustomerLikeGoods(@Param("customerId")BigDecimal customerId);

    Integer selectGoodsEvaluationCount( @Param("id")BigDecimal id);

    List<GoodsEvaluate> findGoodsInfoEvaluates(@Param("id")BigDecimal id);

    BigDecimal findGoodsInfoAvgEvaluate(@Param("id")BigDecimal id);

    void deleteEvaluate(@Param("id")Integer id);
}
