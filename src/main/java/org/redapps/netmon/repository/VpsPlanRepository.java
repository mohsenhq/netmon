package org.redapps.netmon.repository;

import org.redapps.netmon.model.VpsPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VpsPlanRepository extends JpaRepository<VpsPlan, Long> {

    boolean existsByName(String name);
}