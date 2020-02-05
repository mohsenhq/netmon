package org.redapps.netmon.repository;

import org.redapps.netmon.model.IP;
import org.redapps.netmon.model.NetmonService;
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

    List<IP> findAllByNetmonService(NetmonService netmonService);
    
    Page<IP> findAllByNetmonService(NetmonService netmonService, Pageable pageable);

    Boolean existsByIpAndNetmonServiceIdAndNetmonServiceCreateDate(String ip, Long serviceId, LocalDate createDate);

    Boolean existsByIdAndNetmonServiceIdAndNetmonServiceCreateDate(Long id, Long serviceId, LocalDate createDate);
}
