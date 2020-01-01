package org.redapps.netmon.payload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VpsRequest {

    private String name;
    private Long technicalPersonId;
    private String description;
    private int validIp;
    private int invalidIp;
    private boolean vnc;
    private int[] ports;
    private String usageType;
    private Long vpsPlan;
    private double extraRam;
    private double extraCpu;
    private double extraDisk;
    private double extraTraffic;
    private Long osTypeId;

    @NotNull
    private Long duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isVnc() {
        return vnc;
    }

    public void setVnc(boolean vnc) {
        this.vnc = vnc;
    }

    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
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

    public void setVpsPlan(Long vpsPlan) {
        this.vpsPlan = vpsPlan;
    }

    public Long getVpsPlan() {
        return vpsPlan;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
