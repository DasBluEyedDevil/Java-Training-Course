package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.4: Error Handling Across the Full Stack
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-4", "Lesson 8.4: Error Handling - From Database to UI")
            .addTheory("The Full-Stack Error Journey",
                "Errors can occur at ANY layer:\n\n" +
                "DATABASE:\n" +
                "- Connection timeout\n" +
                "- Constraint violation (duplicate email)\n" +
                "- Transaction rollback\n\n" +
                "BACKEND (Spring Boot):\n" +
                "- Validation error (@NotBlank, @Email)\n" +
                "- Business logic error (insufficient funds)\n" +
                "- Resource not found (user ID doesn't exist)\n" +
                "- Authentication/authorization failure\n\n" +
                "NETWORK:\n" +
                "- 500 Internal Server Error\n" +
                "- 404 Not Found\n" +
                "- Timeout\n\n" +
                "FRONTEND (JavaScript):\n" +
                "- Network failure (no internet)\n" +
                "- JSON parsing error\n" +
                "- Display error to user\n\n" +
                "GOAL: Catch errors everywhere, provide clear feedback!")
            .addAnalogy("Error Handling is Like a Hospital Emergency Response",
                "PATIENT EMERGENCY (Error occurs):\n\n" +
                "1. FIRST RESPONDERS (Try-Catch at source):\n" +
                "   Paramedics identify the problem immediately\n" +
                "   Like: Validation catches bad input right away\n\n" +
                "2. EMERGENCY ROOM (Backend exception handler):\n" +
                "   Doctors diagnose and treat the condition\n" +
                "   Like: @RestControllerAdvice catches all exceptions\n" +
                "   Returns: Structured diagnosis (error code + message)\n\n" +
                "3. FAMILY NOTIFICATION (Frontend UI):\n" +
                "   Family gets clear, understandable information\n" +
                "   Like: User sees \"Email already exists\"\n" +
                "   NOT: \"SQLException: Duplicate key violation on idx_email\"\n\n" +
                "4. MEDICAL RECORDS (Logging):\n" +
                "   Everything documented for future analysis\n" +
                "   Like: Server logs stack traces for debugging\n" +
                "   User NEVER sees stack traces (security risk!)")
            .addTheory("Backend: RFC 7807 Problem Details (Spring Boot 3+)",
                "Spring Framework 6 introduced ProblemDetail (RFC 7807 standard):\n\n" +
                "Standard error response format:\n" +
                "{\n" +
                "  \"type\": \"https://api.example.com/errors/not-found\",\n" +
                "  \"title\": \"Resource Not Found\",\n" +
                "  \"status\": 404,\n" +
                "  \"detail\": \"User with ID 123 does not exist\",\n" +
                "  \"instance\": \"/api/users/123\",\n" +
                "  \"timestamp\": \"2025-01-15T10:30:00Z\"\n" +
                "}\n\n" +
                "Implementation:\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    @ExceptionHandler(ResourceNotFoundException.class)\n" +
                "    public ProblemDetail handleNotFound(ResourceNotFoundException ex,\n" +
                "                                         HttpServletRequest request) {\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.NOT_FOUND,\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        problem.setType(URI.create(\"https://api.example.com/errors/not-found\"));\n" +
                "        problem.setTitle(\"Resource Not Found\");\n" +
                "        problem.setProperty(\"timestamp\", Instant.now());\n" +
                "        return problem;\n" +
                "    }\n" +
                "    \n" +
                "    @ExceptionHandler(MethodArgumentNotValidException.class)\n" +
                "    public ProblemDetail handleValidation(\n" +
                "            MethodArgumentNotValidException ex) {\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.BAD_REQUEST,\n" +
                "            \"Validation failed\"\n" +
                "        );\n" +
                "        problem.setTitle(\"Invalid Input\");\n" +
                "        \n" +
                "        // Extract validation errors\n" +
                "        Map<String, String> errors = new HashMap<>();\n" +
                "        ex.getBindingResult().getFieldErrors().forEach(error -> {\n" +
                "            errors.put(error.getField(), error.getDefaultMessage());\n" +
                "        });\n" +
                "        problem.setProperty(\"errors\", errors);\n" +
                "        return problem;\n" +
                "    }\n" +
                "}")
            .addTheory("Custom Business Exceptions",
                "Create meaningful exception classes:\n\n" +
                "// Base exception\n" +
                "public class BusinessException extends RuntimeException {\n" +
                "    private final String errorCode;\n" +
                "    \n" +
                "    public BusinessException(String message, String errorCode) {\n" +
                "        super(message);\n" +
                "        this.errorCode = errorCode;\n" +
                "    }\n" +
                "    \n" +
                "    public String getErrorCode() {\n" +
                "        return errorCode;\n" +
                "    }\n" +
                "}\n\n" +
                "// Specific exceptions\n" +
                "public class ResourceNotFoundException extends BusinessException {\n" +
                "    public ResourceNotFoundException(String resource, Long id) {\n" +
                "        super(resource + \" with ID \" + id + \" not found\", \"RESOURCE_NOT_FOUND\");\n" +
                "    }\n" +
                "}\n\n" +
                "public class DuplicateResourceException extends BusinessException {\n" +
                "    public DuplicateResourceException(String message) {\n" +
                "        super(message, \"DUPLICATE_RESOURCE\");\n" +
                "    }\n" +
                "}\n\n" +
                "public class InsufficientFundsException extends BusinessException {\n" +
                "    public InsufficientFundsException(double available, double required) {\n" +
                "        super(\"Insufficient funds. Available: \" + available + \n" +
                "              \", Required: \" + required, \"INSUFFICIENT_FUNDS\");\n" +
                "    }\n" +
                "}\n\n" +
                "Usage in service:\n\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    \n" +
                "    public User findById(Long id) {\n" +
                "        return userRepository.findById(id)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\"User\", id));\n" +
                "    }\n" +
                "    \n" +
                "    public User create(User user) {\n" +
                "        if (userRepository.existsByEmail(user.getEmail())) {\n" +
                "            throw new DuplicateResourceException(\n" +
                "                \"Email \" + user.getEmail() + \" is already registered\"\n" +
                "            );\n" +
                "        }\n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}")
            .addKeyPoint("Security: Never Expose Stack Traces!",
                "❌ BAD - Exposes internal details:\n" +
                "{\n" +
                "  \"error\": \"java.sql.SQLException: Duplicate entry 'alice@email.com'\",\n" +
                "  \"stackTrace\": [\n" +
                "    \"at com.example.UserRepository.save(UserRepository.java:42)\",\n" +
                "    \"at com.example.UserService.create(UserService.java:28)\",\n" +
                "    ...\n" +
                "  ]\n" +
                "}\n\n" +
                "SECURITY RISKS:\n" +
                "- Reveals database structure\n" +
                "- Shows internal file paths\n" +
                "- Exposes technology stack\n" +
                "- Helps attackers understand your system\n\n" +
                "✓ GOOD - User-friendly message:\n" +
                "{\n" +
                "  \"status\": 409,\n" +
                "  \"title\": \"Duplicate Resource\",\n" +
                "  \"detail\": \"This email address is already registered\",\n" +
                "  \"errorCode\": \"DUPLICATE_RESOURCE\",\n" +
                "  \"timestamp\": \"2025-01-15T10:30:00Z\"\n" +
                "}\n\n" +
                "Meanwhile, on the SERVER:\n" +
                "LOG: Full stack trace for developers\n" +
                "LOG: Request details, user ID, timestamp\n" +
                "LOG: SQL query that failed\n\n" +
                "Implementation:\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    private static final Logger logger = \n" +
                "        LoggerFactory.getLogger(GlobalExceptionHandler.class);\n" +
                "    \n" +
                "    @ExceptionHandler(Exception.class)\n" +
                "    public ProblemDetail handleGenericException(\n" +
                "            Exception ex, HttpServletRequest request) {\n" +
                "        \n" +
                "        // LOG everything for developers\n" +
                "        logger.error(\"Unexpected error at {}: {}\", \n" +
                "                    request.getRequestURI(), ex.getMessage(), ex);\n" +
                "        \n" +
                "        // RETURN safe message to user\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.INTERNAL_SERVER_ERROR,\n" +
                "            \"An unexpected error occurred. Please try again later.\"\n" +
                "        );\n" +
                "        problem.setTitle(\"Internal Server Error\");\n" +
                "        \n" +
                "        // Include error ID for support team\n" +
                "        String errorId = UUID.randomUUID().toString();\n" +
                "        problem.setProperty(\"errorId\", errorId);\n" +
                "        logger.error(\"Error ID: {}\", errorId);\n" +
                "        \n" +
                "        return problem;\n" +
                "    }\n" +
                "}")
            .addTheory("Frontend: Handling Errors in JavaScript",
                "MODERN APPROACH - async/await with try-catch:\n\n" +
                "async function createUser(userData) {\n" +
                "    try {\n" +
                "        const response = await fetch('http://localhost:8080/api/users', {\n" +
                "            method: 'POST',\n" +
                "            headers: { 'Content-Type': 'application/json' },\n" +
                "            body: JSON.stringify(userData)\n" +
                "        });\n" +
                "        \n" +
                "        // Check HTTP status\n" +
                "        if (!response.ok) {\n" +
                "            // Parse error response\n" +
                "            const error = await response.json();\n" +
                "            \n" +
                "            // Handle different error types\n" +
                "            if (response.status === 409) {\n" +
                "                displayError('Email already exists. Please use a different email.');\n" +
                "            } else if (response.status === 400) {\n" +
                "                displayValidationErrors(error.errors);\n" +
                "            } else if (response.status === 500) {\n" +
                "                displayError('Server error. Please try again later.');\n" +
                "            } else {\n" +
                "                displayError(error.detail || 'An error occurred');\n" +
                "            }\n" +
                "            return null;\n" +
                "        }\n" +
                "        \n" +
                "        // Success\n" +
                "        const user = await response.json();\n" +
                "        displaySuccess('User created successfully!');\n" +
                "        return user;\n" +
                "        \n" +
                "    } catch (error) {\n" +
                "        // Network error (no response from server)\n" +
                "        if (error.name === 'TypeError' && error.message.includes('fetch')) {\n" +
                "            displayError('Cannot connect to server. Check your internet connection.');\n" +
                "        } else {\n" +
                "            displayError('An unexpected error occurred: ' + error.message);\n" +
                "        }\n" +
                "        console.error('Error creating user:', error);\n" +
                "        return null;\n" +
                "    }\n" +
                "}")
            .addTheory("Displaying Errors to Users",
                "Create user-friendly error displays:\n\n" +
                "HTML:\n" +
                "<div id=\"errorContainer\" class=\"error-message\" style=\"display: none;\">\n" +
                "    <span id=\"errorText\"></span>\n" +
                "    <button onclick=\"closeError()\">×</button>\n" +
                "</div>\n\n" +
                "<div id=\"successContainer\" class=\"success-message\" style=\"display: none;\">\n" +
                "    <span id=\"successText\"></span>\n" +
                "</div>\n\n" +
                "CSS:\n" +
                ".error-message {\n" +
                "    background-color: #f8d7da;\n" +
                "    color: #721c24;\n" +
                "    padding: 12px;\n" +
                "    border: 1px solid #f5c6cb;\n" +
                "    border-radius: 4px;\n" +
                "    margin: 10px 0;\n" +
                "}\n\n" +
                ".success-message {\n" +
                "    background-color: #d4edda;\n" +
                "    color: #155724;\n" +
                "    padding: 12px;\n" +
                "    border: 1px solid #c3e6cb;\n" +
                "    border-radius: 4px;\n" +
                "    margin: 10px 0;\n" +
                "}\n\n" +
                "JavaScript:\n" +
                "function displayError(message) {\n" +
                "    const errorContainer = document.getElementById('errorContainer');\n" +
                "    const errorText = document.getElementById('errorText');\n" +
                "    errorText.textContent = message;\n" +
                "    errorContainer.style.display = 'block';\n" +
                "    \n" +
                "    // Auto-hide after 5 seconds\n" +
                "    setTimeout(() => {\n" +
                "        errorContainer.style.display = 'none';\n" +
                "    }, 5000);\n" +
                "}\n\n" +
                "function displaySuccess(message) {\n" +
                "    const successContainer = document.getElementById('successContainer');\n" +
                "    const successText = document.getElementById('successText');\n" +
                "    successText.textContent = message;\n" +
                "    successContainer.style.display = 'block';\n" +
                "    \n" +
                "    setTimeout(() => {\n" +
                "        successContainer.style.display = 'none';\n" +
                "    }, 3000);\n" +
                "}\n\n" +
                "function displayValidationErrors(errors) {\n" +
                "    const messages = Object.entries(errors)\n" +
                "        .map(([field, message]) => `${field}: ${message}`)\n" +
                "        .join('\\n');\n" +
                "    displayError(messages);\n" +
                "}")
            .addExample("Complete Full-Stack Error Flow",
                "SCENARIO: User tries to register with existing email\n\n" +
                "1. FRONTEND sends request:\n" +
                "POST /api/users\n" +
                "{\n" +
                "  \"name\": \"Bob\",\n" +
                "  \"email\": \"alice@email.com\"  // Already exists!\n" +
                "}\n\n" +
                "2. BACKEND - Service layer:\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    public User create(User user) {\n" +
                "        if (userRepository.existsByEmail(user.getEmail())) {\n" +
                "            throw new DuplicateResourceException(\n" +
                "                \"Email already registered\"\n" +
                "            );\n" +
                "        }\n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}\n\n" +
                "3. BACKEND - Exception handler catches it:\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    @ExceptionHandler(DuplicateResourceException.class)\n" +
                "    public ProblemDetail handleDuplicate(DuplicateResourceException ex) {\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.CONFLICT,  // 409\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        problem.setProperty(\"errorCode\", ex.getErrorCode());\n" +
                "        return problem;\n" +
                "    }\n" +
                "}\n\n" +
                "4. RESPONSE sent to frontend:\n" +
                "HTTP/1.1 409 Conflict\n" +
                "Content-Type: application/problem+json\n" +
                "{\n" +
                "  \"status\": 409,\n" +
                "  \"detail\": \"Email already registered\",\n" +
                "  \"errorCode\": \"DUPLICATE_RESOURCE\"\n" +
                "}\n\n" +
                "5. FRONTEND handles error:\n" +
                "if (response.status === 409) {\n" +
                "    displayError('This email is already registered. Please login instead.');\n" +
                "}\n\n" +
                "6. USER sees:\n" +
                "[Error Message Box]\n" +
                "This email is already registered. Please login instead.\n" +
                "[×]\n\n" +
                "COMPLETE JOURNEY - Error caught and handled at every layer!")
            .addWarning("Common Error Handling Mistakes",
                "❌ MISTAKE 1: Catching and ignoring exceptions\n" +
                "try {\n" +
                "    userService.create(user);\n" +
                "} catch (Exception e) {\n" +
                "    // Nothing here - ERROR VANISHES!\n" +
                "}\n\n" +
                "❌ MISTAKE 2: Generic error messages\n" +
                "displayError(\"Error occurred\");  // What error? How to fix?\n\n" +
                "✓ FIX: Be specific\n" +
                "displayError(\"Email already exists. Please use a different email.\");\n\n" +
                "❌ MISTAKE 3: Not validating on both sides\n" +
                "// Only frontend validation - can be bypassed!\n" +
                "// Only backend validation - bad UX (slow feedback)\n\n" +
                "✓ FIX: Validate on BOTH frontend and backend\n\n" +
                "❌ MISTAKE 4: Returning 200 OK with error\n" +
                "return ResponseEntity.ok(Map.of(\"error\", \"User not found\"));\n\n" +
                "✓ FIX: Use proper status codes\n" +
                "return ResponseEntity.status(HttpStatus.NOT_FOUND)\n" +
                "    .body(problemDetail);\n\n" +
                "❌ MISTAKE 5: Not logging errors\n" +
                "// Error happens, no log, no way to debug\n\n" +
                "✓ FIX: Always log exceptions\n" +
                "logger.error(\"Failed to create user: {}\", ex.getMessage(), ex);")
            .addChallenge(createExceptionHandlerQuiz())
            .addChallenge(createSecurityQuiz())
            .addChallenge(createFrontendQuiz())
            .estimatedMinutes(50)
            .build();
    }

    private static Challenge createExceptionHandlerQuiz() {
        return new Challenge.Builder("epoch-8-lesson-4-handler", "Exception Handling", ChallengeType.MULTIPLE_CHOICE)
            .description("Where should you handle exceptions in a Spring Boot application?")
            .addMultipleChoiceOption("A) In the controller methods")
            .addMultipleChoiceOption("B) In a @RestControllerAdvice class")
            .addMultipleChoiceOption("C) In the main method")
            .addMultipleChoiceOption("D) In the frontend")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-8-lesson-4-security", "Error Security", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you NEVER include in error responses sent to users?")
            .addMultipleChoiceOption("A) Error message")
            .addMultipleChoiceOption("B) HTTP status code")
            .addMultipleChoiceOption("C) Stack traces")
            .addMultipleChoiceOption("D) Timestamp")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createFrontendQuiz() {
        return new Challenge.Builder("epoch-8-lesson-4-frontend", "Frontend Error Handling", ChallengeType.MULTIPLE_CHOICE)
            .description("In JavaScript fetch(), how do you check if the request failed?")
            .addMultipleChoiceOption("A) if (response.failed)")
            .addMultipleChoiceOption("B) if (!response.ok)")
            .addMultipleChoiceOption("C) if (response.error)")
            .addMultipleChoiceOption("D) if (response.status === 'error')")
            .correctAnswer("B")
            .build();
    }
}
