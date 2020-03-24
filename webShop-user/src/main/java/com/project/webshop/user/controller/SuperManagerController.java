package com.project.webshop.user.controller;

import com.project.webshop.user.serviceImpl.ShopServiceImpl;
import com.project.webshop.user.serviceImpl.SuperManagerServiceImpl;
import common.model.Result;
import common.model.user.Shop;
import common.model.user.SuperManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(value = "/superManager")
public class SuperManagerController {

        @Autowired
        SuperManagerServiceImpl superManagerServiceImpl;

        @RequestMapping(value = "/findByName" ,method = RequestMethod.POST)
        public Result findByName(@RequestBody SuperManager superManager){
            Result result=superManagerServiceImpl.findByName(superManager);
            return result;
        }







}
