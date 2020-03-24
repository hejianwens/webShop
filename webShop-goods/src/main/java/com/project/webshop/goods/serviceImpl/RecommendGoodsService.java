package com.project.webshop.goods.serviceImpl;


import com.project.webshop.goods.mapper.GoodsMapper;
import com.project.webshop.goods.utils.GetGoodsBySetUtil;
import com.project.webshop.goods.utils.RedisUtil;
import com.project.webshop.goods.utils.GetSimilarListUtil;
import common.model.Result;
import common.model.goods.Goods;
import common.model.goods.RecommendGoodsStructure;
import common.model.goods.SimilarCustomersLikeGoods;
import common.model.user.Customer;
import common.utils.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class RecommendGoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    GetGoodsBySetUtil getGoodsBySetUtil;
    @Autowired
    GetSimilarListUtil getSimilarListUtil;

    public Result getRecommendGoods(String loginKey) {

        Result result;
        Customer customer= (Customer) redisUtil.get(loginKey);
        result= CheckLogin.checkCustomerLogin(customer,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        List<Goods>goodsList=null;
        Set<BigDecimal>topTenUserSimilarGoodsIdSet;
        //先看看订单子项表和评论表有没有达到要求，如果没有则直接按照平时浏览爱好推介,是从redis拿
        BigDecimal unSameCustomerEvaluationCount=goodsMapper.selectUnSameCustomerEvaluationCount();
        BigDecimal unSameGoodsEvaluationCount=goodsMapper.selectUnSameGoodsEvaluationCount();
        BigDecimal thirty=new BigDecimal("29");
        BigDecimal one=new BigDecimal("100");
        if(unSameCustomerEvaluationCount.compareTo(thirty)!=-1&&unSameGoodsEvaluationCount.compareTo(one)!=-1){
            //能从Redis拿证明已经经过计算，而且这个设置了一天的过期时间
            String topTenUserSimilarGoodsIdSetKey=loginKey+"TopTenUserSimilarGoodsIdSet";
            SimilarCustomersLikeGoods s= (SimilarCustomersLikeGoods) redisUtil.get(topTenUserSimilarGoodsIdSetKey);
            if(s!=null&&s.getTopTenUserSimilarGoodsIdSet().size()>10){
                topTenUserSimilarGoodsIdSet=s.getTopTenUserSimilarGoodsIdSet();
                goodsList= getGoodsBySetUtil.getGoodsList(topTenUserSimilarGoodsIdSet);
                goodsList=getRandomListByList(goodsList);
                result.setCode("200");
                result.setData(goodsList);
                return  result;
            }
            else {
                goodsList=getRandomRecommendGoodsListByRedis(loginKey);
                result.setCode("200");
                result.setMessage("成功");
                result.setData(goodsList);
                return  result;
            }

        }
        else {
            goodsList=getRandomRecommendGoodsListByRedis(loginKey);
            result.setCode("200");
            result.setMessage("成功");
            result.setData(goodsList);
            return  result;

        }
    }

    private List<Goods> getRandomRecommendGoodsListByRedis(String loginKey) {
        List<Goods>goodsList=null;
        //如果没有则直接按照客户idRedis拿平时浏览商品的类,这个类是如果用户用过商品搜索功能，所找的商品id信息就会被放进这个类
        String key=loginKey+"RedisGoodsList";
        RecommendGoodsStructure recommendGoodsStructure= (RecommendGoodsStructure) redisUtil.get(key);
        //如果拿不到则说明没有搜索过，则随机推介
        if(recommendGoodsStructure==null){
            //随机推介
            goodsList=doRandomGoods();
        }
        else {
            Set<BigDecimal>goodsIdSet=recommendGoodsStructure.getGoodsIdSet();
            //如果浏览得少，也是随机推介
            if(goodsIdSet==null||goodsIdSet.size()<100){
                //随机推介
                goodsList=doRandomGoods();
            }
            else {
                //从redis里面随机拿10个商品返回
                goodsList=recommendByRedisGoodsSet(goodsIdSet);
            }
        }
        return goodsList;

    }

    private List<Goods> recommendByRedisGoodsSet(Set<BigDecimal>goodsIdSet) {
        List<Goods>goodsList=new ArrayList<Goods>();
        int size=goodsIdSet.size();
        BigDecimal[] goodsIdArray=new BigDecimal[size];
        //把set转化成数组
        int index=0;
        for(BigDecimal selectId:goodsIdSet){
            goodsIdArray[index]=selectId;
            index++;
        }
        int[] randomArray=new int[size];
        //产生10个随机商品id
        Random random = new Random();
        for(int i=0;i<10;i++){
            randomArray[i]=random.nextInt(size);
        }
        //根据set中得goodsId查找商品
        for(int i=0;i<10;i++){
            BigDecimal selectId=goodsIdArray[randomArray[i]];
            Goods goods=new Goods();
            goods.setId(selectId);
            goods=goodsMapper.findGoodsInfo(goods);
            goodsList.add(goods);
        }
        goodsList=setEvaluateCount(goodsList);
        return  goodsList;
    }

    private List<Goods> doRandomGoods() {
        Goods goods=new Goods();
        List<Goods>goodsList=goodsMapper.findGoodsList(goods,null);
        //如果得到全部商品的数量<10的直接返回
        if(goodsList==null||goodsList.size()<10){
            if(goodsList.size()>0){
                goodsList=setEvaluateCount(goodsList);
            }
            return goodsList;
        }
        else {
            //如果得到全部商品的数量>10的随机返回
            goodsList=setEvaluateCount(goodsList);
            List<Goods> randomGoodsList=getRandomListByList(goodsList);
            return randomGoodsList;
        }
    }

    private List<Goods> setEvaluateCount(List<Goods> goodsList) {
        for(Goods g:goodsList){
            Integer integer= goodsMapper.selectGoodsEvaluationCount(g.getId());
            if(integer==null){
                integer=new Integer("0");
            }
            g.setEvaluateCount(integer);
        }
        return goodsList;
    }

    private List<Goods> getRandomListByList(List<Goods> goodsList) {
        List<Goods>resGoodsList=new ArrayList<Goods>();
        Set<Integer>indexes=new HashSet<Integer>();
        int size=goodsList.size();
        Random random = new Random();
        if(goodsList==null||goodsList.size()<10){
            return goodsList;
        }
        while (indexes.size()<10){
            indexes.add(random.nextInt(size));

        }
        for(Integer goodsId:indexes){
            Goods goods=goodsList.get(goodsId);
            resGoodsList.add(goods);
        }

        return resGoodsList;
    }
}
