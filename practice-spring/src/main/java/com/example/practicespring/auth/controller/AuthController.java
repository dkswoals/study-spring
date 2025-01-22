package com.example.practicespring.auth.controller;

import com.example.practicespring.auth.dto.LoginRequest;
import com.example.practicespring.auth.dto.RefreshRequest;
import com.example.practicespring.auth.dto.RegisterRequest;
import com.example.practicespring.auth.dto.TokenResponse;
import com.example.practicespring.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest)
        throws NotFoundException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest refreshRequest)
        throws NotFoundException {
        return ResponseEntity.ok(authService.refresh(refreshRequest));
    }
}
