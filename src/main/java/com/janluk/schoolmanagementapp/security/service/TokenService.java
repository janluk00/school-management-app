package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    // TODO: Inject ROLES_CLAIM and ISSUER
    public static final String ROLES_CLAIM = "roles";
    public static final String ISSUER = "self";
    public static final Integer EXPIRATION_TIME = 900; // 15 minutes

    public String generateToken(Authentication authentication) {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .subject(authentication.getName())
                .expiresAt(Instant.now().plusSeconds(EXPIRATION_TIME))
                .claim(ROLES_CLAIM, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
