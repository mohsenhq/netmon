package org.redapps.netmon.repository;

import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.util.NetmonTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetmonServiceRepository extends JpaRepository<NetmonService, Long> {

    Optional<NetmonService> findById(Long serviceId);

    Page<NetmonService> findAllByServiceTypeAndCompanyId(NetmonTypes.SERVICE_TYPES serviceType, Long companyId, Pageable pageable);

    Page<NetmonService> findAllByServiceType(int serviceType, Pageable pageable);

    boolean existsByTechnicalPersonId(Long technicalPersonId);

    boolean existsByIdAndCompanyIdAndServiceType(Long serviceId, Long companyId,
                                                 NetmonTypes.SERVICE_TYPES serviceType);
}
