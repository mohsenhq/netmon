package org.redapps.netmon.repository;

import org.redapps.netmon.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findById(Long id);

    boolean existsByIdAndNetmonServiceId(Long id, Long netmonServiceId);

    Page<Ticket> findAllByNetmonServiceId(Long serviceId, Pageable pageable);

}
