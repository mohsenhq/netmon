package org.redapps.netmon.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.util.NetmonTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NetmonServiceRepository extends JpaRepository<NetmonService, ServiceIdentity> {

    Optional<NetmonService> findById(ServiceIdentity serviceIdentity);

    Page<NetmonService> findAllByServiceTypeAndCompanyId(NetmonTypes.SERVICE_TYPES serviceType, Long companyId, Pageable pageable);

    Page<NetmonService> findAllByServiceType(NetmonTypes.SERVICE_TYPES serviceType, Pageable pageable);

    boolean existsByTechnicalPersonId(Long technicalPersonId);

    boolean existsByIdAndCreateDateAndCompanyIdAndServiceType(Long netmonServiceId, LocalDate creaDate, Long companyId,
                                                 NetmonTypes.SERVICE_TYPES serviceType);

    // @Query("SELECT MAX(services.price) FROM services") Optional<Long> getMax();

    // NetmonService findFirstOrderByNetmonServiceIdDesc();
    NetmonService findTopByOrderByIdDesc();
}
