package com.github.jbence1994.erp.identity.repository;

import com.github.jbence1994.erp.identity.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
