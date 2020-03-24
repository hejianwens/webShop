package com.project.webshop.user.mapper;

import common.model.user.Shop;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface  ShopMapper {
    Shop findShopByName(Shop shop);

    void insert(Shop shop);
}
