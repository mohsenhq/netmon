package org.redapps.netmon.payload;

import java.time.LocalDate;
import org.redapps.netmon.util.NetmonStatus;

public class ColocationResponse {

    private Long id;
    private LocalDate createDate;
    private String name;
    private int unitNumber;
    private String slaType;
    private String description;
    private int validIp;
    private int invalidIp;
    private NetmonStatus.ServiceStatus status;
    private LocalDate startDate;
    private Long duration;
//    private LocalDate endDate;
    private double paymentBase;
    private double discountPercent;
    private String usageType;
    private String rackPosition;
    private String technicalPersonId;
    private Long osTypeId;

    public ColocationResponse(Long id,LocalDate createDate, String name, int unitNumber, String slaType,
                               String description, int validIp, int invalidIp, NetmonStatus.ServiceStatus status,
                               String usageType, String rackPosition, Long osTypeId,
                               LocalDate startDate, Long duration, double discountPercent) {
        this.id = id;
        this.createDate = createDate;
        this.name = name;
        this.unitNumber = unitNumber;
        this.slaType = slaType;
        this.description = description;
        this.validIp = validIp;
        this.invalidIp = invalidIp;
        this.status = status;
        this.usageType = usageType;
        this.rackPosition = rackPosition;
        this.osTypeId = osTypeId;
        this.startDate = startDate;
        this.duration = duration;
        this.discountPercent = discountPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getSlaType() {
        return slaType;
    }

    public void setSlaType(String slaType) {
        this.slaType = slaType;
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

//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
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

    public String getTechnicalPersonId() {
        return technicalPersonId;
    }

    public void setTechnicalPersonId(String technicalPersonId) {
        this.technicalPersonId = technicalPersonId;
    }

    public String getRackPosition() {
        return rackPosition;
    }

    public void setRackPosition(String rackPosition) {
        this.rackPosition = rackPosition;
    }

    public Long getOsTypeId() {
        return osTypeId;
    }

    public void setOsTypeId(Long osTypeId) {
        this.osTypeId = osTypeId;
    }
}
