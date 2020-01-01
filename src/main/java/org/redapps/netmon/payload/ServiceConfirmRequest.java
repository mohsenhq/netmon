package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.redapps.netmon.util.NetmonStatus;

public class ServiceConfirmRequest {

    private String description;
    private double discountPercent;
    private NetmonStatus.ServiceStatus status;
    private int type;//0:unknown, 1:setadi, 2:non-setadi
    private double extraPrice;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public NetmonStatus.ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.ServiceStatus status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

