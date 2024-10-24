package com.janluk.schoolmanagementapp.security.controller;

import com.janluk.schoolmanagementapp.security.schema.LoginRequest;
import com.janluk.schoolmanagementapp.security.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("${api.prefix}/auth")
@AllArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        var authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        return tokenService.generateToken(authenticate);
    }
}
