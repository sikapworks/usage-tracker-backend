package com.example.UsageAppBackend.controller;

import com.example.UsageAppBackend.entity.AuthRequest;
import com.example.UsageAppBackend.entity.UserInfo;
import com.example.UsageAppBackend.service.JwtService;
import com.example.UsageAppBackend.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome! This is not a secure endpoint";
    }

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return userInfoService.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String AuthenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }

    }
}
