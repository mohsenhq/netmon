package org.redapps.netmon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.redapps.netmon.model.audit.UserDateAudit;
import org.redapps.netmon.util.NetmonStatus;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "billings")
public class Billing extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NetmonStatus.BillingStatus status;
    private LocalDate paymentDate;
    private double value;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    private String description;

    @Nullable
    private long orderId;

    private long referenceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns ({
        @JoinColumn(name = "service_id", nullable = false),
        @JoinColumn(name = "CREATE_DATE", nullable = false)
    })
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private NetmonService netmonService;

    public Billing(){

    }

    public Billing(double value, LocalDate startDate, LocalDate endDate,
                   String description, NetmonService netmonService) {
        this.status = NetmonStatus.BillingStatus.CREATED;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.netmonService = netmonService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public NetmonStatus.BillingStatus getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.BillingStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }
}
