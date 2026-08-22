# AIChatAPI

A Spring Boot REST API that uses **Google Gemini** through **Spring AI** to generate concise, structured technical answers to software development questions.

The API accepts a developer's question and returns a structured response containing a **topic, explanation, and example**.

## Features

* Generate AI-powered answers to programming and software development questions
* Uses Google Gemini through Spring AI
* Structured AI responses using `TechnicalAnswer`
* REST API endpoint for generating responses
* System and generation prompts stored as external text resources
* Configurable Gemini model, temperature, and maximum output tokens
* Environment-variable based API key configuration
* Maven-based project
* Spring Boot test configuration

## Tech Stack

* **Java 21**
* **Spring Boot 4.1.0**
* **Spring AI 2.0.0**
* **Google Gemini**
* **Spring Web MVC**
* **Lombok**
* **Maven**
* **JUnit / Spring Boot Test**

## Project Structure

```text
AIChatAPI/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── siddardha_007/
│   │   │           └── AIChatAPI/
│   │   │               ├── controller/
│   │   │               │   └── AiController.java
│   │   │               ├── dto/
│   │   │               │   ├── GenerateRequest.java
│   │   │               │   ├── GenerateResponse.java
│   │   │               │   └── TechnicalAnswer.java
│   │   │               ├── service/
│   │   │               │   └── AiService.java
│   │   │               └── AiChatApiApplication.java
│   │   └── resources/
│   │       ├── prompts/
│   │       │   ├── generate-prompt.txt
│   │       │   └── system-prompt.txt
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── siddardha_007/
│                   └── AIChatAPI/
│                       └── AiChatApiApplicationTests.java
├── .gitignore
├── pom.xml
└── README.md
```

## How It Works

The application follows a simple request-response flow:

```text
Client
  |
  | POST /api/ai/generate
  | JSON request
  v
AiController
  |
  v
AiService
  |
  | Spring AI ChatClient
  v
Google Gemini
  |
  | Structured response
  v
TechnicalAnswer
  |
  v
JSON Response
```

### Request Flow

1. A client sends a `POST` request to `/api/ai/generate`.
2. `AiController` receives the request.
3. The request body is mapped to `GenerateRequest`.
4. `AiService` sends the user's request to Google Gemini through Spring AI's `ChatClient`.
5. The AI response is mapped to the `TechnicalAnswer` record.
6. The API returns the structured response as JSON.

## API Endpoint

### Generate Technical Answer

**POST**

```text
/api/ai/generate
```

### Request Body

```json
{
  "request": "What is a REST API?"
}
```

### Response

The API returns a structured `TechnicalAnswer`:

```json
{
  "topic": "REST API",
  "explanation": "A REST API is an interface that allows applications to communicate over HTTP using standard methods such as GET, POST, PUT, and DELETE.",
  "example": "A client can send a GET request to /api/users to retrieve a list of users."
}
```

## Request DTO

The `GenerateRequest` class accepts a single field:

```java
private String request;
```

Example:

```json
{
  "request": "Explain dependency injection in Spring Boot"
}
```

## Response DTO

The API uses the `TechnicalAnswer` record for structured AI output:

```java
public record TechnicalAnswer(
        String topic,
        String explanation,
        String example
) {
}
```

The response therefore contains three fields:

* `topic` — The main technical concept
* `explanation` — A concise explanation of the concept
* `example` — A practical example

## Google Gemini Configuration

The application uses the Google Gemini integration provided by Spring AI.

The API key is loaded from an environment variable:

```properties
spring.ai.google.genai.api-key=${GEMINI_API_KEY}
```

The configured model is:

```properties
spring.ai.google.genai.chat.model=gemini-3.5-flash
```

Additional configuration:

```properties
spring.ai.google.genai.chat.temperature=0.5
spring.ai.google.genai.chat.max-output-tokens=1000
```

### Configuration Explanation

| Property                                        | Purpose                            |
| ----------------------------------------------- | ---------------------------------- |
| `GEMINI_API_KEY`                                | Google Gemini API key              |
| `spring.ai.google.genai.chat.model`             | Gemini model used for generation   |
| `spring.ai.google.genai.chat.temperature`       | Controls response randomness       |
| `spring.ai.google.genai.chat.max-output-tokens` | Limits the generated output length |

## Environment Setup

