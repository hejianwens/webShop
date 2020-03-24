package com.project.webshop.goods.utils;


import com.project.webshop.goods.mapper.GoodsMapper;
import common.model.goods.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class GetGoodsBySetUtil {
    @Autowired
    private GoodsMapper goodsMapper;

    public Set<BigDecimal>countMainMethod(Map<BigDecimal, Set<BigDecimal>> userRelationshipGoods, Map<BigDecimal, Set<BigDecimal>> targetUserRelationshipGoods, Map<BigDecimal, BigDecimal> topTenUserSimilarList){
        List<Goods>recommendGoods=new ArrayList<Goods>();
        //拿到目标用户喜欢商品集合
        Set<BigDecimal>targetSet=null;
        for(Map.Entry<BigDecimal, Set<BigDecimal>> entry:targetUserRelationshipGoods.entrySet()){
            targetSet=targetUserRelationshipGoods.get(entry.getKey());
        }
        if(targetSet==null||targetSet.size()<=0){
            return null;
        }
        //获得前十名用户全部商品集合
        Set<BigDecimal>topTenUserSimilarGoodsIdSet=getTopTenUserSimilarGoodsIdSet(userRelationshipGoods,topTenUserSimilarList);
        if(null==topTenUserSimilarGoodsIdSet){
            return null;
        }
        //去除目标用户所喜欢的
        topTenUserSimilarGoodsIdSet=removeTargetLikeGoods(targetSet,topTenUserSimilarGoodsIdSet);
        return topTenUserSimilarGoodsIdSet;
    }

    public List<Goods> getGoodsList(Set<BigDecimal> topTenUserSimilarGoodsIdSet) {
        List<Goods>recommendGoods=new ArrayList<Goods>();
        for(BigDecimal goodsId:topTenUserSimilarGoodsIdSet){
            Goods g=new Goods();
            g.setId(goodsId);
            Goods goods=goodsMapper.findGoodsInfo(g);
            Integer integer= goodsMapper.selectGoodsEvaluationCount(goods.getId());
            if(integer==null){
                integer=new Integer("0");
            }
            goods.setEvaluateCount(integer);
            recommendGoods.add(goods);
        }
        return recommendGoods;
    }

    private Set<BigDecimal> removeTargetLikeGoods(Set<BigDecimal> targetSet, Set<BigDecimal> topTenUserSimilarGoodsIdSet) {

        if(topTenUserSimilarGoodsIdSet==null||topTenUserSimilarGoodsIdSet.size()<=0){
            return null;
        }
        Set<BigDecimal> returnTopTenUserSimilarGoodsIdSet = new HashSet<BigDecimal>(topTenUserSimilarGoodsIdSet);
        for(BigDecimal similarGoodsId:topTenUserSimilarGoodsIdSet){
            for(BigDecimal targetGoodsId:targetSet){
                if(similarGoodsId.compareTo(targetGoodsId)==0){
                    returnTopTenUserSimilarGoodsIdSet.remove(similarGoodsId);
                }
            }
        }
        return returnTopTenUserSimilarGoodsIdSet;
    }

    private Set<BigDecimal> getTopTenUserSimilarGoodsIdSet(Map<BigDecimal, Set<BigDecimal>> userRelationshipGoods, Map<BigDecimal, BigDecimal> topTenUserSimilarList) {
        Set<BigDecimal>topTenUserSimilarGoodsIdSet=new HashSet<BigDecimal>();
        if(topTenUserSimilarList==null||topTenUserSimilarList.size()<=0){
            return null;
        }
        BigDecimal zero=new BigDecimal("0");
        for(Map.Entry<BigDecimal,BigDecimal> userIdKey:topTenUserSimilarList.entrySet()){
            if(zero.compareTo(topTenUserSimilarList.get(userIdKey.getKey()))!=0){
                for(Map.Entry<BigDecimal, Set<BigDecimal>> entry:userRelationshipGoods.entrySet()){
                    if(userIdKey.getKey().compareTo(entry.getKey())==0){
                        Set<BigDecimal>middleSet=userRelationshipGoods.get(entry.getKey());
                        topTenUserSimilarGoodsIdSet.addAll(middleSet);
                    }
                }
            }

        }
        return topTenUserSimilarGoodsIdSet;
    }
}
