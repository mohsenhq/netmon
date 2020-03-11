package org.redapps.netmon.repository;

import org.redapps.netmon.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColocationDeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findById(Long deviceId);

    Page<Device> findAllByNetmonServiceIdAndNetmonServiceCreateDate(Long colocationId, LocalDate createDate, Pageable pageable);

    List<Device> findAllByNetmonServiceIdAndNetmonServiceCreateDate(Long colocationId, LocalDate createDate);

    boolean existsByIdAndNetmonServiceIdAndNetmonServiceCreateDate(Long deviceId, Long colocationId, LocalDate createDate);


}
