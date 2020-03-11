package org.redapps.netmon.repository;

import java.time.LocalDate;

import org.redapps.netmon.model.PlanPrice;
import org.redapps.netmon.model.PlanPriceId;
import org.redapps.netmon.model.VpsPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanPriceRepository extends JpaRepository<PlanPrice, PlanPriceId> {

    boolean existsById(PlanPriceId planPriceId);

    boolean existsByDate(LocalDate date);

    PlanPrice findTopByOrderByDateDesc();

    boolean existsTopByOrderByDateDesc();

    // List<PlanPrice> findAllByVpsPlanId(Long vpsPlanId);
    
    // Page<PlanPrice> findAllByVpsPlanId(Long vpsPlanId, Pageable pageable);

	Page<PlanPrice> findByPlanId(VpsPlan planId, Pageable pageable);

}