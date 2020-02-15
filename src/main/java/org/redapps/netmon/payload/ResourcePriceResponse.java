package org.redapps.netmon.payload;

import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ResourcePriceResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double cpuPrice;
    private double ramPrice;
    private double diskPrice;
    private double trafficUploadPrice;
    private double trafficDownloadPrice;
    private double unitSilverPrice;
    private double unitGoldPrice;
    private double ipPrice;
    private LocalDate date;

    public ResourcePriceResponse() {
    }

    public ResourcePriceResponse(Long id, double cpuPrice, double ramPrice, double diskPrice, double trafficUploadPrice,
            double trafficDownloadPrice, double unitSilverPrice, double unitGoldPrice, double ipPrice, LocalDate date) {
        this.id = id;
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