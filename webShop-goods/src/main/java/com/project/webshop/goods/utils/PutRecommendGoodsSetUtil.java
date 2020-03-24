package com.project.webshop.goods.utils;

import com.project.webshop.goods.mapper.GoodsMapper;
import common.model.goods.SimilarCustomersLikeGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PutRecommendGoodsSetUtil {

    @Autowired
    GetSimilarListUtil getSimilarListUtil;

    @Autowired
    GetGoodsBySetUtil getGoodsBySetUtil;

    @Autowired
    RedisUtil redisUtil;

    public void putUserSimilarGoodsSet(Map<BigDecimal, Set<BigDecimal>> customerLikeList, List<BigDecimal> customerIdList) {
        for(BigDecimal customerId:customerIdList){
            setUserSimilarGoodIntoRedis(customerId,customerLikeList);
        }
    }

    private void setUserSimilarGoodIntoRedis(BigDecimal customerId, Map<BigDecimal, Set<BigDecimal>> customerLikeList) {
        Map<BigDecimal,Set<BigDecimal>>targetCustomerLikeMap=new HashMap<BigDecimal,Set<BigDecimal>>();
        //拿到目标用户喜欢的
        targetCustomerLikeMap.put(customerId,customerLikeList.get(customerId));
        //剔除目标用户喜欢商品列表
        customerLikeList.remove(customerId);
        //用用户过滤工具拿到相似度前十的用户列表
        Map<BigDecimal, BigDecimal> topTenUserSimilarList= getSimilarListUtil.countMainMethod(customerLikeList,targetCustomerLikeMap);
        if(null==topTenUserSimilarList){
            return;
        }
        //用工具取前十的用户喜欢商品id列表，会自动剔除目标用户喜欢的商品
        Set<BigDecimal>topTenUserSimilarGoodsIdSet= getGoodsBySetUtil.countMainMethod(customerLikeList,targetCustomerLikeMap,topTenUserSimilarList);
        String storeKey="customer"+customerId.toString()+"TopTenUserSimilarGoodsIdSet";
        SimilarCustomersLikeGoods similarCustomersLikeGoods =new SimilarCustomersLikeGoods();
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        similarCustomersLikeGoods.setCreateTime(timestamp);
        similarCustomersLikeGoods.setTopTenUserSimilarGoodsIdSet(topTenUserSimilarGoodsIdSet);
        redisUtil.set(storeKey,similarCustomersLikeGoods,60*60*24);
    }


}
