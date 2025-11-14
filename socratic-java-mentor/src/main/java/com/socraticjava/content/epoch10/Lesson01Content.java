package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 10, Lesson 1: API Documentation with Swagger/OpenAPI
 *
 * This lesson teaches professional API documentation using OpenAPI 3
 * and Swagger UI with Spring Boot 3.
 */
public class Lesson01Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "API Documentation with Swagger/OpenAPI",
            """
            # API Documentation with Swagger/OpenAPI

            ## Why API Documentation Matters

            Imagine buying a complex device with no manual. You have to figure out every
            feature by trial and error. That's frustrating and time-consuming.

            APIs without documentation feel the same way. Developers need to know:
            - What endpoints are available?
            - What parameters do they accept?
            - What data format is expected?
            - What responses can they expect?
            - What authentication is required?
            - What errors might occur?

            **Poor API documentation costs real money:**
            - Frontend developers waste hours guessing API contracts
            - Integration partners struggle to connect to your API
            - Customer support gets overwhelmed with API questions
            - Bugs emerge from misunderstood API behavior
            - Developer onboarding takes weeks instead of days

            **Good API documentation:**
            - Reduces integration time from weeks to hours
            - Decreases support tickets by 80%
            - Enables self-service API adoption
            - Acts as a contract between frontend and backend
            - Automatically stays in sync with code

            ## The Analogy: Menu vs. Secret Restaurant

            **API without documentation (secret restaurant):**
            - No menu posted anywhere
            - Customers must know someone who's eaten there before
            - Have to guess what the chef might cook
            - Don't know prices until after ordering
            - Kitchen might be closed but no one tells you

            **API with Swagger documentation (professional restaurant):**
            - Complete menu with descriptions and photos
            - Prices clearly listed
            - Allergen information provided
            - Opening hours displayed
            - Online ordering with instant feedback
            - Customer can "try before they buy"

            Professional APIs have professional documentation.

            ## OpenAPI Specification

            OpenAPI Specification (formerly Swagger Specification) is the industry standard
            for describing REST APIs. It's a machine-readable format that describes:

            - Available endpoints and operations
            - Request parameters and request body schemas
            - Response schemas and status codes
            - Authentication methods
            - API metadata (version, terms of service, contact info)

            ### OpenAPI Document Structure

            ```yaml
            openapi: 3.0.3
            info:
              title: Task Management API
              description: API for managing tasks and user accounts
              version: 1.0.0
              contact:
                name: API Support
                email: support@example.com
            servers:
              - url: https://api.example.com/v1
                description: Production server
              - url: https://staging.example.com/v1
                description: Staging server

            paths:
              /tasks:
                get:
                  summary: Get all tasks
                  description: Returns a list of all tasks for the authenticated user
                  operationId: getTasks
                  tags:
                    - Tasks
                  security:
                    - bearerAuth: []
                  parameters:
                    - name: status
                      in: query
                      description: Filter by task status
                      schema:
                        type: string
                        enum: [PENDING, IN_PROGRESS, COMPLETED]
                  responses:
                    '200':
                      description: Successful response
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: '#/components/schemas/Task'
                    '401':
                      $ref: '#/components/responses/UnauthorizedError'

            components:
              schemas:
                Task:
                  type: object
                  required:
                    - id
                    - title
                    - status
                  properties:
                    id:
                      type: integer
                      format: int64
                      example: 1
                    title:
                      type: string
                      example: Complete project documentation
                    status:
                      type: string
                      enum: [PENDING, IN_PROGRESS, COMPLETED]

              securitySchemes:
                bearerAuth:
                  type: http
                  scheme: bearer
                  bearerFormat: JWT
            ```

            **Benefits of OpenAPI:**
            - **Standard Format**: Understood by tools and developers worldwide
            - **Code Generation**: Generate client SDKs, server stubs, tests
            - **Interactive Documentation**: Swagger UI allows trying APIs
            - **Validation**: Tools can validate API responses against spec
            - **Contract-First Development**: Design API before implementation

            ## Implementing Swagger in Spring Boot 3

            ### Step 1: Add Dependencies

            Add springdoc-openapi to `pom.xml`:

            ```xml
            <dependency>
                <groupId>org.springdoc</groupId>
                <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                <version>2.8.5</version>
            </dependency>
            ```

            **Important:** Use version 2.x for Spring Boot 3. Version 1.x only supports Spring Boot 2.

            ### Step 2: Configure OpenAPI

            Create an OpenAPI configuration class:

            ```java
            package com.example.config;

            import io.swagger.v3.oas.models.OpenAPI;
            import io.swagger.v3.oas.models.info.Contact;
            import io.swagger.v3.oas.models.info.Info;
            import io.swagger.v3.oas.models.info.License;
            import io.swagger.v3.oas.models.security.SecurityRequirement;
            import io.swagger.v3.oas.models.security.SecurityScheme;
            import io.swagger.v3.oas.models.servers.Server;
            import org.springframework.beans.factory.annotation.Value;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;

            import java.util.List;

            @Configuration
            public class OpenAPIConfig {

                @Value("${server.port:8080}")
                private String serverPort;

                @Bean
                public OpenAPI customOpenAPI() {
                    return new OpenAPI()
                        .info(new Info()
                            .title("Task Management API")
                            .version("1.0.0")
                            .description("A comprehensive API for managing tasks, users, and authentication")
                            .contact(new Contact()
                                .name("API Support Team")
                                .email("api-support@example.com")
                                .url("https://example.com/support"))
                            .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                        .servers(List.of(
                            new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                            new Server()
                                .url("https://api.example.com")
                                .description("Production Server")))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .components(new io.swagger.v3.oas.models.Components()
                            .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")
                                    .description("Enter JWT token")));
                }
            }
            ```

            ### Step 3: Configure Application Properties

            Add to `application.yml`:

            ```yaml
            springdoc:
              api-docs:
                path: /v3/api-docs
                enabled: true
              swagger-ui:
                path: /swagger-ui.html
                enabled: true
                operationsSorter: method
                tagsSorter: alpha
                tryItOutEnabled: true
                filter: true
                displayRequestDuration: true
              show-actuator: true
              default-consumes-media-type: application/json
              default-produces-media-type: application/json
            ```

            **Key Configuration Options:**
            - `api-docs.path`: Where OpenAPI JSON is served (default: /v3/api-docs)
            - `swagger-ui.path`: Where Swagger UI is served (default: /swagger-ui.html)
            - `operationsSorter`: How to sort operations (alpha, method)
            - `tryItOutEnabled`: Enable "Try it out" feature
            - `filter`: Enable search/filter functionality

            ### Step 4: Update Security Configuration

            Allow unauthenticated access to Swagger UI:

            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .authorizeHttpRequests(auth -> auth
                        // Allow Swagger UI
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Protected endpoints
                        .anyRequest().authenticated()
                    )
                    // ... rest of security configuration
                return http.build();
            }
            ```

            ### Step 5: Document Your Endpoints

            Use OpenAPI annotations to document controllers:

            ```java
            package com.example.controller;

            import io.swagger.v3.oas.annotations.Operation;
            import io.swagger.v3.oas.annotations.Parameter;
            import io.swagger.v3.oas.annotations.media.Content;
            import io.swagger.v3.oas.annotations.media.Schema;
            import io.swagger.v3.oas.annotations.responses.ApiResponse;
            import io.swagger.v3.oas.annotations.responses.ApiResponses;
            import io.swagger.v3.oas.annotations.security.SecurityRequirement;
            import io.swagger.v3.oas.annotations.tags.Tag;
            import org.springframework.http.ProblemDetail;
            import org.springframework.http.ResponseEntity;
            import org.springframework.web.bind.annotation.*;

            @RestController
            @RequestMapping("/api/tasks")
            @Tag(
                name = "Tasks",
                description = "Task management endpoints for creating, reading, updating, and deleting tasks"
            )
            @SecurityRequirement(name = "bearerAuth")
            public class TaskController {

                private final TaskService taskService;

                public TaskController(TaskService taskService) {
                    this.taskService = taskService;
                }

                @Operation(
                    summary = "Get all tasks",
                    description = "Returns a list of all tasks for the authenticated user. " +
                                  "Results can be filtered by status."
                )
                @ApiResponses(value = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved tasks",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized - JWT token missing or invalid",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    )
                })
                @GetMapping
                public ResponseEntity<List<TaskDTO>> getTasks(
                    @Parameter(
                        description = "Filter tasks by status",
                        example = "PENDING"
                    )
                    @RequestParam(required = false) TaskStatus status
                ) {
                    List<TaskDTO> tasks = taskService.getTasks(status);
                    return ResponseEntity.ok(tasks);
                }

                @Operation(
                    summary = "Get task by ID",
                    description = "Returns a single task by its unique identifier"
                )
                @ApiResponses(value = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Task found",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Task not found",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - User doesn't own this task",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    )
                })
                @GetMapping("/{id}")
                public ResponseEntity<TaskDTO> getTask(
                    @Parameter(description = "Task ID", example = "1")
                    @PathVariable Long id
                ) {
                    TaskDTO task = taskService.getTask(id);
                    return ResponseEntity.ok(task);
                }

                @Operation(
                    summary = "Create a new task",
                    description = "Creates a new task for the authenticated user"
                )
                @ApiResponses(value = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Task created successfully",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input - Validation failed",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "422",
                        description = "Business rule violation",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                        )
                    )
                })
                @PostMapping
                public ResponseEntity<TaskDTO> createTask(
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "Task to create",
                        required = true,
                        content = @Content(
                            schema = @Schema(implementation = TaskRequest.class)
                        )
                    )
                    @Valid @RequestBody TaskRequest request
                ) {
                    TaskDTO created = taskService.createTask(request);
                    return ResponseEntity.status(HttpStatus.CREATED).body(created);
                }

                @Operation(
                    summary = "Update an existing task",
                    description = "Updates a task's information. Only the task owner can update it."
                )
                @ApiResponses(value = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Task updated successfully",
                        content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskDTO.class)
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Task not found"
                    ),
                    @ApiResponse(
                        responseCode = "403",
                        description = "Forbidden - User doesn't own this task"
                    )
                })
                @PutMapping("/{id}")
                public ResponseEntity<TaskDTO> updateTask(
                    @Parameter(description = "Task ID", example = "1")
                    @PathVariable Long id,
                    @Valid @RequestBody TaskRequest request
                ) {
                    TaskDTO updated = taskService.updateTask(id, request);
                    return ResponseEntity.ok(updated);
                }

                @Operation(
                    summary = "Delete a task",
                    description = "Permanently deletes a task. This action cannot be undone."
                )
                @ApiResponses(value = {
                    @ApiResponse(
                        responseCode = "204",
                        description = "Task deleted successfully"
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Task not found"
                    ),
                    @ApiResponse(
                        responseCode = "422",
                        description = "Business rule violation - Cannot delete archived tasks"
                    )
                })
                @DeleteMapping("/{id}")
                public ResponseEntity<Void> deleteTask(
                    @Parameter(description = "Task ID", example = "1")
                    @PathVariable Long id
                ) {
                    taskService.deleteTask(id);
                    return ResponseEntity.noContent().build();
                }
            }
            ```

            **Key Annotations:**
            - `@Tag`: Groups endpoints into logical sections
            - `@Operation`: Describes what an endpoint does
            - `@ApiResponses`: Documents possible responses
            - `@Parameter`: Describes path/query parameters
            - `@RequestBody`: Documents request body
            - `@SecurityRequirement`: Indicates authentication needed

            ### Step 6: Document DTOs and Models

            Add schema documentation to your data models:

            ```java
            package com.example.dto;

            import io.swagger.v3.oas.annotations.media.Schema;
            import jakarta.validation.constraints.NotBlank;
            import jakarta.validation.constraints.Size;

            @Schema(description = "Data transfer object for creating or updating tasks")
            public class TaskRequest {

                @Schema(
                    description = "Title of the task",
                    example = "Complete project documentation",
                    requiredMode = Schema.RequiredMode.REQUIRED,
                    minLength = 1,
                    maxLength = 200
                )
                @NotBlank(message = "Title is required")
                @Size(min = 1, max = 200)
                private String title;

                @Schema(
                    description = "Detailed description of the task",
                    example = "Write comprehensive API documentation using OpenAPI 3.0",
                    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                    maxLength = 2000
                )
                @Size(max = 2000)
                private String description;

                @Schema(
                    description = "Task priority level",
                    example = "HIGH",
                    allowableValues = {"LOW", "MEDIUM", "HIGH", "URGENT"}
                )
                private Priority priority;

                @Schema(
                    description = "Due date for the task in ISO 8601 format",
                    example = "2025-01-31T23:59:59Z",
                    type = "string",
                    format = "date-time"
                )
                private LocalDateTime dueDate;

                // Getters and setters
            }

            @Schema(description = "Task response data transfer object")
            public class TaskDTO {

                @Schema(description = "Unique identifier of the task", example = "1")
                private Long id;

                @Schema(description = "Title of the task", example = "Complete documentation")
                private String title;

                @Schema(description = "Detailed description", example = "Write API docs")
                private String description;

                @Schema(description = "Current status", example = "IN_PROGRESS")
                private TaskStatus status;

                @Schema(description = "Priority level", example = "HIGH")
                private Priority priority;

                @Schema(description = "Task creation timestamp", example = "2025-01-15T10:30:00Z")
                private LocalDateTime createdAt;

                @Schema(description = "Last update timestamp", example = "2025-01-15T14:20:00Z")
                private LocalDateTime updatedAt;

                @Schema(description = "Owner of the task")
                private UserDTO owner;

                // Getters and setters
            }
            ```

            ## Accessing Your API Documentation

            Once configured, you can access:

            **Swagger UI (Interactive Documentation):**
            ```
            http://localhost:8080/swagger-ui.html
            ```

            **OpenAPI JSON:**
            ```
            http://localhost:8080/v3/api-docs
            ```

            **OpenAPI YAML:**
            ```
            http://localhost:8080/v3/api-docs.yaml
            ```

            ### Using Swagger UI

            The Swagger UI provides an interactive interface where you can:

            1. **Browse Endpoints**: See all available API endpoints organized by tags
            2. **Try Requests**: Click "Try it out" to execute real API calls
            3. **Authenticate**: Click "Authorize" to add JWT token for protected endpoints
            4. **View Schemas**: Examine request and response data structures
            5. **Copy cURL**: Get cURL commands for any endpoint
            6. **Download Spec**: Export OpenAPI specification

            ## Best Practices for API Documentation

            ### 1. Write Meaningful Descriptions

            ```java
            // ❌ BAD: Generic, unhelpful
            @Operation(summary = "Get tasks")

            // ✅ GOOD: Specific, actionable
            @Operation(
                summary = "Get all tasks for authenticated user",
                description = "Returns a paginated list of tasks. Results can be filtered by " +
                              "status, priority, and due date. Supports sorting by creation date " +
                              "or priority. Maximum 100 tasks per page."
            )
            ```

            ### 2. Document All Response Codes

            ```java
            @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Success"),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Forbidden"),
                @ApiResponse(responseCode = "404", description = "Not found"),
                @ApiResponse(responseCode = "422", description = "Business rule violation"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
            ```

            ### 3. Provide Realistic Examples

            ```java
            @Schema(
                description = "User email address",
                example = "john.doe@example.com",  // ✅ Realistic
                // NOT example = "string"           // ❌ Generic
            )
            ```

            ### 4. Document Validation Rules

            ```java
            @Schema(
                description = "Password must contain at least 8 characters, including uppercase, " +
                              "lowercase, number, and special character",
                example = "SecurePass123!",
                minLength = 8,
                maxLength = 100,
                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            )
            ```

            ### 5. Group Related Endpoints

            ```java
            @Tag(name = "Authentication", description = "User authentication and registration")
            @Tag(name = "Tasks", description = "Task CRUD operations")
            @Tag(name = "Users", description = "User profile management")
            @Tag(name = "Admin", description = "Administrative operations")
            ```

            ### 6. Document Deprecations

            ```java
            @Operation(
                summary = "Get tasks (deprecated)",
                description = "This endpoint is deprecated. Use GET /api/v2/tasks instead.",
                deprecated = true
            )
            @Deprecated
            @GetMapping("/api/v1/tasks")
            public ResponseEntity<List<TaskDTO>> getTasksV1() {
                // ...
            }
            ```

            ### 7. Use Enums for Limited Values

            ```java
            public enum TaskStatus {
                @Schema(description = "Task is pending and not yet started")
                PENDING,

                @Schema(description = "Task is currently being worked on")
                IN_PROGRESS,

                @Schema(description = "Task has been completed")
                COMPLETED,

                @Schema(description = "Task has been cancelled")
                CANCELLED
            }
            ```

            ## Common Mistakes to Avoid

            ### ❌ Missing Security Configuration

            **Wrong:** Forgetting to allow Swagger UI access
            ```java
            // Swagger UI blocked by Spring Security!
            .anyRequest().authenticated()
            ```

            **Right:** Explicitly permit Swagger paths
            ```java
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            ```

            ### ❌ Generic Error Descriptions

            **Wrong:**
            ```java
            @ApiResponse(responseCode = "400", description = "Bad request")
            ```

            **Right:**
            ```java
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input. Check that all required fields are provided and " +
                              "values meet validation requirements (email format, password strength, etc.)"
            )
            ```

            ### ❌ Missing Examples

            **Wrong:**
            ```java
            @Schema(description = "User age")
            private Integer age;
            ```

            **Right:**
            ```java
            @Schema(
                description = "User age in years. Must be 18 or older.",
                example = "25",
                minimum = "18",
                maximum = "120"
            )
            private Integer age;
            ```

            ### ❌ Not Documenting Authentication

            **Wrong:** No indication that authentication is required

            **Right:**
            ```java
            @SecurityRequirement(name = "bearerAuth")
            @Operation(
                summary = "Delete task",
                description = "Requires authentication. Only the task owner can delete the task."
            )
            ```

            ## Summary

            Professional API documentation with Swagger and OpenAPI is essential for modern APIs:

            **Why It Matters:**
            - Reduces integration time dramatically
            - Enables self-service API adoption
            - Serves as a contract between frontend and backend
            - Automatically stays synchronized with code
            - Provides interactive "try it out" functionality

            **Implementation Steps:**
            1. Add springdoc-openapi-starter-webmvc-ui dependency
            2. Create OpenAPI configuration with info and security
            3. Configure springdoc properties in application.yml
            4. Update Spring Security to allow Swagger UI access
            5. Annotate controllers with @Tag, @Operation, @ApiResponses
            6. Document DTOs with @Schema annotations
            7. Provide realistic examples and detailed descriptions

            **Best Practices:**
            - Write meaningful, specific descriptions
            - Document all possible response codes
            - Provide realistic examples for all fields
            - Group related endpoints with tags
            - Document validation rules and constraints
            - Keep documentation synchronized with code
            - Mark deprecated endpoints clearly

            **Access Points:**
            - Swagger UI: http://localhost:8080/swagger-ui.html
            - OpenAPI JSON: http://localhost:8080/v3/api-docs
            - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

            Good API documentation transforms an API from a black box into a clear,
            self-explanatory interface that developers love to use.
            """,
            45  // Estimated minutes to complete
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        QuizQuestion question = new QuizQuestion(
            "What is the PRIMARY advantage of using OpenAPI/Swagger documentation over manually written API documentation?",
            "C",
            """
            Understanding why automated, code-synchronized documentation is superior to manual
            documentation is key to professional API development.

            The correct answer is C. OpenAPI documentation is generated from code annotations,
            so it automatically stays synchronized with the actual implementation. This solves
            the biggest problem with traditional API documentation: documentation drift.

            Why this is critical:

            **The Documentation Drift Problem:**
            With manual documentation (Word docs, wikis, README files):
            1. Developer implements new endpoint or changes existing one
            2. Developer forgets to update documentation (or doesn't have time)
            3. Documentation becomes outdated
            4. Frontend developers integrate against wrong specification
            5. Bugs emerge, time is wasted, frustration ensues

            **Real-world example:**
            ```
            Manual docs say: POST /api/users requires { "name", "email" }
            Actual API now requires: { "name", "email", "phoneNumber" }
            Frontend keeps sending old format → 400 errors → confusion
            ```

            **With OpenAPI/Swagger:**
            ```java
            @PostMapping("/api/users")
            public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest request) {
                // Implementation
            }

            // UserRequest class
            public class UserRequest {
                @NotBlank private String name;
                @Email private String email;
                @NotBlank private String phoneNumber;  // New field added
            }
            ```

            The Swagger documentation is generated from these annotations, so adding `phoneNumber`
            automatically updates the API documentation. Frontend developers see the change
            immediately in Swagger UI.

            Why other answers are incorrect:

            A is wrong because while OpenAPI does support multiple programming languages, this
            isn't its primary advantage over manual documentation. Manual documentation can also
            describe APIs for any language. The key benefit is synchronization, not language support.

            B is wrong because OpenAPI actually generates MORE detailed documentation than most
            manual documentation. With manual docs, developers often skip details to save time.
            With OpenAPI, documentation is a byproduct of annotations you're already adding for
            validation, so you naturally document more:

            ```java
            @Schema(
                description = "User email address. Must be unique in the system.",
                example = "john@example.com",
                format = "email"
            )
            @Email
            @NotBlank
            private String email;
            ```

            This single field is now thoroughly documented with description, example, format, and
            validation rules—much more detail than most manual documentation.

            D is wrong because OpenAPI documentation is NOT read-only. The Swagger UI provides
            interactive "Try it out" functionality where developers can:
            - Execute real API calls
            - See actual request/response
            - Test with different parameters
            - Authenticate and test protected endpoints

            This is MORE interactive than manual documentation, not less.

            Additional benefits of code-synchronized documentation:
            - Impossible for docs to describe non-existent endpoints
            - Request/response schemas always match actual code
            - Validation rules automatically documented
            - Refactoring updates documentation automatically
            - New team members always see current API state
            - No "documentation review" step needed in deployment

            **Best practice:** Think of OpenAPI annotations as documentation that happens to
            generate beautiful UI, rather than documentation separate from code.
            """
        );

        question.addChoice("A", "OpenAPI supports multiple programming languages while manual documentation is language-specific");
        question.addChoice("B", "OpenAPI documentation is simpler and requires less detail than manual documentation");
        question.addChoice("C", "OpenAPI documentation is generated from code, ensuring it stays synchronized with the actual implementation");
        question.addChoice("D", "OpenAPI provides read-only documentation that's safer than interactive manual examples");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "You've added springdoc-openapi to your Spring Boot 3 application with Spring Security enabled, but you're getting 401 Unauthorized when accessing /swagger-ui.html. What's the MOST LIKELY cause?",
            "B",
            """
            This is an extremely common issue when first integrating Swagger with Spring Security.
            Understanding Spring Security's default-deny behavior is critical.

            The correct answer is B. Spring Security is blocking access to Swagger UI endpoints
            because they haven't been explicitly permitted in the security configuration. Spring
            Security follows a "secure by default" principle—any endpoint not explicitly permitted
            requires authentication.

            **Why this happens:**

            When you add Spring Security, you typically have:
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()  // ← This blocks Swagger!
                    );
                return http.build();
            }
            ```

            The `.anyRequest().authenticated()` means everything not explicitly permitted requires
            authentication, including `/swagger-ui.html` and related endpoints.

            **The Solution:**
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .authorizeHttpRequests(auth -> auth
                        // Allow Swagger UI and OpenAPI docs
                        .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                        ).permitAll()
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                    );
                return http.build();
            }
            ```

            **Important patterns to permit:**
            - `/swagger-ui/**` - Swagger UI static resources
            - `/swagger-ui.html` - Main Swagger UI page
            - `/v3/api-docs/**` - OpenAPI specification endpoints
            - `/v3/api-docs` - Main OpenAPI JSON endpoint

            Why other answers are incorrect:

            A is wrong because springdoc-openapi version 2.x is the CORRECT version for Spring
            Boot 3. Version 1.x only supports Spring Boot 2. The error message you'd see with
            version mismatch would be compilation errors or startup failures, not 401 Unauthorized.

            Version compatibility:
            - Spring Boot 2.x → springdoc-openapi v1.x
            - Spring Boot 3.x → springdoc-openapi v2.x

            C is wrong because Swagger UI does NOT require JWT authentication by default. Swagger
            UI itself is meant to be publicly accessible (though you might choose to protect it in
            production). The Swagger UI provides a mechanism to ADD authentication for testing
            PROTECTED endpoints, but accessing Swagger UI itself doesn't require authentication.

            Swagger authentication flow:
            1. Access Swagger UI (no auth needed for the UI itself)
            2. Click "Authorize" button
            3. Enter JWT token
            4. Now you can test protected endpoints

            D is wrong because springdoc.swagger-ui.enabled defaults to true when you include the
            dependency. You don't need to explicitly set it unless you want to DISABLE Swagger UI
            (e.g., in production). The 401 error indicates the request reached Swagger but was
            blocked by security, not that Swagger is disabled.

            **Debugging checklist when Swagger returns 401:**

            1. ✅ Check Spring Security configuration permits Swagger paths
            2. ✅ Verify patterns match actual Swagger paths (use browser dev tools to see what's being requested)
            3. ✅ Ensure security configuration is actually being applied (add logging)
            4. ✅ Check for multiple SecurityFilterChain beans that might conflict
            5. ✅ Verify application.yml doesn't have conflicting security settings

            **Production consideration:**

            In production, you might WANT to protect Swagger UI:
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .authorizeHttpRequests(auth -> auth
                        // In production, protect Swagger UI with basic auth or IP whitelist
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                            .hasRole("ADMIN")  // Only admins can see API docs
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        // Everything else
                        .anyRequest().authenticated()
                    );
                return http.build();
            }
            ```

            Or disable entirely in production:
            ```yaml
            # application-prod.yml
            springdoc:
              swagger-ui:
                enabled: false
              api-docs:
                enabled: false
            ```
            """
        );

        question.addChoice("A", "The springdoc-openapi version is incompatible with Spring Boot 3");
        question.addChoice("B", "Spring Security is blocking access to Swagger UI endpoints - they need to be explicitly permitted");
        question.addChoice("C", "Swagger UI requires JWT authentication to be configured before it can be accessed");
        question.addChoice("D", "The springdoc.swagger-ui.enabled property needs to be set to true in application.yml");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "When documenting your API with OpenAPI annotations, which approach provides the BEST developer experience for API consumers?",
            "D",
            """
            Creating developer-friendly API documentation requires more than just technical accuracy—
            it requires understanding what information developers actually need to successfully use your API.

            The correct answer is D. Detailed descriptions, realistic examples, and comprehensive
            error documentation provide the best developer experience. This approach gives developers
            everything they need to integrate successfully on their first try.

            **Why this is the best approach:**

            Compare these two approaches to documenting the same endpoint:

            **Approach A (Minimal):**
            ```java
            @PostMapping("/api/users")
            public ResponseEntity<User> createUser(@RequestBody User user) {
                return ResponseEntity.ok(userService.create(user));
            }
            ```
            Generated docs just show: "POST /api/users" with User schema.

            **Approach D (Comprehensive):**
            ```java
            @Operation(
                summary = "Create a new user account",
                description = "Registers a new user in the system. Email must be unique. " +
                              "Password must be at least 8 characters with uppercase, lowercase, " +
                              "number, and special character. User will receive a verification email."
            )
            @ApiResponses(value = {
                @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully. Verification email sent.",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
                ),
                @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed. Common issues: " +
                                  "invalid email format, weak password, missing required fields",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
                ),
                @ApiResponse(
                    responseCode = "409",
                    description = "Email already registered. User should try logging in instead.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
                )
            })
            @PostMapping("/api/users")
            public ResponseEntity<UserDTO> createUser(
                @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(
                        schema = @Schema(implementation = UserRequest.class),
                        examples = @ExampleObject(
                            name = "Valid user registration",
                            value = "{\\"name\\": \\"John Doe\\", " +
                                    "\\"email\\": \\"john@example.com\\", " +
                                    "\\"password\\": \\"SecurePass123!\\"}"
                        )
                    )
                )
                @Valid @RequestBody UserRequest request
            ) {
                UserDTO created = userService.createUser(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(created);
            }
            ```

            **What Approach D provides that others don't:**

            1. **Clear expectations** - Developer knows password requirements upfront
            2. **Realistic examples** - "john@example.com" vs "string"
            3. **Error guidance** - Knows what 409 means and what to do about it
            4. **Business context** - Understands verification email will be sent
            5. **Success criteria** - Knows exactly what 201 response means

            **Real developer experience difference:**

            With minimal documentation (Approach A):
            - Developer tries: `{"email": "test", "password": "pass"}`
            - Gets 400 error
            - Doesn't know what's wrong
            - Tries random variations
            - Gives up or asks for help

            With comprehensive documentation (Approach D):
            - Developer reads description, sees requirements
            - Uses realistic example as template
            - Submits correct format first try
            - Handles errors appropriately
            - Integration succeeds quickly

            Why other answers are inadequate:

            A is wrong because while keeping annotations minimal reduces code clutter, it defeats
            the purpose of API documentation. The whole point is to provide comprehensive information
            to API consumers. If you want cleaner code, move documentation to a separate OpenAPI
            spec file, but don't skip documentation entirely.

            B is wrong for two reasons:

            1. **Generic examples like "string" are useless.** Developers need realistic examples:
               - Bad: `"email": "string"`
               - Good: `"email": "john.doe@example.com"`

            2. **You MUST document errors.** Most API integration problems come from misunderstanding
               error conditions. Developers need to know:
               - What errors can occur
               - Why they occur
               - How to fix them

            Example: If your API returns 422 for "business rule violation," developers need to know
            what business rules exist, or they'll be confused when users hit edge cases.

            C is wrong because HTTP status codes are NOT self-documenting. While standard codes
            have standard meanings (404 = Not Found), the REASON something wasn't found and what
            to do about it needs explanation:

            - 404: "Task not found" - Why? Task deleted? Wrong ID? No access permission?
            - 400: "Bad request" - What's bad about it? Which field? What format expected?
            - 409: "Conflict" - Conflict with what? Duplicate email? Concurrent modification?

            Status codes tell you THAT something went wrong, not WHY or HOW TO FIX IT.

            **Best practices for documentation:**

            ```java
            // ✅ GOOD: Specific, actionable, with examples
            @Operation(
                summary = "Delete task by ID",
                description = "Permanently deletes a task. Cannot be undone. " +
                              "Only the task owner can delete. " +
                              "Tasks completed more than 30 days ago cannot be deleted (archived)."
            )
            @ApiResponse(responseCode = "204", description = "Task deleted successfully")
            @ApiResponse(
                responseCode = "422",
                description = "Cannot delete archived task. Tasks completed over 30 days ago " +
                              "are archived and cannot be deleted for compliance reasons."
            )

            // ❌ BAD: Generic, unhelpful
            @Operation(summary = "Delete task")
            @ApiResponse(responseCode = "204", description = "Success")
            ```

            **Developer experience metrics to optimize:**
            - Time to first successful API call
            - Number of support questions about API usage
            - Error rate during integration
            - Developer satisfaction scores

            Comprehensive documentation directly improves all these metrics.
            """
        );

        question.addChoice("A", "Minimal annotations with just @Operation summary - detailed documentation should be in separate wiki pages");
        question.addChoice("B", "Focus on documenting successful responses with example=\"string\" - developers can figure out errors themselves");
        question.addChoice("C", "Only document HTTP status codes since they're self-documenting according to REST standards");
        question.addChoice("D", "Provide detailed descriptions, realistic examples, comprehensive error documentation, and business context");

        return question;
    }
}
