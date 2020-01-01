package org.redapps.netmon.repository;

import org.redapps.netmon.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    Page<Log> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Log> findAllByDateBefore(LocalDateTime endDate, Pageable pageable);

    Page<Log> findAllByDateAfter(LocalDateTime startDate, Pageable pageable);
}