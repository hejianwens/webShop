package com.project.webshop.user.serviceImpl;


import com.project.webshop.user.mapper.CustomerMapper;

import com.project.webshop.user.util.RedisUtil;
import common.model.Result;
import common.model.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Component
@Transactional
public class CustomerServiceImpl {
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    RedisUtil redisUtil;


    public Result findCustomerByName(Customer customer) {
        Result result=new Result();
        Customer resCustomer=customerMapper.findCustomerByName(customer);
        if(resCustomer==null){
            result.setCode("500");
            result.setMessage("账号不存在");
            result.setData(null);
            return result;
        }else if(!resCustomer.getPassword().equals(customer.getPassword())){
            result.setCode("500");
            result.setMessage("密码错误");
            result.setData(null);
            return result;
        }
        String key="customer"+resCustomer.getId();
        redisUtil.set(key,resCustomer,60*60*24);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(key);

        return result;
    }


    public Result register(Customer customer) {
        Result result=new Result();
        Customer selectCustomer=customerMapper.findCustomerByName(customer);
        if(selectCustomer!=null){
            result.setCode("500");
            result.setMessage("注册失败，账号已经被使用");
            return result;
        }
        customerMapper.insert(customer);

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public Result checkCustomerLogin(String loginKey) {
        Result result=new Result();
        Customer customer=null;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result.setCode("500");
            result.setMessage("权限身份不正确或者登陆密钥出错，请重新登录");
            return result;
        }

        if(customer==null){
            result.setCode("500");
            result.setMessage("未登录");
            return result;
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(customer.getNickName());
        return result;
    }

    public Result outLogin(String loginKey) {
        Result result=new Result();
        if("".equals(loginKey)){
            result.setCode("500");
            result.setMessage("失败,登录密钥为空");
            result.setData(null);
            return result;
        }
        redisUtil.del(loginKey);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }
}
