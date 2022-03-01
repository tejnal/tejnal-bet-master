package com.tejnalbetmaster.data.repository;


import com.tejnalbetmaster.data.entity.Role;
import com.tejnalbetmaster.data.entity.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
