package com.metal_parts_production_monitoring_accounting.service;

import com.metal_parts_production_monitoring_accounting.model.User;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.metal_parts_production_monitoring_accounting.repository.UserRepository;
import com.metal_parts_production_monitoring_accounting.security.UserDetailsImpl;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserWithRolesByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return UserDetailsImpl.builder().id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }
}
