package org.redapps.netmon.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.util.NetmonTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetmonServiceRepository extends JpaRepository<NetmonService, ServiceIdentity> {

    Optional<NetmonService> findById(Long netmonServiceId);

    Page<NetmonService> findAllByServiceTypeAndCompanyId(NetmonTypes.SERVICE_TYPES serviceType, Long companyId, Pageable pageable);

    Page<NetmonService> findAllByServiceType(int serviceType, Pageable pageable);

    boolean existsByTechnicalPersonId(Long technicalPersonId);

    boolean existsByIdAndCompanyIdAndServiceType(Long netmonServiceId, Long companyId,
                                                 NetmonTypes.SERVICE_TYPES serviceType);
}
