package common.model.buyCar;

import common.model.goods.Goods;

import java.io.Serializable;
import java.math.BigDecimal;

public class BuyCar implements Serializable {
    private BigDecimal id;
    private BigDecimal amount;
    private BigDecimal onePrice;
    private BigDecimal subtotal;
    private BigDecimal customerId;
    private BigDecimal goodsId;
    private String isDelete;
    private String isStockEnough;
    private Goods goods;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getIsStockEnough() {
        return isStockEnough;
    }

    public void setIsStockEnough(String isStockEnough) {
        this.isStockEnough = isStockEnough;
    }

    public BigDecimal getOnePrice() {
        return onePrice;
    }

    public void setOnePrice(BigDecimal onePrice) {
        this.onePrice = onePrice;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(BigDecimal goodsId) {
        this.goodsId = goodsId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
