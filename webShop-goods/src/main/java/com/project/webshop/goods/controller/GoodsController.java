package com.project.webshop.goods.controller;




import com.project.webshop.goods.serviceImpl.GoodsServiceImpl;
import common.model.QueryParams;
import common.model.Result;
import common.model.goods.Goods;
import common.model.order.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/goods")
public class GoodsController {
    @Autowired
    GoodsServiceImpl goodsServiceImpl;

    //通过名字搜商品
    @RequestMapping(value = "/findGoodsListByName",method = RequestMethod.GET)
    public Result findGoodsListByName(QueryParams qps, Goods goods,HttpServletRequest request){
        String loginKey=new String("");
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if("loginKey".equals(cookie.getName())){
                loginKey=cookie.getValue();
            }
        }

        Result result=goodsServiceImpl.findGoodsListByName(qps,goods,loginKey);
        return result;
    }

    //通过类别搜商品
    @RequestMapping(value = "/findGoodsListByKind",method = RequestMethod.GET)
    public Result findGoodsListByKind(QueryParams qps, Goods goods, HttpServletRequest request){
        String loginKey=new String("");
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if("loginKey".equals(cookie.getName())){
                loginKey=cookie.getValue();
            }
        }
        Result result=goodsServiceImpl.findGoodsListByKind(qps,goods,loginKey);
        return result;
    }

    //删除商品
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result delete(Goods goods,@CookieValue("loginKey")String loginKey){
        Result result=goodsServiceImpl.delete(goods,loginKey);
        return result;
    }

    //新增商品
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public Result insert(@RequestBody Goods goods,@CookieValue("loginKey")String loginKey){
        Result result=goodsServiceImpl.insert(goods,loginKey);
        return result;
    }

    //上传图片
    @RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
    public Result uploadImage(@RequestParam("file") MultipartFile multipartFile,BigDecimal id,@CookieValue("loginKey")String loginKey) throws IOException {
        Result result=goodsServiceImpl.uploadImage(multipartFile,id,loginKey);
        return result;
    }

    //商户获得商品列表
    @RequestMapping(value = "/findGoodsListByShopHost",method = RequestMethod.GET)
    public Result findGoodsListByShopHost(QueryParams qps, Goods goods,@CookieValue("loginKey")String loginKey){
        Result result=goodsServiceImpl.findGoodsListByShopHost(qps,goods,loginKey);
        return result;
    }

    //更新商品信息
    @RequestMapping(value = "/updateGoodsInfo",method = RequestMethod.POST)
    public Result updateGoodsInfo(@RequestBody Goods goods,@CookieValue("loginKey") String loginKey){
        Result result=goodsServiceImpl.updateGoodsInfo(goods,loginKey);
        return result;
    }

    @RequestMapping(value = "/updateGoodsInfo",method = RequestMethod.GET)
    public Result deleteEvaluate(Integer id,@CookieValue("loginKey") String loginKey){
        Result result=goodsServiceImpl.deleteEvaluate(id,loginKey);
        return result;
    }


    //更新库存，主要给订单服务调用
    @RequestMapping(value = "/orderUpdateStockAmount",method = RequestMethod.POST)
    public Result orderUpdateStockAmount(@RequestBody List<OrderItem>orderItems){
        Result result=goodsServiceImpl.orderUpdateStockAmount(orderItems);
        return result;
    }

    //获得商品种类枚举
    @RequestMapping(value = "/getGoodKinds",method = RequestMethod.GET)
    public Result getGoodKind(){
        Result result=goodsServiceImpl.getGoodKinds();
        return result;
    }

    //获得商品详细信息
    @RequestMapping(value = "/findGoodsInfo",method = RequestMethod.GET)
    public Result findGoodsInfo(Goods goods){
        Result result=goodsServiceImpl.findGoodsInfo(goods);
        return result;
    }

    //寻找商品的评价
    @RequestMapping(value = "/findGoodsInfoEvaluates",method = RequestMethod.GET)
    public Result findGoodsInfoEvaluates(BigDecimal id){
        Result result=goodsServiceImpl.findGoodsInfoEvaluates(id);
        return result;
    }




}
