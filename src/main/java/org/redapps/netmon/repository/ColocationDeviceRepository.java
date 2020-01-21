package org.redapps.netmon.repository;

import org.redapps.netmon.model.Device;
import org.redapps.netmon.model.ServiceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColocationDeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findById(Long deviceId);

    Page<Device> findAllByNetmonServiceId(ServiceIdentity colocationId, Pageable pageable);

    List<Device> findAllByNetmonServiceId(ServiceIdentity colocationId);

    boolean existsByIdAndNetmonServiceId(Long deviceId, ServiceIdentity colocationId);


}
