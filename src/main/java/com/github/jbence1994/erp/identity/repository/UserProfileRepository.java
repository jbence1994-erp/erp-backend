package com.github.jbence1994.erp.identity.repository;

import com.github.jbence1994.erp.identity.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
