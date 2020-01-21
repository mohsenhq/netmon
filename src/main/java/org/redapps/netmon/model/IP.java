package org.redapps.netmon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redapps.netmon.model.audit.UserDateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.persistence.*;

@Entity
@Table(name = "ips")
public class IP extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    private String ip;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns ({
        @JoinColumn(name = "service_id", nullable = false),
        @JoinColumn(name = "CREATE_DATE", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private NetmonService netmonService;

    public IP() {

    }

    public IP(String ip, String description, NetmonService netmonService) {
        this.ip = ip;
        this.description = description;
        this.netmonService = netmonService;
    }

    public IP(Long id, String ip, String description, NetmonService netmonService) {
        this.id = id;
        this.ip = ip;
        this.description = description;
        this.netmonService = netmonService;
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

    public NetmonService getNetmonService() {
        return netmonService;
    }

    public void setNetmonService(NetmonService netmonService) {
        this.netmonService = netmonService;
    }
}
