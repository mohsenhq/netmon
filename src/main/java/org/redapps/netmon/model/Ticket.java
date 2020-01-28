package org.redapps.netmon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.redapps.netmon.model.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 140)
    private String title;

    private String description;

    private String status; // OPEN-CLOSED
    private String response;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns ({
        @JoinColumn(name = "createDate", nullable = false),
        @JoinColumn(name = "serviceId", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private NetmonService netmonService;

    public Ticket() {

    }

    public Ticket(String title, String description, String status, NetmonService netmonService) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.netmonService = netmonService;
    }

    public Ticket(Long id, String response, NetmonService netmonService) {
        this.id = id;
        this.response = response;
        this.netmonService = netmonService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public NetmonService getNetmonService() {
        return netmonService;
    }

    public void setNetmonService(NetmonService netmonService) {
        this.netmonService = netmonService;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

