package common.model.goods;


import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class SimilarCustomersLikeGoods implements Serializable {
    private Date createTime;
    private Set<BigDecimal> topTenUserSimilarGoodsIdSet;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<BigDecimal> getTopTenUserSimilarGoodsIdSet() {
        return topTenUserSimilarGoodsIdSet;
    }

    public void setTopTenUserSimilarGoodsIdSet(Set<BigDecimal> topTenUserSimilarGoodsIdSet) {
        this.topTenUserSimilarGoodsIdSet = topTenUserSimilarGoodsIdSet;
    }
}
