package org.redapps.netmon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.redapps.netmon.model.audit.UserDateAudit;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "ports")
public class Port extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private int port;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns ({
        @JoinColumn(name = "createDate", nullable = false),
        @JoinColumn(name = "serviceId", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private NetmonService netmonService;

    public Port() {
    }

    public Port(int port, String description) {
        this.port = port;
        this.description = description;
    }

    public Port(Long id, int port, String description, NetmonService netmonService) {
        this.id = id;
        this.port = port;
        this.description = description;
        this.netmonService = netmonService;
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

    public NetmonService getNetmonService() {
        return netmonService;
    }

    public void setNetmonService(NetmonService netmonService) {
        this.netmonService = netmonService;
    }
}
