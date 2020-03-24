package com.project.webshop.user.controller;



import com.project.webshop.user.serviceImpl.CustomerServiceImpl;
import common.model.Result;
import common.model.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @RequestMapping(value = "/findCustomerByName" ,method = RequestMethod.POST)
    public Result findCustomerByName(@RequestBody Customer customer){
        Result result=customerServiceImpl.findCustomerByName(customer);
        return result;
    }

    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public Result register(@RequestBody Customer customer){
        Result result=customerServiceImpl.register(customer);
        return result;
    }

    @RequestMapping(value = "/checkCustomerLogin" ,method = RequestMethod.GET)
    public Result checkCustomerLogin(String loginKey){
        Result result=customerServiceImpl.checkCustomerLogin(loginKey);
        return result;
    }

    @RequestMapping(value = "/outLogin" ,method = RequestMethod.GET)
    public Result outLogin(String loginKey){
        Result result=customerServiceImpl.outLogin(loginKey);
        return result;
    }

}
