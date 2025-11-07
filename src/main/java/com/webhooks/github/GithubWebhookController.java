package com.webhooks.github;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubWebhookController {

    @PostMapping("/webhook/github")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
                                                @RequestHeader(value = "X-GitHub-Event", required = false) String event) {
        System.out.println("Event: " + event);
        System.out.println("Payload: " + payload);
        // TODO: verify signature
        return ResponseEntity.ok("ok");
    }
}
