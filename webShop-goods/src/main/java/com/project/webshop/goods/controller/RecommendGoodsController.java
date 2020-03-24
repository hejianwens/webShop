package com.project.webshop.goods.controller;

import com.project.webshop.goods.serviceImpl.RecommendGoodsService;
import common.model.QueryParams;
import common.model.Result;
import common.model.goods.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/recommendGoods")
public class RecommendGoodsController {
    @Autowired
    RecommendGoodsService recommendGoodsService;

    @RequestMapping(value = "/getRecommendGoods",method = RequestMethod.GET)
    public Result getRecommendGoods(@CookieValue("loginKey")String loginKey){
        Result result=recommendGoodsService.getRecommendGoods(loginKey);
        return result;
    }
}
