package com.project.webshop.goods.utils;

import com.project.webshop.goods.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
//开启定时任务
@EnableScheduling
public class RecommendUtil implements ApplicationRunner {

    @Autowired
    private  GoodsMapper goodsMapper;

    @Autowired
    PutRecommendGoodsSetUtil putRecommendGoodsSetUtil;

    private Map<BigDecimal, Set<BigDecimal>> customerLikeList;

    private List<BigDecimal>customerIdList;

    private void putUserLikeList(){
        customerLikeList=new HashMap<BigDecimal, Set<BigDecimal>>();
        for(BigDecimal customerId:customerIdList){
             List<BigDecimal>goodsList=goodsMapper.getCustomerLikeGoods(customerId);
             Set<BigDecimal> goodsIdSet = new HashSet<>(goodsList);
             customerLikeList.put(customerId,goodsIdSet);
         }
    }

    //每24小时进行一次学习
    @Scheduled(cron  = "0 0 0/24 * * ?")
    private void recommend(){
        //拿到全部用户集合
        customerIdList=goodsMapper.getAllCustomerId();
        //读取所有用户喜欢的商品集合
        putUserLikeList();
        //把每个用户相似的用户的商品集合放进Redis
        putRecommendGoodsSetUtil.putUserSimilarGoodsSet(customerLikeList,customerIdList);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        BigDecimal unSameCustomerEvaluationCount=goodsMapper.selectUnSameCustomerEvaluationCount();
        BigDecimal unSameGoodsEvaluationCount=goodsMapper.selectUnSameGoodsEvaluationCount();
        BigDecimal thirty=new BigDecimal("30");
        BigDecimal one=new BigDecimal("100");
        if(unSameCustomerEvaluationCount.compareTo(thirty)!=-1&&unSameGoodsEvaluationCount.compareTo(one)!=-1){
            //达到限制条件启动推介计算
            recommend();
        }

    }
}
