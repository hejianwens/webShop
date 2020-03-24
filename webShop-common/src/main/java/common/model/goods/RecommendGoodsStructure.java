package common.model.goods;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class RecommendGoodsStructure {

    private Date createTime;
    private Set<BigDecimal> goodsIdSet;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<BigDecimal> getGoodsIdSet() {
        return goodsIdSet;
    }

    public void setGoodsIdSet(Set<BigDecimal> goodsIdSet) {
        this.goodsIdSet = goodsIdSet;
    }
}
