package org.redapps.netmon.model;

import org.hibernate.annotations.Type;
import org.redapps.netmon.model.audit.UserDateAudit;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "resource_price")
public class ResourcePrice extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private double cpuPrice;
    @NotNull
    private double ramPrice;
    @NotNull
    private double diskPrice;
    @NotNull
    private double trafficUploadPrice;
    @NotNull
    private double trafficDownloadPrice;
    @NotNull
    private double unitSilverPrice;
    @NotNull
    private double unitGoldPrice;
    @NotNull
    private double ipPrice;
    @NotNull
    private LocalDate date;

    // @Type(type="yes_no")
    // private boolean active = false;

    public ResourcePrice() {

    }

    public ResourcePrice(double cpuPrice, double ramPrice, double diskPrice, double trafficUploadPrice,
            double trafficDownloadPrice, double unitSilverPrice, double unitGoldPrice, double ipPrice, LocalDate date) {
        this.cpuPrice = cpuPrice;
        this.ramPrice = ramPrice;
        this.diskPrice = diskPrice;
        this.trafficUploadPrice = trafficUploadPrice;
        this.trafficDownloadPrice = trafficDownloadPrice;
        this.unitSilverPrice = unitSilverPrice;
        this.unitGoldPrice = unitGoldPrice;
        this.ipPrice = ipPrice;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getCpuPrice() {
        return this.cpuPrice;
    }

    public void setCpuPrice(double cpuPrice) {
        this.cpuPrice = cpuPrice;
    }

    public double getRamPrice() {
        return this.ramPrice;
    }

    public void setRamPrice(double ramPrice) {
        this.ramPrice = ramPrice;
    }

    public double getDiskPrice() {
        return this.diskPrice;
    }

    public void setDiskPrice(double diskPrice) {
        this.diskPrice = diskPrice;
    }

    public double getTrafficUploadPrice() {
        return this.trafficUploadPrice;
    }

    public void setTrafficUploadPrice(double trafficUploadPrice) {
        this.trafficUploadPrice = trafficUploadPrice;
    }

    public double getTrafficDownloadPrice() {
        return this.trafficDownloadPrice;
    }

    public void setTrafficDownloadPrice(double trafficDownloadPrice) {
        this.trafficDownloadPrice = trafficDownloadPrice;
    }

    public double getUnitSilverPrice() {
        return this.unitSilverPrice;
    }

    public void setUnitSilverPrice(double unitSilverPrice) {
        this.unitSilverPrice = unitSilverPrice;
    }

    public double getUnitGoldPrice() {
        return this.unitGoldPrice;
    }

    public void setUnitGoldPrice(double unitGoldPrice) {
        this.unitGoldPrice = unitGoldPrice;
    }

    public double getIpPrice() {
        return this.ipPrice;
    }

    public void setIpPrice(double ipPrice) {
        this.ipPrice = ipPrice;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
