package com.metal_parts_production_monitoring_accounting.controller;


import com.metal_parts_production_monitoring_accounting.model.ERole;
import com.metal_parts_production_monitoring_accounting.model.Role;
import com.metal_parts_production_monitoring_accounting.model.User;
import com.metal_parts_production_monitoring_accounting.payload.request.SigninRequest;
import com.metal_parts_production_monitoring_accounting.payload.request.SignupRequest;
import com.metal_parts_production_monitoring_accounting.payload.response.JwtResponse;
import com.metal_parts_production_monitoring_accounting.repository.RoleRepository;
import com.metal_parts_production_monitoring_accounting.repository.UserRepository;
import com.metal_parts_production_monitoring_accounting.security.JwtCore;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@Data
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    private final RoleRepository roleRepository;


    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtCore jwtCore, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.findByUsername(signupRequest.username()).isPresent()){
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        Role userRole = roleRepository.findByName(ERole.ROLE_USER).
                orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(signupRequest.username());
        user.setPassword(passwordEncoder.encode(signupRequest.password()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            String token = jwtCore.generateToken(authentication);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
