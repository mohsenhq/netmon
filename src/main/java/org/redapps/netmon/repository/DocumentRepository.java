package org.redapps.netmon.repository;

import org.redapps.netmon.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findById(Long docId);

    Optional<Document> findByIdAndCompanyId(Long docId, Long companyId);

    Page<Document> findAllByCompanyId(Long companyId, Pageable pageable);

}
