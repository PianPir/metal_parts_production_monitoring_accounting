package com.metal_parts_production_monitoring_accounting.repository;

import com.metal_parts_production_monitoring_accounting.model.ERole;
import com.metal_parts_production_monitoring_accounting.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
