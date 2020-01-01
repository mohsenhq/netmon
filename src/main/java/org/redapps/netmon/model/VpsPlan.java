package org.redapps.netmon.model;

import org.hibernate.annotations.Type;
import org.redapps.netmon.model.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "vps_plans")
public class VpsPlan extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private double ram;
    private double cpu;
    private double maxTraffic;
    private double disk;
    private double monthlyPrice;
    private double freeTraffic;

    @Type(type="yes_no")
    private boolean active = false;

    public VpsPlan() {

    }

    public VpsPlan(String name, double ram, double cpu, double maxTraffic, double disk,
                   double monthlyPrice, double freeTraffic) {
        this.name = name;
        this.ram = ram;
        this.cpu = cpu;
        this.maxTraffic = maxTraffic;
        this.disk = disk;
        this.monthlyPrice = monthlyPrice;
        this.freeTraffic = freeTraffic;
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

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        this.ram = ram;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMaxTraffic() {
        return maxTraffic;
    }

    public void setMaxTraffic(double maxTraffic) {
        this.maxTraffic = maxTraffic;
    }

    public double getDisk() {
        return disk;
    }

    public void setDisk(double disk) {
        this.disk = disk;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getFreeTraffic() {
        return freeTraffic;
    }

    public void setFreeTraffic(double freeTraffic) {
        this.freeTraffic = freeTraffic;
    }
}
