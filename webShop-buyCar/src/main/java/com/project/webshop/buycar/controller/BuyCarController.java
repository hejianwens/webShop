package com.project.webshop.buycar.controller;

import com.project.webshop.buycar.serviceImpl.BuyCarServiceImpl;
import common.model.Result;
import common.model.buyCar.BuyCar;
import common.model.goods.Goods;
import common.model.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/buyCar")
public class BuyCarController {
    @Autowired
    BuyCarServiceImpl buyCarServiceImpl;

    //购物车列表
    @RequestMapping(value = "/findBuyCarById",method = RequestMethod.GET)
    public Result findBuyCarById(BuyCar buyCar,@CookieValue("loginKey") String loginKey ){
        Result result=buyCarServiceImpl.findBuyCarById(buyCar,loginKey);
        return result;
    }

    //新增购物车
    @RequestMapping(value = "/insert",method = RequestMethod.POST )
    public Result insert(@RequestBody BuyCar buyCar,@CookieValue("loginKey") String loginKey){
        Result result=buyCarServiceImpl.insert(buyCar,loginKey);
        return result;
    }

    //数量+1
    @RequestMapping(value = "/addOneAmount",method = RequestMethod.POST)
    public Result addOneAmount(BuyCar buyCar,@CookieValue("loginKey") String loginKey){
        Result result=buyCarServiceImpl.addOneAmount(buyCar,loginKey);
        return result;
    }

    //数量-1
    @RequestMapping(value = "/subtractOneAmount" ,method = RequestMethod.POST)
    public Result subtractOneAmount(BuyCar buyCar,@CookieValue("loginKey") String loginKey){
        Result result=buyCarServiceImpl.subtractOneAmount(buyCar,loginKey);
        return result;
    }

    //删除
    @RequestMapping(value = "/delete" ,method = RequestMethod.POST)
    public Result delete(@RequestBody List<BuyCar> buyCars,@CookieValue("loginKey") String loginKey){
        Result result=buyCarServiceImpl.delete(buyCars,loginKey);
        return result;
    }

    @RequestMapping(value = "/deleteByInnerService" ,method = RequestMethod.POST)
    public Result deleteByInnerService(@RequestBody List<BuyCar> buyCars){
        Result result=buyCarServiceImpl.deleteByInnerService(buyCars);
        return result;
    }
}
