# GitHub Webhooks Server

A Spring Boot application that receives and processes GitHub webhooks with signature verification.

## What are Webhooks?

Webhooks are HTTP callbacks that allow services like GitHub to send real-time notifications to your application when events occur (e.g., push, pull request, issues). Instead of polling GitHub's API repeatedly, GitHub sends a POST request to your server when something happens.

## Features

- ✅ Receives GitHub webhook events
- ✅ HMAC-SHA256 signature verification for security
- ✅ Embedded Jetty server (no external server needed)
- ✅ Environment-based secret management

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Setup

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd github-webhooks
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   ```
   Edit `.env` and set your GitHub webhook secret:
   ```
   GITHUB_WEBHOOK_SECRET=your_actual_secret
   ```

3. **Run the application**
   ```bash
   source .env && mvn spring-boot:run
   ```

   The server will start on `http://localhost:8080`

## Exposing to GitHub

Since GitHub needs a public URL, use ngrok to expose your local server:

```bash
ngrok http 8080
```

Copy the ngrok HTTPS URL (e.g., `https://abc123.ngrok.io`) and use it in your GitHub webhook configuration.

## GitHub Webhook Configuration

1. Go to your GitHub repository → Settings → Webhooks → Add webhook
2. Set **Payload URL**: `https://your-ngrok-url.ngrok.io/webhook/github`
3. Set **Content type**: `application/json`
4. Set **Secret**: Same value as your `GITHUB_WEBHOOK_SECRET`
5. Choose events you want to receive
6. Click **Add webhook**

## How It Works

1. GitHub sends a POST request to `/webhook/github` when an event occurs
2. The request includes:
   - `X-GitHub-Event` header: Event type (push, pull_request, etc.)
   - `X-Hub-Signature-256` header: HMAC-SHA256 signature for verification
   - JSON payload: Event details
3. Our server verifies the signature using the shared secret
4. If valid, processes the event and logs details
5. Returns 200 OK to GitHub

## Project Structure

```
github-webhooks/
├── src/main/java/com/webhooks/github/
│   ├── Application.java              # Spring Boot entry point
│   ├── GithubWebhookController.java  # Webhook endpoint
│   └── GithubSignatureVerifier.java  # HMAC signature verification
├── src/main/resources/
│   └── application.properties        # Configuration
├── pom.xml                           # Maven dependencies
├── .env                              # Secret (gitignored)
└── .env.example                      # Environment template
```

## Building for Production

Create an executable JAR:
```bash
mvn clean package
```

Run the JAR:
```bash
java -jar target/github-webhooks-1.0.0.jar
```

Make sure to set the environment variable:
```bash
export GITHUB_WEBHOOK_SECRET=your_secret
java -jar target/github-webhooks-1.0.0.jar
```

## Security

- **Signature Verification**: All webhooks are verified using HMAC-SHA256
- **Secret Management**: Secrets are stored in `.env` (gitignored) and loaded via environment variables
- **HTTPS Required**: GitHub webhooks should use HTTPS in production (ngrok provides this automatically)

## Tech Stack

- **Spring Boot 3.2.0** - Application framework
- **Jetty** - Embedded web server
- **Maven** - Build tool
- **Java 17** - Programming language

## Learning Resources

- [GitHub Webhooks Documentation](https://docs.github.com/en/webhooks)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Securing Webhooks](https://docs.github.com/en/webhooks/using-webhooks/validating-webhook-deliveries)
