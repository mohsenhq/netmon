package org.redapps.netmon.repository;

import java.time.LocalDate;
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

    Boolean existsByIdAndNetmonServiceIdAndNetmonServiceCreateDate(Long id, Long netmonServiceId, LocalDate netmonCreateDate);

    Page<Port> findAllByNetmonService(NetmonService netmonService, Pageable pageable);
}
