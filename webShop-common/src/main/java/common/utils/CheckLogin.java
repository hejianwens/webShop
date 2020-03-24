package common.utils;

import common.model.Result;
import common.model.user.Customer;
import common.model.user.Shop;
import common.model.user.SuperManager;

public class CheckLogin {
    public static Result checkShopLoginKey(Shop shop, String loginKey) {
        Result checkShopLoginKeyResult=new Result();
        if("".equals(loginKey)){
            checkShopLoginKeyResult.setCode("500");
            checkShopLoginKeyResult.setMessage("未登录");
            return checkShopLoginKeyResult;
        }
        if(!loginKey.startsWith("shop")){
            checkShopLoginKeyResult.setCode("500");
            checkShopLoginKeyResult.setMessage("您所用的用户权限是客户权限，非店家权限，或者密钥错误");
            return checkShopLoginKeyResult;
        }
        if(null==shop){
            checkShopLoginKeyResult.setCode("500");
            checkShopLoginKeyResult.setMessage("未登录");
            return checkShopLoginKeyResult;
        }
        return null;
    }

    public static Result checkCustomerLogin(Customer customer, String loginKey) {
        Result checkCustomerLoginResult=new Result();
        if("".equals(loginKey)){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("未登录");
            return checkCustomerLoginResult;
        }
        if(!loginKey.startsWith("customer")){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("您所用的用户权限是客户权限，非店家权限，或者密钥错误");
            return checkCustomerLoginResult;
        }
        if(null==customer){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("未登录");
            return checkCustomerLoginResult;
        }

        return null;
    }

    public static Result checkSuperManagerLogin(SuperManager customer, String loginKey) {
        Result checkCustomerLoginResult=new Result();
        if("".equals(loginKey)){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("未登录");
            return checkCustomerLoginResult;
        }
        if(!loginKey.startsWith("superManager")){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("您所用的用户权限不是超级管理员权限，或者密钥错误");
            return checkCustomerLoginResult;
        }
        if(null==customer){
            checkCustomerLoginResult.setCode("500");
            checkCustomerLoginResult.setMessage("未登录");
            return checkCustomerLoginResult;
        }
        return null;
    }
}
