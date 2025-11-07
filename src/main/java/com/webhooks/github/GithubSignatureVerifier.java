package com.webhooks.github;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.HexFormat;

public class GithubSignatureVerifier {

    public static boolean isValid(String secret, String payload, String headerSig) {
        if (headerSig == null) return false;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
            byte[] hmac = mac.doFinal(payload.getBytes());
            String expected = "sha256=" + HexFormat.of().formatHex(hmac);
            return MessageDigest.isEqual(expected.getBytes(), headerSig.getBytes());
        } catch (Exception e) {
            return false;
        }
    }
}
