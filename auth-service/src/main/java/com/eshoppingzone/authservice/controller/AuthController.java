package com.eshoppingzone.authservice.controller;

import com.eshoppingzone.authservice.dto.AuthRequest;
import com.eshoppingzone.authservice.entity.UserInfo;
import com.eshoppingzone.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addUser(@RequestBody UserInfo userInfo) {
        return authService.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return authService.generateToken(authRequest.getEmail());
        } else {
            throw new RuntimeException("Invalid credentials.");
        }
    }

    @GetMapping("/validateToken")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }

}
