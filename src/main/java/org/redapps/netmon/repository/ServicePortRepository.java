package org.redapps.netmon.repository;

import org.redapps.netmon.model.Port;
import org.redapps.netmon.model.ServiceIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicePortRepository extends JpaRepository<Port, Long> {

    Optional<Port> findById(Long id);

    Boolean existsByPortAndNetmonServiceId(int port, ServiceIdentity netmonServiceId);

    Boolean existsByIdAndNetmonServiceId(Long id, ServiceIdentity netmonServiceId);

    Page<Port> findAllByNetmonServiceId(ServiceIdentity serviceId, Pageable pageable);
}
