package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.5: Building Your Backend - Services and Controllers
 */
public class Lesson05Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-5", "Lesson 9.5: Services and Controllers - The Business Logic")
            .addTheory("Moving Up the Stack",
                "You have entities and repositories. Now add business logic!\n\n" +
                "THE LAYERED ARCHITECTURE:\n\n" +
                "┌─────────────────────────────┐\n" +
                "│ CONTROLLER (REST API)       │ ← We'll build this\n" +
                "│ Handles HTTP requests       │\n" +
                "│ @RestController             │\n" +
                "├─────────────────────────────┤\n" +
                "│ SERVICE (Business Logic)    │ ← We'll build this\n" +
                "│ Validation, authorization   │\n" +
                "│ @Service                    │\n" +
                "├─────────────────────────────┤\n" +
                "│ REPOSITORY (Database)       │ ✓ Already built!\n" +
                "│ CRUD operations             │\n" +
                "│ JpaRepository               │\n" +
                "├─────────────────────────────┤\n" +
                "│ DATABASE (PostgreSQL)       │ ✓ Already set up!\n" +
                "│ Stores data                 │\n" +
                "└─────────────────────────────┘\n\n" +
                "PLUS: DTOs (Data Transfer Objects)\n" +
                "- Never expose entities to clients!\n" +
                "- DTOs control what data is sent/received\n\n" +
                "GOAL: Build complete backend API!")
            .addAnalogy("Service and Controller are Like Restaurant Kitchen",
                "CONTROLLER (The waiter):\n" +
                "Takes orders from customers (HTTP requests)\n" +
                "Like: \"I'd like a cheeseburger, please\"\n" +
                "Validates: \"Sorry, we don't serve pizza\"\n" +
                "Returns: Food to customer (HTTP response)\n\n" +
                "SERVICE (The chef):\n" +
                "Prepares the food (business logic)\n" +
                "Like: Checks if ingredients available\n" +
                "      Follows recipe (business rules)\n" +
                "      Quality control\n\n" +
                "REPOSITORY (The pantry/storage):\n" +
                "Stores and retrieves ingredients\n" +
                "Like: Get cheese, get buns, store leftovers\n\n" +
                "DTO (The menu item description):\n" +
                "Customer sees \"Cheeseburger - $10\"\n" +
                "NOT: \"Ground beef (cost: $3), bun (cost: $0.50)...\"\n" +
                "DTOs hide internal details!\n\n" +
                "WORKFLOW:\n" +
                "1. Customer orders via waiter (Controller)\n" +
                "2. Waiter tells chef (Service)\n" +
                "3. Chef gets ingredients from pantry (Repository)\n" +
                "4. Chef prepares food (Business logic)\n" +
                "5. Waiter serves customer (Response)")
            .addTheory("Step 1: Create DTOs (Data Transfer Objects)",
                "WHY DTOs?\n" +
                "❌ DON'T return entities from REST API:\n" +
                "- Exposes database structure\n" +
                "- May leak sensitive data (passwords!)\n" +
                "- Tight coupling\n" +
                "- Circular reference issues (User → Task → User)\n\n" +
                "✓ DO use DTOs:\n" +
                "- Control exactly what data is sent/received\n" +
                "- Hide sensitive fields\n" +
                "- Validation annotations\n" +
                "- Decouple API from database\n\n" +
                "Create: src/main/java/com/yourname/taskmanager/dto/UserDTO.java\n\n" +
                "package com.yourname.taskmanager.dto;\n\n" +
                "import jakarta.validation.constraints.Email;\n" +
                "import jakarta.validation.constraints.NotBlank;\n" +
                "import jakarta.validation.constraints.Size;\n" +
                "import java.time.LocalDateTime;\n\n" +
                "// Response DTO - What we send to client\n" +
                "public class UserDTO {\n" +
                "    private Long id;\n" +
                "    private String email;\n" +
                "    private String name;\n" +
                "    private LocalDateTime createdAt;\n" +
                "    // No password field! Security!\n" +
                "    \n" +
                "    // Constructors\n" +
                "    public UserDTO() {}\n" +
                "    \n" +
                "    public UserDTO(Long id, String email, String name, LocalDateTime createdAt) {\n" +
                "        this.id = id;\n" +
                "        this.email = email;\n" +
                "        this.name = name;\n" +
                "        this.createdAt = createdAt;\n" +
                "    }\n" +
                "    \n" +
                "    // Getters and Setters\n" +
                "    public Long getId() { return id; }\n" +
                "    public void setId(Long id) { this.id = id; }\n" +
                "    \n" +
                "    public String getEmail() { return email; }\n" +
                "    public void setEmail(String email) { this.email = email; }\n" +
                "    \n" +
                "    public String getName() { return name; }\n" +
                "    public void setName(String name) { this.name = name; }\n" +
                "    \n" +
                "    public LocalDateTime getCreatedAt() { return createdAt; }\n" +
                "    public void setCreatedAt(LocalDateTime createdAt) { \n" +
                "        this.createdAt = createdAt; \n" +
                "    }\n" +
                "}\n\n" +
                "// Request DTO - What we receive from client\n" +
                "public class CreateUserRequest {\n" +
                "    \n" +
                "    @NotBlank(message = \"Email is required\")\n" +
                "    @Email(message = \"Email must be valid\")\n" +
                "    private String email;\n" +
                "    \n" +
                "    @NotBlank(message = \"Password is required\")\n" +
                "    @Size(min = 8, message = \"Password must be at least 8 characters\")\n" +
                "    private String password;\n" +
                "    \n" +
                "    @NotBlank(message = \"Name is required\")\n" +
                "    @Size(max = 100, message = \"Name must be less than 100 characters\")\n" +
                "    private String name;\n" +
                "    \n" +
                "    // Getters and Setters\n" +
                "    public String getEmail() { return email; }\n" +
                "    public void setEmail(String email) { this.email = email; }\n" +
                "    \n" +
                "    public String getPassword() { return password; }\n" +
                "    public void setPassword(String password) { this.password = password; }\n" +
                "    \n" +
                "    public String getName() { return name; }\n" +
                "    public void setName(String name) { this.name = name; }\n" +
                "}")
            .addTheory("Step 2: Create TaskDTO",
                "Create: src/main/java/com/yourname/taskmanager/dto/TaskDTO.java\n\n" +
                "package com.yourname.taskmanager.dto;\n\n" +
                "import jakarta.validation.constraints.NotBlank;\n" +
                "import jakarta.validation.constraints.Size;\n" +
                "import java.time.LocalDateTime;\n\n" +
                "// Response DTO\n" +
                "public class TaskDTO {\n" +
                "    private Long id;\n" +
                "    private String title;\n" +
                "    private String description;\n" +
                "    private Boolean completed;\n" +
                "    private Long userId;\n" +
                "    private LocalDateTime createdAt;\n" +
                "    private LocalDateTime updatedAt;\n" +
                "    \n" +
                "    // Constructors\n" +
                "    public TaskDTO() {}\n" +
                "    \n" +
                "    public TaskDTO(Long id, String title, String description, \n" +
                "                   Boolean completed, Long userId,\n" +
                "                   LocalDateTime createdAt, LocalDateTime updatedAt) {\n" +
                "        this.id = id;\n" +
                "        this.title = title;\n" +
                "        this.description = description;\n" +
                "        this.completed = completed;\n" +
                "        this.userId = userId;\n" +
                "        this.createdAt = createdAt;\n" +
                "        this.updatedAt = updatedAt;\n" +
                "    }\n" +
                "    \n" +
                "    // Getters and Setters (all fields)\n" +
                "}\n\n" +
                "// Request DTO for creating/updating tasks\n" +
                "public class TaskRequest {\n" +
                "    \n" +
                "    @NotBlank(message = \"Title is required\")\n" +
                "    @Size(max = 255, message = \"Title must be less than 255 characters\")\n" +
                "    private String title;\n" +
                "    \n" +
                "    @Size(max = 5000, message = \"Description too long\")\n" +
                "    private String description;\n" +
                "    \n" +
                "    private Boolean completed;\n" +
                "    \n" +
                "    // Getters and Setters\n" +
                "    public String getTitle() { return title; }\n" +
                "    public void setTitle(String title) { this.title = title; }\n" +
                "    \n" +
                "    public String getDescription() { return description; }\n" +
                "    public void setDescription(String description) { \n" +
                "        this.description = description; \n" +
                "    }\n" +
                "    \n" +
                "    public Boolean getCompleted() { return completed; }\n" +
                "    public void setCompleted(Boolean completed) { \n" +
                "        this.completed = completed; \n" +
                "    }\n" +
                "}\n\n" +
                "VALIDATION ANNOTATIONS (2024-2025):\n" +
                "@NotBlank - String cannot be null or empty\n" +
                "@NotNull - Value cannot be null (but can be empty)\n" +
                "@Email - Must be valid email format\n" +
                "@Size(min=x, max=y) - Length constraints\n" +
                "@Min(x) / @Max(y) - Number constraints\n" +
                "@Pattern(regexp=\"...\") - Regex validation\n" +
                "@Past / @Future - Date constraints")
            .addTheory("Step 3: Create TaskService",
                "Create: src/main/java/com/yourname/taskmanager/service/TaskService.java\n\n" +
                "package com.yourname.taskmanager.service;\n\n" +
                "import com.yourname.taskmanager.dto.TaskDTO;\n" +
                "import com.yourname.taskmanager.dto.TaskRequest;\n" +
                "import com.yourname.taskmanager.model.Task;\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import com.yourname.taskmanager.repository.TaskRepository;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import org.springframework.transaction.annotation.Transactional;\n" +
                "import java.util.List;\n" +
                "import java.util.stream.Collectors;\n\n" +
                "@Service\n" +
                "@Transactional\n" +
                "public class TaskService {\n" +
                "    \n" +
                "    private final TaskRepository taskRepository;\n" +
                "    \n" +
                "    // Constructor injection (best practice)\n" +
                "    public TaskService(TaskRepository taskRepository) {\n" +
                "        this.taskRepository = taskRepository;\n" +
                "    }\n" +
                "    \n" +
                "    // Get all tasks for user\n" +
                "    public List<TaskDTO> getAllTasksForUser(Long userId) {\n" +
                "        return taskRepository.findByUserId(userId)\n" +
                "            .stream()\n" +
                "            .map(this::convertToDTO)\n" +
                "            .collect(Collectors.toList());\n" +
                "    }\n" +
                "    \n" +
                "    // Get single task by ID\n" +
                "    public TaskDTO getTaskById(Long taskId, Long userId) {\n" +
                "        Task task = taskRepository.findById(taskId)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "                \"Task not found with id: \" + taskId));\n" +
                "        \n" +
                "        // Security check: ensure task belongs to user\n" +
                "        if (!task.getUser().getId().equals(userId)) {\n" +
                "            throw new ForbiddenException(\n" +
                "                \"You don't have permission to access this task\");\n" +
                "        }\n" +
                "        \n" +
                "        return convertToDTO(task);\n" +
                "    }\n" +
                "    \n" +
                "    // Create new task\n" +
                "    public TaskDTO createTask(TaskRequest request, Long userId) {\n" +
                "        // Validation already done by @Valid in controller\n" +
                "        \n" +
                "        // Create entity\n" +
                "        User user = new User();\n" +
                "        user.setId(userId);  // In real app, fetch from UserService\n" +
                "        \n" +
                "        Task task = new Task();\n" +
                "        task.setTitle(request.getTitle());\n" +
                "        task.setDescription(request.getDescription());\n" +
                "        task.setCompleted(false);  // Always false for new tasks\n" +
                "        task.setUser(user);\n" +
                "        \n" +
                "        // Save to database\n" +
                "        Task saved = taskRepository.save(task);\n" +
                "        \n" +
                "        return convertToDTO(saved);\n" +
                "    }\n" +
                "    \n" +
                "    // Update existing task\n" +
                "    public TaskDTO updateTask(Long taskId, TaskRequest request, Long userId) {\n" +
                "        Task task = taskRepository.findById(taskId)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "                \"Task not found with id: \" + taskId));\n" +
                "        \n" +
                "        // Security check\n" +
                "        if (!task.getUser().getId().equals(userId)) {\n" +
                "            throw new ForbiddenException(\n" +
                "                \"You don't have permission to update this task\");\n" +
                "        }\n" +
                "        \n" +
                "        // Update fields (only if provided)\n" +
                "        if (request.getTitle() != null) {\n" +
                "            task.setTitle(request.getTitle());\n" +
                "        }\n" +
                "        if (request.getDescription() != null) {\n" +
                "            task.setDescription(request.getDescription());\n" +
                "        }\n" +
                "        if (request.getCompleted() != null) {\n" +
                "            task.setCompleted(request.getCompleted());\n" +
                "        }\n" +
                "        \n" +
                "        // Save (triggers @PreUpdate)\n" +
                "        Task updated = taskRepository.save(task);\n" +
                "        \n" +
                "        return convertToDTO(updated);\n" +
                "    }\n" +
                "    \n" +
                "    // Delete task\n" +
                "    public void deleteTask(Long taskId, Long userId) {\n" +
                "        Task task = taskRepository.findById(taskId)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "                \"Task not found with id: \" + taskId));\n" +
                "        \n" +
                "        // Security check\n" +
                "        if (!task.getUser().getId().equals(userId)) {\n" +
                "            throw new ForbiddenException(\n" +
                "                \"You don't have permission to delete this task\");\n" +
                "        }\n" +
                "        \n" +
                "        taskRepository.delete(task);\n" +
                "    }\n" +
                "    \n" +
                "    // Helper: Convert Entity to DTO\n" +
                "    private TaskDTO convertToDTO(Task task) {\n" +
                "        return new TaskDTO(\n" +
                "            task.getId(),\n" +
                "            task.getTitle(),\n" +
                "            task.getDescription(),\n" +
                "            task.getCompleted(),\n" +
                "            task.getUser().getId(),\n" +
                "            task.getCreatedAt(),\n" +
                "            task.getUpdatedAt()\n" +
                "        );\n" +
                "    }\n" +
                "}")
            .addKeyPoint("Understanding @Transactional",
                "@Transactional on class or method:\n\n" +
                "WHAT IT DOES:\n" +
                "- Opens database transaction at method start\n" +
                "- Commits transaction if method succeeds\n" +
                "- Rolls back transaction if exception thrown\n\n" +
                "EXAMPLE:\n" +
                "@Transactional\n" +
                "public void updateTask(...) {\n" +
                "    Task task = taskRepository.findById(id);  // Query 1\n" +
                "    task.setTitle(\"New title\");\n" +
                "    taskRepository.save(task);                // Query 2\n" +
                "    \n" +
                "    // If this throws exception, both queries rollback!\n" +
                "    sendNotification(task);\n" +
                "}\n\n" +
                "WITHOUT @Transactional:\n" +
                "- Each query commits immediately\n" +
                "- Can't rollback if something fails\n" +
                "- LazyInitializationException errors\n\n" +
                "BEST PRACTICE:\n" +
                "✓ Put @Transactional on service methods (not repository)\n" +
                "✓ Service methods = business logic boundaries\n" +
                "✓ Keep transactions short (performance)\n" +
                "✓ Use readOnly=true for read-only methods:\n" +
                "  @Transactional(readOnly = true)")
            .addTheory("Step 4: Create TaskController",
                "Create: src/main/java/com/yourname/taskmanager/controller/TaskController.java\n\n" +
                "package com.yourname.taskmanager.controller;\n\n" +
                "import com.yourname.taskmanager.dto.TaskDTO;\n" +
                "import com.yourname.taskmanager.dto.TaskRequest;\n" +
                "import com.yourname.taskmanager.service.TaskService;\n" +
                "import jakarta.validation.Valid;\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "import java.util.List;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/tasks\")\n" +
                "@CrossOrigin(origins = \"http://localhost:3000\")  // Allow frontend\n" +
                "public class TaskController {\n" +
                "    \n" +
                "    private final TaskService taskService;\n" +
                "    \n" +
                "    // Constructor injection\n" +
                "    public TaskController(TaskService taskService) {\n" +
                "        this.taskService = taskService;\n" +
                "    }\n" +
                "    \n" +
                "    // GET /api/tasks - Get all tasks for current user\n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<List<TaskDTO>> getAllTasks() {\n" +
                "        // TODO: Get userId from authenticated user (Spring Security)\n" +
                "        Long userId = 1L;  // Hardcoded for now\n" +
                "        \n" +
                "        List<TaskDTO> tasks = taskService.getAllTasksForUser(userId);\n" +
                "        return ResponseEntity.ok(tasks);\n" +
                "    }\n" +
                "    \n" +
                "    // GET /api/tasks/{id} - Get single task\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {\n" +
                "        Long userId = 1L;  // TODO: From auth\n" +
                "        \n" +
                "        TaskDTO task = taskService.getTaskById(id, userId);\n" +
                "        return ResponseEntity.ok(task);\n" +
                "    }\n" +
                "    \n" +
                "    // POST /api/tasks - Create new task\n" +
                "    @PostMapping\n" +
                "    public ResponseEntity<TaskDTO> createTask(\n" +
                "            @Valid @RequestBody TaskRequest request) {\n" +
                "        Long userId = 1L;  // TODO: From auth\n" +
                "        \n" +
                "        TaskDTO created = taskService.createTask(request, userId);\n" +
                "        return ResponseEntity.status(HttpStatus.CREATED).body(created);\n" +
                "    }\n" +
                "    \n" +
                "    // PUT /api/tasks/{id} - Update task\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public ResponseEntity<TaskDTO> updateTask(\n" +
                "            @PathVariable Long id,\n" +
                "            @Valid @RequestBody TaskRequest request) {\n" +
                "        Long userId = 1L;  // TODO: From auth\n" +
                "        \n" +
                "        TaskDTO updated = taskService.updateTask(id, request, userId);\n" +
                "        return ResponseEntity.ok(updated);\n" +
                "    }\n" +
                "    \n" +
                "    // DELETE /api/tasks/{id} - Delete task\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {\n" +
                "        Long userId = 1L;  // TODO: From auth\n" +
                "        \n" +
                "        taskService.deleteTask(id, userId);\n" +
                "        return ResponseEntity.noContent().build();\n" +
                "    }\n" +
                "}\n\n" +
                "KEY ANNOTATIONS:\n" +
                "@RestController = @Controller + @ResponseBody\n" +
                "@RequestMapping(\"/api/tasks\") = Base URL for all endpoints\n" +
                "@GetMapping = Handle GET requests\n" +
                "@PostMapping = Handle POST requests\n" +
                "@PutMapping = Handle PUT requests\n" +
                "@DeleteMapping = Handle DELETE requests\n" +
                "@PathVariable = Extract from URL (/api/tasks/{id})\n" +
                "@RequestBody = Parse JSON from request body\n" +
                "@Valid = Trigger DTO validation\n" +
                "@CrossOrigin = Allow requests from frontend domain")
            .addTheory("Step 5: Create Custom Exceptions",
                "Create: src/main/java/com/yourname/taskmanager/exception/ResourceNotFoundException.java\n\n" +
                "package com.yourname.taskmanager.exception;\n\n" +
                "public class ResourceNotFoundException extends RuntimeException {\n" +
                "    public ResourceNotFoundException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n\n" +
                "Create: src/main/java/com/yourname/taskmanager/exception/ForbiddenException.java\n\n" +
                "package com.yourname.taskmanager.exception;\n\n" +
                "public class ForbiddenException extends RuntimeException {\n" +
                "    public ForbiddenException(String message) {\n" +
                "        super(message);\n" +
                "    }\n" +
                "}\n\n" +
                "Create: src/main/java/com/yourname/taskmanager/exception/GlobalExceptionHandler.java\n\n" +
                "package com.yourname.taskmanager.exception;\n\n" +
                "import org.springframework.http.HttpStatus;\n" +
                "import org.springframework.http.ProblemDetail;\n" +
                "import org.springframework.validation.FieldError;\n" +
                "import org.springframework.web.bind.MethodArgumentNotValidException;\n" +
                "import org.springframework.web.bind.annotation.ExceptionHandler;\n" +
                "import org.springframework.web.bind.annotation.RestControllerAdvice;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n\n" +
                "@RestControllerAdvice\n" +
                "public class GlobalExceptionHandler {\n" +
                "    \n" +
                "    // Handle ResourceNotFoundException (404)\n" +
                "    @ExceptionHandler(ResourceNotFoundException.class)\n" +
                "    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.NOT_FOUND,\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        problem.setTitle(\"Resource Not Found\");\n" +
                "        return problem;\n" +
                "    }\n" +
                "    \n" +
                "    // Handle ForbiddenException (403)\n" +
                "    @ExceptionHandler(ForbiddenException.class)\n" +
                "    public ProblemDetail handleForbidden(ForbiddenException ex) {\n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.FORBIDDEN,\n" +
                "            ex.getMessage()\n" +
                "        );\n" +
                "        problem.setTitle(\"Access Forbidden\");\n" +
                "        return problem;\n" +
                "    }\n" +
                "    \n" +
                "    // Handle validation errors (400)\n" +
                "    @ExceptionHandler(MethodArgumentNotValidException.class)\n" +
                "    public ProblemDetail handleValidation(\n" +
                "            MethodArgumentNotValidException ex) {\n" +
                "        Map<String, String> errors = new HashMap<>();\n" +
                "        ex.getBindingResult().getAllErrors().forEach(error -> {\n" +
                "            String fieldName = ((FieldError) error).getField();\n" +
                "            String errorMessage = error.getDefaultMessage();\n" +
                "            errors.put(fieldName, errorMessage);\n" +
                "        });\n" +
                "        \n" +
                "        ProblemDetail problem = ProblemDetail.forStatusAndDetail(\n" +
                "            HttpStatus.BAD_REQUEST,\n" +
                "            \"Validation failed\"\n" +
                "        );\n" +
                "        problem.setTitle(\"Invalid Request\");\n" +
                "        problem.setProperty(\"errors\", errors);\n" +
                "        return problem;\n" +
                "    }\n" +
                "}")
            .addExample("Testing Your API with Postman",
                "Now test your complete backend!\n\n" +
                "1. START APPLICATION:\n" +
                "   Run TaskmanagerApplication.java\n" +
                "   Check console: \"Started TaskmanagerApplication\"\n\n" +
                "2. TEST CREATE TASK:\n" +
                "   POST http://localhost:8080/api/tasks\n" +
                "   Headers: Content-Type: application/json\n" +
                "   Body:\n" +
                "   {\n" +
                "     \"title\": \"Buy groceries\",\n" +
                "     \"description\": \"Milk, eggs, bread\"\n" +
                "   }\n" +
                "   \n" +
                "   Expected: 201 Created\n" +
                "   Response:\n" +
                "   {\n" +
                "     \"id\": 1,\n" +
                "     \"title\": \"Buy groceries\",\n" +
                "     \"description\": \"Milk, eggs, bread\",\n" +
                "     \"completed\": false,\n" +
                "     \"userId\": 1,\n" +
                "     \"createdAt\": \"2025-01-15T10:30:00\",\n" +
                "     \"updatedAt\": \"2025-01-15T10:30:00\"\n" +
                "   }\n\n" +
                "3. TEST GET ALL TASKS:\n" +
                "   GET http://localhost:8080/api/tasks\n" +
                "   \n" +
                "   Expected: 200 OK\n" +
                "   Response: Array of tasks\n\n" +
                "4. TEST UPDATE TASK:\n" +
                "   PUT http://localhost:8080/api/tasks/1\n" +
                "   Body:\n" +
                "   {\n" +
                "     \"title\": \"Buy groceries\",\n" +
                "     \"completed\": true\n" +
                "   }\n" +
                "   \n" +
                "   Expected: 200 OK\n" +
                "   Response: Updated task with completed=true\n\n" +
                "5. TEST VALIDATION:\n" +
                "   POST http://localhost:8080/api/tasks\n" +
                "   Body:\n" +
                "   {\n" +
                "     \"title\": \"\"\n" +
                "   }\n" +
                "   \n" +
                "   Expected: 400 Bad Request\n" +
                "   Response:\n" +
                "   {\n" +
                "     \"status\": 400,\n" +
                "     \"title\": \"Invalid Request\",\n" +
                "     \"detail\": \"Validation failed\",\n" +
                "     \"errors\": {\n" +
                "       \"title\": \"Title is required\"\n" +
                "     }\n" +
                "   }\n\n" +
                "6. TEST DELETE:\n" +
                "   DELETE http://localhost:8080/api/tasks/1\n" +
                "   \n" +
                "   Expected: 204 No Content\n" +
                "   (No response body)\n\n" +
                "✓ ALL TESTS PASS = Backend is working!")
            .addWarning("Common Service and Controller Mistakes",
                "❌ MISTAKE 1: Business logic in controller\n" +
                "@PostMapping\n" +
                "public Task create(@RequestBody Task task) {\n" +
                "    if (taskRepository.existsByTitle(task.getTitle())) {\n" +
                "        throw new Exception(\"Duplicate\");\n" +
                "    }\n" +
                "    return taskRepository.save(task);  // Logic in controller!\n" +
                "}\n" +
                "FIX: Move ALL business logic to service layer\n\n" +
                "❌ MISTAKE 2: Returning entities from controller\n" +
                "return userRepository.findAll();  // Exposes password!\n" +
                "FIX: Always use DTOs\n\n" +
                "❌ MISTAKE 3: No @Valid on @RequestBody\n" +
                "@PostMapping\n" +
                "public Task create(@RequestBody TaskRequest request) {\n" +
                "    // Validation annotations ignored!\n" +
                "}\n" +
                "FIX: @Valid @RequestBody TaskRequest request\n\n" +
                "❌ MISTAKE 4: Not checking authorization\n" +
                "public void deleteTask(Long taskId) {\n" +
                "    taskRepository.deleteById(taskId);  // Anyone can delete!\n" +
                "}\n" +
                "FIX: Check if task belongs to current user\n\n" +
                "❌ MISTAKE 5: Wrong HTTP status codes\n" +
                "return ResponseEntity.ok(created);  // Should be 201 Created\n" +
                "FIX: Use appropriate status codes:\n" +
                "- 200 OK (GET, PUT)\n" +
                "- 201 Created (POST)\n" +
                "- 204 No Content (DELETE)\n" +
                "- 400 Bad Request (validation)\n" +
                "- 404 Not Found\n" +
                "- 403 Forbidden\n\n" +
                "❌ MISTAKE 6: Not handling Optional\n" +
                "Task task = taskRepository.findById(id);  // Compile error!\n" +
                "FIX: .orElseThrow(() -> new ResourceNotFoundException(...))\n\n" +
                "❌ MISTAKE 7: Swallowing exceptions\n" +
                "try {\n" +
                "    taskService.delete(id);\n" +
                "} catch (Exception e) {\n" +
                "    // Silent failure - user never knows!\n" +
                "}\n" +
                "FIX: Let exceptions propagate to GlobalExceptionHandler")
            .addKeyPoint("Best Practices Summary",
                "SERVICE LAYER:\n" +
                "✓ ALL business logic goes here\n" +
                "✓ Validation, authorization, data transformation\n" +
                "✓ Use @Service annotation\n" +
                "✓ Use @Transactional for database operations\n" +
                "✓ Convert entities to DTOs\n" +
                "✓ Throw meaningful exceptions\n\n" +
                "CONTROLLER LAYER:\n" +
                "✓ Handle HTTP requests/responses only\n" +
                "✓ Use @RestController\n" +
                "✓ Use @Valid for DTO validation\n" +
                "✓ Return DTOs, never entities\n" +
                "✓ Use proper HTTP status codes\n" +
                "✓ Keep methods thin (delegate to service)\n\n" +
                "DTOs:\n" +
                "✓ Separate DTOs for request and response\n" +
                "✓ Add validation annotations (@NotBlank, @Email, etc.)\n" +
                "✓ Never include sensitive data in response DTOs\n" +
                "✓ Use meaningful names (CreateUserRequest, UserDTO)\n\n" +
                "EXCEPTION HANDLING:\n" +
                "✓ Create custom exceptions\n" +
                "✓ Use @RestControllerAdvice for global handling\n" +
                "✓ Use ProblemDetail (Spring 6+, RFC 7807)\n" +
                "✓ Return consistent error format\n" +
                "✓ Never expose stack traces to clients")
            .addChallenge(createServiceQuiz())
            .addChallenge(createValidationQuiz())
            .addChallenge(createStatusCodeQuiz())
            .estimatedMinutes(55)
            .build();
    }

    private static Challenge createServiceQuiz() {
        return new Challenge.Builder("epoch-9-lesson-5-service", "Service Layer", ChallengeType.MULTIPLE_CHOICE)
            .description("Where should business logic be implemented?")
            .addMultipleChoiceOption("A) Controller")
            .addMultipleChoiceOption("B) Repository")
            .addMultipleChoiceOption("C) Service")
            .addMultipleChoiceOption("D) Entity")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createValidationQuiz() {
        return new Challenge.Builder("epoch-9-lesson-5-validation", "DTO Validation", ChallengeType.MULTIPLE_CHOICE)
            .description("What annotation triggers DTO validation in a controller?")
            .addMultipleChoiceOption("A) @Validated")
            .addMultipleChoiceOption("B) @Valid")
            .addMultipleChoiceOption("C) @Check")
            .addMultipleChoiceOption("D) @Verify")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createStatusCodeQuiz() {
        return new Challenge.Builder("epoch-9-lesson-5-status", "HTTP Status Codes", ChallengeType.MULTIPLE_CHOICE)
            .description("What HTTP status code should be returned when creating a new resource?")
            .addMultipleChoiceOption("A) 200 OK")
            .addMultipleChoiceOption("B) 201 Created")
            .addMultipleChoiceOption("C) 204 No Content")
            .addMultipleChoiceOption("D) 202 Accepted")
            .correctAnswer("B")
            .build();
    }
}
