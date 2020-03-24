package common.model.goods;

import common.model.user.Shop;

import java.io.Serializable;
import java.math.BigDecimal;

public class Goods implements Serializable {
    private BigDecimal id;
    private String name;
    private BigDecimal price;
    private BigDecimal stockAmount;
    private String kind;
    private String image;
    private BigDecimal shopId;
    private Shop shop;
    private BigDecimal soldAmount;
    private Integer evaluateCount;
    private String isDelete;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getEvaluateCount() {
        return evaluateCount;
    }

    public void setEvaluateCount(Integer evaluateCount) {
        this.evaluateCount = evaluateCount;
    }

    public BigDecimal getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(BigDecimal soldAmount) {
        this.soldAmount = soldAmount;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(BigDecimal stockAmount) {
        this.stockAmount = stockAmount;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getShopId() {
        return shopId;
    }

    public void setShopId(BigDecimal shopId) {
        this.shopId = shopId;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
