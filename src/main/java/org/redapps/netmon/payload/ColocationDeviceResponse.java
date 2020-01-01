package org.redapps.netmon.payload;

import org.redapps.netmon.util.NetmonStatus;

public class ColocationDeviceResponse {

    private Long id;
    private String name;
    private String description;
    private NetmonStatus.DEVICE_STATUS status;

    public ColocationDeviceResponse(Long id, String name, String description, NetmonStatus.DEVICE_STATUS status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NetmonStatus.DEVICE_STATUS getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.DEVICE_STATUS status) {
        this.status = status;
    }
}
