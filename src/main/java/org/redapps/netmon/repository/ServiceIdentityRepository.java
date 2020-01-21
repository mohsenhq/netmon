package org.redapps.netmon.repository;

import org.redapps.netmon.model.NetmonService;
import org.redapps.netmon.model.ServiceIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceIdentityRepository extends JpaRepository<NetmonService, ServiceIdentity> {

}
