package com.socraticjava.content.epoch7;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 7.7: Spring Security Basics - Authentication and Authorization
 */
public class Lesson07Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-7-lesson-7", "Lesson 7.7: Spring Security - Protecting Your API")
            .addTheory("The Problem: Anyone Can Access Everything",
                "Without security, your API is wide open:\n\n" +
                "@GetMapping(\"/admin/users\")\n" +
                "public List<User> getAllUsers() {\n" +
                "    return userRepository.findAll();\n" +
                "}\n\n" +
                "@DeleteMapping(\"/admin/users/{id}\")\n" +
                "public void deleteUser(@PathVariable Long id) {\n" +
                "    userRepository.deleteById(id);\n" +
                "}\n\n" +
                "PROBLEMS:\n" +
                "❌ No login required - anyone can access\n" +
                "❌ No user identification - can't track who did what\n" +
                "❌ No permissions - regular users can delete data\n" +
                "❌ No protection - hackers can wreak havoc\n\n" +
                "Real applications need:\n" +
                "✓ AUTHENTICATION - Who are you? (Login)\n" +
                "✓ AUTHORIZATION - What can you do? (Permissions)\n" +
                "✓ Protection from attacks (CSRF, XSS, etc.)\n\n" +
                "Solution: SPRING SECURITY")
            .addAnalogy("Spring Security is Like a Building with Security Guards",
                "BUILDING WITHOUT SECURITY:\n" +
                "- Anyone walks in through any door\n" +
                "- Access CEO's office? Sure!\n" +
                "- Delete company files? Go ahead!\n" +
                "- No one knows who did what\n\n" +
                "BUILDING WITH SECURITY:\n" +
                "1. LOBBY (Public Area):\n" +
                "   - Anyone can enter\n" +
                "   - Read brochures, see directory\n" +
                "   - = Public endpoints (/login, /register)\n\n" +
                "2. BADGE CHECK (Authentication):\n" +
                "   - Show your ID badge\n" +
                "   - Verify you are who you claim\n" +
                "   - = Username + Password\n\n" +
                "3. ACCESS LEVELS (Authorization):\n" +
                "   - Green badge: Employee areas only\n" +
                "   - Blue badge: Manager areas\n" +
                "   - Red badge: Executive areas\n" +
                "   - = Roles: USER, MANAGER, ADMIN\n\n" +
                "4. SECURITY LOG:\n" +
                "   - Record who entered when\n" +
                "   - Track actions taken\n" +
                "   - = Audit logging\n\n" +
                "Spring Security = The entire security system!")
            .addTheory("Authentication vs Authorization",
                "AUTHENTICATION: \"Who are you?\"\n" +
                "- Verifying identity\n" +
                "- Username + password\n" +
                "- Login process\n" +
                "- Result: User is identified\n\n" +
                "Example:\n" +
                "POST /login\n" +
                "Body: { \"username\": \"alice\", \"password\": \"secret123\" }\n" +
                "→ Spring verifies credentials\n" +
                "→ If valid, user is \"authenticated\"\n\n" +
                "AUTHORIZATION: \"What can you do?\"\n" +
                "- Checking permissions\n" +
                "- Based on roles (USER, ADMIN, etc.)\n" +
                "- Access control\n" +
                "- Result: User can/cannot access resource\n\n" +
                "Example:\n" +
                "GET /admin/users (authenticated as USER role)\n" +
                "→ Spring checks: Does USER role have permission?\n" +
                "→ No! Return 403 Forbidden\n\n" +
                "GET /admin/users (authenticated as ADMIN role)\n" +
                "→ Spring checks: Does ADMIN role have permission?\n" +
                "→ Yes! Return user list\n\n" +
                "BOTH ARE NEEDED:\n" +
                "1. First: Authenticate (prove who you are)\n" +
                "2. Then: Authorize (check what you can do)")
            .addTheory("Adding Spring Security to Your Project",
                "Step 1: Add dependency to pom.xml:\n\n" +
                "<dependency>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-security</artifactId>\n" +
                "</dependency>\n\n" +
                "That's it! Spring Security is now active.\n\n" +
                "DEFAULT BEHAVIOR (automatic):\n" +
                "- ALL endpoints require authentication\n" +
                "- Default user: 'user'\n" +
                "- Random password: Printed in console on startup\n" +
                "- Login form at /login\n\n" +
                "Console output:\n" +
                "Using generated security password: a1b2c3d4-e5f6-7890-abcd-ef1234567890\n\n" +
                "Now every API call needs authentication!")
            .addTheory("SecurityFilterChain - Configuring Security (Spring Security 6)",
                "Create SecurityConfig class to customize security:\n\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "public class SecurityConfig {\n" +
                "    \n" +
                "    @Bean\n" +
                "    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {\n" +
                "        http\n" +
                "            .authorizeHttpRequests(auth -> auth\n" +
                "                .requestMatchers(\"/public/**\").permitAll()      // Anyone\n" +
                "                .requestMatchers(\"/api/users/**\").hasRole(\"USER\")  // USER role\n" +
                "                .requestMatchers(\"/api/admin/**\").hasRole(\"ADMIN\") // ADMIN role\n" +
                "                .anyRequest().authenticated()  // All others need login\n" +
                "            )\n" +
                "            .formLogin(Customizer.withDefaults())  // Enable login form\n" +
                "            .httpBasic(Customizer.withDefaults()); // Enable basic auth\n" +
                "        \n" +
                "        return http.build();\n" +
                "    }\n" +
                "}\n\n" +
                "BREAKDOWN:\n" +
                "- authorizeHttpRequests: Define access rules\n" +
                "- requestMatchers: URL patterns to match\n" +
                "- permitAll(): No authentication needed\n" +
                "- hasRole(\"USER\"): Requires USER role\n" +
                "- authenticated(): Any logged-in user\n" +
                "- formLogin: Enable HTML login form\n" +
                "- httpBasic: Enable HTTP Basic Auth (for APIs)")
            .addTheory("In-Memory Users - Simple Authentication",
                "For development/testing, store users in memory:\n\n" +
                "@Bean\n" +
                "public UserDetailsService userDetailsService() {\n" +
                "    UserDetails user = User.builder()\n" +
                "        .username(\"alice\")\n" +
                "        .password(passwordEncoder().encode(\"password123\"))\n" +
                "        .roles(\"USER\")\n" +
                "        .build();\n" +
                "    \n" +
                "    UserDetails admin = User.builder()\n" +
                "        .username(\"admin\")\n" +
                "        .password(passwordEncoder().encode(\"admin123\"))\n" +
                "        .roles(\"ADMIN\")\n" +
                "        .build();\n" +
                "    \n" +
                "    return new InMemoryUserDetailsManager(user, admin);\n" +
                "}\n\n" +
                "@Bean\n" +
                "public PasswordEncoder passwordEncoder() {\n" +
                "    return new BCryptPasswordEncoder();  // Secure password hashing\n" +
                "}\n\n" +
                "Now you have:\n" +
                "- Username: alice, Password: password123, Role: USER\n" +
                "- Username: admin, Password: admin123, Role: ADMIN\n\n" +
                "NEVER store plain passwords!\n" +
                "BCryptPasswordEncoder hashes them securely.")
            .addTheory("Database-Based Authentication",
                "For production, load users from database:\n\n" +
                "1. User Entity:\n\n" +
                "@Entity\n" +
                "@Table(name = \"users\")\n" +
                "public class User {\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    private Long id;\n" +
                "    \n" +
                "    @Column(unique = true, nullable = false)\n" +
                "    private String username;\n" +
                "    \n" +
                "    @Column(nullable = false)\n" +
                "    private String password;  // Hashed!\n" +
                "    \n" +
                "    private String role;  // USER, ADMIN, etc.\n" +
                "    \n" +
                "    // Getters, setters\n" +
                "}\n\n" +
                "2. Custom UserDetailsService:\n\n" +
                "@Service\n" +
                "public class CustomUserDetailsService implements UserDetailsService {\n" +
                "    \n" +
                "    @Autowired\n" +
                "    private UserRepository userRepository;\n" +
                "    \n" +
                "    @Override\n" +
                "    public UserDetails loadUserByUsername(String username) \n" +
                "            throws UsernameNotFoundException {\n" +
                "        User user = userRepository.findByUsername(username)\n" +
                "            .orElseThrow(() -> new UsernameNotFoundException(\n" +
                "                \"User not found: \" + username));\n" +
                "        \n" +
                "        return org.springframework.security.core.userdetails.User\n" +
                "            .builder()\n" +
                "            .username(user.getUsername())\n" +
                "            .password(user.getPassword())  // Already hashed\n" +
                "            .roles(user.getRole())\n" +
                "            .build();\n" +
                "    }\n" +
                "}\n\n" +
                "Spring automatically calls loadUserByUsername() during login!")
            .addKeyPoint("Method-Level Security with @PreAuthorize",
                "Secure individual methods:\n\n" +
                "Enable in SecurityConfig:\n\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "@EnableMethodSecurity  // Enable method-level security\n" +
                "public class SecurityConfig {\n" +
                "    // ...\n" +
                "}\n\n" +
                "Use in controllers/services:\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/users\")\n" +
                "public class UserController {\n" +
                "    \n" +
                "    @GetMapping\n" +
                "    @PreAuthorize(\"hasRole('USER')\")  // Requires USER role\n" +
                "    public List<User> getAllUsers() {\n" +
                "        return userService.findAll();\n" +
                "    }\n" +
                "    \n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    @PreAuthorize(\"hasRole('ADMIN')\")  // Requires ADMIN role\n" +
                "    public void deleteUser(@PathVariable Long id) {\n" +
                "        userService.delete(id);\n" +
                "    }\n" +
                "    \n" +
                "    @GetMapping(\"/me\")\n" +
                "    @PreAuthorize(\"isAuthenticated()\")  // Any logged-in user\n" +
                "    public User getCurrentUser() {\n" +
                "        return userService.getCurrentUser();\n" +
                "    }\n" +
                "}\n\n" +
                "EXPRESSIONS:\n" +
                "- hasRole('ROLE'): Has specific role\n" +
                "- hasAnyRole('ROLE1', 'ROLE2'): Has any of these roles\n" +
                "- isAuthenticated(): Logged in\n" +
                "- permitAll(): Anyone can access")
            .addTheory("Getting the Current User",
                "Access authenticated user in controllers:\n\n" +
                "METHOD 1: Principal parameter\n\n" +
                "@GetMapping(\"/profile\")\n" +
                "public String getProfile(Principal principal) {\n" +
                "    String username = principal.getName();\n" +
                "    return \"Logged in as: \" + username;\n" +
                "}\n\n" +
                "METHOD 2: Authentication parameter\n\n" +
                "@GetMapping(\"/profile\")\n" +
                "public String getProfile(Authentication authentication) {\n" +
                "    String username = authentication.getName();\n" +
                "    Collection<? extends GrantedAuthority> authorities = \n" +
                "        authentication.getAuthorities();\n" +
                "    return \"User: \" + username + \", Roles: \" + authorities;\n" +
                "}\n\n" +
                "METHOD 3: SecurityContextHolder (anywhere in code)\n\n" +
                "Authentication auth = SecurityContextHolder.getContext().getAuthentication();\n" +
                "String username = auth.getName();\n" +
                "boolean isAdmin = auth.getAuthorities().stream()\n" +
                "    .anyMatch(a -> a.getAuthority().equals(\"ROLE_ADMIN\"));\n\n" +
                "Spring injects the current authenticated user automatically!")
            .addWarning("Password Security - Never Store Plain Passwords!",
                "❌ TERRIBLE - Plain passwords:\n\n" +
                "users table:\n" +
                "| id | username | password    |\n" +
                "|----|----------|-------------|\n" +
                "| 1  | alice    | password123 |  ← ANYONE CAN READ THIS!\n" +
                "| 2  | bob      | secret456   |  ← DATABASE BREACH = DISASTER\n\n" +
                "✓ CORRECT - Hashed passwords:\n\n" +
                "users table:\n" +
                "| id | username | password                                                   |\n" +
                "|----|----------|---------------------------------------------------------|\n" +
                "| 1  | alice    | $2a$10$abcd1234...xyz789 |  ← Hashed, can't reverse\n" +
                "| 2  | bob      | $2a$10$wxyz9876...abc123 |  ← Even if stolen, useless\n\n" +
                "Use BCryptPasswordEncoder:\n\n" +
                "@Service\n" +
                "public class UserService {\n" +
                "    @Autowired\n" +
                "    private PasswordEncoder passwordEncoder;\n" +
                "    \n" +
                "    public User registerUser(String username, String plainPassword) {\n" +
                "        User user = new User();\n" +
                "        user.setUsername(username);\n" +
                "        \n" +
                "        // Hash password before saving\n" +
                "        String hashedPassword = passwordEncoder.encode(plainPassword);\n" +
                "        user.setPassword(hashedPassword);\n" +
                "        \n" +
                "        return userRepository.save(user);\n" +
                "    }\n" +
                "}\n\n" +
                "BCrypt:\n" +
                "- One-way hash (can't reverse)\n" +
                "- Salted (same password = different hash)\n" +
                "- Slow on purpose (prevents brute force)\n" +
                "- Industry standard")
            .addExample("Complete Security Configuration",
                "SecurityConfig.java:\n\n" +
                "@Configuration\n" +
                "@EnableWebSecurity\n" +
                "@EnableMethodSecurity\n" +
                "public class SecurityConfig {\n" +
                "    \n" +
                "    @Bean\n" +
                "    public SecurityFilterChain securityFilterChain(HttpSecurity http) \n" +
                "            throws Exception {\n" +
                "        http\n" +
                "            .csrf(csrf -> csrf.disable())  // Disable for REST APIs\n" +
                "            .authorizeHttpRequests(auth -> auth\n" +
                "                // Public endpoints\n" +
                "                .requestMatchers(\"/api/auth/**\").permitAll()\n" +
                "                .requestMatchers(\"/api/public/**\").permitAll()\n" +
                "                \n" +
                "                // User endpoints\n" +
                "                .requestMatchers(\"/api/users/**\").hasAnyRole(\"USER\", \"ADMIN\")\n" +
                "                \n" +
                "                // Admin endpoints\n" +
                "                .requestMatchers(\"/api/admin/**\").hasRole(\"ADMIN\")\n" +
                "                \n" +
                "                // All other requests need authentication\n" +
                "                .anyRequest().authenticated()\n" +
                "            )\n" +
                "            .httpBasic(Customizer.withDefaults())  // Enable HTTP Basic\n" +
                "            .sessionManagement(session -> session\n" +
                "                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)\n" +
                "            );  // Stateless for REST APIs\n" +
                "        \n" +
                "        return http.build();\n" +
                "    }\n" +
                "    \n" +
                "    @Bean\n" +
                "    public PasswordEncoder passwordEncoder() {\n" +
                "        return new BCryptPasswordEncoder();\n" +
                "    }\n" +
                "}\n\n" +
                "This configuration:\n" +
                "✓ Allows public access to /api/auth/** and /api/public/**\n" +
                "✓ Requires USER or ADMIN role for /api/users/**\n" +
                "✓ Requires ADMIN role for /api/admin/**\n" +
                "✓ Uses BCrypt for password hashing\n" +
                "✓ Stateless sessions (REST API pattern)")
            .addChallenge(createAuthenticationQuiz())
            .addChallenge(createAuthorizationQuiz())
            .addChallenge(createPasswordQuiz())
            .estimatedMinutes(50)
            .build();
    }

    private static Challenge createAuthenticationQuiz() {
        return new Challenge.Builder("epoch-7-lesson-7-authentication", "Authentication vs Authorization", ChallengeType.MULTIPLE_CHOICE)
            .description("What is the difference between authentication and authorization?")
            .addMultipleChoiceOption("A) They are the same thing")
            .addMultipleChoiceOption("B) Authentication verifies who you are, authorization checks what you can do")
            .addMultipleChoiceOption("C) Authorization verifies who you are, authentication checks what you can do")
            .addMultipleChoiceOption("D) Authentication is only for admins")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createAuthorizationQuiz() {
        return new Challenge.Builder("epoch-7-lesson-7-roles", "Role-Based Access", ChallengeType.MULTIPLE_CHOICE)
            .description("Which annotation restricts a method to users with ADMIN role?")
            .addMultipleChoiceOption("A) @Secured(\"ADMIN\")")
            .addMultipleChoiceOption("B) @PreAuthorize(\"hasRole('ADMIN')\")")
            .addMultipleChoiceOption("C) @RoleRequired(\"ADMIN\")")
            .addMultipleChoiceOption("D) @AdminOnly")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createPasswordQuiz() {
        return new Challenge.Builder("epoch-7-lesson-7-password", "Password Security", ChallengeType.MULTIPLE_CHOICE)
            .description("Why should you NEVER store plain passwords in the database?")
            .addMultipleChoiceOption("A) Plain passwords take up more space")
            .addMultipleChoiceOption("B) If the database is breached, all user passwords are exposed")
            .addMultipleChoiceOption("C) Plain passwords are slower to query")
            .addMultipleChoiceOption("D) Spring Security doesn't support plain passwords")
            .correctAnswer("B")
            .build();
    }
}
