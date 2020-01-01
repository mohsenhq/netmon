package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class VpsPlanRequest {

    private String name;
    private double ram;
    private double cpu;
    private double maxTraffic;
    private double disk;
    private double monthlyPrice;
    private double freeTraffic;

    public VpsPlanRequest() {

    }

    public VpsPlanRequest(String name, double ram, double cpu, double maxTraffic, double disk,
                          double monthlyPrice, double freeTraffic) {
        this.name = name;
        this.ram = ram;
        this.cpu = cpu;
        this.maxTraffic = maxTraffic;
        this.monthlyPrice = monthlyPrice;
        this.disk = disk;
        this.freeTraffic = freeTraffic;
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

    public double getFreeTraffic() {
        return freeTraffic;
    }

    public void setFreeTraffic(double freeTraffic) {
        this.freeTraffic = freeTraffic;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
