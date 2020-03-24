package common.model.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class LikeStructure implements Serializable {

    private Map<BigDecimal, Set<BigDecimal>> customerLikeList;
    private Date createTime;

    public Map<BigDecimal, Set<BigDecimal>> getCustomerLikeList() {
        return customerLikeList;
    }

    public void setCustomerLikeList(Map<BigDecimal, Set<BigDecimal>> customerLikeList) {
        this.customerLikeList = customerLikeList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
