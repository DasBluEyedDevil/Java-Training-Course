package com.socraticjava.content.epoch9;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 9, Lesson 11: Error Handling and Logging Best Practices
 *
 * This lesson teaches professional error handling and logging strategies
 * for production Spring Boot applications.
 */
public class Lesson11Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Error Handling and Logging Best Practices",
            """
            # Error Handling and Logging Best Practices

            ## Why Professional Error Handling Matters

            Imagine calling customer support and hearing: "Something went wrong. Goodbye."
            That's frustrating, right? That's what poor error handling feels like to users.

            Professional applications need:
            - **Clear, actionable error messages** for users
            - **Detailed diagnostic information** for developers
            - **Consistent error format** across all endpoints
            - **Security** - don't leak sensitive information
            - **Observability** - track and debug issues in production

            Poor error handling costs money:
            - Users abandon frustrated experiences
            - Support teams can't help without information
            - Developers waste hours debugging without proper logs
            - Security vulnerabilities from information leakage

            ## The Analogy: Restaurant Error Messages

            **Bad restaurant (poor error handling):**
            - Order fails, waiter shrugs: "Kitchen error. Dunno."
            - Kitchen has no record of what went wrong
            - Manager can't improve because problems aren't tracked
            - Customers leave confused and frustrated

            **Good restaurant (professional error handling):**
            - Order fails: "Sorry, we're out of salmon. May I suggest the tuna?"
            - Kitchen logs: "Salmon shortage - Supplier #3 - Order #1234"
            - Manager reviews logs, finds patterns, fixes supplier issues
            - Customer gets helpful alternative, knows what happened

            Professional error handling does both: helps users AND helps developers.

            ## RFC 7807: Problem Details for HTTP APIs

            Spring Boot 3 supports RFC 7807, a standard format for HTTP error responses.

            ### Traditional Error Response (Inconsistent)
            ```json
            {
              "error": "Bad Request",
              "message": "Validation failed",
              "status": 400
            }
            ```

            ### RFC 7807 ProblemDetail (Standard)
            ```json
            {
              "type": "https://api.example.com/errors/validation-error",
              "title": "Validation Error",
              "status": 400,
              "detail": "Email address is invalid",
              "instance": "/api/users/register",
              "timestamp": "2025-01-15T10:30:00Z",
              "errors": {
                "email": "must be a valid email address"
              }
            }
            ```

            **Benefits:**
            - `type`: URI identifying the error type (documentation link)
            - `title`: Human-readable summary
            - `status`: HTTP status code
            - `detail`: Specific explanation for this occurrence
            - `instance`: URI where the error occurred
            - Additional properties for context

            ## Implementing Global Exception Handling

            ### Step 1: Custom Exception Classes

            Create specific exceptions for different error scenarios:

            ```java
            package com.example.exception;

            /**
             * Base class for all business exceptions
             */
            public abstract class BusinessException extends RuntimeException {
                private final String errorCode;

                public BusinessException(String message, String errorCode) {
                    super(message);
                    this.errorCode = errorCode;
                }

                public String getErrorCode() {
                    return errorCode;
                }
            }

            /**
             * Thrown when requested resource is not found
             */
            public class ResourceNotFoundException extends BusinessException {
                private final String resourceType;
                private final Object resourceId;

                public ResourceNotFoundException(String resourceType, Object resourceId) {
                    super(
                        String.format("%s not found with id: %s", resourceType, resourceId),
                        "RESOURCE_NOT_FOUND"
                    );
                    this.resourceType = resourceType;
                    this.resourceId = resourceId;
                }

                public String getResourceType() {
                    return resourceType;
                }

                public Object getResourceId() {
                    return resourceId;
                }
            }

            /**
             * Thrown when business validation fails
             */
            public class ValidationException extends BusinessException {
                private final Map<String, String> errors;

                public ValidationException(String message, Map<String, String> errors) {
                    super(message, "VALIDATION_ERROR");
                    this.errors = errors;
                }

                public Map<String, String> getErrors() {
                    return errors;
                }
            }

            /**
             * Thrown when user lacks required permissions
             */
            public class InsufficientPermissionsException extends BusinessException {
                private final String requiredPermission;

                public InsufficientPermissionsException(String requiredPermission) {
                    super(
                        String.format("Required permission: %s", requiredPermission),
                        "INSUFFICIENT_PERMISSIONS"
                    );
                    this.requiredPermission = requiredPermission;
                }

                public String getRequiredPermission() {
                    return requiredPermission;
                }
            }

            /**
             * Thrown when business rule is violated
             */
            public class BusinessRuleViolationException extends BusinessException {
                public BusinessRuleViolationException(String message) {
                    super(message, "BUSINESS_RULE_VIOLATION");
                }
            }
            ```

            ### Step 2: Global Exception Handler

            Use `@RestControllerAdvice` to handle exceptions globally:

            ```java
            package com.example.exception;

            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.ProblemDetail;
            import org.springframework.security.access.AccessDeniedException;
            import org.springframework.security.core.AuthenticationException;
            import org.springframework.validation.FieldError;
            import org.springframework.web.bind.MethodArgumentNotValidException;
            import org.springframework.web.bind.annotation.ExceptionHandler;
            import org.springframework.web.bind.annotation.RestControllerAdvice;
            import org.springframework.web.context.request.WebRequest;

            import java.net.URI;
            import java.time.Instant;
            import java.util.HashMap;
            import java.util.Map;

            @RestControllerAdvice
            public class GlobalExceptionHandler {

                private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
                private static final String ERROR_TYPE_PREFIX = "https://api.example.com/errors/";

                /**
                 * Handle ResourceNotFoundException (404)
                 */
                @ExceptionHandler(ResourceNotFoundException.class)
                public ProblemDetail handleResourceNotFound(
                    ResourceNotFoundException ex,
                    WebRequest request
                ) {
                    logger.warn("Resource not found: {} with id: {}",
                        ex.getResourceType(), ex.getResourceId());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "resource-not-found"));
                    problemDetail.setTitle("Resource Not Found");
                    problemDetail.setProperty("timestamp", Instant.now());
                    problemDetail.setProperty("resourceType", ex.getResourceType());
                    problemDetail.setProperty("resourceId", ex.getResourceId());

                    return problemDetail;
                }

                /**
                 * Handle ValidationException (400)
                 */
                @ExceptionHandler(ValidationException.class)
                public ProblemDetail handleValidationException(
                    ValidationException ex,
                    WebRequest request
                ) {
                    logger.warn("Validation failed: {}", ex.getMessage());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "validation-error"));
                    problemDetail.setTitle("Validation Error");
                    problemDetail.setProperty("timestamp", Instant.now());
                    problemDetail.setProperty("errors", ex.getErrors());

                    return problemDetail;
                }

                /**
                 * Handle Spring validation errors (400)
                 */
                @ExceptionHandler(MethodArgumentNotValidException.class)
                public ProblemDetail handleMethodArgumentNotValid(
                    MethodArgumentNotValidException ex,
                    WebRequest request
                ) {
                    Map<String, String> errors = new HashMap<>();
                    ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                    });

                    logger.warn("Validation failed for request: {}", errors);

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Validation failed for one or more fields"
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "validation-error"));
                    problemDetail.setTitle("Validation Error");
                    problemDetail.setProperty("timestamp", Instant.now());
                    problemDetail.setProperty("errors", errors);

                    return problemDetail;
                }

                /**
                 * Handle InsufficientPermissionsException (403)
                 */
                @ExceptionHandler(InsufficientPermissionsException.class)
                public ProblemDetail handleInsufficientPermissions(
                    InsufficientPermissionsException ex,
                    WebRequest request
                ) {
                    logger.warn("Access denied: {}", ex.getMessage());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        ex.getMessage()
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "insufficient-permissions"));
                    problemDetail.setTitle("Insufficient Permissions");
                    problemDetail.setProperty("timestamp", Instant.now());
                    problemDetail.setProperty("requiredPermission", ex.getRequiredPermission());

                    return problemDetail;
                }

                /**
                 * Handle Spring Security AccessDeniedException (403)
                 */
                @ExceptionHandler(AccessDeniedException.class)
                public ProblemDetail handleAccessDenied(
                    AccessDeniedException ex,
                    WebRequest request
                ) {
                    logger.warn("Access denied: {}", ex.getMessage());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        "You don't have permission to access this resource"
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "access-denied"));
                    problemDetail.setTitle("Access Denied");
                    problemDetail.setProperty("timestamp", Instant.now());

                    return problemDetail;
                }

                /**
                 * Handle AuthenticationException (401)
                 */
                @ExceptionHandler(AuthenticationException.class)
                public ProblemDetail handleAuthenticationException(
                    AuthenticationException ex,
                    WebRequest request
                ) {
                    logger.warn("Authentication failed: {}", ex.getMessage());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "Authentication is required to access this resource"
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "authentication-required"));
                    problemDetail.setTitle("Authentication Required");
                    problemDetail.setProperty("timestamp", Instant.now());

                    return problemDetail;
                }

                /**
                 * Handle BusinessRuleViolationException (422)
                 */
                @ExceptionHandler(BusinessRuleViolationException.class)
                public ProblemDetail handleBusinessRuleViolation(
                    BusinessRuleViolationException ex,
                    WebRequest request
                ) {
                    logger.warn("Business rule violation: {}", ex.getMessage());

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        ex.getMessage()
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "business-rule-violation"));
                    problemDetail.setTitle("Business Rule Violation");
                    problemDetail.setProperty("timestamp", Instant.now());

                    return problemDetail;
                }

                /**
                 * Handle all other exceptions (500)
                 */
                @ExceptionHandler(Exception.class)
                public ProblemDetail handleGeneralException(
                    Exception ex,
                    WebRequest request
                ) {
                    // Log full stack trace for unexpected errors
                    logger.error("Unexpected error occurred", ex);

                    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred. Please try again later."
                    );

                    problemDetail.setType(URI.create(ERROR_TYPE_PREFIX + "internal-error"));
                    problemDetail.setTitle("Internal Server Error");
                    problemDetail.setProperty("timestamp", Instant.now());

                    // Don't expose internal details in production
                    // Use application property to control this
                    // problemDetail.setProperty("message", ex.getMessage());

                    return problemDetail;
                }
            }
            ```

            **Key Points:**
            - Different handlers for different exception types
            - Returns RFC 7807 ProblemDetail format
            - Logs appropriate information at appropriate levels
            - Doesn't leak sensitive information to clients
            - Includes timestamp and error codes for tracking

            ### Step 3: Using Custom Exceptions in Services

            ```java
            package com.example.service;

            import com.example.exception.ResourceNotFoundException;
            import com.example.exception.ValidationException;
            import com.example.exception.BusinessRuleViolationException;
            import lombok.RequiredArgsConstructor;
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;
            import org.springframework.stereotype.Service;
            import org.springframework.transaction.annotation.Transactional;

            import java.util.HashMap;
            import java.util.Map;

            @Service
            @Transactional
            @RequiredArgsConstructor
            public class TaskService {

                private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

                private final TaskRepository taskRepository;
                private final UserRepository userRepository;

                public TaskDTO createTask(TaskRequest request, Long userId) {
                    logger.info("Creating task for user: {}", userId);

                    // Find user
                    User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

                    // Business validation
                    if (user.getTaskCount() >= user.getMaxTasks()) {
                        logger.warn("User {} exceeded task limit: {}/{}",
                            userId, user.getTaskCount(), user.getMaxTasks());

                        throw new BusinessRuleViolationException(
                            String.format("Task limit reached. Maximum %d tasks allowed.",
                                user.getMaxTasks())
                        );
                    }

                    // Create task
                    Task task = new Task();
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    task.setUser(user);

                    Task saved = taskRepository.save(task);
                    logger.info("Task created successfully: {}", saved.getId());

                    return convertToDTO(saved);
                }

                public TaskDTO getTask(Long taskId, Long userId) {
                    logger.debug("Fetching task: {} for user: {}", taskId, userId);

                    Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

                    // Check ownership
                    if (!task.getUser().getId().equals(userId)) {
                        logger.warn("User {} attempted to access task {} owned by user {}",
                            userId, taskId, task.getUser().getId());

                        throw new InsufficientPermissionsException("TASK_OWNER");
                    }

                    return convertToDTO(task);
                }

                public void deleteTask(Long taskId, Long userId) {
                    logger.info("Deleting task: {} for user: {}", taskId, userId);

                    Task task = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

                    // Check ownership
                    if (!task.getUser().getId().equals(userId)) {
                        throw new InsufficientPermissionsException("TASK_OWNER");
                    }

                    // Business rule: can't delete completed tasks older than 30 days
                    if (task.isCompleted() &&
                        task.getCompletedAt().isBefore(LocalDateTime.now().minusDays(30))) {

                        logger.warn("Attempt to delete archived task: {}", taskId);
                        throw new BusinessRuleViolationException(
                            "Cannot delete tasks completed more than 30 days ago. " +
                            "These are archived for record-keeping."
                        );
                    }

                    taskRepository.delete(task);
                    logger.info("Task deleted successfully: {}", taskId);
                }
            }
            ```

            ## Professional Logging with SLF4J and Logback

            ### Logging Levels

            Use appropriate levels for different situations:

            ```java
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;

            public class TaskService {
                private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

                public void processTask(Task task) {
                    // TRACE: Very detailed, usually disabled
                    logger.trace("Processing task with full details: {}", task);

                    // DEBUG: Detailed information for debugging
                    logger.debug("Task status before processing: {}", task.getStatus());

                    // INFO: Important business events
                    logger.info("Processing task: {} for user: {}", task.getId(), task.getUserId());

                    try {
                        // Process task...

                        // INFO: Successful operations
                        logger.info("Task processed successfully: {}", task.getId());

                    } catch (TransientException e) {
                        // WARN: Recoverable issues, degraded functionality
                        logger.warn("Temporary issue processing task {}: {}. Will retry.",
                            task.getId(), e.getMessage());

                    } catch (Exception e) {
                        // ERROR: Serious issues requiring attention
                        logger.error("Failed to process task: {}", task.getId(), e);
                        throw new TaskProcessingException("Task processing failed", e);
                    }
                }
            }
            ```

            **Guideline:**
            - **TRACE**: Extremely detailed, step-by-step execution
            - **DEBUG**: Detailed diagnostic information
            - **INFO**: Important business events and milestones
            - **WARN**: Unexpected situations that don't prevent operation
            - **ERROR**: Serious problems requiring immediate attention

            ### Structured Logging

            Use placeholders for better performance:

            ```java
            // ❌ BAD: String concatenation (always executes)
            logger.debug("Task details: " + task.toString());

            // ✅ GOOD: Placeholder (only evaluates if DEBUG enabled)
            logger.debug("Task details: {}", task);

            // ✅ GOOD: Multiple parameters
            logger.info("User {} created task {} with priority {}",
                userId, taskId, priority);

            // ✅ GOOD: Exception as last parameter
            logger.error("Failed to process task: {}", taskId, exception);
            ```

            ### Logback Configuration

            Create `src/main/resources/logback-spring.xml`:

            ```xml
            <?xml version="1.0" encoding="UTF-8"?>
            <configuration>

                <!-- Console appender for development -->
                <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder>
                        <pattern>
                            %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
                        </pattern>
                    </encoder>
                </appender>

                <!-- File appender for production -->
                <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/application.log</file>
                    <encoder>
                        <pattern>
                            %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
                        </pattern>
                    </encoder>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/application.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                        <maxHistory>30</maxHistory>
                        <maxFileSize>100MB</maxFileSize>
                    </rollingPolicy>
                </appender>

                <!-- JSON appender for log aggregation (production) -->
                <appender name="JSON" class="ch.qos.logback.core.rolling.RollingFileAppender">
                    <file>logs/application.json</file>
                    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
                        <includeMdcKeyName>traceId</includeMdcKeyName>
                        <includeMdcKeyName>userId</includeMdcKeyName>
                    </encoder>
                    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                        <fileNamePattern>logs/application.%d{yyyy-MM-dd}.json.gz</fileNamePattern>
                        <maxHistory>30</maxHistory>
                    </rollingPolicy>
                </appender>

                <!-- Set logging levels for different packages -->
                <logger name="com.example" level="DEBUG"/>
                <logger name="org.springframework.web" level="INFO"/>
                <logger name="org.springframework.security" level="INFO"/>
                <logger name="org.hibernate.SQL" level="DEBUG"/>
                <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE"/>

                <!-- Root logger -->
                <root level="INFO">
                    <appender-ref ref="CONSOLE"/>
                    <appender-ref ref="FILE"/>
                </root>

                <!-- Production profile -->
                <springProfile name="prod">
                    <root level="INFO">
                        <appender-ref ref="JSON"/>
                    </root>
                </springProfile>

            </configuration>
            ```

            ### MDC (Mapped Diagnostic Context)

            Add contextual information to all logs in a request:

            ```java
            package com.example.filter;

            import jakarta.servlet.FilterChain;
            import jakarta.servlet.ServletException;
            import jakarta.servlet.http.HttpServletRequest;
            import jakarta.servlet.http.HttpServletResponse;
            import org.slf4j.MDC;
            import org.springframework.security.core.Authentication;
            import org.springframework.security.core.context.SecurityContextHolder;
            import org.springframework.stereotype.Component;
            import org.springframework.web.filter.OncePerRequestFilter;

            import java.io.IOException;
            import java.util.UUID;

            @Component
            public class LoggingFilter extends OncePerRequestFilter {

                @Override
                protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
                ) throws ServletException, IOException {

                    try {
                        // Add trace ID for request tracking
                        String traceId = UUID.randomUUID().toString();
                        MDC.put("traceId", traceId);

                        // Add user information if authenticated
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if (auth != null && auth.isAuthenticated()) {
                            MDC.put("userId", auth.getName());
                        }

                        // Add to response header for client-side tracking
                        response.setHeader("X-Trace-Id", traceId);

                        filterChain.doFilter(request, response);

                    } finally {
                        // Always clear MDC after request
                        MDC.clear();
                    }
                }
            }
            ```

            Now all logs automatically include trace ID and user ID:
            ```
            2025-01-15 10:30:00.123 [http-nio-8080-exec-1] INFO  c.e.TaskService [traceId=abc123, userId=john@example.com] - Creating task for user: 42
            ```

            ## Common Mistakes to Avoid

            ### ❌ Logging Sensitive Information

            **Wrong:**
            ```java
            logger.info("User login: {} with password: {}", email, password);
            logger.debug("Credit card: {}", user.getCreditCard());
            logger.info("JWT token: {}", jwtToken);
            ```

            **Why:** Logs are often stored in plain text and accessible to many people.

            **Right:**
            ```java
            logger.info("User login attempt: {}", email);
            logger.debug("Credit card ending in: {}", user.getCreditCardLast4());
            logger.debug("JWT token length: {}", jwtToken.length());
            ```

            ### ❌ Catching and Logging Without Rethrowing

            **Wrong:**
            ```java
            try {
                processPayment(order);
            } catch (PaymentException e) {
                logger.error("Payment failed", e);
                // Exception silently swallowed!
            }
            ```

            **Why:** Caller thinks operation succeeded. Data inconsistency results.

            **Right:**
            ```java
            try {
                processPayment(order);
            } catch (PaymentException e) {
                logger.error("Payment failed for order: {}", order.getId(), e);
                throw new OrderProcessingException("Payment failed", e);
            }
            ```

            ### ❌ Logging at Wrong Level

            **Wrong:**
            ```java
            logger.error("User viewed dashboard");  // Not an error!
            logger.debug("Database connection failed");  // This is ERROR!
            ```

            **Why:** Log aggregation and alerting become useless.

            **Right:**
            ```java
            logger.info("User viewed dashboard");
            logger.error("Database connection failed", exception);
            ```

            ### ❌ Generic Error Messages

            **Wrong:**
            ```java
            throw new RuntimeException("Error");
            throw new Exception("Something went wrong");
            ```

            **Why:** Impossible to debug. No context.

            **Right:**
            ```java
            throw new ResourceNotFoundException("Task", taskId);
            throw new BusinessRuleViolationException(
                String.format("Cannot delete task %d: still has %d active subtasks",
                    taskId, subtaskCount)
            );
            ```

            ### ❌ Exposing Stack Traces to Clients

            **Wrong:**
            ```java
            @ExceptionHandler(Exception.class)
            public ResponseEntity<String> handle(Exception e) {
                return ResponseEntity.status(500)
                    .body(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
            ```

            **Why:** Leaks implementation details, potential security vulnerability.

            **Right:**
            ```java
            @ExceptionHandler(Exception.class)
            public ProblemDetail handle(Exception e) {
                logger.error("Unexpected error", e);  // Log full details

                // Return generic message to client
                return ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please contact support with trace ID: " + traceId
                );
            }
            ```

            ## Best Practices Summary

            ### Error Handling Checklist

            ✅ Use specific exception types for different errors
            ✅ Implement global exception handler with @RestControllerAdvice
            ✅ Return RFC 7807 ProblemDetail format
            ✅ Include trace IDs for error tracking
            ✅ Don't expose internal details to clients
            ✅ Log appropriate information at appropriate levels
            ✅ Provide actionable error messages to users
            ✅ Handle edge cases gracefully

            ### Logging Checklist

            ✅ Use SLF4J with appropriate logging levels
            ✅ Use parameterized logging for performance
            ✅ Add MDC for contextual information
            ✅ Configure log rotation and retention
            ✅ Use structured logging (JSON) for production
            ✅ Never log sensitive information
            ✅ Include correlation IDs for distributed tracing
            ✅ Set up log aggregation (ELK, Splunk, etc.)

            ### Production Monitoring

            ```yaml
            # application.yml
            management:
              endpoints:
                web:
                  exposure:
                    include: health,metrics,prometheus
              endpoint:
                health:
                  show-details: always
              metrics:
                export:
                  prometheus:
                    enabled: true

            logging:
              level:
                root: INFO
                com.example: DEBUG
              file:
                name: logs/application.log
              pattern:
                console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
                file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
            ```

            ## Summary

            Professional error handling and logging are essential for production applications:

            **Error Handling:**
            - Use RFC 7807 ProblemDetail for standardized API errors
            - Create specific exception types for different scenarios
            - Implement global exception handler with @RestControllerAdvice
            - Provide clear, actionable messages to users
            - Include trace IDs for tracking and debugging
            - Never expose sensitive information in errors

            **Logging:**
            - Use SLF4J with Logback for flexible logging
            - Choose appropriate log levels (TRACE, DEBUG, INFO, WARN, ERROR)
            - Use parameterized logging for performance
            - Add MDC for contextual information (trace ID, user ID)
            - Configure log rotation and retention
            - Use structured logging (JSON) for production
            - Set up centralized log aggregation

            **Security:**
            - Never log passwords, tokens, or credit cards
            - Don't expose stack traces to clients
            - Sanitize user input in log messages
            - Use different log levels for different environments

            Good error handling and logging save countless hours of debugging and
            significantly improve user experience and operational reliability.
            """,
            50  // Estimated minutes to complete
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        QuizQuestion question = new QuizQuestion(
            "Which of the following is the MOST important principle when handling exceptions in a production REST API?",
            "C",
            """
            Understanding the balance between user-friendly error messages and detailed logging
            is critical for professional API development.

            The correct answer is C. You should log detailed error information (including stack traces)
            for debugging, but return user-friendly error messages to the client that don't expose
            internal implementation details. This approach provides the best of both worlds:

            - Developers get full diagnostic information in logs
            - Users get clear, actionable error messages
            - Security is maintained by not leaking implementation details
            - Errors can be tracked and debugged effectively

            Example of correct implementation:
            ```java
            @ExceptionHandler(Exception.class)
            public ProblemDetail handleException(Exception e) {
                // Log full details for developers
                logger.error("Unexpected error processing request", e);

                // Return safe message to client
                return ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again or contact support."
                );
            }
            ```

            Why other answers are incorrect:

            A is wrong because returning full stack traces to clients is a security vulnerability.
            Stack traces reveal:
            - Internal package structure
            - Framework versions (attackers can look for known vulnerabilities)
            - Database schema and query details
            - File system paths
            - Third-party library usage

            B is wrong because generic messages like "Error occurred" frustrate users and make
            support difficult. Users need enough information to understand what went wrong and
            what they can do about it. Compare:
            - Bad: "Error occurred"
            - Good: "Email address is already registered. Please use a different email or try logging in."

            D is wrong because 500 Internal Server Error should only be used for actual server errors,
            not client errors like validation failures. HTTP status codes have specific meanings:
            - 400 Bad Request: Client sent invalid data
            - 404 Not Found: Resource doesn't exist
            - 500 Internal Server Error: Server encountered unexpected condition

            Using correct status codes helps clients handle errors appropriately and makes APIs
            easier to use and debug.

            Key principle: Be generous with logging, conservative with client error messages.
            """
        );

        question.addChoice("A", "Always return the full stack trace to the client so they can debug the issue");
        question.addChoice("B", "Use generic error messages like 'Error occurred' to avoid confusing users with details");
        question.addChoice("C", "Log detailed error information for debugging, but return user-friendly messages to clients");
        question.addChoice("D", "Return HTTP 500 for all errors since they all represent server-side issues");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "You notice that your application logs are consuming excessive disk space and impacting performance. What is the BEST solution?",
            "B",
            """
            Managing log volume and performance is a critical operational concern. This question
            tests understanding of proper logging configuration and best practices.

            The correct answer is B. Using parameterized logging with appropriate log levels and
            configuring log rotation/compression addresses both performance and storage concerns
            effectively.

            Here's why this is the best approach:

            1. **Parameterized Logging** (Performance):
            ```java
            // ❌ SLOW: String concatenation always executes
            logger.debug("Processing " + taskCount + " tasks for user " + userId);

            // ✅ FAST: Only evaluates if DEBUG is enabled
            logger.debug("Processing {} tasks for user {}", taskCount, userId);
            ```

            2. **Appropriate Log Levels** (Volume Control):
            ```java
            // Development: DEBUG level
            // Staging: INFO level
            // Production: WARN/ERROR level (unless investigating specific issues)
            ```

            3. **Log Rotation Configuration** (Storage Management):
            ```xml
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>logs/app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
                <maxHistory>30</maxHistory>      <!-- Keep 30 days -->
                <maxFileSize>100MB</maxFileSize>  <!-- Max size per file -->
            </rollingPolicy>
            ```

            Why other answers are problematic:

            A is wrong because disabling DEBUG in production is good practice, but reducing INFO
            logs to ERROR-only is too extreme. INFO logs capture important business events:
            - User registrations
            - Order completions
            - Payment processing
            - Authentication events
            - Configuration changes

            Without INFO logs, you lose visibility into normal application behavior, making it
            harder to identify when things start going wrong before they become errors.

            C is wrong because this creates a critical single point of failure. If logging starts
            blocking on network I/O (because remote server is slow/down), your entire application
            becomes slow or unresponsive. Logging should never block the main application flow.

            Better approach: Use async appenders for remote logging:
            ```xml
            <appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
                <appender-ref ref="REMOTE"/>
                <queueSize>500</queueSize>
                <discardingThreshold>0</discardingThreshold>
            </appender>
            ```

            D is wrong because:
            1. Database logging is much slower than file logging
            2. Creates performance bottleneck (database writes for every log)
            3. Log database can grow uncontrollably
            4. If database goes down, you lose all logs
            5. Adds unnecessary load to your database

            Logs should be written to files (fast, reliable, local) and then shipped to centralized
            log aggregation systems (ELK, Splunk, CloudWatch) asynchronously.

            Complete solution architecture:
            1. Use parameterized logging everywhere
            2. Set appropriate log levels per environment
            3. Configure log rotation (daily, max size 100MB)
            4. Compress rotated logs automatically
            5. Keep logs for 30-90 days (compliance dependent)
            6. Ship logs to centralized system asynchronously
            7. Monitor log volume with alerts

            Performance impact comparison:
            - String concatenation: ~1000ns per log statement
            - Parameterized logging (level disabled): ~10ns
            - File logging: ~1-10μs
            - Network logging (synchronous): ~1-100ms (100-10000x slower!)
            - Database logging: ~10-100ms
            """
        );

        question.addChoice("A", "Disable DEBUG logging in production and reduce INFO logs to ERROR only");
        question.addChoice("B", "Use parameterized logging, set appropriate log levels, and configure log rotation with compression");
        question.addChoice("C", "Keep all logging but send logs synchronously to a remote logging server");
        question.addChoice("D", "Store logs in the database instead of files for better query performance");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "When implementing a global exception handler, you need to handle a database connection failure. What should you do?",
            "D",
            """
            Handling infrastructure failures like database connection issues requires careful
            consideration of logging, user messaging, and operational alerting.

            The correct answer is D. Log at ERROR level with full details, return a 503 Service
            Unavailable status, and implement alerting for infrastructure issues. This approach
            provides the best operational response to a serious infrastructure problem.

            Here's why each part matters:

            1. **ERROR Level Logging** - This is a critical infrastructure failure:
            ```java
            @ExceptionHandler(DataAccessException.class)
            public ProblemDetail handleDatabaseError(DataAccessException e) {
                logger.error("Database connection failure - service degraded", e);
                // Log full exception including stack trace for investigation
            }
            ```

            2. **503 Service Unavailable** - Correct HTTP semantics:
            ```java
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service temporarily unavailable. Please try again in a few moments."
            );
            problem.setProperty("retryAfter", 60); // Suggest retry in 60 seconds
            ```

            Why 503 instead of 500?
            - 503: Service temporarily unavailable (might recover)
            - 500: Internal server error (generic server problem)
            - 503 signals to load balancers/proxies to route traffic elsewhere
            - Clients know to retry (unlike 500 which might indicate permanent failure)

            3. **Alerting** - Critical infrastructure issues need immediate attention:
            ```java
            private void alertOnDatabaseFailure() {
                // Send to PagerDuty, OpsGenie, etc.
                alertingService.sendCriticalAlert(
                    "Database Connection Failure",
                    "Application cannot connect to database"
                );
            }
            ```

            Why other answers are inadequate:

            A is completely wrong because this is NOT a client error. The client did nothing wrong—
            the server infrastructure failed. HTTP status codes have specific meanings:
            - 400 Bad Request: Client sent malformed/invalid request
            - 503 Service Unavailable: Server infrastructure issue

            Using 400 would:
            - Confuse API clients
            - Cause clients to not retry (they think their request is bad)
            - Break semantic HTTP conventions
            - Make monitoring and alerting harder

            B is problematic because WARNING level is insufficient for a database connection failure.
            This is a critical error that prevents the application from functioning:

            Log Levels Guide:
            - TRACE/DEBUG: Detailed diagnostic information
            - INFO: Important business events (user registration, order placed)
            - WARN: Unexpected situations that don't prevent operation (cache miss, degraded mode)
            - ERROR: Serious problems requiring immediate attention (database down, external service unreachable)

            Database connection failure should be ERROR with immediate alerting to on-call engineers.

            C has the right status code but suggesting "contact support" for infrastructure issues
            is poor UX. Users can't do anything to fix a database connection problem. Better messages:

            Bad: "Error occurred. Please contact support."
            Better: "Service temporarily unavailable. Please try again in a few moments."
            Best: "We're experiencing technical difficulties. Please try again shortly. If the problem persists, check our status page at status.example.com"

            Complete implementation example:
            ```java
            @ExceptionHandler({
                DataAccessException.class,
                CannotGetJdbcConnectionException.class
            })
            public ProblemDetail handleDatabaseError(Exception e) {
                // Log at ERROR level with full context
                logger.error("Database connection failure detected", e);

                // Alert operations team
                if (shouldAlert(e)) {
                    alertingService.sendCriticalAlert(
                        "Database Connection Failure",
                        "Application: " + appName,
                        "Environment: " + environment,
                        "Error: " + e.getMessage()
                    );
                }

                // Return appropriate response to client
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Service temporarily unavailable. Please try again in a few moments."
                );

                problem.setType(URI.create(ERROR_TYPE_PREFIX + "service-unavailable"));
                problem.setTitle("Service Unavailable");
                problem.setProperty("timestamp", Instant.now());
                problem.setProperty("retryAfter", 60);

                return problem;
            }

            private boolean shouldAlert(Exception e) {
                // Implement throttling to avoid alert storms
                return rateLimiter.tryAcquire("db-error-alert", 1, Duration.ofMinutes(5));
            }
            ```

            Additional considerations:
            - Implement circuit breaker pattern to fail fast when database is down
            - Consider fallback mechanisms (cached data, read replicas)
            - Set up health check endpoints that monitor database connectivity
            - Configure load balancer to route traffic away from unhealthy instances
            - Have runbooks for common infrastructure failures
            """
        );

        question.addChoice("A", "Return HTTP 400 Bad Request since the request couldn't be processed");
        question.addChoice("B", "Log at WARN level and return HTTP 503 with details about the database connection");
        question.addChoice("C", "Log at INFO level, return HTTP 503, and tell users to contact support");
        question.addChoice("D", "Log at ERROR level with full details, return HTTP 503 Service Unavailable, and trigger alerts");

        return question;
    }
}
