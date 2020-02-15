package org.redapps.netmon.repository;

import java.time.LocalDate;

import org.redapps.netmon.model.ResourcePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcePriceRepository extends JpaRepository<ResourcePrice, Long> {

    boolean existsById(Long id);

    boolean existsByDate(LocalDate date);

    ResourcePrice findTopByOrderByDateDesc();

    boolean existsTopByOrderByDateDesc();

}