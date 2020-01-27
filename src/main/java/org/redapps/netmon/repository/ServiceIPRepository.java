package org.redapps.netmon.repository;

import org.redapps.netmon.model.IP;
import org.redapps.netmon.model.ServiceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceIPRepository extends JpaRepository<IP, Long> {
    Optional<IP> findById(Long id);

    List<IP> findByNetmonServiceId(Long serviceId);

    Page<IP> findByNetmonServiceId(Long serviceId, Pageable pageable);

    Boolean existsByIpAndNetmonServiceId(String ip, Long serviceId);

    Boolean existsByIdAndNetmonServiceId(Long id, Long serviceId);
}
