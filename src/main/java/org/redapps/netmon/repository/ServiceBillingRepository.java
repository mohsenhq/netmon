package org.redapps.netmon.repository;


import org.redapps.netmon.model.Billing;
import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.util.NetmonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceBillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findById(Long id);

    Page<Billing> findByNetmonServiceId(ServiceIdentity serviceId, Pageable pageable);

    List<Billing> findByNetmonServiceIdAndStatus(ServiceIdentity serviceId, NetmonStatus.BillingStatus status);

}