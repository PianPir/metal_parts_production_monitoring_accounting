package com.metal_parts_production_monitoring_accounting.config;


import com.metal_parts_production_monitoring_accounting.model.ERole;
import com.metal_parts_production_monitoring_accounting.model.Role;
import com.metal_parts_production_monitoring_accounting.security.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
                Set<Role> roles = username.contains("admin") ?
                        Set.of(new Role(1L, ERole.ROLE_ADMIN)) :
                        Set.of(new Role(2L, ERole.ROLE_USER));

                return UserDetailsImpl.builder()
                        .username("testuser")
                        .password("ignored")
                        .roles(roles)
                        .build();
        };
    }
}
