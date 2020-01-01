package org.redapps.netmon.payload;

import org.redapps.netmon.util.NetmonStatus;

import java.time.LocalDate;

public class ServiceBillingResponse {

    private Long id;
    private NetmonStatus.BillingStatus status;
    private double value;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate paymentDate;
    private String description;
    private long orderId;

    public ServiceBillingResponse(){

    }

    public ServiceBillingResponse(Long id, NetmonStatus.BillingStatus status, double value, LocalDate startDate,
                                  LocalDate endDate, LocalDate paymentDate,
                                  String description, long orderId) {
        this.id = id;
        this.status = status;
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentDate = paymentDate;
        this.description = description;
        this.orderId = orderId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NetmonStatus.BillingStatus getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.BillingStatus status) {
        this.status = status;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
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
}
