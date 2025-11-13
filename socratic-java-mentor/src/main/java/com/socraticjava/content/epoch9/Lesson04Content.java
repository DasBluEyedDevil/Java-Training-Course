package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.4: Building Your Backend - Entities and Repositories
 */
public class Lesson04Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-4", "Lesson 9.4: Building Your Backend - Entities and Repositories")
            .addTheory("The Data Layer Foundation",
                "You've set up your project. Now let's build the data layer!\n\n" +
                "THE DATA LAYER (Bottom of the stack):\n\n" +
                "1. ENTITIES (Java classes → Database tables)\n" +
                "   @Entity, @Table, @Id, @Column\n" +
                "   Example: User class → users table\n\n" +
                "2. REPOSITORIES (Database access layer)\n" +
                "   JpaRepository interface\n" +
                "   Spring generates SQL automatically!\n\n" +
                "WHY START HERE?\n" +
                "✓ Foundation for everything else\n" +
                "✓ Can test immediately (no UI needed)\n" +
                "✓ Database schema auto-created by Hibernate\n" +
                "✓ Easy to debug when isolated\n\n" +
                "BUILD ORDER:\n" +
                "1. Create User entity\n" +
                "2. Create UserRepository\n" +
                "3. Test with @SpringBootTest\n" +
                "4. Create Task entity\n" +
                "5. Create TaskRepository\n" +
                "6. Test relationships\n\n" +
                "Let's build from the ground up!")
            .addAnalogy("Entities and Repositories are Like Blueprint and Builder",
                "ENTITY (The blueprint):\n" +
                "Detailed architectural plan for a house\n" +
                "Like: User class defines what a user looks like\n" +
                "Specifies: id, email, password, name, created date\n\n" +
                "@Entity annotation = \"This is a blueprint for a database table\"\n" +
                "@Id annotation = \"This is the house number (unique identifier)\"\n" +
                "@Column = \"This is a room in the house\"\n\n" +
                "REPOSITORY (The builder/contractor):\n" +
                "The construction crew that builds houses from blueprints\n" +
                "Like: UserRepository saves/finds users in database\n\n" +
                "save() = Build a new house\n" +
                "findById() = Find house by address\n" +
                "findAll() = List all houses in neighborhood\n" +
                "delete() = Demolish a house\n\n" +
                "Spring Data JPA = The crew manager\n" +
                "You just say \"build me a house\"\n" +
                "Spring figures out HOW to do it (generates SQL)")
            .addTheory("Step 1: Create the User Entity",
                "Create: src/main/java/com/yourname/taskmanager/model/User.java\n\n" +
                "package com.yourname.taskmanager.model;\n\n" +
                "import jakarta.persistence.*;\n" +
                "import java.time.LocalDateTime;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n\n" +
                "@Entity\n" +
                "@Table(name = \"users\")\n" +
                "public class User {\n" +
                "    \n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @Column(nullable = false, unique = true, length = 255)\n" +
                "    private String email;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private String password;  // Will be hashed!\n" +
                "    \n" +
                "    @Column(length = 100)\n" +
                "    private String name;\n" +
                "    \n" +
                "    @Column(name = \"created_at\", updatable = false)\n" +
                "    private LocalDateTime createdAt;\n" +
                "    \n" +
                "    @OneToMany(mappedBy = \"user\", cascade = CascadeType.ALL, orphanRemoval = true)\n" +
                "    private List<Task> tasks = new ArrayList<>();\n" +
                "    \n" +
                "    // Default constructor (required by JPA)\n" +
                "    public User() {\n" +
                "    }\n" +
                "    \n" +
                "    // Constructor for creating new users\n" +
                "    public User(String email, String password, String name) {\n" +
                "        this.email = email;\n" +
                "        this.password = password;\n" +
                "        this.name = name;\n" +
                "    }\n" +
                "    \n" +
                "    // Lifecycle callback: set timestamp before insert\n" +
                "    @PrePersist\n" +
                "    protected void onCreate() {\n" +
                "        createdAt = LocalDateTime.now();\n" +
                "    }\n" +
                "    \n" +
                "    // Getters and Setters\n" +
                "    public Long getId() { return id; }\n" +
                "    public void setId(Long id) { this.id = id; }\n" +
                "    \n" +
                "    public String getEmail() { return email; }\n" +
                "    public void setEmail(String email) { this.email = email; }\n" +
                "    \n" +
                "    public String getPassword() { return password; }\n" +
                "    public void setPassword(String password) { this.password = password; }\n" +
                "    \n" +
                "    public String getName() { return name; }\n" +
                "    public void setName(String name) { this.name = name; }\n" +
                "    \n" +
                "    public LocalDateTime getCreatedAt() { return createdAt; }\n" +
                "    \n" +
                "    public List<Task> getTasks() { return tasks; }\n" +
                "    public void setTasks(List<Task> tasks) { this.tasks = tasks; }\n" +
                "}")
            .addTheory("Understanding Entity Annotations",
                "KEY ANNOTATIONS EXPLAINED:\n\n" +
                "@Entity\n" +
                "- Marks class as JPA entity\n" +
                "- Hibernate will create database table for this class\n\n" +
                "@Table(name = \"users\")\n" +
                "- Specifies table name in database\n" +
                "- Without this, table name = class name (\"User\")\n" +
                "- Best practice: explicit table names\n\n" +
                "@Id\n" +
                "- Marks the primary key field\n" +
                "- Every entity MUST have @Id\n\n" +
                "@GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "- Database auto-generates ID (1, 2, 3, ...)\n" +
                "- IDENTITY = uses database auto-increment\n" +
                "- Other options: SEQUENCE, AUTO, UUID\n\n" +
                "@Column(nullable = false, unique = true, length = 255)\n" +
                "- nullable = false → SQL: NOT NULL\n" +
                "- unique = true → SQL: UNIQUE constraint\n" +
                "- length = 255 → SQL: VARCHAR(255)\n" +
                "- name = \"created_at\" → column name different from field name\n\n" +
                "@OneToMany(mappedBy = \"user\", cascade = CascadeType.ALL)\n" +
                "- One user has many tasks (1:N relationship)\n" +
                "- mappedBy = \"user\" → Tasks have a 'user' field\n" +
                "- cascade = ALL → Save/delete user cascades to tasks\n" +
                "- orphanRemoval = true → Delete task if removed from list\n\n" +
                "@PrePersist\n" +
                "- Lifecycle callback: runs before INSERT\n" +
                "- Automatically sets createdAt timestamp\n" +
                "- Other callbacks: @PreUpdate, @PostLoad, etc.")
            .addTheory("Step 2: Create the Task Entity",
                "Create: src/main/java/com/yourname/taskmanager/model/Task.java\n\n" +
                "package com.yourname.taskmanager.model;\n\n" +
                "import jakarta.persistence.*;\n" +
                "import java.time.LocalDateTime;\n\n" +
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
                "    @Column(name = \"created_at\", updatable = false)\n" +
                "    private LocalDateTime createdAt;\n" +
                "    \n" +
                "    @Column(name = \"updated_at\")\n" +
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
                "    // Lifecycle callbacks\n" +
                "    @PrePersist\n" +
                "    protected void onCreate() {\n" +
                "        createdAt = LocalDateTime.now();\n" +
                "        updatedAt = LocalDateTime.now();\n" +
                "    }\n" +
                "    \n" +
                "    @PreUpdate\n" +
                "    protected void onUpdate() {\n" +
                "        updatedAt = LocalDateTime.now();\n" +
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
            .addKeyPoint("Understanding Relationships",
                "@ManyToOne (Task side - many tasks to one user):\n" +
                "@ManyToOne(fetch = FetchType.LAZY)\n" +
                "@JoinColumn(name = \"user_id\", nullable = false)\n" +
                "private User user;\n\n" +
                "- Many tasks belong to one user\n" +
                "- FetchType.LAZY = Don't load user until accessed\n" +
                "- JoinColumn = Creates 'user_id' foreign key column\n" +
                "- nullable = false = Every task MUST have a user\n\n" +
                "@OneToMany (User side - one user to many tasks):\n" +
                "@OneToMany(mappedBy = \"user\", cascade = CascadeType.ALL)\n" +
                "private List<Task> tasks = new ArrayList<>();\n\n" +
                "- One user has many tasks\n" +
                "- mappedBy = \"user\" = Task entity owns the relationship\n" +
                "- cascade = ALL = Operations cascade (save, delete, etc.)\n" +
                "- orphanRemoval = true = Delete orphaned tasks\n\n" +
                "DATABASE RESULT:\n" +
                "users table:\n" +
                "  id | email           | password | name\n" +
                "  1  | alice@email.com | hashed   | Alice\n\n" +
                "tasks table:\n" +
                "  id | title      | description | completed | user_id\n" +
                "  1  | Buy milk   | From store  | false     | 1\n" +
                "  2  | Walk dog   | 30 minutes  | true      | 1\n\n" +
                "LAZY vs EAGER Fetching:\n" +
                "LAZY (recommended):\n" +
                "- Loads relationship only when accessed\n" +
                "- Better performance\n" +
                "- Avoids N+1 query problem\n\n" +
                "EAGER (use sparingly):\n" +
                "- Loads relationship immediately\n" +
                "- Can cause performance issues\n" +
                "- Use only for small, always-needed data")
            .addTheory("Step 3: Create UserRepository",
                "Create: src/main/java/com/yourname/taskmanager/repository/UserRepository.java\n\n" +
                "package com.yourname.taskmanager.repository;\n\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "import java.util.Optional;\n\n" +
                "@Repository\n" +
                "public interface UserRepository extends JpaRepository<User, Long> {\n" +
                "    \n" +
                "    // Find user by email (for login)\n" +
                "    Optional<User> findByEmail(String email);\n" +
                "    \n" +
                "    // Check if email already exists (for registration)\n" +
                "    boolean existsByEmail(String email);\n" +
                "    \n" +
                "    // Find users by name containing (case-insensitive search)\n" +
                "    List<User> findByNameContainingIgnoreCase(String name);\n" +
                "}\n\n" +
                "WHAT YOU GET FOR FREE:\n" +
                "From JpaRepository<User, Long>:\n\n" +
                "save(User user) - Insert or update\n" +
                "findById(Long id) - Find by primary key\n" +
                "findAll() - Get all users\n" +
                "count() - Count total users\n" +
                "delete(User user) - Delete user\n" +
                "deleteById(Long id) - Delete by ID\n" +
                "existsById(Long id) - Check if exists\n\n" +
                "SPRING GENERATES SQL AUTOMATICALLY!\n" +
                "findByEmail(\"alice@email.com\") becomes:\n" +
                "SELECT * FROM users WHERE email = 'alice@email.com';\n\n" +
                "existsByEmail(\"bob@email.com\") becomes:\n" +
                "SELECT COUNT(*) > 0 FROM users WHERE email = 'bob@email.com';\n\n" +
                "NO SQL WRITING REQUIRED!")
            .addTheory("Step 4: Create TaskRepository",
                "Create: src/main/java/com/yourname/taskmanager/repository/TaskRepository.java\n\n" +
                "package com.yourname.taskmanager.repository;\n\n" +
                "import com.yourname.taskmanager.model.Task;\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import org.springframework.data.jpa.repository.Query;\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "import java.util.List;\n\n" +
                "@Repository\n" +
                "public interface TaskRepository extends JpaRepository<Task, Long> {\n" +
                "    \n" +
                "    // Find all tasks for a specific user\n" +
                "    List<Task> findByUserId(Long userId);\n" +
                "    \n" +
                "    // Find completed/incomplete tasks for user\n" +
                "    List<Task> findByUserIdAndCompleted(Long userId, Boolean completed);\n" +
                "    \n" +
                "    // Find tasks with title containing text (case-insensitive)\n" +
                "    List<Task> findByUserIdAndTitleContainingIgnoreCase(\n" +
                "        Long userId, String searchTerm);\n" +
                "    \n" +
                "    // Count incomplete tasks for user\n" +
                "    Long countByUserIdAndCompletedFalse(Long userId);\n" +
                "    \n" +
                "    // Check if task belongs to user (for authorization)\n" +
                "    boolean existsByIdAndUserId(Long taskId, Long userId);\n" +
                "    \n" +
                "    // Custom JPQL query: Find tasks ordered by created date\n" +
                "    @Query(\"SELECT t FROM Task t WHERE t.user.id = :userId \" +\n" +
                "           \"ORDER BY t.createdAt DESC\")\n" +
                "    List<Task> findRecentTasksByUserId(Long userId);\n" +
                "    \n" +
                "    // Native SQL query: Complex aggregation\n" +
                "    @Query(value = \"SELECT COUNT(*) FROM tasks WHERE user_id = :userId \" +\n" +
                "                   \"AND created_at > NOW() - INTERVAL '7 days'\",\n" +
                "           nativeQuery = true)\n" +
                "    Long countTasksCreatedLastWeek(Long userId);\n" +
                "}\n\n" +
                "QUERY METHOD NAMING CONVENTIONS:\n" +
                "findBy + Property + Condition\n\n" +
                "findByUserId → WHERE user_id = ?\n" +
                "findByTitleContaining → WHERE title LIKE '%?%'\n" +
                "findByCompletedFalse → WHERE completed = false\n" +
                "findByUserIdAndCompleted → WHERE user_id = ? AND completed = ?\n" +
                "countByUserIdAndCompletedFalse → SELECT COUNT(*) WHERE ...\n" +
                "existsByIdAndUserId → SELECT COUNT(*) > 0 WHERE ...")
            .addExample("Testing Your Repositories",
                "Create a test to verify repositories work:\n\n" +
                "src/test/java/com/yourname/taskmanager/repository/UserRepositoryTest.java\n\n" +
                "package com.yourname.taskmanager.repository;\n\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;\n" +
                "import java.util.Optional;\n" +
                "import static org.assertj.core.api.Assertions.assertThat;\n\n" +
                "@DataJpaTest\n" +
                "class UserRepositoryTest {\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private UserRepository userRepository;\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldSaveAndFindUser() {\n" +
                "        // Create user\n" +
                "        User user = new User(\"test@email.com\", \"password123\", \"Test User\");\n" +
                "        \n" +
                "        // Save to database\n" +
                "        User saved = userRepository.save(user);\n" +
                "        \n" +
                "        // Verify ID was generated\n" +
                "        assertThat(saved.getId()).isNotNull();\n" +
                "        \n" +
                "        // Find by ID\n" +
                "        Optional<User> found = userRepository.findById(saved.getId());\n" +
                "        assertThat(found).isPresent();\n" +
                "        assertThat(found.get().getEmail()).isEqualTo(\"test@email.com\");\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldFindUserByEmail() {\n" +
                "        // Create and save user\n" +
                "        User user = new User(\"alice@email.com\", \"password\", \"Alice\");\n" +
                "        userRepository.save(user);\n" +
                "        \n" +
                "        // Find by email\n" +
                "        Optional<User> found = userRepository.findByEmail(\"alice@email.com\");\n" +
                "        assertThat(found).isPresent();\n" +
                "        assertThat(found.get().getName()).isEqualTo(\"Alice\");\n" +
                "    }\n" +
                "    \n" +
                "    @Test\n" +
                "    void shouldCheckEmailExists() {\n" +
                "        // Create and save user\n" +
                "        User user = new User(\"bob@email.com\", \"password\", \"Bob\");\n" +
                "        userRepository.save(user);\n" +
                "        \n" +
                "        // Check exists\n" +
                "        assertThat(userRepository.existsByEmail(\"bob@email.com\")).isTrue();\n" +
                "        assertThat(userRepository.existsByEmail(\"charlie@email.com\")).isFalse();\n" +
                "    }\n" +
                "}\n\n" +
                "RUN TESTS:\n" +
                "IntelliJ: Right-click test class → Run\n" +
                "Command line: ./mvnw test\n\n" +
                "✓ All tests pass → Repositories work!\n" +
                "❌ Tests fail → Check error messages, fix code")
            .addWarning("Common Entity and Repository Mistakes",
                "❌ MISTAKE 1: No default constructor\n" +
                "public class User {\n" +
                "    // Only parameterized constructor\n" +
                "    public User(String email) { ... }\n" +
                "}\n" +
                "FIX: JPA requires no-arg constructor:\n" +
                "public User() {}\n\n" +
                "❌ MISTAKE 2: Forgot @Id annotation\n" +
                "JPA doesn't know which field is the primary key\n" +
                "FIX: Always mark primary key with @Id\n\n" +
                "❌ MISTAKE 3: Storing plain text passwords\n" +
                "private String password = \"password123\";  // NEVER!\n" +
                "FIX: Hash passwords before storing (BCrypt)\n\n" +
                "❌ MISTAKE 4: EAGER fetching relationships\n" +
                "@OneToMany(fetch = FetchType.EAGER)\n" +
                "Causes N+1 query problem, poor performance\n" +
                "FIX: Use LAZY (default for @OneToMany)\n\n" +
                "❌ MISTAKE 5: Exposing entities to controllers\n" +
                "return userRepository.findAll();  // Returns password field!\n" +
                "FIX: Use DTOs (Data Transfer Objects)\n\n" +
                "❌ MISTAKE 6: Not handling Optional\n" +
                "User user = userRepository.findById(id);  // Compile error!\n" +
                "FIX: Optional<User> user = userRepository.findById(id);\n" +
                "      user.orElseThrow(() -> new NotFoundException(...));\n\n" +
                "❌ MISTAKE 7: Circular references in JSON\n" +
                "User has tasks, Task has user → infinite loop!\n" +
                "FIX: Use @JsonIgnore or DTOs")
            .addKeyPoint("Best Practices Summary",
                "ENTITIES:\n" +
                "✓ Always provide no-arg constructor\n" +
                "✓ Use @Table with explicit table name\n" +
                "✓ Use @Column for nullable, unique, length constraints\n" +
                "✓ Use LAZY fetching for relationships\n" +
                "✓ Use @PrePersist/@PreUpdate for timestamps\n" +
                "✓ Don't expose entities directly to API\n\n" +
                "REPOSITORIES:\n" +
                "✓ Extend JpaRepository<Entity, ID>\n" +
                "✓ Use query method naming conventions\n" +
                "✓ Use @Query for complex queries\n" +
                "✓ Return Optional<T> for single results\n" +
                "✓ Use @DataJpaTest for testing\n" +
                "✓ Keep repositories thin (logic in service layer)\n\n" +
                "DATABASE:\n" +
                "✓ Use spring.jpa.hibernate.ddl-auto=update in dev\n" +
                "✓ Use validate in production\n" +
                "✓ Enable show-sql in development\n" +
                "✓ Use indexes for frequently queried columns\n" +
                "✓ Test with real database (PostgreSQL), not just H2\n\n" +
                "NAMING:\n" +
                "✓ Entity class: User (singular, PascalCase)\n" +
                "✓ Table name: users (plural, snake_case)\n" +
                "✓ Repository: UserRepository (Entity + Repository)\n" +
                "✓ Package: model, repository (lowercase)")
            .addChallenge(createEntityQuiz())
            .addChallenge(createRepositoryQuiz())
            .addChallenge(createRelationshipQuiz())
            .estimatedMinutes(50)
            .build();
    }

    private static Challenge createEntityQuiz() {
        return new Challenge.Builder("epoch-9-lesson-4-entity", "Entity Annotations", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation marks a class as a JPA entity?")
            .addMultipleChoiceOption("A) @Table")
            .addMultipleChoiceOption("B) @Entity")
            .addMultipleChoiceOption("C) @Repository")
            .addMultipleChoiceOption("D) @Id")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createRepositoryQuiz() {
        return new Challenge.Builder("epoch-9-lesson-4-repository", "Repository Interface", ChallengeType.MULTIPLE_CHOICE)
            .description("What interface should your repository extend for full CRUD operations?")
            .addMultipleChoiceOption("A) CrudRepository")
            .addMultipleChoiceOption("B) Repository")
            .addMultipleChoiceOption("C) JpaRepository")
            .addMultipleChoiceOption("D) DataRepository")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createRelationshipQuiz() {
        return new Challenge.Builder("epoch-9-lesson-4-relationship", "Entity Relationships", ChallengeType.MULTIPLE_CHOICE)
            .description("What fetch type is recommended for @ManyToOne relationships?")
            .addMultipleChoiceOption("A) EAGER")
            .addMultipleChoiceOption("B) LAZY")
            .addMultipleChoiceOption("C) IMMEDIATE")
            .addMultipleChoiceOption("D) AUTOMATIC")
            .correctAnswer("B")
            .build();
    }
}
