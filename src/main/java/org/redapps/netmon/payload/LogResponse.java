package org.redapps.netmon.payload;

import org.redapps.netmon.util.NetmonStatus;

import java.time.LocalDateTime;

public class LogResponse {

    private Long id;
    private String action;
    private LocalDateTime date;
    private String user;
    private NetmonStatus.LOG_STATUS status;
    private String details;

    public LogResponse() {
    }

    public LogResponse(Long id, String action, LocalDateTime date, String user,
                       NetmonStatus.LOG_STATUS status, String details) {
        this.id = id;
        this.action = action;
        this.date = date;
        this.user = user;
        this.status = status;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public NetmonStatus.LOG_STATUS getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.LOG_STATUS status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
