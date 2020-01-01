package org.redapps.netmon.repository;

import org.redapps.netmon.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Boolean existsByName(String name);

    Boolean existsByUserId(Long userId);

    Optional<Company> findById(Long companyId);

    Optional<Company> findByUserId(Long userId);

    Page<Company> findAllByUserId(Long companyId, Pageable pageable);

    boolean existsByIdAndUserId(Long companyId, Long customerId);
}
