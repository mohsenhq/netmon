package org.redapps.netmon.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ServiceIdentity implements Serializable {

    private Long id;
    private LocalDate createDate;

    public ServiceIdentity() {
    }

    public ServiceIdentity(Long id, LocalDate createDate) {
        this.id = id;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceIdentity serviceIdentity = (ServiceIdentity) o;
        return id.equals(serviceIdentity.id) &&
                createDate.equals(serviceIdentity.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate);
    }
}
