package org.redapps.netmon.payload;

import org.redapps.netmon.model.IpUsage;

import java.time.LocalDate;
import java.util.Vector;

public class IpUsageResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private String ip;
    private Vector<IpUsage> ipUploadUsages;
    private Vector<IpUsage> ipDownloadUsages;

    public IpUsageResponse(LocalDate startDate, LocalDate endDate, String ip,
                           Vector<IpUsage> ipUploadUsages, Vector<IpUsage> ipDownloadUsages) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.ip = ip;
        this.ipUploadUsages = ipUploadUsages;
        this.ipDownloadUsages = ipDownloadUsages;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Vector<IpUsage> getIpUploadUsages() {
        return ipUploadUsages;
    }

    public void setIpUploadUsages(Vector<IpUsage> ipUploadUsages) {
        this.ipUploadUsages = ipUploadUsages;
    }

    public Vector<IpUsage> getIpDownloadUsages() {
        return ipDownloadUsages;
    }

    public void setIpDownloadUsages(Vector<IpUsage> ipDownloadUsages) {
        this.ipDownloadUsages = ipDownloadUsages;
    }
}


