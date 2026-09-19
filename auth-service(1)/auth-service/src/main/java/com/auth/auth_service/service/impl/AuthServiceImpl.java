package com.auth.auth_service.service.impl;

import com.auth.auth_service.dto.AuthResponse;
import com.auth.auth_service.dto.LoginRequest;
import com.auth.auth_service.dto.RegisterRequest;
import com.auth.auth_service.entity.Role;
import com.auth.auth_service.entity.UserEntity;
import com.auth.auth_service.exception.InvalidCredentialsException;
import com.auth.auth_service.exception.UserAlreadyExistsException;
import com.auth.auth_service.repository.UserRepository;
import com.auth.auth_service.service.AuthService;
import com.auth.auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepo.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username already exists"
            );
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepo.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        UserEntity user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username"));

        if (!encoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }

        return new AuthResponse(jwtService.generateToken(user));
    }
}
