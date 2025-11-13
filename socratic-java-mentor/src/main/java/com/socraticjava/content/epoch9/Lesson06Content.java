package com.socraticjava.content.epoch9;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 9.6: Adding Authentication with Spring Security
 */
public class Lesson06Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-9-lesson-6", "Lesson 9.6: Authentication - Securing Your API")
            .addTheory("Why Authentication Matters",
                "Right now, ANYONE can access your API!\n\n" +
                "CURRENT PROBLEM:\n" +
                "GET /api/tasks → Returns ALL tasks\n" +
                "DELETE /api/tasks/123 → Deletes ANYONE's task\n" +
                "No login required!\n\n" +
                "AUTHENTICATION = Who are you?\n" +
                "AUTHORIZATION = What can you do?\n\n" +
                "SPRING SECURITY 6 provides:\n" +
                "✓ User registration and login\n" +
                "✓ Password hashing (BCrypt)\n" +
                "✓ Session management\n" +
                "✓ Protection against common attacks\n" +
                "✓ JWT token support (stateless auth)\n\n" +
                "TWO APPROACHES:\n" +
                "1. SESSION-BASED (Simpler, for beginners)\n" +
                "   - Login creates session\n" +
                "   - Session stored on server\n" +
                "   - Good for traditional web apps\n\n" +
                "2. TOKEN-BASED (JWT)\n" +
                "   - Login returns JWT token\n" +
                "   - Client sends token with each request\n" +
                "   - Stateless, good for APIs\n\n" +
                "We'll build both!")
            .addAnalogy("Authentication is Like Airport Security",
                "WITHOUT AUTHENTICATION (Current state):\n" +
                "Anyone walks onto any airplane\n" +
                "Like: Anyone can access /api/tasks\n\n" +
                "WITH AUTHENTICATION:\n\n" +
                "1. REGISTRATION (Create account):\n" +
                "   Get a passport (one time)\n" +
                "   Like: POST /api/auth/register\n" +
                "   Stores: email, hashed password, name\n\n" +
                "2. LOGIN (Authenticate):\n" +
                "   Show passport at security\n" +
                "   Like: POST /api/auth/login\n" +
                "   Receives: Boarding pass (session/JWT)\n\n" +
                "3. ACCESS PROTECTED RESOURCE:\n" +
                "   Show boarding pass to board plane\n" +
                "   Like: Send session cookie/JWT with request\n" +
                "   Server checks: Is boarding pass valid?\n\n" +
                "4. LOGOUT:\n" +
                "   Destroy boarding pass\n" +
                "   Like: POST /api/auth/logout\n" +
                "   Session invalidated\n\n" +
                "SECURITY CHECKPOINT = Spring Security Filter\n" +
                "Checks every request before allowing access")
            .addTheory("Step 1: Add Spring Security Dependency",
                "Add to pom.xml:\n\n" +
                "<dependency>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-security</artifactId>\n" +
                "</dependency>\n\n" +
                "<!-- For JWT (optional, advanced) -->\n" +
                "<dependency>\n" +
                "    <groupId>io.jsonwebtoken</groupId>\n" +
                "    <artifactId>jjwt-api</artifactId>\n" +
                "    <version>0.12.5</version>\n" +
                "</dependency>\n" +
                "<dependency>\n" +
                "    <groupId>io.jsonwebtoken</groupId>\n" +
                "    <artifactId>jjwt-impl</artifactId>\n" +
                "    <version>0.12.5</version>\n" +
                "    <scope>runtime</scope>\n" +
                "</dependency>\n" +
                "<dependency>\n" +
                "    <groupId>io.jsonwebtoken</groupId>\n" +
                "    <artifactId>jjwt-jackson</artifactId>\n" +
                "    <version>0.12.5</version>\n" +
                "    <scope>runtime</scope>\n" +
                "</dependency>\n\n" +
                "RESTART APPLICATION:\n" +
                "./mvnw spring-boot:run\n\n" +
                "WHAT CHANGED?\n" +
                "- All endpoints now require authentication!\n" +
                "- GET http://localhost:8080/api/tasks → 401 Unauthorized\n" +
                "- Default user: \"user\"\n" +
                "- Password: Printed in console logs\n" +
                "  \"Using generated security password: abc123-def456-...\"\n\n" +
                "Test with curl:\n" +
                "curl -u user:abc123-def456 http://localhost:8080/api/tasks")
            .addTheory("Step 2: Update User Entity for Authentication",
                "Modify User.java to implement UserDetails:\n\n" +
                "import org.springframework.security.core.GrantedAuthority;\n" +
                "import org.springframework.security.core.userdetails.UserDetails;\n" +
                "import java.util.Collection;\n" +
                "import java.util.Collections;\n\n" +
                "@Entity\n" +
                "@Table(name = \"users\")\n" +
                "public class User implements UserDetails {\n" +
                "    \n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @Column(nullable = false, unique = true)\n" +
                "    private String email;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private String password;  // Will be hashed!\n" +
                "    \n" +
                "    private String name;\n" +
                "    \n" +
                "    @Column(name = \"created_at\")\n" +
                "    private LocalDateTime createdAt;\n" +
                "    \n" +
                "    @OneToMany(mappedBy = \"user\", cascade = CascadeType.ALL)\n" +
                "    private List<Task> tasks = new ArrayList<>();\n" +
                "    \n" +
                "    // UserDetails methods (required by Spring Security)\n" +
                "    @Override\n" +
                "    public Collection<? extends GrantedAuthority> getAuthorities() {\n" +
                "        return Collections.emptyList();  // No roles for now\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public String getUsername() {\n" +
                "        return email;  // Use email as username\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public boolean isAccountNonExpired() {\n" +
                "        return true;\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public boolean isAccountNonLocked() {\n" +
                "        return true;\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public boolean isCredentialsNonExpired() {\n" +
                "        return true;\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public boolean isEnabled() {\n" +
                "        return true;\n" +
                "    }\n" +
                "    \n" +
                "    // Regular getters/setters...\n" +
                "}")
            .addTheory("Step 3: Create UserService for Authentication",
                "Create custom UserDetailsService:\n\n" +
                "package com.yourname.taskmanager.service;\n\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import com.yourname.taskmanager.repository.UserRepository;\n" +
                "import org.springframework.security.core.userdetails.UserDetails;\n" +
                "import org.springframework.security.core.userdetails.UserDetailsService;\n" +
                "import org.springframework.security.core.userdetails.UsernameNotFoundException;\n" +
                "import org.springframework.security.crypto.password.PasswordEncoder;\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "@Service\n" +
                "public class UserService implements UserDetailsService {\n" +
                "    \n" +
                "    private final UserRepository userRepository;\n" +
                "    private final PasswordEncoder passwordEncoder;\n" +
                "    \n" +
                "    public UserService(UserRepository userRepository,\n" +
                "                      PasswordEncoder passwordEncoder) {\n" +
                "        this.userRepository = userRepository;\n" +
                "        this.passwordEncoder = passwordEncoder;\n" +
                "    }\n" +
                "    \n" +
                "    // Required by Spring Security\n" +
                "    @Override\n" +
                "    public UserDetails loadUserByUsername(String email) \n" +
                "            throws UsernameNotFoundException {\n" +
                "        return userRepository.findByEmail(email)\n" +
                "            .orElseThrow(() -> new UsernameNotFoundException(\n" +
                "                \"User not found with email: \" + email));\n" +
                "    }\n" +
                "    \n" +
                "    // Register new user\n" +
                "    public User registerUser(String email, String password, String name) {\n" +
                "        // Check if email already exists\n" +
                "        if (userRepository.existsByEmail(email)) {\n" +
                "            throw new IllegalArgumentException(\n" +
                "                \"Email already registered: \" + email);\n" +
                "        }\n" +
                "        \n" +
                "        // Hash password\n" +
                "        String hashedPassword = passwordEncoder.encode(password);\n" +
                "        \n" +
                "        // Create user\n" +
                "        User user = new User();\n" +
                "        user.setEmail(email);\n" +
                "        user.setPassword(hashedPassword);\n" +
                "        user.setName(name);\n" +
                "        \n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}")
            .addKeyPoint("Step 4: Configure Spring Security",
                "Create SecurityConfig.java:\n\n" +
                "package com.yourname.taskmanager.config;\n\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.http.HttpMethod;\n" +
                "import org.springframework.security.authentication.AuthenticationManager;\n" +
                "import org.springframework.security.authentication.dao.DaoAuthenticationProvider;\n" +
                "import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;\n" +
                "import org.springframework.security.config.annotation.web.builders.HttpSecurity;\n" +
                "import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;\n" +
                "import org.springframework.security.config.http.SessionCreationPolicy;\n" +
                "import org.springframework.security.core.userdetails.UserDetailsService;\n" +
                "import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;\n" +
                "import org.springframework.security.crypto.password.PasswordEncoder;\n" +
                "import org.springframework.security.web.SecurityFilterChain;\n\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "public class SecurityConfig {\n" +
                "    \n" +
                "    private final UserDetailsService userDetailsService;\n" +
                "    \n" +
                "    public SecurityConfig(UserDetailsService userDetailsService) {\n" +
                "        this.userDetailsService = userDetailsService;\n" +
                "    }\n" +
                "    \n" +
                "    @Bean\n" +
                "    public SecurityFilterChain securityFilterChain(HttpSecurity http) \n" +
                "            throws Exception {\n" +
                "        http\n" +
                "            .csrf(csrf -> csrf.disable())  // Disable for API\n" +
                "            .authorizeHttpRequests(auth -> auth\n" +
                "                .requestMatchers(\"/api/auth/**\").permitAll()  // Public\n" +
                "                .anyRequest().authenticated()  // Everything else requires auth\n" +
                "            )\n" +
                "            .sessionManagement(session -> session\n" +
                "                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)\n" +
                "            )\n" +
                "            .httpBasic();  // For testing with Postman\n" +
                "        \n" +
                "        return http.build();\n" +
                "    }\n" +
                "    \n" +
                "    @Bean\n" +
                "    public PasswordEncoder passwordEncoder() {\n" +
                "        return new BCryptPasswordEncoder();\n" +
                "    }\n" +
                "    \n" +
                "    @Bean\n" +
                "    public AuthenticationManager authenticationManager(\n" +
                "            AuthenticationConfiguration config) throws Exception {\n" +
                "        return config.getAuthenticationManager();\n" +
                "    }\n" +
                "    \n" +
                "    @Bean\n" +
                "    public DaoAuthenticationProvider authenticationProvider() {\n" +
                "        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();\n" +
                "        provider.setUserDetailsService(userDetailsService);\n" +
                "        provider.setPasswordEncoder(passwordEncoder());\n" +
                "        return provider;\n" +
                "    }\n" +
                "}\n\n" +
                "KEY CONCEPTS:\n" +
                "- SecurityFilterChain: New in Spring Security 6 (replaces WebSecurityConfigurerAdapter)\n" +
                "- .csrf().disable(): OK for stateless APIs\n" +
                "- .permitAll(): No authentication required\n" +
                "- .authenticated(): Must be logged in\n" +
                "- SessionCreationPolicy.STATELESS: No sessions, use tokens\n" +
                "- BCryptPasswordEncoder: Securely hash passwords")
            .addTheory("Step 5: Create AuthController",
                "Create authentication endpoints:\n\n" +
                "package com.yourname.taskmanager.controller;\n\n" +
                "import com.yourname.taskmanager.dto.LoginRequest;\n" +
                "import com.yourname.taskmanager.dto.RegisterRequest;\n" +
                "import com.yourname.taskmanager.dto.AuthResponse;\n" +
                "import com.yourname.taskmanager.service.UserService;\n" +
                "import com.yourname.taskmanager.model.User;\n" +
                "import jakarta.validation.Valid;\n" +
                "import org.springframework.http.ResponseEntity;\n" +
                "import org.springframework.security.authentication.AuthenticationManager;\n" +
                "import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;\n" +
                "import org.springframework.security.core.Authentication;\n" +
                "import org.springframework.web.bind.annotation.*;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/auth\")\n" +
                "@CrossOrigin(origins = \"http://localhost:3000\")\n" +
                "public class AuthController {\n" +
                "    \n" +
                "    private final UserService userService;\n" +
                "    private final AuthenticationManager authenticationManager;\n" +
                "    \n" +
                "    public AuthController(UserService userService,\n" +
                "                         AuthenticationManager authenticationManager) {\n" +
                "        this.userService = userService;\n" +
                "        this.authenticationManager = authenticationManager;\n" +
                "    }\n" +
                "    \n" +
                "    // POST /api/auth/register\n" +
                "    @PostMapping(\"/register\")\n" +
                "    public ResponseEntity<AuthResponse> register(\n" +
                "            @Valid @RequestBody RegisterRequest request) {\n" +
                "        \n" +
                "        User user = userService.registerUser(\n" +
                "            request.getEmail(),\n" +
                "            request.getPassword(),\n" +
                "            request.getName()\n" +
                "        );\n" +
                "        \n" +
                "        return ResponseEntity.ok(\n" +
                "            new AuthResponse(\"User registered successfully\", user.getEmail())\n" +
                "        );\n" +
                "    }\n" +
                "    \n" +
                "    // POST /api/auth/login\n" +
                "    @PostMapping(\"/login\")\n" +
                "    public ResponseEntity<AuthResponse> login(\n" +
                "            @Valid @RequestBody LoginRequest request) {\n" +
                "        \n" +
                "        // Authenticate user\n" +
                "        Authentication authentication = authenticationManager.authenticate(\n" +
                "            new UsernamePasswordAuthenticationToken(\n" +
                "                request.getEmail(),\n" +
                "                request.getPassword()\n" +
                "            )\n" +
                "        );\n" +
                "        \n" +
                "        // If we reach here, authentication succeeded\n" +
                "        return ResponseEntity.ok(\n" +
                "            new AuthResponse(\"Login successful\", request.getEmail())\n" +
                "        );\n" +
                "    }\n" +
                "}\n\n" +
                "CREATE DTOs:\n\n" +
                "// RegisterRequest.java\n" +
                "public class RegisterRequest {\n" +
                "    @NotBlank @Email\n" +
                "    private String email;\n" +
                "    \n" +
                "    @NotBlank @Size(min = 8)\n" +
                "    private String password;\n" +
                "    \n" +
                "    @NotBlank\n" +
                "    private String name;\n" +
                "    \n" +
                "    // Getters/Setters\n" +
                "}\n\n" +
                "// LoginRequest.java\n" +
                "public class LoginRequest {\n" +
                "    @NotBlank @Email\n" +
                "    private String email;\n" +
                "    \n" +
                "    @NotBlank\n" +
                "    private String password;\n" +
                "    \n" +
                "    // Getters/Setters\n" +
                "}\n\n" +
                "// AuthResponse.java\n" +
                "public class AuthResponse {\n" +
                "    private String message;\n" +
                "    private String email;\n" +
                "    \n" +
                "    // Constructor, Getters/Setters\n" +
                "}")
            .addExample("Step 6: Get Current User in Controllers",
                "Update TaskController to use authenticated user:\n\n" +
                "import org.springframework.security.core.annotation.AuthenticationPrincipal;\n" +
                "import org.springframework.security.core.userdetails.UserDetails;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/tasks\")\n" +
                "public class TaskController {\n" +
                "    \n" +
                "    private final TaskService taskService;\n" +
                "    private final UserRepository userRepository;\n" +
                "    \n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<List<TaskDTO>> getAllTasks(\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        \n" +
                "        // Get current user's ID\n" +
                "        User user = userRepository.findByEmail(userDetails.getUsername())\n" +
                "            .orElseThrow();\n" +
                "        \n" +
                "        List<TaskDTO> tasks = taskService.getAllTasksForUser(user.getId());\n" +
                "        return ResponseEntity.ok(tasks);\n" +
                "    }\n" +
                "    \n" +
                "    @PostMapping\n" +
                "    public ResponseEntity<TaskDTO> createTask(\n" +
                "            @Valid @RequestBody TaskRequest request,\n" +
                "            @AuthenticationPrincipal UserDetails userDetails) {\n" +
                "        \n" +
                "        User user = userRepository.findByEmail(userDetails.getUsername())\n" +
                "            .orElseThrow();\n" +
                "        \n" +
                "        TaskDTO created = taskService.createTask(request, user.getId());\n" +
                "        return ResponseEntity.status(HttpStatus.CREATED).body(created);\n" +
                "    }\n" +
                "    \n" +
                "    // Similar for other endpoints...\n" +
                "}\n\n" +
                "@AuthenticationPrincipal = Get currently logged-in user\n" +
                "UserDetails = Spring Security interface\n" +
                "userDetails.getUsername() = Email (we set this in User.getUsername())")
            .addTheory("Testing Authentication with Postman",
                "1. REGISTER USER:\n" +
                "   POST http://localhost:8080/api/auth/register\n" +
                "   Body:\n" +
                "   {\n" +
                "     \"email\": \"alice@email.com\",\n" +
                "     \"password\": \"password123\",\n" +
                "     \"name\": \"Alice\"\n" +
                "   }\n" +
                "   \n" +
                "   Expected: 200 OK\n" +
                "   Response: {\"message\": \"User registered successfully\"}\n\n" +
                "2. LOGIN:\n" +
                "   POST http://localhost:8080/api/auth/login\n" +
                "   Body:\n" +
                "   {\n" +
                "     \"email\": \"alice@email.com\",\n" +
                "     \"password\": \"password123\"\n" +
                "   }\n" +
                "   \n" +
                "   Expected: 200 OK\n\n" +
                "3. ACCESS PROTECTED ENDPOINT (with HTTP Basic Auth):\n" +
                "   GET http://localhost:8080/api/tasks\n" +
                "   \n" +
                "   In Postman:\n" +
                "   - Go to Authorization tab\n" +
                "   - Type: Basic Auth\n" +
                "   - Username: alice@email.com\n" +
                "   - Password: password123\n" +
                "   \n" +
                "   Expected: 200 OK + your tasks\n\n" +
                "4. WITHOUT AUTH:\n" +
                "   GET http://localhost:8080/api/tasks\n" +
                "   (No Authorization header)\n" +
                "   \n" +
                "   Expected: 401 Unauthorized")
            .addWarning("Common Authentication Mistakes",
                "❌ MISTAKE 1: Storing plain passwords\n" +
                "user.setPassword(\"password123\");  // NEVER!\n" +
                "FIX: Always use PasswordEncoder\n\n" +
                "❌ MISTAKE 2: Comparing hashed passwords with ==\n" +
                "if (user.getPassword().equals(inputPassword)) { }  // Wrong!\n" +
                "FIX: Use passwordEncoder.matches(inputPassword, user.getPassword())\n\n" +
                "❌ MISTAKE 3: Not disabling CSRF for APIs\n" +
                "For stateless APIs, CSRF protection causes issues\n" +
                "FIX: .csrf().disable() in SecurityFilterChain\n\n" +
                "❌ MISTAKE 4: Returning password in API responses\n" +
                "return userRepository.findAll();  // Exposes passwords!\n" +
                "FIX: Use DTOs without password field\n\n" +
                "❌ MISTAKE 5: Weak passwords allowed\n" +
                "No validation on password strength\n" +
                "FIX: @Size(min = 8) on DTO, consider regex pattern\n\n" +
                "❌ MISTAKE 6: Not handling authentication failures\n" +
                "User gets generic \"401 Unauthorized\"\n" +
                "FIX: Custom error messages in exception handler\n\n" +
                "❌ MISTAKE 7: Hard-coding user IDs\n" +
                "Long userId = 1L;  // Always user 1!\n" +
                "FIX: Get from @AuthenticationPrincipal")
            .addKeyPoint("Best Practices Summary",
                "SECURITY:\n" +
                "✓ ALWAYS hash passwords with BCryptPasswordEncoder\n" +
                "✓ Never store passwords in plain text\n" +
                "✓ Never return passwords in API responses\n" +
                "✓ Use HTTPS in production (not HTTP)\n" +
                "✓ Implement rate limiting on login endpoint\n" +
                "✓ Add account lockout after failed attempts\n\n" +
                "SPRING SECURITY 6:\n" +
                "✓ Use SecurityFilterChain (not WebSecurityConfigurerAdapter)\n" +
                "✓ SessionCreationPolicy.STATELESS for APIs\n" +
                "✓ Implement UserDetailsService\n" +
                "✓ Use @AuthenticationPrincipal to get current user\n" +
                "✓ Disable CSRF for stateless APIs\n\n" +
                "AUTHENTICATION FLOW:\n" +
                "1. User registers → Password hashed → Stored in DB\n" +
                "2. User logs in → AuthenticationManager validates\n" +
                "3. If valid → User authenticated\n" +
                "4. Protected endpoints → Check authentication\n" +
                "5. @AuthenticationPrincipal → Get current user\n\n" +
                "NEXT STEPS (Advanced):\n" +
                "- Implement JWT tokens (stateless)\n" +
                "- Add refresh tokens\n" +
                "- Implement remember-me functionality\n" +
                "- Add OAuth2 (Google, GitHub login)\n" +
                "- Add role-based authorization (ADMIN, USER)")
            .addChallenge(createPasswordQuiz())
            .addChallenge(createSecurityQuiz())
            .addChallenge(createAuthQuiz())
            .estimatedMinutes(60)
            .build();
    }

    private static Challenge createPasswordQuiz() {
        return new Challenge.Builder("epoch-9-lesson-6-password", "Password Security", ChallengeType.MULTIPLE_CHOICE)
            .description("How should passwords be stored in the database?")
            .addMultipleChoiceOption("A) Plain text")
            .addMultipleChoiceOption("B) Hashed with BCrypt")
            .addMultipleChoiceOption("C) Encrypted with AES")
            .addMultipleChoiceOption("D) Base64 encoded")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createSecurityQuiz() {
        return new Challenge.Builder("epoch-9-lesson-6-security", "Spring Security", ChallengeType.MULTIPLE_CHOICE)
            .description("What replaces WebSecurityConfigurerAdapter in Spring Security 6?")
            .addMultipleChoiceOption("A) SecurityConfig")
            .addMultipleChoiceOption("B) SecurityFilterChain")
            .addMultipleChoiceOption("C) WebSecurity")
            .addMultipleChoiceOption("D) AuthenticationManager")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAuthQuiz() {
        return new Challenge.Builder("epoch-9-lesson-6-auth", "Authentication", ChallengeType.MULTIPLE_CHOICE)
            .description("How do you get the currently authenticated user in a controller?")
            .addMultipleChoiceOption("A) @CurrentUser")
            .addMultipleChoiceOption("B) @Authenticated")
            .addMultipleChoiceOption("C) @AuthenticationPrincipal")
            .addMultipleChoiceOption("D) SecurityContext.getUser()")
            .correctAnswer("C")
            .build();
    }
}
