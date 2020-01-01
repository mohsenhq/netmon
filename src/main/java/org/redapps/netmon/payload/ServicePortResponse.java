package org.redapps.netmon.payload;

public class ServicePortResponse {

    private Long id;
    private int port;
    private String description;

    public ServicePortResponse() {
    }

    public ServicePortResponse(Long id, int port, String description) {
        this.id = id;
        this.port = port;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

