package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.time.LocalDate;

public class ColocationRequest {

    private String name;
    private int unitNumber;
    private String slaType;
    private Long technicalPersonId;
    private String description;
    private int validIp;
    private int invalidIp;
    private String usageType;
    private Long osTypeId;
    private LocalDate startDate;
//    private LocalDate endDate;
    private Long duration;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getSlaType() {
        return this.slaType;
    }

    public void setSlaType(String slaType) {
        this.slaType = slaType;
    }

    public Long getTechnicalPersonId() {
        return technicalPersonId;
    }

    public void setTechnicalPersonId(Long technicalPersonId) {
        this.technicalPersonId = technicalPersonId;
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

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public Long getOsTypeId() {
        return osTypeId;
    }

    public void setOsTypeId(Long osTypeId) {
        this.osTypeId = osTypeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    //    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }
}
