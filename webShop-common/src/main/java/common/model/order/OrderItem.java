package common.model.order;

import common.model.goods.Goods;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem  implements Serializable {
    private BigDecimal id;
    private String orderNumber;
    private BigDecimal goodsId;
    private BigDecimal amount;
    private BigDecimal subtotal;
    private BigDecimal shopId;
    private Order order;
    private Goods goods;
    private String orderItemStatus;

    public String getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(String orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", goodsId=" + goodsId +
                ", amount=" + amount +
                ", subtotal=" + subtotal +
                ", shopId=" + shopId +
                ", order=" + order +
                ", goods=" + goods +
                '}';
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(BigDecimal goodsId) {
        this.goodsId = goodsId;
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

    public BigDecimal getShopId() {
        return shopId;
    }

    public void setShopId(BigDecimal shopId) {
        this.shopId = shopId;
    }
}
