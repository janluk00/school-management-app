package com.janluk.schoolmanagementapp.security.jwk;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(String publicKey, String privateKey) {
}
