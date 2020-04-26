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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/recommendGoods")
public class RecommendGoodsController {
    @Autowired
    RecommendGoodsService recommendGoodsService;

    @RequestMapping(value = "/getRecommendGoods",method = RequestMethod.GET)
    public Result getRecommendGoods(HttpServletRequest request){
        String loginKey=new String("");
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for(Cookie cookie:cookies){
                if("loginKey".equals(cookie.getName())){
                    loginKey=cookie.getValue();
                }
            }
        }
        Result result=recommendGoodsService.getRecommendGoods(loginKey);
        return result;
    }
}
