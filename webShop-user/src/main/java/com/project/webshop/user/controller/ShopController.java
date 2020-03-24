package com.project.webshop.user.controller;



import com.project.webshop.user.serviceImpl.CustomerServiceImpl;
import com.project.webshop.user.serviceImpl.ShopServiceImpl;
import common.model.Result;
import common.model.user.Customer;
import common.model.user.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/shop")
public class ShopController {
    @Autowired
    ShopServiceImpl shopService;

    @RequestMapping(value = "/findShopByName" ,method = RequestMethod.POST)
    public Result findShopByName(@RequestBody Shop shop){
        Result result=shopService.findShopByName(shop);
        return result;
    }

    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public Result register(@RequestBody Shop shop){
        Result result=shopService.register(shop);
        return result;
    }

    @RequestMapping(value = "/checkShopLogin" ,method = RequestMethod.GET)
    public Result checkShopLogin(String loginKey){
        Result result=shopService.checkShopLogin(loginKey);
        return result;
    }

//    @RequestMapping(value = "/outLogin" ,method = RequestMethod.GET)
//    public Result outLogin(String loginKey){
//        Result result=customerServiceImpl.outLogin(loginKey);
//        return result;
//    }

}
