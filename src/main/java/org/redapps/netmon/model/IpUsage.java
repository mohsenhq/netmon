package org.redapps.netmon.model;

import java.time.LocalDate;

public class IpUsage {

    private LocalDate date;
    private double ipUsage;

    public IpUsage(LocalDate date, double ipUsage) {
        this.date = date;
        this.ipUsage = ipUsage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIpUsage() {
        return ipUsage;
    }

    public void setIpUsage(double ipUsage) {
        this.ipUsage = ipUsage;
    }
}

