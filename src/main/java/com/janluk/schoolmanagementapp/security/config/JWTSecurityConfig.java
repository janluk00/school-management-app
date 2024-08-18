package com.janluk.schoolmanagementapp.security.config;

import com.janluk.schoolmanagementapp.security.jwk.RSAKeyProvider;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@RequiredArgsConstructor
class JWTSecurityConfig {

    private final RSAKeyProvider rsaKeyProvider;

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProvider.getRSAPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        JWK jwk = new RSAKey.Builder(rsaKeyProvider.getRSAPublicKey())
                .privateKey(rsaKeyProvider.getRSAPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
