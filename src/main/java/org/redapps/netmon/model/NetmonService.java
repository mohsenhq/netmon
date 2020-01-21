package org.redapps.netmon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redapps.netmon.model.audit.UserDateAudit;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.redapps.netmon.util.NetmonStatus;
import org.redapps.netmon.util.NetmonTypes;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "services")
public class NetmonService extends UserDateAudit {

    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ServiceIdentity serviceIdentity;

    private String slaType;
    private String description;
    private String name;

    @Type(type = "yes_no")
    private boolean active = false;

    private NetmonTypes.SERVICE_TYPES serviceType; //COLOCATION = 0 or VPS = 1
    private int unitNumber;

    private int validIp;
    private int invalidIp;

    @Type(type = "yes_no")
    private boolean vnc;

    private NetmonStatus.ServiceStatus status;

    private double extraRam;
    private double extraCpu;
    private double extraDisk;
    private double extraTraffic;
    private double discountPercent;

    private String usageType;
    private String rackPosition;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    private Long duration;

    private int type; // 0:unknown, 1:setadi, 2:non-setadi
    private double price;
    private double finalPrice; // paymentBase * discountPercent
    private double extraPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technical_person_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TechnicalPerson technicalPerson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "os_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private OSType osType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vps_plan_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private VpsPlan vpsPlan;

    public NetmonService() {

    }

    // Create a colocation service
    public NetmonService(String slaType, String description, String name, NetmonTypes.SERVICE_TYPES serviceType,
                         int unitNumber, int validIp, int invalidIp, LocalDate startDate, Long duration,
                         Company company, TechnicalPerson technicalPerson, OSType osType) {
        this.slaType = slaType;
        this.description = description;
        this.name = name;
        this.serviceType = serviceType;
        this.unitNumber = unitNumber;
        this.validIp = validIp;
        this.invalidIp = invalidIp;
        this.status = NetmonStatus.ServiceStatus.REQUEST_FOR_NEW_SERVICE;
        this.startDate = startDate;
        this.duration = duration;
        this.company = company;
        this.technicalPerson = technicalPerson;
        this.osType = osType;
    }

    // Create a VPS service
    public NetmonService(String description, String name, NetmonTypes.SERVICE_TYPES serviceType,
                         int validIp, int invalidIp, boolean vnc, VpsPlan vpsPlan, double extraRam,
                         double extraCpu, double extraDisk, double extraTraffic,
                         Long duration, Company company, TechnicalPerson technicalPerson,
                         OSType osType, double price) {
        this.description = description;
        this.name = name;
        this.serviceType = serviceType;
        this.validIp = validIp;
        this.invalidIp = invalidIp;
        this.vnc = vnc;
        this.status = NetmonStatus.ServiceStatus.REQUEST_FOR_NEW_SERVICE;
        this.vpsPlan = vpsPlan;
        this.extraRam = extraRam;
        this.extraCpu = extraCpu;
        this.extraDisk = extraDisk;
        this.extraTraffic = extraTraffic;
        this.duration = duration;
        this.company = company;
        this.technicalPerson = technicalPerson;
        this.osType = osType;
        this.price = price;
        this.finalPrice = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public NetmonTypes.SERVICE_TYPES getServiceType() {
        return serviceType;
    }

    public void setServiceType(NetmonTypes.SERVICE_TYPES serviceType) {
        this.serviceType = serviceType;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Id
    public ServiceIdentity getId() {
        return serviceIdentity;
    }

    public void setId(ServiceIdentity serviceIdentity) {
        this.serviceIdentity = serviceIdentity;
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

    public String getRackPosition() {
        return rackPosition;
    }

    public void setRackPosition(String rackPosition) {
        this.rackPosition = rackPosition;
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

    public TechnicalPerson getTechnicalPerson() {
        return technicalPerson;
    }

    public void setTechnicalPerson(TechnicalPerson technicalPerson) {
        this.technicalPerson = technicalPerson;
    }

    public OSType getOsType() {
        return osType;
    }

    public void setOsType(OSType osType) {
        this.osType = osType;
    }

    public VpsPlan getVpsPlan() {
        return vpsPlan;
    }

    public void setVpsPlan(VpsPlan vpsPlan) {
        this.vpsPlan = vpsPlan;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
