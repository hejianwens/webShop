package com.project.webshop.user.serviceImpl;

import com.project.webshop.user.mapper.ShopMapper;
import com.project.webshop.user.util.RedisUtil;
import common.model.Result;
import common.model.user.Customer;
import common.model.user.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShopServiceImpl {
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    RedisUtil redisUtil;

    public Result findShopByName(Shop shop) {
        Result result=new Result();
        Shop resShop=shopMapper.findShopByName(shop);
        if(resShop==null){
            result.setCode("500");
            result.setMessage("账号不存在");
            result.setData(null);
            return result;
        }else if(!resShop.getPassword().equals(shop.getPassword())){
            result.setCode("500");
            result.setMessage("密码错误");
            result.setData(null);
            return result;
        }
        String key="shop"+resShop.getId();
        redisUtil.set(key,resShop,60*60*24);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(key);

        return result;
    }

    public Result register(Shop shop) {
        Result result=new Result();
        Shop selectShop=shopMapper.findShopByName(shop);
        if(selectShop!=null){
            result.setCode("500");
            result.setMessage("注册失败，账号已经被使用");
            return result;
        }
        shopMapper.insert(shop);

        result.setCode("200");
        result.setMessage("成功");
        return result;
    }

    public Result checkShopLogin(String loginKey) {
        Result result=new Result();
        Shop shop= null;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result.setCode("500");
            result.setMessage("权限身份不正确或者登陆密钥出错，请重新登录");
            return result;
        }
        if(shop==null){
            result.setCode("500");
            result.setMessage("未登录");
            return result;
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(shop.getShopName());
        return result;
    }
}
