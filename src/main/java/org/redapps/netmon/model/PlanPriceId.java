package org.redapps.netmon.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PlanPriceId implements Serializable {

    private Long planId;
    private LocalDate date;

    public PlanPriceId() {
    }

    public PlanPriceId(Long planId, LocalDate date) {
        this.planId = planId;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanPriceId planPriceId = (PlanPriceId) o;
        return planId.equals(planPriceId.planId) &&
                date.equals(planPriceId.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId, date);
    }
}
