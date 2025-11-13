package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.6: Building a Complete Full-Stack Feature
 */
public class Lesson06Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-6", "Lesson 8.6: Complete Feature - Database to UI")
            .addTheory("The Full-Stack Feature Journey",
                "Let's build a complete feature: USER TASK MANAGEMENT\n\n" +
                "Feature: Users can create, view, update, and delete tasks\n\n" +
                "THE COMPLETE STACK:\n\n" +
                "1. DATABASE (PostgreSQL):\n" +
                "   - tasks table (id, title, description, completed, user_id)\n\n" +
                "2. BACKEND (Spring Boot):\n" +
                "   - Task entity (Java class mapped to database)\n" +
                "   - TaskRepository (database access)\n" +
                "   - TaskService (business logic)\n" +
                "   - TaskController (REST API endpoints)\n\n" +
                "3. API LAYER (REST):\n" +
                "   - GET /api/tasks - List all tasks\n" +
                "   - POST /api/tasks - Create task\n" +
                "   - PUT /api/tasks/{id} - Update task\n" +
                "   - DELETE /api/tasks/{id} - Delete task\n\n" +
                "4. FRONTEND (HTML + JavaScript):\n" +
                "   - Task list display\n" +
                "   - Create task form\n" +
                "   - Mark complete button\n" +
                "   - Delete button\n\n" +
                "We'll build this step-by-step, bottom-up!")
            .addAnalogy("Building a Feature is Like Building a Highway System",
                "DATABASE (The land/foundation):\n" +
                "Where everything is stored permanently\n" +
                "Like: The actual roads built into the ground\n\n" +
                "ENTITY (The blueprint):\n" +
                "Defines what a 'Task' looks like in Java\n" +
                "Like: Highway specifications (lanes, width, materials)\n\n" +
                "REPOSITORY (The construction crew):\n" +
                "Builds, reads, updates, destroys database records\n" +
                "Like: Workers who build and maintain the roads\n\n" +
                "SERVICE (Traffic management):\n" +
                "Business rules (e.g., can't delete someone else's task)\n" +
                "Like: Traffic laws and management systems\n\n" +
                "CONTROLLER (Toll booths/entrances):\n" +
                "Where requests come in and responses go out\n" +
                "Like: Highway entrances/exits with toll systems\n\n" +
                "FRONTEND (The cars and drivers):\n" +
                "What users actually see and interact with\n" +
                "Like: People driving cars on the highway\n\n" +
                "All parts must work together!")
            .addTheory("Step 1: Database Schema",
                "Create the tasks table:\n\n" +
                "CREATE TABLE tasks (\n" +
                "    id BIGSERIAL PRIMARY KEY,\n" +
                "    title VARCHAR(255) NOT NULL,\n" +
                "    description TEXT,\n" +
                "    completed BOOLEAN DEFAULT FALSE,\n" +
                "    user_id BIGINT NOT NULL,\n" +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE\n" +
                ");\n\n" +
                "Key decisions:\n" +
                "- BIGSERIAL: Auto-incrementing ID\n" +
                "- VARCHAR(255): Short text for title\n" +
                "- TEXT: Longer text for description\n" +
                "- BOOLEAN: True/false for completed status\n" +
                "- TIMESTAMP: When created/updated\n" +
                "- FOREIGN KEY: Links to users table\n" +
                "- ON DELETE CASCADE: Delete tasks when user deleted\n\n" +
                "Spring Boot can create this automatically:\n\n" +
                "application.yml:\n" +
                "spring:\n" +
                "  jpa:\n" +
                "    hibernate:\n" +
                "      ddl-auto: update  # Creates/updates tables automatically")
            .addTheory("Step 2: Create the Entity",
                "Map Java class to database table:\n\n" +
                "@Entity\n" +
                "@Table(name = \"tasks\")\n" +
                "public class Task {\n" +
                "    \n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private String title;\n" +
                "    \n" +
                "    @Column(columnDefinition = \"TEXT\")\n" +
                "    private String description;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private Boolean completed = false;\n" +
                "    \n" +
                "    @ManyToOne(fetch = FetchType.LAZY)\n" +
                "    @JoinColumn(name = \"user_id\", nullable = false)\n" +
                "    private User user;\n" +
                "    \n" +
                "    @CreationTimestamp\n" +
                "    private LocalDateTime createdAt;\n" +
                "    \n" +
                "    @UpdateTimestamp\n" +
                "    private LocalDateTime updatedAt;\n" +
                "    \n" +
                "    // Constructors\n" +
                "    public Task() {}\n" +
                "    \n" +
                "    public Task(String title, String description, User user) {\n" +
                "        this.title = title;\n" +
                "        this.description = description;\n" +
                "        this.user = user;\n" +
                "        this.completed = false;\n" +
                "    }\n" +
                "    \n" +
                "    // Getters and Setters\n" +
                "    public Long getId() { return id; }\n" +
                "    public void setId(Long id) { this.id = id; }\n" +
                "    \n" +
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
                "    \n" +
                "    public User getUser() { return user; }\n" +
                "    public void setUser(User user) { this.user = user; }\n" +
                "    \n" +
                "    public LocalDateTime getCreatedAt() { return createdAt; }\n" +
                "    public LocalDateTime getUpdatedAt() { return updatedAt; }\n" +
                "}")
            .addTheory("Step 3: Create the Repository",
                "Interface for database access:\n\n" +
                "@Repository\n" +
                "public interface TaskRepository extends JpaRepository<Task, Long> {\n" +
                "    \n" +
                "    // Find all tasks for a specific user\n" +
                "    List<Task> findByUserId(Long userId);\n" +
                "    \n" +
                "    // Find only completed tasks\n" +
                "    List<Task> findByUserIdAndCompleted(Long userId, Boolean completed);\n" +
                "    \n" +
                "    // Find tasks with title containing text (case-insensitive)\n" +
                "    List<Task> findByUserIdAndTitleContainingIgnoreCase(\n" +
                "        Long userId, String searchTerm);\n" +
                "    \n" +
                "    // Count incomplete tasks\n" +
                "    Long countByUserIdAndCompletedFalse(Long userId);\n" +
                "    \n" +
                "    // Check if task belongs to user (for authorization)\n" +
                "    boolean existsByIdAndUserId(Long taskId, Long userId);\n" +
                "}\n\n" +
                "Spring Data JPA generates SQL automatically!\n\n" +
                "findByUserId(1L) becomes:\n" +
                "SELECT * FROM tasks WHERE user_id = 1;\n\n" +
                "findByUserIdAndCompleted(1L, true) becomes:\n" +
                "SELECT * FROM tasks WHERE user_id = 1 AND completed = true;")
            .addTheory("Step 4: Create the Service",
                "Business logic layer:\n\n" +
                "@Service\n" +
                "public class TaskService {\n" +
                "    private final TaskRepository taskRepository;\n" +
                "    \n" +
                "    public TaskService(TaskRepository taskRepository) {\n" +
                "        this.taskRepository = taskRepository;\n" +
                "    }\n" +
                "    \n" +
                "    public List<Task> getAllTasksForUser(Long userId) {\n" +
                "        return taskRepository.findByUserId(userId);\n" +
                "    }\n" +
                "    \n" +
                "    public Task createTask(Task task, Long userId) {\n" +
                "        // Validation\n" +
                "        if (task.getTitle() == null || task.getTitle().isBlank()) {\n" +
                "            throw new IllegalArgumentException(\"Title cannot be empty\");\n" +
                "        }\n" +
                "        \n" +
                "        // Set user (security: use authenticated user, not from request)\n" +
                "        User user = new User();\n" +
                "        user.setId(userId);\n" +
                "        task.setUser(user);\n" +
                "        \n" +
                "        return taskRepository.save(task);\n" +
                "    }\n" +
                "    \n" +
                "    public Task updateTask(Long taskId, Task updates, Long userId) {\n" +
                "        Task existing = taskRepository.findById(taskId)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "                \"Task\", taskId));\n" +
                "        \n" +
                "        // Security check: can only update your own tasks\n" +
                "        if (!existing.getUser().getId().equals(userId)) {\n" +
                "            throw new ForbiddenException(\n" +
                "                \"Cannot update another user's task\");\n" +
                "        }\n" +
                "        \n" +
                "        // Update fields\n" +
                "        if (updates.getTitle() != null) {\n" +
                "            existing.setTitle(updates.getTitle());\n" +
                "        }\n" +
                "        if (updates.getDescription() != null) {\n" +
                "            existing.setDescription(updates.getDescription());\n" +
                "        }\n" +
                "        if (updates.getCompleted() != null) {\n" +
                "            existing.setCompleted(updates.getCompleted());\n" +
                "        }\n" +
                "        \n" +
                "        return taskRepository.save(existing);\n" +
                "    }\n" +
                "    \n" +
                "    public void deleteTask(Long taskId, Long userId) {\n" +
                "        Task task = taskRepository.findById(taskId)\n" +
                "            .orElseThrow(() -> new ResourceNotFoundException(\n" +
                "                \"Task\", taskId));\n" +
                "        \n" +
                "        // Security check\n" +
                "        if (!task.getUser().getId().equals(userId)) {\n" +
                "            throw new ForbiddenException(\n" +
                "                \"Cannot delete another user's task\");\n" +
                "        }\n" +
                "        \n" +
                "        taskRepository.delete(task);\n" +
                "    }\n" +
                "}")
            .addTheory("Step 5: Create the Controller",
                "REST API endpoints:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/tasks\")\n" +
                "@CrossOrigin(origins = \"http://localhost:3000\")\n" +
                "public class TaskController {\n" +
                "    private final TaskService taskService;\n" +
                "    \n" +
                "    public TaskController(TaskService taskService) {\n" +
                "        this.taskService = taskService;\n" +
                "    }\n" +
                "    \n" +
                "    // GET /api/tasks - Get all tasks for current user\n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<List<Task>> getAllTasks(\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        Long userId = getCurrentUserId(userDetails);\n" +
                "        List<Task> tasks = taskService.getAllTasksForUser(userId);\n" +
                "        return ResponseEntity.ok(tasks);\n" +
                "    }\n" +
                "    \n" +
                "    // POST /api/tasks - Create new task\n" +
                "    @PostMapping\n" +
                "    public ResponseEntity<Task> createTask(\n" +
                "            @Valid @RequestBody TaskRequest request,\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        Long userId = getCurrentUserId(userDetails);\n" +
                "        Task task = new Task(request.getTitle(), \n" +
                "                            request.getDescription(), null);\n" +
                "        Task created = taskService.createTask(task, userId);\n" +
                "        return ResponseEntity\n" +
                "            .status(HttpStatus.CREATED)\n" +
                "            .body(created);\n" +
                "    }\n" +
                "    \n" +
                "    // PUT /api/tasks/{id} - Update task\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Task> updateTask(\n" +
                "            @PathVariable Long id,\n" +
                "            @RequestBody TaskRequest request,\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        Long userId = getCurrentUserId(userDetails);\n" +
                "        Task updates = new Task();\n" +
                "        updates.setTitle(request.getTitle());\n" +
                "        updates.setDescription(request.getDescription());\n" +
                "        updates.setCompleted(request.getCompleted());\n" +
                "        Task updated = taskService.updateTask(id, updates, userId);\n" +
                "        return ResponseEntity.ok(updated);\n" +
                "    }\n" +
                "    \n" +
                "    // DELETE /api/tasks/{id} - Delete task\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Void> deleteTask(\n" +
                "            @PathVariable Long id,\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        Long userId = getCurrentUserId(userDetails);\n" +
                "        taskService.deleteTask(id, userId);\n" +
                "        return ResponseEntity.noContent().build();\n" +
                "    }\n" +
                "    \n" +
                "    private Long getCurrentUserId(UserDetails userDetails) {\n" +
                "        // Extract user ID from authenticated user\n" +
                "        // Implementation depends on your security setup\n" +
                "        return 1L; // Placeholder\n" +
                "    }\n" +
                "}")
            .addExample("Step 6: Create the Frontend (HTML)",
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Task Manager</title>\n" +
                "    <link rel=\"stylesheet\" href=\"style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>My Tasks</h1>\n" +
                "        \n" +
                "        <!-- Create Task Form -->\n" +
                "        <div class=\"create-task\">\n" +
                "            <h2>Create New Task</h2>\n" +
                "            <form id=\"createTaskForm\">\n" +
                "                <input type=\"text\" id=\"title\" placeholder=\"Task title\" required>\n" +
                "                <textarea id=\"description\" placeholder=\"Description\"></textarea>\n" +
                "                <button type=\"submit\">Add Task</button>\n" +
                "            </form>\n" +
                "        </div>\n" +
                "        \n" +
                "        <!-- Error/Success Messages -->\n" +
                "        <div id=\"message\" class=\"message\" style=\"display: none;\"></div>\n" +
                "        \n" +
                "        <!-- Task List -->\n" +
                "        <div class=\"task-list\">\n" +
                "            <h2>Tasks</h2>\n" +
                "            <div id=\"tasks\">\n" +
                "                <!-- Tasks will be loaded here by JavaScript -->\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <script src=\"app.js\"></script>\n" +
                "</body>\n" +
                "</html>")
            .addExample("Step 7: Create the Frontend (JavaScript)",
                "const API_URL = 'http://localhost:8080/api/tasks';\n\n" +
                "// Load tasks when page loads\n" +
                "document.addEventListener('DOMContentLoaded', () => {\n" +
                "    loadTasks();\n" +
                "    \n" +
                "    // Handle form submission\n" +
                "    document.getElementById('createTaskForm')\n" +
                "        .addEventListener('submit', createTask);\n" +
                "});\n\n" +
                "// Fetch and display all tasks\n" +
                "async function loadTasks() {\n" +
                "    try {\n" +
                "        const response = await fetch(API_URL);\n" +
                "        if (!response.ok) throw new Error('Failed to load tasks');\n" +
                "        \n" +
                "        const tasks = await response.json();\n" +
                "        displayTasks(tasks);\n" +
                "    } catch (error) {\n" +
                "        showMessage('Error loading tasks: ' + error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Display tasks in the UI\n" +
                "function displayTasks(tasks) {\n" +
                "    const container = document.getElementById('tasks');\n" +
                "    \n" +
                "    if (tasks.length === 0) {\n" +
                "        container.innerHTML = '<p>No tasks yet. Create one above!</p>';\n" +
                "        return;\n" +
                "    }\n" +
                "    \n" +
                "    container.innerHTML = tasks.map(task => `\n" +
                "        <div class=\"task ${task.completed ? 'completed' : ''}\">\n" +
                "            <div class=\"task-content\">\n" +
                "                <h3>${escapeHtml(task.title)}</h3>\n" +
                "                <p>${escapeHtml(task.description || '')}</p>\n" +
                "            </div>\n" +
                "            <div class=\"task-actions\">\n" +
                "                <button onclick=\"toggleComplete(${task.id}, ${!task.completed})\">\n" +
                "                    ${task.completed ? '✓ Completed' : 'Mark Complete'}\n" +
                "                </button>\n" +
                "                <button onclick=\"deleteTask(${task.id})\" class=\"delete-btn\">\n" +
                "                    Delete\n" +
                "                </button>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    `).join('');\n" +
                "}\n\n" +
                "// Create new task\n" +
                "async function createTask(event) {\n" +
                "    event.preventDefault();\n" +
                "    \n" +
                "    const title = document.getElementById('title').value;\n" +
                "    const description = document.getElementById('description').value;\n" +
                "    \n" +
                "    try {\n" +
                "        const response = await fetch(API_URL, {\n" +
                "            method: 'POST',\n" +
                "            headers: { 'Content-Type': 'application/json' },\n" +
                "            body: JSON.stringify({ title, description })\n" +
                "        });\n" +
                "        \n" +
                "        if (!response.ok) throw new Error('Failed to create task');\n" +
                "        \n" +
                "        // Clear form\n" +
                "        document.getElementById('createTaskForm').reset();\n" +
                "        \n" +
                "        // Reload tasks\n" +
                "        loadTasks();\n" +
                "        showMessage('Task created successfully!', 'success');\n" +
                "    } catch (error) {\n" +
                "        showMessage('Error: ' + error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Toggle task completion\n" +
                "async function toggleComplete(taskId, completed) {\n" +
                "    try {\n" +
                "        const response = await fetch(`${API_URL}/${taskId}`, {\n" +
                "            method: 'PUT',\n" +
                "            headers: { 'Content-Type': 'application/json' },\n" +
                "            body: JSON.stringify({ completed })\n" +
                "        });\n" +
                "        \n" +
                "        if (!response.ok) throw new Error('Failed to update task');\n" +
                "        \n" +
                "        loadTasks();\n" +
                "        showMessage('Task updated!', 'success');\n" +
                "    } catch (error) {\n" +
                "        showMessage('Error: ' + error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Delete task\n" +
                "async function deleteTask(taskId) {\n" +
                "    if (!confirm('Delete this task?')) return;\n" +
                "    \n" +
                "    try {\n" +
                "        const response = await fetch(`${API_URL}/${taskId}`, {\n" +
                "            method: 'DELETE'\n" +
                "        });\n" +
                "        \n" +
                "        if (!response.ok) throw new Error('Failed to delete task');\n" +
                "        \n" +
                "        loadTasks();\n" +
                "        showMessage('Task deleted!', 'success');\n" +
                "    } catch (error) {\n" +
                "        showMessage('Error: ' + error.message, 'error');\n" +
                "    }\n" +
                "}\n\n" +
                "// Show message to user\n" +
                "function showMessage(text, type) {\n" +
                "    const msg = document.getElementById('message');\n" +
                "    msg.textContent = text;\n" +
                "    msg.className = `message ${type}`;\n" +
                "    msg.style.display = 'block';\n" +
                "    setTimeout(() => msg.style.display = 'none', 3000);\n" +
                "}\n\n" +
                "// Prevent XSS attacks\n" +
                "function escapeHtml(text) {\n" +
                "    const div = document.createElement('div');\n" +
                "    div.textContent = text;\n" +
                "    return div.innerHTML;\n" +
                "}")
            .addWarning("Common Full-Stack Mistakes",
                "❌ MISTAKE 1: Not handling errors\n" +
                "Frontend sends request, backend fails, UI shows nothing\n" +
                "FIX: try-catch everywhere, display errors to user\n\n" +
                "❌ MISTAKE 2: No loading states\n" +
                "User clicks button, waits... did it work?\n" +
                "FIX: Show 'Loading...' or disable buttons during requests\n\n" +
                "❌ MISTAKE 3: Not escaping user input\n" +
                "User enters: <script>alert('hacked')</script>\n" +
                "FIX: Use escapeHtml() or textContent instead of innerHTML\n\n" +
                "❌ MISTAKE 4: Missing CORS configuration\n" +
                "Frontend: http://localhost:3000\n" +
                "Backend: http://localhost:8080\n" +
                "Browser blocks request!\n" +
                "FIX: @CrossOrigin on controller\n\n" +
                "❌ MISTAKE 5: Not validating on backend\n" +
                "Trust frontend validation? Attacker bypasses it!\n" +
                "FIX: ALWAYS validate on backend with @Valid\n\n" +
                "❌ MISTAKE 6: Exposing IDs without authorization\n" +
                "User can delete task ID 123, not theirs!\n" +
                "FIX: Check task.userId === currentUserId\n\n" +
                "❌ MISTAKE 7: N+1 query problem\n" +
                "Loading 100 tasks → 101 database queries!\n" +
                "FIX: Use @EntityGraph or JOIN FETCH")
            .addKeyPoint("Testing the Complete Flow",
                "Test from DATABASE → FRONTEND:\n\n" +
                "1. TEST DATABASE:\n" +
                "psql> SELECT * FROM tasks;\n" +
                "Should see tasks table\n\n" +
                "2. TEST REPOSITORY (Unit test):\n" +
                "@Test\n" +
                "void shouldFindTasksByUserId() {\n" +
                "    List<Task> tasks = taskRepository.findByUserId(1L);\n" +
                "    assertFalse(tasks.isEmpty());\n" +
                "}\n\n" +
                "3. TEST SERVICE (Unit test):\n" +
                "@Test\n" +
                "void shouldCreateTask() {\n" +
                "    Task task = new Task(\"Buy milk\", \"From store\", user);\n" +
                "    Task saved = taskService.createTask(task, 1L);\n" +
                "    assertNotNull(saved.getId());\n" +
                "}\n\n" +
                "4. TEST API (Integration test or Postman):\n" +
                "POST http://localhost:8080/api/tasks\n" +
                "{\n" +
                "  \"title\": \"Test task\",\n" +
                "  \"description\": \"Testing\"\n" +
                "}\n" +
                "Expect: 201 Created\n\n" +
                "5. TEST FRONTEND:\n" +
                "Open index.html in browser\n" +
                "- Create task → Should appear in list\n" +
                "- Mark complete → Should show checkmark\n" +
                "- Delete → Should disappear\n\n" +
                "6. TEST ERROR CASES:\n" +
                "- No title → Should show error\n" +
                "- Server down → Should show 'cannot connect'\n" +
                "- Delete someone else's task → Should return 403")
            .addChallenge(createLayerQuiz())
            .addChallenge(createSecurityQuiz())
            .addChallenge(createWorkflowQuiz())
            .estimatedMinutes(60)
            .build();
    }

    private static Challenge createLayerQuiz() {
        return new Challenge.Builder("epoch-8-lesson-6-layers", "Application Layers", ChallengeType.MULTIPLE_CHOICE)
            .description("Which layer contains business logic and validation rules?")
            .addMultipleChoiceOption("A) Controller")
            .addMultipleChoiceOption("B) Repository")
            .addMultipleChoiceOption("C) Service")
            .addMultipleChoiceOption("D) Entity")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-8-lesson-6-security", "Security Best Practices", ChallengeType.MULTIPLE_CHOICE)
            .description("What should you ALWAYS check before allowing a user to delete a task?")
            .addMultipleChoiceOption("A) If the task exists")
            .addMultipleChoiceOption("B) If the task belongs to the current user")
            .addMultipleChoiceOption("C) If the task is completed")
            .addMultipleChoiceOption("D) If the title is valid")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createWorkflowQuiz() {
        return new Challenge.Builder("epoch-8-lesson-6-workflow", "Development Workflow", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the recommended order to build a full-stack feature?")
            .addMultipleChoiceOption("A) Frontend → Backend → Database")
            .addMultipleChoiceOption("B) Database → Entity → Repository → Service → Controller → Frontend")
            .addMultipleChoiceOption("C) Controller → Service → Repository → Database")
            .addMultipleChoiceOption("D) Frontend → Controller → Database")
            .correctAnswer("B")
            .build();
    }
}
