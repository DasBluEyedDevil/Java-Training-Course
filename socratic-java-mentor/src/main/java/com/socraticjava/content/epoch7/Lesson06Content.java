package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.6: Exception Handling and Validation in Spring Boot
 */
public class Lesson06Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-6", "Lesson 7.6: Exception Handling - Failing Gracefully")
            .addTheory("The Problem: Ugly Error Messages",
                "Without proper exception handling:\n\n" +
                "@GetMapping(\"/users/{id}\")\n" +
                "public User getUser(@PathVariable Long id) {\n" +
                "    User user = userRepository.findById(id).orElse(null);\n" +
                "    return user;  // What if user is null?\n" +
                "}\n\n" +
                "When user doesn't exist:\n" +
                "{\n" +
                "  \"timestamp\": \"2025-01-15T10:30:00\",\n" +
                "  \"status\": 500,\n" +
                "  \"error\": \"Internal Server Error\",\n" +
                "  \"trace\": \"java.lang.NullPointerException\\n\\tat...\"\n" +
                "}\n\n" +
                "PROBLEMS:\n" +
                "❌ Exposes stack traces (security risk!)\n" +
                "❌ Wrong status code (500 instead of 404)\n" +
                "❌ Unhelpful message for clients\n" +
                "❌ No consistent error format\n\n" +
                "Professional APIs need:\n" +
                "✓ Clear, consistent error messages\n" +
                "✓ Correct HTTP status codes\n" +
                "✓ No sensitive data exposed\n" +
                "✓ Structured JSON responses")
            .addAnalogy("Exception Handling is Like a Customer Service Desk",
                "BAD CUSTOMER SERVICE (No Exception Handling):\n" +
                "Customer: \"I can't find product #999\"\n" +
                "System: *CRASHES* *SHOWS INTERNAL ERROR LOGS*\n" +
                "Customer sees: Stack traces, database errors, system paths\n\n" +
                "GOOD CUSTOMER SERVICE (Proper Exception Handling):\n" +
                "Customer: \"I can't find product #999\"\n" +
                "Service: \"Product #999 not found. Please check the ID or browse our catalog.\"\n" +
                "- Clear message\n" +
                "- Helpful guidance\n" +
                "- No internal system details exposed\n\n" +
                "@RestControllerAdvice = Customer Service Manager:\n" +
                "- Intercepts all errors\n" +
                "- Translates technical problems into user-friendly messages\n" +
                "- Returns appropriate responses\n" +
                "- Logs details for developers")
            .addTheory("Custom Exceptions - Creating Meaningful Errors",
                "Create custom exceptions for business logic:\n\n" +
                "public class ResourceNotFoundException extends RuntimeException {\n" +
                "    public ResourceNotFoundException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n\n" +
                "public class InvalidRequestException extends RuntimeException {\n" +
                "    public InvalidRequestException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n\n" +
                "Use in controller:\n\n" +
                "@GetMapping(\"/users/{id}\")\n" +
                "public User getUser(@PathVariable Long id) {\n" +
                "    return userRepository.findById(id)\n" +
                "        .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "            \"User not found with id: \" + id\n" +
                "        ));\n" +
                "}\n\n" +
                "Now when user doesn't exist, throw ResourceNotFoundException\n" +
                "instead of returning null!")
            .addTheory("@RestControllerAdvice - Global Exception Handler",
                "Centralize ALL exception handling in one place:\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    // Handle ResourceNotFoundException\n" +
                "    @ExceptionHandler(ResourceNotFoundException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {\n" +
                "        ErrorResponse error = new ErrorResponse(\n" +
                "            HttpStatus.NOT_FOUND.value(),\n" +
                "            ex.getMessage(),\n" +
                "            System.currentTimeMillis()\n" +
                "        );\n" +
                "        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);\n" +
                "    }\n" +
                "    \n" +
                "    // Handle InvalidRequestException\n" +
                "    @ExceptionHandler(InvalidRequestException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleBadRequest(InvalidRequestException ex) {\n" +
                "        ErrorResponse error = new ErrorResponse(\n" +
                "            HttpStatus.BAD_REQUEST.value(),\n" +
                "            ex.getMessage(),\n" +
                "            System.currentTimeMillis()\n" +
                "        );\n" +
                "        return ResponseEntity.badRequest().body(error);\n" +
                "    }\n" +
                "    \n" +
                "    // Handle all other exceptions (fallback)\n" +
                "    @ExceptionHandler(Exception.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {\n" +
                "        // Log full error for developers\n" +
                "        System.err.println(\"Unexpected error: \" + ex.getMessage());\n" +
                "        \n" +
                "        // Return safe message to client\n" +
                "        ErrorResponse error = new ErrorResponse(\n" +
                "            HttpStatus.INTERNAL_SERVER_ERROR.value(),\n" +
                "            \"An unexpected error occurred. Please try again later.\",\n" +
                "            System.currentTimeMillis()\n" +
                "        );\n" +
                "        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);\n" +
                "    }\n" +
                "}\n\n" +
                "@RestControllerAdvice:\n" +
                "- Applies to ALL @RestController classes\n" +
                "- Catches exceptions from any controller\n" +
                "- Returns consistent error format")
            .addTheory("Error Response Object - Structured Errors",
                "Create a standard error response format:\n\n" +
                "public class ErrorResponse {\n" +
                "    private int status;\n" +
                "    private String message;\n" +
                "    private long timestamp;\n" +
                "    \n" +
                "    public ErrorResponse(int status, String message, long timestamp) {\n" +
                "        this.status = status;\n" +
                "        this.message = message;\n" +
                "        this.timestamp = timestamp;\n" +
                "    }\n" +
                "    \n" +
                "    // Getters and setters\n" +
                "}\n\n" +
                "Now ALL errors return this format:\n\n" +
                "{\n" +
                "  \"status\": 404,\n" +
                "  \"message\": \"User not found with id: 999\",\n" +
                "  \"timestamp\": 1705315800000\n" +
                "}\n\n" +
                "Clients can reliably parse errors!")
            .addTheory("Validation with @Valid and @Validated",
                "Validate request bodies automatically:\n\n" +
                "Entity with validation:\n\n" +
                "import jakarta.validation.constraints.*;\n\n" +
                "public class User {\n" +
                "    @NotNull(message = \"ID is required\")\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @NotBlank(message = \"Name cannot be empty\")\n" +
                "    @Size(min = 2, max = 100, message = \"Name must be 2-100 characters\")\n" +
                "    private String name;\n" +
                "    \n" +
                "    @Email(message = \"Invalid email format\")\n" +
                "    private String email;\n" +
                "    \n" +
                "    @Min(value = 18, message = \"Must be at least 18 years old\")\n" +
                "    private int age;\n" +
                "    \n" +
                "    // Constructor, getters, setters\n" +
                "}\n\n" +
                "Controller with validation:\n\n" +
                "@PostMapping(\"/users\")\n" +
                "public ResponseEntity<User> createUser(@Valid @RequestBody User user) {\n" +
                "    // If validation fails, Spring throws MethodArgumentNotValidException\n" +
                "    User saved = userService.save(user);\n" +
                "    return ResponseEntity.status(HttpStatus.CREATED).body(saved);\n" +
                "}\n\n" +
                "@Valid triggers validation automatically!")
            .addTheory("Handling Validation Errors",
                "Catch validation exceptions in @RestControllerAdvice:\n\n" +
                "@ExceptionHandler(MethodArgumentNotValidException.class)\n" +
                "public ResponseEntity<ValidationErrorResponse> handleValidation(\n" +
                "        MethodArgumentNotValidException ex) {\n" +
                "    \n" +
                "    // Collect all validation errors\n" +
                "    List<String> errors = ex.getBindingResult()\n" +
                "        .getFieldErrors()\n" +
                "        .stream()\n" +
                "        .map(error -> error.getField() + \": \" + error.getDefaultMessage())\n" +
                "        .toList();\n" +
                "    \n" +
                "    ValidationErrorResponse response = new ValidationErrorResponse(\n" +
                "        HttpStatus.BAD_REQUEST.value(),\n" +
                "        \"Validation failed\",\n" +
                "        errors,\n" +
                "        System.currentTimeMillis()\n" +
                "    );\n" +
                "    \n" +
                "    return ResponseEntity.badRequest().body(response);\n" +
                "}\n\n" +
                "ValidationErrorResponse class:\n\n" +
                "public class ValidationErrorResponse {\n" +
                "    private int status;\n" +
                "    private String message;\n" +
                "    private List<String> errors;\n" +
                "    private long timestamp;\n" +
                "    \n" +
                "    // Constructor, getters, setters\n" +
                "}\n\n" +
                "Response when validation fails:\n\n" +
                "{\n" +
                "  \"status\": 400,\n" +
                "  \"message\": \"Validation failed\",\n" +
                "  \"errors\": [\n" +
                "    \"name: Name cannot be empty\",\n" +
                "    \"email: Invalid email format\",\n" +
                "    \"age: Must be at least 18 years old\"\n" +
                "  ],\n" +
                "  \"timestamp\": 1705315800000\n" +
                "}")
            .addKeyPoint("Common Validation Annotations",
                "Jakarta Validation (javax.validation → jakarta.validation in Spring Boot 3):\n\n" +
                "NULL/NOT NULL:\n" +
                "@NotNull - Value cannot be null\n" +
                "@Null - Value must be null\n\n" +
                "STRINGS:\n" +
                "@NotBlank - Not null, not empty, not whitespace\n" +
                "@NotEmpty - Not null and not empty (but can be whitespace)\n" +
                "@Size(min, max) - Length constraints\n" +
                "@Pattern(regexp) - Must match regex\n\n" +
                "NUMBERS:\n" +
                "@Min(value) - Minimum value\n" +
                "@Max(value) - Maximum value\n" +
                "@Positive - Must be > 0\n" +
                "@PositiveOrZero - Must be >= 0\n" +
                "@Negative - Must be < 0\n" +
                "@NegativeOrZero - Must be <= 0\n\n" +
                "FORMAT:\n" +
                "@Email - Valid email format\n" +
                "@Past - Date in the past\n" +
                "@Future - Date in the future\n\n" +
                "Example:\n\n" +
                "public class Product {\n" +
                "    @NotBlank\n" +
                "    @Size(min = 3, max = 100)\n" +
                "    private String name;\n" +
                "    \n" +
                "    @Positive\n" +
                "    private double price;\n" +
                "    \n" +
                "    @Min(0)\n" +
                "    @Max(10000)\n" +
                "    private int stockQuantity;\n" +
                "}")
            .addWarning("Security: Never Expose Stack Traces!",
                "❌ BAD - Exposing internal details:\n\n" +
                "@ExceptionHandler(Exception.class)\n" +
                "public ResponseEntity<String> handleError(Exception ex) {\n" +
                "    return ResponseEntity.status(500)\n" +
                "        .body(\"Error: \" + ex.getMessage() + \"\\n\" + \n" +
                "              Arrays.toString(ex.getStackTrace()));\n" +
                "}\n\n" +
                "This exposes:\n" +
                "- Internal class names\n" +
                "- File paths\n" +
                "- Database structure\n" +
                "- Library versions\n" +
                "→ Helps attackers!\n\n" +
                "✓ GOOD - Safe error handling:\n\n" +
                "@ExceptionHandler(Exception.class)\n" +
                "public ResponseEntity<ErrorResponse> handleError(Exception ex) {\n" +
                "    // Log full details for developers (server-side only)\n" +
                "    logger.error(\"Unexpected error\", ex);\n" +
                "    \n" +
                "    // Return safe, generic message to client\n" +
                "    ErrorResponse error = new ErrorResponse(\n" +
                "        500,\n" +
                "        \"An unexpected error occurred\",\n" +
                "        System.currentTimeMillis()\n" +
                "    );\n" +
                "    return ResponseEntity.status(500).body(error);\n" +
                "}\n\n" +
                "BEST PRACTICES:\n" +
                "✓ Log full errors server-side (for debugging)\n" +
                "✓ Return generic messages to clients\n" +
                "✓ Use error codes/IDs to correlate logs\n" +
                "✓ Never expose paths, SQL, or stack traces")
            .addExample("Complete Exception Handling System",
                "1. Custom Exceptions:\n\n" +
                "public class UserNotFoundException extends RuntimeException {\n" +
                "    public UserNotFoundException(Long id) {\n" +
                "        super(\"User not found with id: \" + id);\n" +
                "    }\n" +
                "}\n\n" +
                "public class DuplicateEmailException extends RuntimeException {\n" +
                "    public DuplicateEmailException(String email) {\n" +
                "        super(\"Email already exists: \" + email);\n" +
                "    }\n" +
                "}\n\n" +
                "2. Error Response Classes:\n\n" +
                "public record ErrorResponse(\n" +
                "    int status,\n" +
                "    String message,\n" +
                "    long timestamp\n" +
                ") { }\n\n" +
                "public record ValidationErrorResponse(\n" +
                "    int status,\n" +
                "    String message,\n" +
                "    List<String> errors,\n" +
                "    long timestamp\n" +
                ") { }\n\n" +
                "3. Global Exception Handler:\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    @ExceptionHandler(UserNotFoundException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {\n" +
                "        return ResponseEntity.status(HttpStatus.NOT_FOUND)\n" +
                "            .body(new ErrorResponse(404, ex.getMessage(), System.currentTimeMillis()));\n" +
                "    }\n" +
                "    \n" +
                "    @ExceptionHandler(DuplicateEmailException.class)\n" +
                "    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEmailException ex) {\n" +
                "        return ResponseEntity.status(HttpStatus.CONFLICT)\n" +
                "            .body(new ErrorResponse(409, ex.getMessage(), System.currentTimeMillis()));\n" +
                "    }\n" +
                "    \n" +
                "    @ExceptionHandler(MethodArgumentNotValidException.class)\n" +
                "    public ResponseEntity<ValidationErrorResponse> handleValidation(\n" +
                "            MethodArgumentNotValidException ex) {\n" +
                "        List<String> errors = ex.getBindingResult().getFieldErrors()\n" +
                "            .stream()\n" +
                "            .map(err -> err.getField() + \": \" + err.getDefaultMessage())\n" +
                "            .toList();\n" +
                "        \n" +
                "        return ResponseEntity.badRequest()\n" +
                "            .body(new ValidationErrorResponse(400, \"Validation failed\", \n" +
                "                  errors, System.currentTimeMillis()));\n" +
                "    }\n" +
                "}\n\n" +
                "4. Controller Using Exceptions:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/users\")\n" +
                "public class UserController {\n" +
                "    private final UserService userService;\n" +
                "    \n" +
                "    public UserController(UserService userService) {\n" +
                "        this.userService = userService;\n" +
                "    }\n" +
                "    \n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public User getUser(@PathVariable Long id) {\n" +
                "        // Throws UserNotFoundException if not found\n" +
                "        return userService.findById(id);\n" +
                "    }\n" +
                "    \n" +
                "    @PostMapping\n" +
                "    public User createUser(@Valid @RequestBody User user) {\n" +
                "        // @Valid triggers validation\n" +
                "        // Throws DuplicateEmailException if email exists\n" +
                "        return userService.create(user);\n" +
                "    }\n" +
                "}")
            .addChallenge(createExceptionQuiz())
            .addChallenge(createValidationQuiz())
            .addChallenge(createSecurityQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createExceptionQuiz() {
        return new Challenge.Builder("epoch-7-lesson-6-exception", "Exception Handling", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the purpose of @RestControllerAdvice?")
            .addMultipleChoiceOption("A) To create REST controllers")
            .addMultipleChoiceOption("B) To centralize exception handling across all controllers")
            .addMultipleChoiceOption("C) To validate request data")
            .addMultipleChoiceOption("D) To configure application properties")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createValidationQuiz() {
        return new Challenge.Builder("epoch-7-lesson-6-validation", "Bean Validation", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation triggers validation in a controller method parameter?")
            .addMultipleChoiceOption("A) @Validated")
            .addMultipleChoiceOption("B) @Valid")
            .addMultipleChoiceOption("C) @Check")
            .addMultipleChoiceOption("D) @Validate")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-7-lesson-6-security", "Security Best Practice", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should you never expose stack traces in API error responses?")
            .addMultipleChoiceOption("A) Stack traces make responses too large")
            .addMultipleChoiceOption("B) Stack traces are hard to read")
            .addMultipleChoiceOption("C) Stack traces expose internal system details that help attackers")
            .addMultipleChoiceOption("D) Stack traces slow down the API")
            .correctAnswer("C")
            .build();
    }
}
