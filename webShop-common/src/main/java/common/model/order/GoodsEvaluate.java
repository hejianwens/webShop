package common.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;



public class GoodsEvaluate {
    private BigDecimal id;
    private BigDecimal goodsId;
    private BigDecimal goodsQualityMark;
    private String content;
    private String orderNumber;
    private BigDecimal customerId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private String createTime;
    private String nickName;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(BigDecimal goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getGoodsQualityMark() {
        return goodsQualityMark;
    }

    public void setGoodsQualityMark(BigDecimal goodsQualityMark) {
        this.goodsQualityMark = goodsQualityMark;
    }
}
