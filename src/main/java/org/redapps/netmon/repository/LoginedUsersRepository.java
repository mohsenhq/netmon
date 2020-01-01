package org.redapps.netmon.repository;

import org.redapps.netmon.model.LoginedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginedUsersRepository extends JpaRepository<LoginedUser, Long> {

    List<LoginedUser> findByUsername(String username);

    void deleteAllByUsername(String username);

     Boolean existsByToken(String token);
}
