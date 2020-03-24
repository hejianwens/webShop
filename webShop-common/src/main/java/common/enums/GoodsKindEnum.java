package common.enums;

import java.util.ArrayList;
import java.util.List;

public enum GoodsKindEnum {
    BOOK("小说/教材书/散文","1"),
    HOMEEQUIPMENT("家用电器/","2"),
    ELECTRONICS("手机/电脑/相机","3"),
    COSMETICS("化妆品/护肤品","4"),
    FOOD("冰鲜食品/零食","5"),
    DAILYTHING("生活用品/衣服","6")

    ;
    private String name;
    private String value;

    GoodsKindEnum(String name,String value){
        this.name=name;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<GoodsKindEnum> getGoodsKindEnums(){
        List<GoodsKindEnum>goodsKindEnums=new ArrayList<GoodsKindEnum>();
        for (GoodsKindEnum goodsKindEnum:GoodsKindEnum.values()){
            goodsKindEnums.add(goodsKindEnum);
        }
        return goodsKindEnums;
    }
}
