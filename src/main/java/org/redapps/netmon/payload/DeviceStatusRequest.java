package org.redapps.netmon.payload;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.redapps.netmon.util.NetmonStatus;

public class DeviceStatusRequest {

    private NetmonStatus.DEVICE_STATUS status;

    public NetmonStatus.DEVICE_STATUS getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.DEVICE_STATUS status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
