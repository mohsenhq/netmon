package org.redapps.netmon.repository;

import java.util.Optional;
import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.Port;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePortRepository extends JpaRepository<Port, Long> {

    Optional<Port> findById(Long id);

    Boolean existsByPortAndNetmonService(int port, NetmonService netmonService);

    Boolean existsByIdAndNetmonServiceId(Long id, Long netmonServiceId);

    Page<Port> findAllByNetmonServiceId(Long serviceId, Pageable pageable);
}
