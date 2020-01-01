package org.redapps.netmon.repository;

import org.redapps.netmon.model.OSType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OsTypeRepository extends JpaRepository<OSType, Long> {

}
