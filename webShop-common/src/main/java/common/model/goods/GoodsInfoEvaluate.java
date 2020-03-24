package common.model.goods;

import common.model.order.GoodsEvaluate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class GoodsInfoEvaluate implements Serializable {
    private List<GoodsEvaluate>evaluates;
    private BigDecimal avgEvaluate;

    public List<GoodsEvaluate> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<GoodsEvaluate> evaluates) {
        this.evaluates = evaluates;
    }

    public BigDecimal getAvgEvaluate() {
        return avgEvaluate;
    }

    public void setAvgEvaluate(BigDecimal avgEvaluate) {
        this.avgEvaluate = avgEvaluate;
    }
}
