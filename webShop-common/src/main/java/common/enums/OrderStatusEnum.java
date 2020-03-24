package common.enums;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatusEnum {
    HaveNotApply("未付款","0"),
    AlreadyApply("已付款","1"),
    AlreadySend("已发货","2"),
    AlreadyReceive("已收货","3"),
    WaitRefund("退款中","-1"),
    AlreadyCancel("已取消","-2"),

   ; private String name;
    private String value;

    OrderStatusEnum(String name, String value){
        this.name=name;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<OrderStatusEnum> getOrderStatusEnums(){
        List<OrderStatusEnum>orderStatusEnums=new ArrayList<OrderStatusEnum>();
        for (OrderStatusEnum orderStatusEnum:OrderStatusEnum.values()){
            orderStatusEnums.add(orderStatusEnum);
        }
        return orderStatusEnums;
    }
}
