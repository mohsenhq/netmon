package org.redapps.netmon.payload;


import org.redapps.netmon.util.NetmonStatus;

import java.time.LocalDate;

public class VpsResponse {

    private Long id;
    private String name;
    private String description;
    private int validIp;
    private int invalidIp;
    private boolean vnc;
    private NetmonStatus.ServiceStatus status;
    private LocalDate startDate;
    private Long duration;
    private double paymentBase;
    private double discountPercent;
    private double ramSize;
    private double cpu;
    private String usageType;
    private Long vpsPlan;
    private double extraRam;
    private double extraCpu;
    private double extraDisk;
    private double extraTraffic;
    private Long osTypeId;
    private Long technicalPersonId;
    private double price;
    private double finalPrice;
    private double extraPrice;
    private int type;
    private Long customerId;

    public VpsResponse(Long id, String name, String description, int validIp, int invalidIp, boolean vnc,
                       NetmonStatus.ServiceStatus status, String usageType, Long vpsPlan, double extraRam,
                       double extraCpu, double extraDisk, double extraTraffic, Long technicalPersonId,
                       Long osTypeId, LocalDate startDate, Long duration, double discountPercent,
                       double price, double finalPrice, double extraPrice, int type, Long customerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.validIp = validIp;
        this.invalidIp = invalidIp;
        this.vnc = vnc;
        this.status = status;
        this.usageType = usageType;
        this.vpsPlan = vpsPlan;
        this.extraRam = extraRam;
        this.extraCpu = extraCpu;
        this.extraDisk = extraDisk;
        this.extraTraffic = extraTraffic;
        this.technicalPersonId = technicalPersonId;
        this.osTypeId = osTypeId;
        this.startDate = startDate;
        this.duration = duration;
        this.discountPercent = discountPercent;
        this.price = price;
        this.finalPrice = finalPrice;
        this.extraPrice = extraPrice;
        this.type = type;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValidIp() {
        return validIp;
    }

    public void setValidIp(int validIp) {
        this.validIp = validIp;
    }

    public int getInvalidIp() {
        return invalidIp;
    }

    public void setInvalidIp(int invalidIp) {
        this.invalidIp = invalidIp;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getPaymentBase() {
        return paymentBase;
    }

    public void setPaymentBase(double paymentBase) {
        this.paymentBase = paymentBase;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getRamSize() {
        return ramSize;
    }

    public void setRamSize(double ramSize) {
        this.ramSize = ramSize;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVnc() {
        return vnc;
    }

    public void setVnc(boolean vnc) {
        this.vnc = vnc;
    }

    public NetmonStatus.ServiceStatus getStatus() {
        return status;
    }

    public void setStatus(NetmonStatus.ServiceStatus status) {
        this.status = status;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Long getVpsPlan() {
        return vpsPlan;
    }

    public void setVpsPlan(Long vpsPlan) {
        this.vpsPlan = vpsPlan;
    }

    public double getExtraRam() {
        return extraRam;
    }

    public void setExtraRam(double extraRam) {
        this.extraRam = extraRam;
    }

    public double getExtraCpu() {
        return extraCpu;
    }

    public void setExtraCpu(double extraCpu) {
        this.extraCpu = extraCpu;
    }

    public double getExtraDisk() {
        return extraDisk;
    }

    public void setExtraDisk(double extraDisk) {
        this.extraDisk = extraDisk;
    }

    public double getExtraTraffic() {
        return extraTraffic;
    }

    public void setExtraTraffic(double extraTraffic) {
        this.extraTraffic = extraTraffic;
    }

    public Long getOsTypeId() {
        return osTypeId;
    }

    public void setOsTypeId(Long osTypeId) {
        this.osTypeId = osTypeId;
    }

    public Long getTechnicalPersonId() {
        return technicalPersonId;
    }

    public void setTechnicalPersonId(Long technicalPersonId) {
        this.technicalPersonId = technicalPersonId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }
}
