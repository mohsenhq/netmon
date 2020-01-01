package org.redapps.netmon.model;

import org.redapps.netmon.model.audit.UserDateAudit;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.redapps.netmon.util.NetmonStatus;

@Entity
@Table(name = "devices")
public class Device extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    private NetmonStatus.DEVICE_STATUS status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private NetmonService netmonService;

    public Device() {

    }

    public Device(Long id) {
        this.id = id;
    }

    public Device(String name, String description, NetmonService netmonService) {
        this.name = name;
        this.description = description;
        this.netmonService = netmonService;
        this.status = NetmonStatus.DEVICE_STATUS.INIT;
    }

    public Device(Long id, String name, String description, NetmonService netmonService) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.netmonService = netmonService;
        this.status = NetmonStatus.DEVICE_STATUS.INIT;
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

    public NetmonService getNetmonService() {
        return netmonService;
    }

    public void setNetmonService(NetmonService netmonService) {
        this.netmonService = netmonService;
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
