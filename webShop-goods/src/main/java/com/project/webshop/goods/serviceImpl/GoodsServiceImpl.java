package com.project.webshop.goods.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.project.webshop.goods.mapper.GoodsMapper;
import com.project.webshop.goods.utils.RedisUtil;
import com.project.webshop.service.goods.GoodsService;
import com.project.webshop.goods.utils.PageUtils;
import common.model.PageResult;
import common.model.QueryParams;
import common.model.Result;
import common.model.goods.Goods;
import common.model.goods.GoodsInfoEvaluate;
import common.model.goods.GoodsKind;
import common.enums.GoodsKindEnum;
import common.model.goods.RecommendGoodsStructure;
import common.model.order.GoodsEvaluate;
import common.model.order.OrderItem;
import common.model.user.Customer;
import common.model.user.Shop;
import common.model.user.SuperManager;
import common.utils.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Component
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;
//    private Logger logger = LoggerFactory.getLogger(AssetredemptionController.class);
    @Autowired
    RedisUtil redisUtil;

    public Result findGoodsListByName(QueryParams qps, Goods goods,String loginKey) {
        Result result=new Result();
        //储存商品类别,懒加载，用来接收商品名称联想
        List<String>goodKinds=getKinds(goods.getName());
        PageHelper.startPage(qps.getPage(),qps.getRows());
        List<Goods> goodsList=goodsMapper.findGoodsList(goods,goodKinds);
        if(!"".equals(loginKey)){
            Customer customer=null;
            try {
                customer= (Customer) redisUtil.get(loginKey);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(null!=customer){
                doRecommendStore(loginKey,goodsList);
            }
        }
        goodsList=setEvaluateCount(goodsList);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
        PageResult pageResult = PageUtils.getPageResult(qps,pageInfo);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(pageResult);
        return result;
    }

    private List<Goods> setEvaluateCount(List<Goods> goodsList) {
        if(goodsList==null||goodsList.size()<=0){
            return goodsList;
        }
        for(Goods g:goodsList){
            Integer integer= goodsMapper.selectGoodsEvaluationCount(g.getId());
            if(integer==null){
                integer=new Integer("0");
            }
            g.setEvaluateCount(integer);
        }
        return goodsList;
    }


    public Result findGoodsListByKind(QueryParams qps, Goods goods,String loginKey) {
        Result result=new Result();
        List<String>goodKinds=null;
        PageHelper.startPage(qps.getPage(),qps.getRows());
        List<Goods> goodsList=goodsMapper.findGoodsList(goods,goodKinds);
        if(!"".equals(loginKey)){
            Customer customer=null;
            try {
                customer= (Customer) redisUtil.get(loginKey);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(null!=customer){
                doRecommendStore(loginKey,goodsList);
            }
        }
        goodsList=setEvaluateCount(goodsList);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
        PageResult pageResult = PageUtils.getPageResult(qps,pageInfo);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(pageResult);
        return result;
    }


    public Result findGoodsInfo(Goods goods) {
        Result result=new Result();
        Goods resGoods=goodsMapper.findGoodsInfo(goods);
        if(null==resGoods||"1".equals(resGoods.getIsDelete())){
            result.setCode("500");
            result.setMessage("商品不存在，或者已经被删除");
            result.setData(resGoods);
        }
        Integer integer= goodsMapper.selectGoodsEvaluationCount(resGoods.getId());
        if(integer==null){
            integer=new Integer("0");
        }
        resGoods.setEvaluateCount(integer);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(resGoods);
        return result;
    }


    public Result delete(Goods goods, String loginKey) {
        Result result;
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        goodsMapper.delete(goods);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;

    }



    @Override
    public Result orderUpdateStockAmount(List<OrderItem> orderItems) {
        Result result=new Result();
        for(OrderItem orderItem:orderItems){
            Goods goods=new Goods();
            goods.setId(orderItem.getGoodsId());
            Goods g=goodsMapper.selectStockAmountAndSoldAmount(goods);

            if (g==null){
                result.setCode("500");
                result.setMessage("数据异常");
                result.setData(null);
                return result;
            }
            BigDecimal stockAmount=g.getStockAmount();
            BigDecimal soldAmount=g.getSoldAmount();
            stockAmount=stockAmount.subtract(orderItem.getAmount());
            soldAmount=soldAmount.add(orderItem.getAmount());
            goods.setStockAmount(stockAmount);
            goods.setSoldAmount(soldAmount);
            goodsMapper.updateGoodsInfo(goods);
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }



    public Result insert(Goods goods,String loginKey) {
        Result result=new Result();
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        goods.setShopId(shop.getId());
        goodsMapper.insert(goods);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }


    public Result getGoodKinds() {
        Result result=new Result();
        List<GoodsKind>goodsKinds=new ArrayList<GoodsKind>();
        List<GoodsKindEnum>goodsKindEnums=GoodsKindEnum.getGoodsKindEnums();

        for(GoodsKindEnum goodsKindEnum:goodsKindEnums){
            GoodsKind goodsKind=new GoodsKind();
            goodsKind.setName(goodsKindEnum.getName());
            goodsKind.setValue(goodsKindEnum.getValue());
            goodsKinds.add(goodsKind);
        }
        result.setCode("200");
        result.setMessage("成功");
        result.setData(goodsKinds);
        return result;
    }


    public Result uploadImage(MultipartFile multipartFile, BigDecimal id, String loginKey) throws IOException {
        Result result;
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        String multipartFileName=multipartFile.getOriginalFilename();
        String suffix=multipartFileName.substring(multipartFileName.indexOf("."));
        String filePath=this.getClass().getResource("/static/goodsImages/").getPath();
        String fileName;
        if(null==id){
            fileName=goodsMapper.getGoodsMaxId().toString()+suffix;
        }else {
            File file=getExistsFilePathByName(id);
            if(file==null){
                result.setCode("500");
                result.setMessage("更新图片失败,找到不到原来图片,系统出错");
                return result;
            }
            if(file.exists()){
                boolean deleteFlag=file.delete();
                if(!deleteFlag){
                    result.setCode("500");
                    result.setMessage("更新图片失败,系统出错");
                    return result;
                }
            }
            fileName=id.toString()+suffix;
        }
        String resFilePath="http://localhost:8003/goodsImages/"+fileName;
        File file=new File(filePath+fileName);
        multipartFile.transferTo(file);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(resFilePath);
        return result;
    }

    public Result findGoodsListByShopHost(QueryParams qps, Goods goods, String loginKey) {
        Result result;
        List<String>goodKinds=null;
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }

        result=CheckLogin.checkShopLoginKey(shop,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        PageHelper.startPage(qps.getPage(),qps.getRows());
        goods.setShopId(shop.getId());
        if(!"".equals(goods.getName())){
            goodKinds=getKinds(goods.getName());
        }
        List<Goods> goodsList=goodsMapper.findGoodsList(goods,goodKinds);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
        PageResult pageResult = PageUtils.getPageResult(qps,pageInfo);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(pageResult);
        return result;
    }



    public Result updateGoodsInfo(Goods goods, String loginKey) {
        Result result;
        Shop shop;
        try {
            shop= (Shop) redisUtil.get(loginKey);
        }catch (Exception e){
            result=new Result();
            result.setCode("500");
            result.setData("权限出错，登录信息错误");
            return  result;
        }
        result= CheckLogin.checkShopLoginKey(shop, loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        goodsMapper.updateGoodsInfo(goods);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(null);
        return result;
    }

    private File getExistsFilePathByName(BigDecimal id) {
        File file=new File(this.getClass().getResource("/static/goodsImages/").getPath());
        if(file.exists()){
            File []files=file.listFiles();
            for(File f:files){
                String fileName=f.getPath();
                if(fileName.contains(id.toString())){
                    return f;
                }
            }
        }
        return null;
    }

    private synchronized void doRecommendStore(String loginKey,List<Goods> goodsList) {
        if(goodsList==null||goodsList.size()<=0){
            return;
        }
        String key=loginKey+"RedisGoodsList";
        RecommendGoodsStructure recommendGoodsStructure= (RecommendGoodsStructure) redisUtil.get(key);
        Set<BigDecimal>goodsIdSet=null;
        if(recommendGoodsStructure==null){
            goodsIdSet=new HashSet<BigDecimal>();
            RecommendGoodsStructure recommendGoodsStructureNew=new RecommendGoodsStructure();
            recommendGoodsStructureNew.setCreateTime(new Date());
            for(Goods g:goodsList){
                goodsIdSet.add(g.getId());
            }
            recommendGoodsStructureNew.setGoodsIdSet(goodsIdSet);
            redisUtil.set(key,recommendGoodsStructureNew,60*60*24);
        }else {
            goodsIdSet=recommendGoodsStructure.getGoodsIdSet();
            for(Goods g:goodsList){
                goodsIdSet.add(g.getId());
            }
            recommendGoodsStructure.setGoodsIdSet(goodsIdSet);
            redisUtil.set(key,recommendGoodsStructure);
        }
    }

    //这个方法经常被调用，所以设置为static，减少系统开销和相应时间
    private static List<String> getKinds(String name) {
        List<String>goodKinds=null;
        //这是一个开关记录智能处理名字是否能匹配上商品类别
        boolean containKindFlag=false;
        List<GoodsKindEnum>goodsKindEnums=GoodsKindEnum.getGoodsKindEnums();
        for(GoodsKindEnum goodsKindEnum:goodsKindEnums){
            for(int i=0;i<name.length();i++){
                containKindFlag=goodsKindEnum.getName().contains(String.valueOf(name.charAt(i)));
            }
            if(containKindFlag){
                if(null==goodKinds){
                    goodKinds=new ArrayList<String>();
                }
                goodKinds.add(goodsKindEnum.getValue());
            }
        }
        return goodKinds;
    }


    public Result findGoodsInfoEvaluates(BigDecimal id) {
        Result result=new Result();
        GoodsInfoEvaluate goodsInfoEvaluate=new GoodsInfoEvaluate();
        List<GoodsEvaluate>goodsEvaluates=goodsMapper.findGoodsInfoEvaluates(id);
        BigDecimal avgEvaluate=goodsMapper.findGoodsInfoAvgEvaluate(id);
        avgEvaluate=avgEvaluate.setScale(1, BigDecimal.ROUND_HALF_UP);
        goodsInfoEvaluate.setAvgEvaluate(avgEvaluate);
        goodsInfoEvaluate.setEvaluates(goodsEvaluates);
        result.setCode("200");
        result.setMessage("成功");
        result.setData(goodsInfoEvaluate);
        return result;
    }

    public Result deleteEvaluate(Integer id, String loginKey) {
        Result result;
        SuperManager superManager= (SuperManager) redisUtil.get(loginKey);
        result=CheckLogin.checkSuperManagerLogin(superManager,loginKey);
        if(result!=null){
            return result;
        }
        result=new Result();
        goodsMapper.deleteEvaluate(id);
        result.setCode("200");
        result.setMessage("成功");
        return result;
    }
}
