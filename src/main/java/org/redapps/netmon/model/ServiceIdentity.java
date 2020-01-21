package org.redapps.netmon.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class ServiceIdentity implements Serializable {

    @NotNull
    @Column(name = "SERVICE_ID")
    private Long serviceId;

    @NotNull
    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    // public ServiceIdentity() {

    // }

    public ServiceIdentity(Long serviceId, LocalDate createDate) {
        this.serviceId = serviceId;
        this.createDate = createDate;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;

    //     ServiceIdentity that = (ServiceIdentity) o;

    //     if (!serviceId.equals(that.serviceId)) return false;
    //     return createDate.equals(that.createDate);
    // }

    // @Override
    // public int hashCode() {
    //     int result = serviceId.hashCode();
    //     result = 31 * result + createDate.hashCode();
    //     return result;
    // }
}
