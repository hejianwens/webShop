package com.project.webshop.buycar.serviceImpl;

import com.project.webshop.buycar.feign.GoodsFeignService;
import com.project.webshop.buycar.mapper.BuyCarMapper;
import com.project.webshop.buycar.util.RedisUtil;
import com.project.webshop.service.buycar.BuyCarService;
import common.model.Result;
import common.model.buyCar.BuyCar;
import common.model.goods.Goods;
import common.model.user.Customer;
import common.utils.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@Transactional
public class BuyCarServiceImpl implements BuyCarService {
    @Autowired
    BuyCarMapper buyCarMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    GoodsFeignService goodsFeignService;


    public Result findBuyCarById(BuyCar buyCar,String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        buyCar.setCustomerId(customer.getId());
        List<BuyCar> buyCarList=buyCarMapper.findBuyCarList(buyCar);
        //这里一个流程检测购物车商品是否被逻辑删除了、库存是否充足
        for(BuyCar b:buyCarList){
            //检测购物车商品是否被逻辑删除了
            if(b.getGoods().getIsDelete().equals("1")){
                b.setIsDelete("1");
            }
            if(b.getGoods().getStockAmount().compareTo(new BigDecimal("0"))==0||b.getGoods().getStockAmount().compareTo(new BigDecimal("0"))==-1){
                b.setIsStockEnough("0");
            }
        }

        result.setCode("200");
        result.setMessage("成功");
        result.setData(buyCarList);
        return result;
    }

    public Result insert(BuyCar buyCar,String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        buyCar.setCustomerId(customer.getId());
        //先检查前端上送的商品id是否存在
        Goods selectGoods=new Goods();
        selectGoods.setId(buyCar.getGoodsId());
        Result selectResult=goodsFeignService.findGoodsInfo(selectGoods);
        if(!"200".equals(selectResult.getCode())){
            result.setCode("200");
            result.setMessage("失败，远程调用商品系统错误");
            result.setData(null);
            return result;
        }
        Goods goods= (Goods) result.getData();
        if(null==goods||"1".equals(goods.getIsDelete())){
            result.setCode("200");
            result.setMessage("失败，商品不存在或者已经被删除");
            result.setData(null);
            return result;
        }
        //检查表里是否有同样的
        BuyCar selectBuyCar=buyCarMapper.findBuyCarByGoodsId(buyCar);
        //有就直接加数量
        if(selectBuyCar==null){
            insertNewItem(buyCar,loginKey);
        } else {
            insertOldItem(selectBuyCar,buyCar,loginKey);
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;


    }


    private void insertOldItem(BuyCar selectBuyCar, BuyCar buyCar,String loginKey) {
        BigDecimal finaAmount=selectBuyCar.getAmount().add(buyCar.getAmount());
        selectBuyCar.setAmount(finaAmount);
        selectBuyCar.setSubtotal(selectBuyCar.getOnePrice().multiply(finaAmount));
        selectBuyCar.setCustomerId(buyCar.getCustomerId());
        buyCarMapper.updateAmountAndSubtotal(selectBuyCar);
    }

    private void insertNewItem(BuyCar buyCar,String loginKey) {
        BuyCar insertBuyCar=new BuyCar();
        BigDecimal subtotal=buyCar.getOnePrice();
        subtotal=subtotal.multiply(buyCar.getAmount());

        insertBuyCar.setSubtotal(subtotal);
        insertBuyCar.setAmount(buyCar.getAmount());
        insertBuyCar.setGoodsId(buyCar.getGoodsId());
        insertBuyCar.setOnePrice(buyCar.getOnePrice());
        insertBuyCar.setCustomerId(buyCar.getCustomerId());
        buyCarMapper.insert(insertBuyCar);
    }

    @Override
    public Result delete(List<BuyCar> buyCars,String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        try{
            for(BuyCar buyCar:buyCars){
                BuyCar selectBuyCar=buyCarMapper.selectById(buyCar.getId());
                if(null==selectBuyCar){
                    result.setCode("500");
                    result.setMessage("删除购物车失败");
                    result.setData(null);
                    return result;
                }
            }
            buyCarMapper.delete(buyCars);
        }catch (Exception e){
            result.setCode("500");
            result.setMessage("删除购物车失败");
            result.setData(null);
            return result;
        }

        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    public Result addOneAmount(BuyCar buyCar,String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        //计算小计的价钱
        BigDecimal amount=buyCar.getAmount();
        BigDecimal subtotal=buyCar.getSubtotal();

        amount=amount.add(new BigDecimal("1"));
        subtotal=subtotal.add(buyCar.getOnePrice());

        buyCar.setSubtotal(subtotal);
        buyCar.setAmount(amount);
        buyCarMapper.updateAmountAndSubtotal(buyCar);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    public Result subtractOneAmount(BuyCar buyCar,String loginKey) {
        Result result;
        Customer customer;
        try {
            customer= (Customer) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        //计算小计的价钱
        BigDecimal amount=buyCar.getAmount();
        BigDecimal subtotal=buyCar.getSubtotal();

        amount=amount.subtract(new BigDecimal("1"));
        subtotal=subtotal.subtract(buyCar.getOnePrice());

        buyCar.setSubtotal(subtotal);
        buyCar.setAmount(amount);
        buyCarMapper.updateAmountAndSubtotal(buyCar);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    public Result deleteByInnerService(List<BuyCar> buyCars) {
        Result result=new Result();
        try{

            for(BuyCar buyCar:buyCars){
                BuyCar selectBuyCar=buyCarMapper.selectById(buyCar.getId());
                if(null==selectBuyCar){
                    result.setCode("500");
                    result.setMessage("删除购物车失败");
                    result.setData(null);
                    return result;
                }
            }
            buyCarMapper.delete(buyCars);
        }catch (Exception e){
            result.setCode("500");
            result.setMessage("删除购物车失败");
            result.setData(null);
            return result;
        }

        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }
}
