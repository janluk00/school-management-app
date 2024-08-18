package com.janluk.schoolmanagementapp.security.controller;

import com.janluk.schoolmanagementapp.security.schema.ConfirmPasswordRequest;
import com.janluk.schoolmanagementapp.security.schema.EmailRequest;
import com.janluk.schoolmanagementapp.security.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/account")
@RequiredArgsConstructor
class AccountController {

    private final AccountService accountService;

    @PostMapping("/confirm/{token}")
    public ResponseEntity<String> confirmPassword(
            @PathVariable String token,
            @RequestBody @Valid ConfirmPasswordRequest request
    ) {
        accountService.confirmPassword(token, request);

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody EmailRequest email) {
        accountService.changePassword(email.email());
    }
}
