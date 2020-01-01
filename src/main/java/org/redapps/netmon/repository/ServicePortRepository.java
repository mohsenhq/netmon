package org.redapps.netmon.repository;

import org.redapps.netmon.model.Port;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicePortRepository extends JpaRepository<Port, Long> {

    Optional<Port> findById(Long id);

    Boolean existsByPortAndNetmonServiceId(int port, Long netmonServiceId);

    Boolean existsByIdAndNetmonServiceId(Long id, Long netmonServiceId);

    Page<Port> findAllByNetmonServiceId(Long serviceId, Pageable pageable);
}
