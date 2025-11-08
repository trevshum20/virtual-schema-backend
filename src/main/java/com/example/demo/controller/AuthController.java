package com.example.demo.controller;

import com.example.demo.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.example.demo.auth.LoginRequest;
import com.example.demo.auth.LoginResponse;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthController(AuthenticationManager authManager, JwtService jwt) {
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.username, req.password);
        authManager.authenticate(authToken); // throws if bad creds

        var token = jwt.generateToken(req.username);
        // quick decode to get exp (optional; you could also compute same as JwtService)
        var exp = Instant.now().toEpochMilli() + 60L * 60_000L; // replace if TTL differs
        return ResponseEntity.ok(new LoginResponse(token, exp));
    }
}
