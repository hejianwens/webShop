package com.project.webshop.buycar.mapper;


import common.model.buyCar.BuyCar;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BuyCarMapper {
    public List<BuyCar> findBuyCarList(BuyCar buyCar);

    public void insert(BuyCar buyCar);

    public void updateAmountAndSubtotal(BuyCar buyCar);

    public void delete(List<BuyCar> buyCars);

    BuyCar findBuyCarByGoodsId(BuyCar buyCar);

    BuyCar selectById(BigDecimal id);
}
