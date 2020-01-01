package org.redapps.netmon.model;

import org.redapps.netmon.util.NetmonStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime date;
    private String user;
    private NetmonStatus.LOG_STATUS status;
    private String requestParameters;

    @Column(length = 500)
    private String bodyParameters;
    private String details;

    public Log(){

    }

    public Log(String action, LocalDateTime date, String user, NetmonStatus.LOG_STATUS status,
               String requestParameters, String bodyParameters, String details) {
        this.action = action;
        this.date = date;
        this.user = user;
        this.status = status;
        this.requestParameters = requestParameters;
        this.bodyParameters = bodyParameters;
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

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }

    public String getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(String bodyParameters) {
        this.bodyParameters = bodyParameters;
    }
}
