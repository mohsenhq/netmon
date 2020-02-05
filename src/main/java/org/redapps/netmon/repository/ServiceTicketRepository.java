package org.redapps.netmon.repository;

import org.redapps.netmon.model.ServiceIdentity;
import org.redapps.netmon.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ServiceTicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long id);

    boolean existsByIdAndNetmonServiceIdAndNetmonServiceCreateDate(Long id, Long serviceId, LocalDate createDate);

    Page<Ticket> findAllByNetmonServiceId(Long serviceId, LocalDate createDate, Pageable pageable);

}
