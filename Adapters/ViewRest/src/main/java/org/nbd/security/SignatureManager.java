package org.nbd.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SignatureManager {

    private final String secret;

    public SignatureManager(@Value("${jws.secret}") String secret) {
        this.secret = secret;
    }

    public String sign(String payload) {
        try {
            JWSSigner signer = new MACSigner(secret);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Błąd podczas podpisywania danych", e);
        }
    }

    public boolean verify(String token, String expectedPayload) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);
            return jwsObject.verify(verifier) && jwsObject.getPayload().toString().equals(expectedPayload);
        } catch (Exception e) {
            return false;
        }
    }
}