package org.redapps.netmon.repository;

import org.redapps.netmon.model.IP;
import org.redapps.netmon.model.ServiceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceIPRepository extends JpaRepository<IP, Long> {
    Optional<IP> findById(Long id);

    List<IP> findByNetmonServiceId(ServiceIdentity serviceId);

    Page<IP> findByNetmonServiceId(ServiceIdentity serviceId, Pageable pageable);

    Boolean existsByIpAndNetmonServiceId(String ip, ServiceIdentity serviceId);

    Boolean existsByIdAndNetmonServiceId(Long id, ServiceIdentity serviceId);
}
