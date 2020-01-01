package org.redapps.netmon.payload;

public class ServiceIPResponse {

    private Long id;
    private String ip;
    private String description;

    public ServiceIPResponse() {
    }

    public ServiceIPResponse(Long id, String ip, String description) {
        this.id = id;
        this.ip = ip;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
