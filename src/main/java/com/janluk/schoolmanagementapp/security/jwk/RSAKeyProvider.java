package com.janluk.schoolmanagementapp.security.jwk;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class RSAKeyProvider {

    private final RsaKeyProperties rsaKeyProperties;
    public static final String RSA = "RSA";

    public RSAPublicKey getRSAPublicKey() throws Exception {
        byte[] keyBytes = prepareKey(rsaKeyProperties.publicKey(), KeyType.PUBLIC);

        PublicKey publicKey =
                KeyFactory.getInstance(RSA)
                        .generatePublic(new X509EncodedKeySpec(keyBytes));

        return (RSAPublicKey) publicKey;
    }

    public RSAPrivateKey getRSAPrivateKey() throws Exception {
        byte[] keyBytes = prepareKey(rsaKeyProperties.privateKey(), KeyType.PRIVATE);

        PrivateKey privateKey =
                KeyFactory.getInstance(RSA)
                        .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));

        return (RSAPrivateKey) privateKey;
    }

    private String removeHeadersFromPEMFormat(String key, KeyType type) {
        return key
                .replace("-----BEGIN %s KEY-----".formatted(type.name()), "")
                .replace("-----END %s KEY-----".formatted(type.name()), "")
                .replaceAll("\\s", "");
    }

    private byte[] prepareKey(String key, KeyType type) {
        String pemKey = new String(Base64.getDecoder().decode(key));
        String base64Key = removeHeadersFromPEMFormat(pemKey, type);

        return Base64.getDecoder().decode(base64Key);
    }
}
