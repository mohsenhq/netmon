package org.redapps.netmon.repository;

import org.redapps.netmon.model.TechnicalPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicalPersonRepository extends JpaRepository<TechnicalPerson, Long> {

    Optional<TechnicalPerson> findByIdAndUserId(Long technicalPersonId, Long userId);

    Page<TechnicalPerson> findAllByUserId(Long userId, Pageable pageable);

    Boolean existsByIdAndUserId(Long id, Long userId);

    Boolean existsByNationalIdAndUserId(long nationalId, Long id);
}