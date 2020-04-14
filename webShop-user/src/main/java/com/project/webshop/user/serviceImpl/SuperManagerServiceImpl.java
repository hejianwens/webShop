package com.project.webshop.user.serviceImpl;

import com.project.webshop.user.mapper.SuperManagerMapper;
import com.project.webshop.user.util.RedisUtil;
import common.model.Result;
import common.model.user.Customer;
import common.model.user.SuperManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuperManagerServiceImpl {
    @Autowired
    SuperManagerMapper superManagerMapper;

    @Autowired
    RedisUtil redisUtil;

    public Result findByName(SuperManager superManager) {
        Result result=new Result();
        SuperManager selectSuperManager=superManagerMapper.findByName(superManager.getAccount());
        if(selectSuperManager==null){
            result.setCode("500");
            result.setMessage("账号不存在");
            result.setData(null);
            return result;
        }else if(!selectSuperManager.getPassword().equals(selectSuperManager.getPassword())){
            result.setCode("500");
            result.setMessage("密码错误");
            result.setData(null);
            return result;
        }
        String key="superManager"+selectSuperManager.getId();
        redisUtil.set(key,selectSuperManager,60*60*24);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(key);
        return result;
    }

    public Result checkLogin(String loginKey) {
        Result result=new Result();
        SuperManager superManager=null;
        try {
            superManager= (SuperManager) redisUtil.get(loginKey);
        }catch (Exception e){
            result.setCode("500");
            result.setMessage("权限身份不正确或者登陆密钥出错，请重新登录");
            return result;
        }

        if(superManager==null){
            result.setCode("500");
            result.setMessage("未登录");
            return result;
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }
}
