package common.model.order;

import common.model.buyCar.BuyCar;

import java.util.List;

public class ReceiveCreateOrderParams {
    private  List<BuyCar> buyCars;
    private String receiverName;
    private String receiverTelephone;
    private String receiverAddress;

    public List<BuyCar> getBuyCars() {
        return buyCars;
    }

    public void setBuyCars(List<BuyCar> buyCars) {
        this.buyCars = buyCars;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverTelephone() {
        return receiverTelephone;
    }

    public void setReceiverTelephone(String receiverTelephone) {
        this.receiverTelephone = receiverTelephone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }
}
