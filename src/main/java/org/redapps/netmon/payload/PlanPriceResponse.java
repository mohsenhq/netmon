package org.redapps.netmon.payload;

import java.time.LocalDate;

import org.redapps.netmon.model.VpsPlan;

public class PlanPriceResponse {

    private VpsPlan PlanId;
    private double price;
    private LocalDate date;

    public PlanPriceResponse() {
    }

    public PlanPriceResponse(VpsPlan PlanId, LocalDate date, double price) {
        this.PlanId = PlanId;
        this.date = date;
        this.price = price;
    }

    public VpsPlan getPlanId() {
        return this.PlanId;
    }

    public void setPlanId(VpsPlan PlanId) {
        this.PlanId = PlanId;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}