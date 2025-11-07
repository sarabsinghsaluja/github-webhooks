package com.webhooks.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubWebhookController {

    @Value("${github.webhook.secret:}")
    private String webhookSecret;

    @PostMapping("/webhook/github")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
                                                @RequestHeader(value = "X-GitHub-Event", required = false) String event) {

        // Verify signature if secret is configured
        if (!webhookSecret.isEmpty() && !GithubSignatureVerifier.isValid(webhookSecret, payload, signature)) {
            System.out.println("Invalid signature!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }

        System.out.println("Event details: " + event);
        System.out.println("Payload: " + payload);
        return ResponseEntity.ok("ok");
    }
}
