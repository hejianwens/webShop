package com.project.webshop.goods.utils;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class GetSimilarListUtil {

    public Map<BigDecimal, BigDecimal> countMainMethod(Map<BigDecimal, Set<BigDecimal>> userRelationshipGoods,Map<BigDecimal, Set<BigDecimal>> targetUserRelationshipGoods){
        if(userRelationshipGoods==null||userRelationshipGoods.size()<10){
            //如果用户喜欢商品列表维度小于10或者没有，直接返回空值
            return null;
        }
        if(targetUserRelationshipGoods==null){
            //如果目标用户列表维度小于10或者没有，直接返回空值
            return null;
        }
        //得到相似表
        BigDecimal[][] similarUserList=getSimilarList(userRelationshipGoods,targetUserRelationshipGoods);
        //快速排序
        similarUserList=sortList(similarUserList,0,similarUserList.length-1);
        if(null==similarUserList){
            return null;
        }
         //取前十
        Map<BigDecimal, BigDecimal>topTenUserSimilarList=new HashMap<BigDecimal,BigDecimal>();
        BigDecimal zero=new BigDecimal("0");
        for(int i=0;i<10;i++){
            topTenUserSimilarList.put(similarUserList[i][0],similarUserList[i][1]);
        }
        return topTenUserSimilarList;
    }


    private static BigDecimal[][] sortList(BigDecimal[][] similarUserList,int low,int high) {
        //利用快排算法，对相似度进行排序
        int i,j;
        BigDecimal temp=null;
        BigDecimal t0=null;
        if(low>high){
            return null;
        }
        i=low;//左边哨兵的索引
        j=high;//右边哨兵的索引
        temp = similarUserList[low][1];
        t0=similarUserList[low][0];
        while (i<j) {
            while (temp.compareTo(similarUserList[j][1])!=-1&&i<j){
                j--;
            }
            while (temp.compareTo(similarUserList[i][1])!=1&&i<j){
                i++;
            }
            if (i<j) {
                BigDecimal userIdI=similarUserList[i][0];
                BigDecimal userIdJ=similarUserList[j][0];;
                BigDecimal similarUserI=similarUserList[i][1];
                BigDecimal similarUserJ=similarUserList[j][1];

                similarUserList[j][0]=userIdI;
                similarUserList[i][0]=userIdJ;
                similarUserList[j][1]=similarUserI;
                similarUserList[i][1]=similarUserJ;
            }
        }
        //这时 跳出了 “while (i<j) {}” 循环,说明 i=j 左右在同一位置
        //最后将基准为与i和j相等位置的数字交换
        similarUserList[low][0]= similarUserList[j][0];
        similarUserList[low][1]= similarUserList[j][1];
        similarUserList[i][0]=t0;
        similarUserList[i][1]=temp;

        //递归调用左半数组
        sortList(similarUserList, low, j-1);
        //递归调用右半数组
        sortList(similarUserList, j+1, high);
        return similarUserList;
    }

    private BigDecimal[][] getSimilarList(Map<BigDecimal, Set<BigDecimal>> userRelationshipGoods,Map<BigDecimal, Set<BigDecimal>> targetUserRelationshipGoods) {
        int rowSum=userRelationshipGoods.size();
        BigDecimal[][] similarUserList=new BigDecimal[rowSum][2];
        BigDecimal one=new BigDecimal("1");
        BigDecimal zero=new BigDecimal("0");
        int i=0;
        for(Map.Entry<BigDecimal, Set<BigDecimal>> entry:userRelationshipGoods.entrySet()){
            similarUserList[i][0]=entry.getKey();
            i++;
        }
        for(int columnIndex=0;columnIndex<rowSum;columnIndex++){
            //拿到比较用户的商品集合
            Set<BigDecimal>compareSet=userRelationshipGoods.get(similarUserList[columnIndex][0]);
            //目标用户集合引用
            Set<BigDecimal>targetSet=null;
            //相似商品数
            BigDecimal similarItem=new BigDecimal("0");
            //余弦值
            BigDecimal cosNum=null;
            //拿到目标集合内容
            for(Map.Entry<BigDecimal, Set<BigDecimal>> entry:targetUserRelationshipGoods.entrySet()){
                targetSet=targetUserRelationshipGoods.get(entry.getKey());
            }
            //遍历比较用户喜欢商品和每个目标用户的商品
            for(BigDecimal compareGoodsId:compareSet){
                for(BigDecimal targetGoodsId:targetSet){
                    if(compareGoodsId.compareTo(targetGoodsId)==0){
                        similarItem=similarItem.add(one);
                    }
                }
            }
            //如果两个集合的相似商品如果为零，直接为零
            if(similarItem.compareTo(new BigDecimal("0"))==0){
                similarUserList[columnIndex][1]=zero;
            }else {
                //不为零，进行余弦值计算
                BigDecimal compareSetSize=new BigDecimal(userRelationshipGoods.get(similarUserList[columnIndex][0]).size());
                BigDecimal targetSetSize=new BigDecimal(targetSet.size());
                double sqrt = Math.sqrt(compareSetSize.multiply(targetSetSize).doubleValue());
                BigDecimal denominator=new BigDecimal(sqrt);
                cosNum=similarItem.divide(denominator, 6, RoundingMode.HALF_UP);
                similarUserList[columnIndex][1]=cosNum;
            }
        }
        return similarUserList;
    }



}