Before running the application, configure your Gemini API key.

### macOS / Linux

```bash
export GEMINI_API_KEY="your-api-key"
```

### Windows PowerShell

```powershell
$env:GEMINI_API_KEY="your-api-key"
```

Do not commit your actual API key to GitHub.

## Prerequisites

Make sure you have installed:

* Java 21 or later
* Maven
* A Google Gemini API key

You can verify Java installation with:

```bash
java -version
```

## Installation

Clone the repository:

```bash
git clone https://github.com/<your-username>/AIChatAPI.git
```

Navigate into the project:

```bash
cd AIChatAPI
```

Set your Gemini API key:

```bash
export GEMINI_API_KEY="your-api-key"
```

Build the project:

```bash
./mvnw clean install
```

## Running the Application

Start the Spring Boot application using Maven:

```bash
./mvnw spring-boot:run
```

The application will start as a Spring Boot application.

You can also run the generated JAR file after building the project:

```bash
java -jar target/AIChatAPI-0.0.1-SNAPSHOT.jar
```

## Testing the API

You can test the endpoint using Postman, cURL, or any API client.

### Using cURL

```bash
curl -X POST http://localhost:8080/api/ai/generate \
  -H "Content-Type: application/json" \
  -d '{
    "request": "Explain what dependency injection is in Spring Boot"
  }'
```

Example response:

```json
{
  "topic": "Dependency Injection",
  "explanation": "Dependency injection is a design pattern where required dependencies are provided to a class instead of the class creating them itself.",
  "example": "Spring Boot can inject an AiService into a controller through constructor injection."
}
```

## Prompt Configuration

The project keeps its AI prompts in separate resource files:

```text
src/main/resources/prompts/
├── system-prompt.txt
└── generate-prompt.txt
```

This allows the application's AI instructions to be maintained separately from the Java source code.

The `AiService` loads the prompt resources using Spring's `Resource` abstraction and passes the generation prompt to the Spring AI `ChatClient`.

## AI Service

The main AI integration is handled by `AiService`.

The service creates a Spring AI `ChatClient`:

```java
this.chatClient = chatClientBuilder.build();
```

It then sends the user's request to the configured Gemini model:

```java
return chatClient
        .prompt()
        .system(generatePrompt)
        .user(prompt)
        .call()
        .entity(
                TechnicalAnswer.class,
                spec -> spec.useProviderStructuredOutput()
        );
```

The use of `TechnicalAnswer.class` allows the generated response to be mapped into the application's structured response type.

## Controller

The REST controller exposes the AI endpoint:

```java
@RestController
@RequestMapping("/api/ai")
public class AiController {
```

The generation endpoint is:

```java
@PostMapping("/generate")
```

It receives the request and passes the user's input to `AiService`:

```java
return aiService.generate(request.getRequest());
```

## Running Tests

The project includes Spring Boot tests.

Run the tests with:

```bash
./mvnw test
```

The existing test verifies that the Spring application context can load successfully.

## Maven Commands

### Clean the project

```bash
./mvnw clean
```

### Compile the project

```bash
./mvnw compile
```

### Run tests

```bash
./mvnw test
```

### Build the project

```bash
./mvnw clean install
```

### Run the application

```bash
./mvnw spring-boot:run
```

## Example Use Cases

This API can be used for developer-focused questions such as:

```text
What is dependency injection in Spring Boot?
```

```text
Explain how JWT authentication works.
```

```text
What is the difference between SQL and NoSQL databases?
```

```text
Explain the difference between an interface and an abstract class in Java.
```

The API is designed to return concise answers with an explanation and a practical example.

## Security

The Gemini API key should be provided through an environment variable:

```text
GEMINI_API_KEY
```

Never commit API keys, passwords, tokens, or other secrets to the repository.

## Future Improvements

Potential improvements include:

* Add authentication and authorization
* Add request validation
* Add centralized exception handling
* Add API documentation using OpenAPI / Swagger
* Add more comprehensive unit and integration tests
* Add conversation history
* Add rate limiting
* Add logging and monitoring
* Support additional AI providers or models
* Add frontend integration

## License

This project currently does not specify a license.

If you plan to make the project open source, consider adding an appropriate license such as the MIT License.

---

## Author

Developed as an AI-powered technical answer API using Spring Boot, Spring AI, and Google Gemini.
