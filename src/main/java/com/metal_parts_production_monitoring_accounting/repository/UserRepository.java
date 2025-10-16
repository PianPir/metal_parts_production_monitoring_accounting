package com.metal_parts_production_monitoring_accounting.repository;


import com.metal_parts_production_monitoring_accounting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

}
