package org.redapps.netmon.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.redapps.netmon.model.audit.UserDateAudit;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "plan_price")
@IdClass(PlanPriceId.class)
public class PlanPrice extends UserDateAudit {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "plan_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private VpsPlan planId;
    @NotNull
    private double price;
    @Id
    private LocalDate date;

    public PlanPrice() {

    }


    public PlanPrice(VpsPlan planId, LocalDate date, double price) {
        this.planId = planId;
        this.price = price;
        this.date = date;
    }

    public VpsPlan getPlanId() {
        return this.planId;
    }

    public void setPlanId(VpsPlan planId) {
        this.planId = planId;
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
