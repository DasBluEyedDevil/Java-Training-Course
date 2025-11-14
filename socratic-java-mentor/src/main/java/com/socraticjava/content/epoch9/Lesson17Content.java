package com.socraticjava.content.epoch9;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 9, Lesson 17: Security Best Practices for Production
 *
 * This lesson teaches essential security practices for building secure
 * Spring Boot applications ready for production.
 */
public class Lesson17Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Security Best Practices for Production",
            """
            # Security Best Practices for Production

            ## Why Security Matters

            Imagine leaving your house with all doors unlocked, windows open, and a sign
            saying "Help yourself to anything inside." That's what deploying an unsecured
            application to the internet feels like.

            **Real-world security breach statistics:**
            - 85% of web vulnerabilities from improper input validation
            - 60% of attacks exploit CSRF vulnerabilities
            - 50% reduction in XSS with proper Content Security Policy
            - Average cost of data breach: $4.45 million (2023)

            **Without proper security:**
            - User data stolen (emails, passwords, personal info)
            - Database dumped or deleted
            - Application defaced or taken offline
            - Legal liability and regulatory fines
            - Destroyed reputation and customer trust

            **With security best practices:**
            - Protected user data
            - Prevented common attacks
            - Compliance with regulations (GDPR, HIPAA, PCI-DSS)
            - Customer confidence
            - Professional reputation

            ## The OWASP Top 10 (2021)

            The Open Web Application Security Project (OWASP) identifies the top security risks:

            1. **Broken Access Control** - Users access unauthorized resources
            2. **Cryptographic Failures** - Weak encryption, exposed sensitive data
            3. **Injection** - SQL injection, command injection, XSS
            4. **Insecure Design** - Missing security controls in design phase
            5. **Security Misconfiguration** - Default passwords, unnecessary features
            6. **Vulnerable Components** - Outdated libraries with known exploits
            7. **Authentication Failures** - Weak passwords, session hijacking
            8. **Software and Data Integrity Failures** - Unsigned code, tampered data
            9. **Security Logging Failures** - Can't detect or investigate breaches
            10. **Server-Side Request Forgery (SSRF)** - Attacker makes server fetch URLs

            We'll cover how Spring Boot addresses these vulnerabilities.

            ## Input Validation

            **Never trust user input. Ever.**

            ### Bean Validation (JSR-380)

            ```java
            package com.example.dto;

            import jakarta.validation.constraints.*;
            import lombok.Data;

            @Data
            public class UserRegistrationRequest {

                @NotBlank(message = "Name is required")
                @Size(min = 2, max = 100, message = "Name must be 2-100 characters")
                @Pattern(
                    regexp = "^[a-zA-Z\\s'-]+$",
                    message = "Name contains invalid characters"
                )
                private String name;

                @NotBlank(message = "Email is required")
                @Email(message = "Invalid email format")
                @Size(max = 255)
                private String email;

                @NotBlank(message = "Password is required")
                @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
                @Pattern(
                    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                    message = "Password must contain uppercase, lowercase, number, and special character"
                )
                private String password;

                @NotNull(message = "Age is required")
                @Min(value = 18, message = "Must be at least 18 years old")
                @Max(value = 120, message = "Age must be realistic")
                private Integer age;

                @Pattern(
                    regexp = "^\\+?[1-9]\\d{1,14}$",
                    message = "Invalid phone number format"
                )
                private String phoneNumber;
            }
            ```

            **Use in Controller:**
            ```java
            @RestController
            @RequestMapping("/api/users")
            public class UserController {

                @PostMapping("/register")
                public ResponseEntity<UserDTO> register(
                    @Valid @RequestBody UserRegistrationRequest request
                ) {
                    // @Valid triggers validation
                    // If validation fails, throws MethodArgumentNotValidException
                    // GlobalExceptionHandler catches and returns 400 Bad Request

                    UserDTO user = userService.register(request);
                    return ResponseEntity.status(HttpStatus.CREATED).body(user);
                }
            }
            ```

            ### Custom Validators

            ```java
            package com.example.validator;

            import jakarta.validation.Constraint;
            import jakarta.validation.ConstraintValidator;
            import jakarta.validation.ConstraintValidatorContext;
            import jakarta.validation.Payload;

            import java.lang.annotation.*;

            /**
             * Custom annotation for strong password validation
             */
            @Target({ElementType.FIELD})
            @Retention(RetentionPolicy.RUNTIME)
            @Constraint(validatedBy = StrongPasswordValidator.class)
            public @interface StrongPassword {
                String message() default "Password must be strong";
                Class<?>[] groups() default {};
                Class<? extends Payload>[] payload() default {};
            }

            /**
             * Validator implementation
             */
            public class StrongPasswordValidator
                implements ConstraintValidator<StrongPassword, String> {

                @Override
                public boolean isValid(String password, ConstraintValidatorContext context) {
                    if (password == null) {
                        return false;
                    }

                    // Check password against common passwords list
                    if (isCommonPassword(password)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(
                            "Password is too common. Please choose a stronger password."
                        ).addConstraintViolation();
                        return false;
                    }

                    // Check for sequential characters
                    if (hasSequentialChars(password)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(
                            "Password contains sequential characters (123, abc, etc.)"
                        ).addConstraintViolation();
                        return false;
                    }

                    return true;
                }

                private boolean isCommonPassword(String password) {
                    Set<String> commonPasswords = Set.of(
                        "password", "12345678", "qwerty", "admin123", "welcome"
                    );
                    return commonPasswords.contains(password.toLowerCase());
                }

                private boolean hasSequentialChars(String password) {
                    for (int i = 0; i < password.length() - 2; i++) {
                        if (password.charAt(i+1) == password.charAt(i) + 1 &&
                            password.charAt(i+2) == password.charAt(i) + 2) {
                            return true;
                        }
                    }
                    return false;
                }
            }

            // Usage:
            @StrongPassword
            private String password;
            ```

            ## SQL Injection Prevention

            ### ❌ NEVER Do This (Vulnerable)

            ```java
            // CATASTROPHICALLY INSECURE!
            public List<User> searchUsers(String name) {
                String sql = "SELECT * FROM users WHERE name = '" + name + "'";
                return jdbcTemplate.query(sql, userRowMapper);
            }

            // Attack:
            // Input: ' OR '1'='1
            // SQL: SELECT * FROM users WHERE name = '' OR '1'='1'
            // Result: Returns ALL users! ✗
            ```

            ### ✅ Always Do This (Secure)

            **Option 1: Spring Data JPA**
            ```java
            public interface UserRepository extends JpaRepository<User, Long> {
                // Spring Data generates safe parameterized query
                List<User> findByNameContaining(String name);

                // Custom query with parameter binding
                @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
                List<User> searchByName(@Param("name") String name);
            }
            ```

            **Option 2: JdbcTemplate with Parameters**
            ```java
            public List<User> searchUsers(String name) {
                String sql = "SELECT * FROM users WHERE name LIKE ?";
                return jdbcTemplate.query(sql, userRowMapper, "%" + name + "%");
                // Parameter is safely escaped
            }
            ```

            **Option 3: JPA Criteria API**
            ```java
            public List<User> searchUsers(String name) {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<User> query = cb.createQuery(User.class);
                Root<User> root = query.from(User.class);

                query.where(cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
                ));

                return entityManager.createQuery(query).getResultList();
                // Safely parameterized
            }
            ```

            ## Cross-Site Scripting (XSS) Prevention

            ### Output Encoding

            ```java
            @Service
            public class CommentService {

                /**
                 * Sanitize user-generated content before storing
                 */
                public Comment createComment(CommentRequest request) {
                    Comment comment = new Comment();

                    // Encode HTML to prevent script injection
                    comment.setText(HtmlUtils.htmlEscape(request.getText()));

                    return commentRepository.save(comment);
                }
            }
            ```

            ### Content Security Policy (CSP)

            ```java
            @Configuration
            public class SecurityConfig {

                @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                        .headers(headers -> headers
                            .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                    "default-src 'self'; " +
                                    "script-src 'self' https://trusted-cdn.com; " +
                                    "style-src 'self' 'unsafe-inline'; " +
                                    "img-src 'self' data: https:; " +
                                    "font-src 'self'; " +
                                    "connect-src 'self'; " +
                                    "frame-ancestors 'none'"
                                )
                            )
                            .xssProtection(xss -> xss.headerValue("1; mode=block"))
                            .frameOptions(frame -> frame.deny())
                        );

                    return http.build();
                }
            }
            ```

            **CSP Directives Explained:**
            - `default-src 'self'`: Only load resources from same origin
            - `script-src 'self' https://trusted-cdn.com`: Scripts from own site or trusted CDN
            - `style-src 'self' 'unsafe-inline'`: Styles from own site, allow inline styles
            - `frame-ancestors 'none'`: Prevent clickjacking

            ### Frontend XSS Prevention

            ```javascript
            // ❌ NEVER do this:
            element.innerHTML = userInput;  // XSS vulnerable!

            // ✅ Do this instead:
            element.textContent = userInput;  // Safe - doesn't parse HTML

            // Or sanitize with DOMPurify library:
            element.innerHTML = DOMPurify.sanitize(userInput);
            ```

            ## CSRF Protection

            ### Keep CSRF Enabled for Browser Clients

            ```java
            @Configuration
            @EnableWebSecurity
            public class SecurityConfig {

                @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                        .csrf(csrf -> csrf
                            // CSRF enabled by default for Spring Security 6
                            // Only disable for stateless APIs with JWT
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        );

                    return http.build();
                }
            }
            ```

            ### For Stateless JWT APIs

            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf.disable())  // OK for JWT-only APIs
                    .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    );

                return http.build();
            }
            ```

            **When to disable CSRF:**
            - ✅ Stateless REST API with JWT (no cookies, no sessions)
            - ❌ Traditional web app with session cookies
            - ❌ Any cookie-based authentication

            ## CORS Configuration

            ### Strict CORS Policy

            ```java
            @Configuration
            public class CorsConfig implements WebMvcConfigurer {

                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/api/**")
                        // ❌ NEVER use "*" in production!
                        // .allowedOrigins("*")

                        // ✅ Specify exact allowed origins
                        .allowedOrigins(
                            "https://yourdomain.com",
                            "https://app.yourdomain.com"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)  // Allow cookies
                        .maxAge(3600);  // Cache preflight for 1 hour
                }
            }
            ```

            ### Environment-Specific CORS

            ```java
            @Configuration
            public class CorsConfig implements WebMvcConfigurer {

                @Value("${app.cors.allowed-origins}")
                private List<String> allowedOrigins;

                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                }
            }
            ```

            **application.yml:**
            ```yaml
            # Development
            app:
              cors:
                allowed-origins:
                  - http://localhost:3000
                  - http://localhost:8080

            # Production (override)
            app:
              cors:
                allowed-origins:
                  - https://yourdomain.com
            ```

            ## Secure Password Storage

            ### Use BCrypt (Already Covered, But Critical)

            ```java
            @Configuration
            public class SecurityConfig {

                @Bean
                public PasswordEncoder passwordEncoder() {
                    // BCrypt with strength 12 (default is 10)
                    return new BCryptPasswordEncoder(12);
                }
            }

            @Service
            public class UserService {

                private final PasswordEncoder passwordEncoder;

                public void registerUser(RegisterRequest request) {
                    User user = new User();

                    // ✅ ALWAYS hash passwords
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    // ❌ NEVER store plain text!
                    // user.setPassword(request.getPassword());

                    userRepository.save(user);
                }
            }
            ```

            ## Secrets Management

            ### ❌ NEVER Commit Secrets to Git

            ```java
            // ❌ CATASTROPHICALLY BAD:
            spring.datasource.password=MySecretPassword123!
            jwt.secret=MySuperSecretKey
            aws.access.key=AKIAIOSFODNN7EXAMPLE
            ```

            ### ✅ Use Environment Variables

            ```yaml
            # application.yml
            spring:
              datasource:
                url: jdbc:postgresql://localhost:5432/mydb
                username: ${DB_USERNAME}
                password: ${DB_PASSWORD}

            jwt:
              secret: ${JWT_SECRET}
              expiration: ${JWT_EXPIRATION:900000}

            aws:
              access-key: ${AWS_ACCESS_KEY}
              secret-key: ${AWS_SECRET_KEY}
            ```

            **Set environment variables:**
            ```bash
            export DB_USERNAME=produser
            export DB_PASSWORD=super_secret_password
            export JWT_SECRET=$(openssl rand -base64 32)
            export AWS_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE
            export AWS_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
            ```

            ### Secrets Management Services

            ```java
            // AWS Secrets Manager
            @Configuration
            public class SecretsConfig {

                @Bean
                public DataSource dataSource() {
                    SecretsManagerClient client = SecretsManagerClient.create();

                    GetSecretValueRequest request = GetSecretValueRequest.builder()
                        .secretId("prod/myapp/database")
                        .build();

                    GetSecretValueResponse response = client.getSecretValue(request);
                    String secret = response.secretString();

                    // Parse JSON secret
                    ObjectMapper mapper = new ObjectMapper();
                    DbCredentials creds = mapper.readValue(secret, DbCredentials.class);

                    // Create DataSource with fetched credentials
                    return DataSourceBuilder.create()
                        .url(creds.getUrl())
                        .username(creds.getUsername())
                        .password(creds.getPassword())
                        .build();
                }
            }
            ```

            ## Dependency Security

            ### Check for Vulnerable Dependencies

            **Add OWASP Dependency-Check Maven Plugin:**
            ```xml
            <plugin>
                <groupId>org.owasp</groupId>
                <artifactId>dependency-check-maven</artifactId>
                <version>9.0.9</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            ```

            **Run security scan:**
            ```bash
            mvn dependency-check:check

            # Generates report: target/dependency-check-report.html
            ```

            ### Keep Dependencies Updated

            ```xml
            <!-- Use versions Maven plugin -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>versions-maven-plugin</artifactId>
                <version>2.16.2</version>
            </plugin>
            ```

            **Check for updates:**
            ```bash
            mvn versions:display-dependency-updates

            # Shows outdated dependencies with available updates
            ```

            ### Dependabot (GitHub)

            **Create `.github/dependabot.yml`:**
            ```yaml
            version: 2
            updates:
              - package-ecosystem: "maven"
                directory: "/"
                schedule:
                  interval: "weekly"
                open-pull-requests-limit: 10
            ```

            Automatically creates PRs when dependencies have security updates.

            ## Rate Limiting

            Prevent brute force and DoS attacks:

            ```java
            @Component
            public class RateLimitFilter extends OncePerRequestFilter {

                private final Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

                @Override
                protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
                ) throws ServletException, IOException {

                    String clientId = getClientId(request);
                    RateLimiter limiter = limiters.computeIfAbsent(
                        clientId,
                        k -> RateLimiter.create(10.0)  // 10 requests per second
                    );

                    if (!limiter.tryAcquire()) {
                        response.setStatus(429);  // Too Many Requests
                        response.getWriter().write("Rate limit exceeded");
                        return;
                    }

                    filterChain.doFilter(request, response);
                }

                private String getClientId(HttpServletRequest request) {
                    // Use IP address + User-Agent for identification
                    return request.getRemoteAddr() + ":" + request.getHeader("User-Agent");
                }
            }
            ```

            ### Spring Cloud Gateway Rate Limiting

            ```yaml
            spring:
              cloud:
                gateway:
                  routes:
                    - id: api-route
                      uri: http://localhost:8080
                      predicates:
                        - Path=/api/**
                      filters:
                        - name: RequestRateLimiter
                          args:
                            redis-rate-limiter:
                              replenishRate: 10  # tokens per second
                              burstCapacity: 20  # max burst
            ```

            ## Security Headers

            ### Comprehensive Security Headers

            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .headers(headers -> headers
                        // Prevent clickjacking
                        .frameOptions(frame -> frame.deny())

                        // XSS protection
                        .xssProtection(xss -> xss.headerValue("1; mode=block"))

                        // Content Security Policy
                        .contentSecurityPolicy(csp -> csp
                            .policyDirectives("default-src 'self'")
                        )

                        // Prevent MIME sniffing
                        .contentTypeOptions(contentType -> contentType.disable())

                        // HSTS: Force HTTPS
                        .httpStrictTransportSecurity(hsts -> hsts
                            .includeSubDomains(true)
                            .maxAgeInSeconds(31536000)  // 1 year
                        )

                        // Referrer Policy
                        .referrerPolicy(referrer -> referrer
                            .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                        )

                        // Permissions Policy
                        .permissionsPolicy(permissions -> permissions
                            .policy("geolocation=(self), microphone=()")
                        )
                    );

                return http.build();
            }
            ```

            ## Audit Logging

            Track security-relevant events:

            ```java
            @Service
            @Slf4j
            public class AuditService {

                @Autowired
                private AuditLogRepository auditLogRepository;

                public void logAuthenticationSuccess(String username, String ipAddress) {
                    AuditLog log = AuditLog.builder()
                        .event("AUTHENTICATION_SUCCESS")
                        .username(username)
                        .ipAddress(ipAddress)
                        .timestamp(LocalDateTime.now())
                        .build();

                    auditLogRepository.save(log);
                    log.info("Authentication success: user={}, ip={}", username, ipAddress);
                }

                public void logAuthenticationFailure(String username, String ipAddress, String reason) {
                    AuditLog log = AuditLog.builder()
                        .event("AUTHENTICATION_FAILURE")
                        .username(username)
                        .ipAddress(ipAddress)
                        .details(reason)
                        .timestamp(LocalDateTime.now())
                        .build();

                    auditLogRepository.save(log);
                    log.warn("Authentication failure: user={}, ip={}, reason={}",
                        username, ipAddress, reason);
                }

                public void logPrivilegedAction(String username, String action, String resource) {
                    AuditLog log = AuditLog.builder()
                        .event("PRIVILEGED_ACTION")
                        .username(username)
                        .action(action)
                        .resource(resource)
                        .timestamp(LocalDateTime.now())
                        .build();

                    auditLogRepository.save(log);
                    log.info("Privileged action: user={}, action={}, resource={}",
                        username, action, resource);
                }
            }
            ```

            ## Security Checklist for Production

            ### Application Security
            - [ ] All user input validated with Bean Validation
            - [ ] SQL injection prevented (using JPA/parameterized queries)
            - [ ] XSS prevented (CSP headers, output encoding)
            - [ ] CSRF protection enabled for session-based auth
            - [ ] CORS configured with specific origins (no wildcards)
            - [ ] Passwords hashed with BCrypt (strength ≥ 12)
            - [ ] Secrets in environment variables, not code
            - [ ] Security headers configured (CSP, HSTS, X-Frame-Options)

            ### Authentication & Authorization
            - [ ] JWT tokens short-lived (≤ 15 minutes)
            - [ ] Refresh tokens implemented securely
            - [ ] Account lockout after failed login attempts
            - [ ] Password complexity requirements enforced
            - [ ] Multi-factor authentication (MFA) available
            - [ ] Session timeout configured
            - [ ] Role-based access control (RBAC) implemented

            ### Data Protection
            - [ ] HTTPS enforced (HSTS header)
            - [ ] Sensitive data encrypted at rest
            - [ ] Database credentials secured
            - [ ] PII (Personally Identifiable Information) protected
            - [ ] Data retention policies implemented

            ### Monitoring & Incident Response
            - [ ] Audit logging for security events
            - [ ] Failed login attempts monitored
            - [ ] Anomaly detection configured
            - [ ] Incident response plan documented
            - [ ] Security alerts configured

            ### Dependency Management
            - [ ] Dependencies scanned for vulnerabilities
            - [ ] Automated dependency updates (Dependabot)
            - [ ] No known CVEs in dependencies
            - [ ] Regular security updates applied

            ### Infrastructure
            - [ ] Rate limiting implemented
            - [ ] DDoS protection configured
            - [ ] Firewall rules configured
            - [ ] Database access restricted
            - [ ] Regular security assessments

            ## Summary

            Security must be built into applications from day one:

            **Input Validation:**
            - Use Bean Validation (@Valid, @NotBlank, @Email, etc.)
            - Custom validators for complex rules
            - Validate on server side always

            **Injection Prevention:**
            - Use JPA repositories (never raw SQL strings)
            - Parameterized queries with JdbcTemplate
            - JPA Criteria API for dynamic queries

            **XSS Prevention:**
            - Content Security Policy headers
            - HTML encoding user content
            - Sanitize on frontend with DOMPurify

            **Authentication & Authorization:**
            - BCrypt password hashing (strength ≥ 12)
            - JWT with short expiration
            - RBAC with @PreAuthorize

            **Configuration:**
            - CSRF enabled for session-based auth
            - Strict CORS policy (no wildcards)
            - Security headers (CSP, HSTS, X-Frame-Options)
            - Secrets in environment variables

            **Monitoring:**
            - Audit logging for security events
            - Rate limiting to prevent abuse
            - Dependency vulnerability scanning

            Security is not optional—it's fundamental to professional software development.
            """,
            60  // Estimated minutes to complete
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        QuizQuestion question = new QuizQuestion(
            "Your API uses JWT authentication with no sessions or cookies. A security audit recommends enabling CSRF protection. Should you follow this recommendation?",
            "B",
            """
            This question tests understanding of when CSRF protection is necessary and when
            it's not. Many developers enable or disable CSRF without understanding why.

            The correct answer is B. No, CSRF protection is unnecessary for stateless JWT APIs
            that don't use cookies. CSRF only affects cookie-based authentication.

            **Understanding CSRF (Cross-Site Request Forgery):**

            CSRF exploits the browser's automatic inclusion of cookies with requests:

            **The Attack (Session-Based Auth):**
            ```
            1. User logs into bank.com (receives session cookie)
            2. User visits evil.com (while still logged in)
            3. evil.com contains: <img src="https://bank.com/transfer?to=attacker&amount=1000">
            4. Browser automatically sends session cookie to bank.com
            5. Bank processes request (authenticated via cookie)
            6. Money transferred to attacker! ✗
            ```

            **Why This Works:**
            - Browser automatically includes cookies with ALL requests to a domain
            - Bank can't distinguish legitimate requests from CSRF attacks
            - User doesn't even see the request happening

            **How CSRF Protection Works:**
            ```
            1. Server generates unique CSRF token
            2. Token stored in session (server-side)
            3. Token sent to client (in cookie or response body)
            4. Client must include token in request headers/body
            5. Server verifies token matches session
            6. Attacker can't get token (same-origin policy)
            ```

            **With JWT (No CSRF Risk):**

            ```javascript
            // JWT stored in localStorage (not cookies)
            const token = localStorage.getItem('access_token');

            // JWT sent in Authorization header
            fetch('https://bank.com/api/transfer', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,  // Manual header
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ to: 'friend', amount: 100 })
            });
            ```

            **Evil.com tries CSRF attack:**
            ```html
            <img src="https://bank.com/api/transfer?to=attacker&amount=1000">
            ```

            **What happens:**
            ```
            1. Browser makes request to bank.com
            2. NO Authorization header included (browsers don't auto-send)
            3. JWT not in localStorage of evil.com (same-origin policy)
            4. Bank API returns 401 Unauthorized
            5. Attack fails! ✓
            ```

            **The Key Difference:**

            ```
            Cookies:
            - Browser automatically includes with EVERY request
            - Even requests initiated by evil.com
            - CSRF protection REQUIRED

            JWT in Authorization header:
            - Must be manually added to each request
            - JavaScript from evil.com can't access tokens from bank.com
            - CSRF protection UNNECESSARY
            ```

            **Spring Security Configuration:**

            **For JWT (Stateless):**
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf.disable())  // Safe to disable
                    .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                    );

                return http.build();
            }
            ```

            **For Session-Based (Cookies):**
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf
                        // CSRF enabled by default - KEEP IT!
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    )
                    .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    );

                return http.build();
            }
            ```

            Why other answers are incorrect:

            A is wrong because enabling CSRF protection on a JWT API adds unnecessary
            complexity without security benefit:

            ```java
            // Unnecessary for JWT!
            http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            );
            ```

            This would require:
            - Frontend to fetch CSRF token
            - Include CSRF token in every request
            - Server to validate CSRF token
            - All for zero actual security improvement

            JWT APIs are already protected from CSRF by design.

            C is wrong because while CSRF and XSS are both security concerns, they're
            completely different attacks:

            **CSRF:** Attacker tricks browser into making authenticated requests
            **XSS:** Attacker injects malicious JavaScript that executes on your site

            Enabling CSRF protection does NOT protect against XSS. XSS is prevented by:
            - Content Security Policy headers
            - Output encoding (HTML escaping)
            - Input validation
            - Sanitizing user content

            These are separate concerns requiring separate protections.

            D is wrong because CSRF is not required by "modern security standards" for
            stateless APIs. In fact, OWASP and security best practices explicitly state
            CSRF protection is only needed for session-based authentication:

            **OWASP Recommendation:**
            "CSRF protection is required for any request that could be processed by a
            browser by normal users. If you only create a service that is used by
            non-browser clients, you will likely want to disable CSRF protection."

            **When to Enable CSRF:**
            ✅ Session cookies (traditional web apps)
            ✅ Any cookie-based authentication
            ✅ OAuth with cookies

            **When to Disable CSRF:**
            ✅ Pure JWT API (Authorization header)
            ✅ Mobile app backend
            ✅ Microservice-to-microservice communication

            **Hybrid Approach (If Using Both):**
            ```java
            @Bean
            public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf
                        // Disable CSRF for API endpoints (JWT)
                        .ignoringRequestMatchers("/api/**")
                        // Enable CSRF for web endpoints (sessions)
                    );

                return http.build();
            }
            ```

            **Real-World Scenario:**

            ```
            Application: Banking app with mobile app and web app

            Mobile app:
            - Uses JWT in Authorization header
            - CSRF protection: DISABLED ✓

            Web app:
            - Uses session cookies
            - CSRF protection: ENABLED ✓

            Configuration:
            @Bean
            public SecurityFilterChain apiSecurity(HttpSecurity http) {
                http
                    .securityMatcher("/api/**")
                    .csrf(csrf -> csrf.disable());
                return http.build();
            }

            @Bean
            public SecurityFilterChain webSecurity(HttpSecurity http) {
                http
                    .securityMatcher("/web/**")
                    .csrf(csrf -> csrf.csrfTokenRepository(...));
                return http.build();
            }
            ```

            **Testing:**

            ```java
            @Test
            void csrfDisabledForJwtApi() throws Exception {
                mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + validJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Task\"}"))
                    .andExpect(status().isCreated());  // Works without CSRF token
            }

            @Test
            void csrfEnabledForWebEndpoints() throws Exception {
                mockMvc.perform(post("/web/tasks")
                        .sessionAttr("user", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isForbidden());  // Fails without CSRF token

                mockMvc.perform(post("/web/tasks")
                        .sessionAttr("user", user)
                        .with(csrf())  // Include CSRF token
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                    .andExpect(status().isOk());  // Succeeds with CSRF token
            }
            ```

            **Key Takeaway:**

            CSRF protection is authentication-method specific, not application-type specific.
            Understand your authentication mechanism:
            - Cookies → Enable CSRF
            - JWT in headers → Disable CSRF

            Don't blindly follow recommendations without understanding the security model.
            """
        );

        question.addChoice("A", "Yes - CSRF protection should always be enabled as a security best practice");
        question.addChoice("B", "No - CSRF protection is unnecessary for stateless JWT APIs that don't use cookies");
        question.addChoice("C", "Yes - CSRF protection prevents XSS attacks which are common in modern web apps");
        question.addChoice("D", "Yes - Modern security standards require CSRF for all production APIs");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "A developer stores the database password in application.yml and commits it to Git. What is the MOST serious consequence?",
            "A",
            """
            This is one of the most common and dangerous security mistakes developers make.
            Understanding the consequences is critical for protecting production systems.

            The correct answer is A. Anyone with repository access (including future
            contributors and attackers who compromise Git) can access production credentials
            and potentially steal or destroy all data.

            **Understanding the Severity:**

            **What Gets Exposed:**
            ```yaml
            # application.yml (committed to Git!)
            spring:
              datasource:
                url: jdbc:postgresql://prod-db.company.com:5432/production
                username: admin
                password: SuperSecret123!

            aws:
              access-key: AKIAIOSFODNN7EXAMPLE
              secret-key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

            jwt:
              secret: MySecretJWTKey2024
            ```

            **Who Can Access:**
            1. **Current team members** - All developers, QA, interns
            2. **Past team members** - People who left months/years ago
            3. **Future team members** - New hires have full access
            4. **Third-party contractors**
            5. **Attackers** - If Git repo compromised or public by mistake
            6. **Git history** - Even if you delete later, it's in history!

            **How Attackers Find Exposed Credentials:**

            **GitHub Scanning:**
            ```bash
            # Automated bots scan GitHub 24/7 for:
            - "password"
            - "secret"
            - AWS access keys
            - Database connection strings
            - API keys
            ```

            **Real Statistics:**
            - Exposed credentials detected on GitHub: **Within 5 minutes** of commit
            - Time to first unauthorized access: **Within 1 hour**
            - Data breaches from exposed secrets: **Thousands annually**

            **Real-World Attack Scenario:**
            ```
            09:00 AM - Developer commits application.yml with DB password
            09:03 AM - GitHub scanning bot detects password
            09:15 AM - Attacker accesses repository
            09:20 AM - Attacker connects to production database
            09:25 AM - Attacker dumps all user data (emails, names, addresses)
            09:30 AM - Attacker deletes all tables
            09:31 AM - Production goes down
            10:00 AM - Team discovers breach
            10:01 AM - Damage already done: All data stolen and destroyed
            ```

            **What Attackers Can Do:**

            **With Database Credentials:**
            ```sql
            -- Read everything
            SELECT * FROM users;  -- Steal all user data
            SELECT * FROM credit_cards;  -- Steal payment info

            -- Modify everything
            UPDATE users SET is_admin = true WHERE email = 'attacker@evil.com';

            -- Destroy everything
            DROP TABLE users CASCADE;
            DROP DATABASE production;
            ```

            **With AWS Credentials:**
            ```bash
            # Spin up expensive EC2 instances for crypto mining
            # (Your bill: $50,000)
            aws ec2 run-instances --instance-type p3.16xlarge --count 100

            # Delete all S3 buckets
            aws s3 rb s3://production-data --force

            # Access sensitive data
            aws s3 sync s3://customer-data ./stolen/
            ```

            **With JWT Secret:**
            ```java
            // Create admin token for any user
            String jwtSecret = "MySecretJWTKey2024";
            String adminToken = Jwts.builder()
                .setSubject("attacker@evil.com")
                .claim("roles", List.of("ADMIN"))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();

            // Now has admin access to entire application!
            ```

            **The Correct Approach:**

            **1. Environment Variables:**
            ```yaml
            # application.yml (safe to commit)
            spring:
              datasource:
                url: ${DB_URL}
                username: ${DB_USERNAME}
                password: ${DB_PASSWORD}

            aws:
              access-key: ${AWS_ACCESS_KEY}
              secret-key: ${AWS_SECRET_KEY}

            jwt:
              secret: ${JWT_SECRET}
            ```

            **2. Set in Environment:**
            ```bash
            export DB_URL=jdbc:postgresql://prod-db.company.com:5432/production
            export DB_USERNAME=produser
            export DB_PASSWORD=ActualSecretPassword123!
            export AWS_ACCESS_KEY=AKIAIOSFODNN7EXAMPLE
            export AWS_SECRET_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
            export JWT_SECRET=$(openssl rand -base64 32)
            ```

            **3. .gitignore Sensitive Files:**
            ```
            # .gitignore
            application-local.yml
            application-dev.yml
            .env
            .env.local
            secrets/
            *.key
            *.pem
            ```

            **4. Secrets Management (Production):**
            ```java
            // AWS Secrets Manager, HashiCorp Vault, Azure Key Vault
            @Bean
            public DataSource dataSource(SecretsManagerClient secretsClient) {
                GetSecretValueResponse response = secretsClient.getSecretValue(
                    GetSecretValueRequest.builder()
                        .secretId("prod/db/credentials")
                        .build()
                );

                String secret = response.secretString();
                DbCredentials creds = objectMapper.readValue(secret, DbCredentials.class);

                return DataSourceBuilder.create()
                    .url(creds.getUrl())
                    .username(creds.getUsername())
                    .password(creds.getPassword())
                    .build();
            }
            ```

            Why other answers underestimate the risk:

            B is wrong because while changing the password is necessary, it doesn't undo
            the damage:

            ```
            Problem with "just change the password":
            1. Attacker may have already copied data
            2. Attacker may have created backdoor accounts
            3. Attacker may have modified data
            4. Password still in Git history forever
            5. Attacker can find password in historical commits
            ```

            **Git Never Forgets:**
            ```bash
            # Even after deleting password from current commit
            git log --all -S "SuperSecret123!" --source --full-history

            # Shows all commits that ever contained that string
            # Attacker can checkout old commit and get password
            ```

            To truly remove from Git history (difficult and disruptive):
            ```bash
            git filter-branch --force --index-filter \
                "git rm --cached --ignore-unmatch application.yml" \
                --prune-empty --tag-name-filter cat -- --all

            # Force push to rewrite history
            git push --force --all

            # Still not enough if others forked repository!
            ```

            C is wrong because code review doesn't prevent this problem:

            ```
            Why code review isn't sufficient:
            1. Reviewers might not notice (hundreds of lines changed)
            2. Credentials might look like example values
            3. Everyone has access once merged anyway
            4. Problem exists from first commit, before review
            5. Doesn't prevent accidental public repository
            ```

            Code review is important but is not a security control for secrets.

            D is wrong because encryption is insufficient and misses the point:

            ```
            Why encrypting application.yml doesn't help:
            1. How do you decrypt it? Need decryption key!
            2. Where do you store decryption key? Same problem!
            3. Adds complexity without solving core issue
            4. Application needs plaintext to connect anyway
            ```

            **Encrypted secrets still in Git:**
            ```yaml
            # Still a bad idea!
            spring:
              datasource:
                password: U2FsdGVkX1/Encrypted+Base64+String==
            ```

            Application needs to decrypt, so decryption key must exist somewhere.
            This just moves the problem, doesn't solve it.

            **Additional Security Measures:**

            **1. Git Secret Scanning:**
            ```yaml
            # .github/workflows/secret-scan.yml
            name: Secret Scanning
            on: [push, pull_request]
            jobs:
              scan:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/checkout@v3
                  - uses: trufflesecurity/trufflehog@main
                    with:
                      path: ./
                      base: ${{ github.event.repository.default_branch }}
            ```

            **2. Pre-Commit Hooks:**
            ```bash
            # .git/hooks/pre-commit
            #!/bin/bash

            # Check for potential secrets
            if git diff --cached | grep -i "password\s*="; then
                echo "Error: Potential password found in commit!"
                echo "Use environment variables instead."
                exit 1
            fi
            ```

            **3. Rotate Compromised Secrets Immediately:**
            ```
            If secrets are exposed:
            1. Revoke compromised credentials immediately
            2. Generate new credentials
            3. Update production environment
            4. Audit access logs for unauthorized use
            5. Notify security team
            6. Consider data breach notification (legal requirement)
            ```

            **4. Principle of Least Privilege:**
            ```
            Don't use admin/root credentials in application:
            - Create application-specific database user
            - Grant only necessary permissions
            - Use separate credentials per environment
            - Rotate credentials regularly
            ```

            **Key Takeaway:**

            Never, ever commit credentials to version control. The damage is:
            - Permanent (Git history)
            - Wide-reaching (everyone with access)
            - Severe (full system compromise)
            - Expensive (data breach, downtime, fines)

            Use environment variables and secrets management services.
            """
        );

        question.addChoice("A", "Anyone with repository access can steal credentials, access production database, and potentially destroy all data");
        question.addChoice("B", "Password is exposed, but can be easily fixed by changing it in the database");
        question.addChoice("C", "Other team members can see the password, but code review process will catch it");
        question.addChoice("D", "Password is in plaintext, but since application.yml is encrypted in Git, it's relatively safe");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "Your search endpoint accepts user input: GET /api/products/search?name={input}. Which validation approach is MOST important for security?",
            "D",
            """
            This question tests understanding of the defense-in-depth principle and the
            importance of parameterized queries over input validation for SQL injection prevention.

            The correct answer is D. All of the above - validation prevents various attacks,
            but parameterized queries are the primary defense against SQL injection. Both
            are necessary for comprehensive security.

            **Why All Three Matter:**

            **1. Server-Side Validation (@Valid in Controller)**

            ```java
            @RestController
            @RequestMapping("/api/products")
            public class ProductController {

                @GetMapping("/search")
                public ResponseEntity<List<ProductDTO>> search(
                    @Valid @RequestParam
                    @Size(min = 1, max = 100, message = "Search must be 1-100 characters")
                    @Pattern(
                        regexp = "^[a-zA-Z0-9\\s-]+$",
                        message = "Search contains invalid characters"
                    )
                    String name
                ) {
                    List<ProductDTO> products = productService.searchByName(name);
                    return ResponseEntity.ok(products);
                }
            }
            ```

            **What this prevents:**
            - Excessively long input (DoS via large strings)
            - Special characters that could cause issues
            - Empty/whitespace-only searches
            - Invalid Unicode characters

            **What this DOESN'T fully prevent:**
            - SQL injection (if query is built improperly)
            - XSS (if output not encoded)
            - Business logic exploits

            **2. Parameterized Queries (Primary SQL Injection Defense)**

            **❌ Vulnerable (Input Validation Isn't Enough):**
            ```java
            // Even with validation, this is STILL vulnerable!
            public List<Product> searchByName(String name) {
                // Input validated: only alphanumeric + spaces
                // But validator might have edge cases!

                String sql = "SELECT * FROM products WHERE name LIKE '%" + name + "%'";
                return jdbcTemplate.query(sql, productMapper);

                // Attack that bypasses simple validation:
                // name = "test%' UNION SELECT * FROM users WHERE ''='"
                // If validator allows %, this works!
            }
            ```

            **✅ Secure (Parameterized Query):**
            ```java
            public List<Product> searchByName(String name) {
                // Parameterized query - SQL injection impossible
                String sql = "SELECT * FROM products WHERE name LIKE ?";
                return jdbcTemplate.query(sql, productMapper, "%" + name + "%");

                // Even if name = "' OR '1'='1", it's treated as literal string
                // Query becomes: WHERE name LIKE '%' OR '1'='1%'
                // Searches for products literally named "' OR '1'='1"
            }
            ```

            **Even Better (Spring Data JPA):**
            ```java
            public interface ProductRepository extends JpaRepository<Product, Long> {
                // Automatically parameterized - safe by design
                List<Product> findByNameContaining(String name);
            }
            ```

            **3. XSS Prevention on Output**

            **The Attack:**
            ```
            User searches for: <script>alert('XSS')</script>
            Server stores this in database
            Later, website displays: "Search results for: <script>alert('XSS')</script>"
            Script executes in other users' browsers!
            ```

            **Frontend Prevention:**
            ```javascript
            // ❌ Vulnerable to XSS
            searchResults.innerHTML = `Search results for: ${userInput}`;

            // ✅ Safe - no HTML parsing
            searchResults.textContent = `Search results for: ${userInput}`;

            // Or sanitize if HTML needed
            searchResults.innerHTML = DOMPurify.sanitize(userInput);
            ```

            **Backend Prevention:**
            ```java
            @GetMapping("/search")
            public ResponseEntity<SearchResultDTO> search(@RequestParam String name) {
                List<ProductDTO> products = productService.searchByName(name);

                // Encode for HTML output
                SearchResultDTO result = SearchResultDTO.builder()
                    .query(HtmlUtils.htmlEscape(name))  // Prevents XSS
                    .products(products)
                    .build();

                return ResponseEntity.ok(result);
            }
            ```

            **Content Security Policy (Defense in Depth):**
            ```java
            http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self'")
                )
            );
            ```

            **Why "All of the Above" is Correct:**

            **Layered Security (Defense in Depth):**
            ```
            Layer 1: Input Validation
            - Rejects obviously malicious input
            - Prevents some attacks
            - NOT sufficient alone

            Layer 2: Parameterized Queries
            - Prevents SQL injection completely
            - Works even if validation bypassed
            - PRIMARY defense for database

            Layer 3: Output Encoding
            - Prevents XSS attacks
            - Protects even if malicious data stored
            - Necessary for user-facing output

            All three together = Comprehensive protection
            ```

            **Real Attack Examples:**

            **SQL Injection (Defeated by Parameterization):**
            ```
            Input: ' OR 1=1--
            Without parameterization: SELECT * FROM products WHERE name LIKE '%' OR 1=1--%'
            Result: Returns ALL products (authentication bypass)

            With parameterization: SELECT * FROM products WHERE name LIKE '%'' OR 1=1--%'
            Result: Searches for literal string "' OR 1=1--" (harmless)
            ```

            **XSS (Defeated by Output Encoding):**
            ```
            Input: <img src=x onerror=alert('XSS')>
            Without encoding: <div>Search: <img src=x onerror=alert('XSS')></div>
            Result: JavaScript executes (XSS attack)

            With encoding: <div>Search: &lt;img src=x onerror=alert('XSS')&gt;</div>
            Result: Displays as text (safe)
            ```

            **Complete Secure Implementation:**

            ```java
            // Controller - Input Validation
            @RestController
            @RequestMapping("/api/products")
            public class ProductController {

                @GetMapping("/search")
                public ResponseEntity<SearchResultDTO> search(
                    @Valid @RequestParam
                    @Size(min = 1, max = 100)
                    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$")
                    String name
                ) {
                    List<ProductDTO> products = productService.searchByName(name);

                    SearchResultDTO result = SearchResultDTO.builder()
                        .query(HtmlUtils.htmlEscape(name))  // XSS prevention
                        .products(products)
                        .build();

                    return ResponseEntity.ok(result);
                }
            }

            // Repository - SQL Injection Prevention
            public interface ProductRepository extends JpaRepository<Product, Long> {
                @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
                List<Product> searchByName(@Param("name") String name);
                // Parameterized - SQL injection impossible
            }
            ```

            Why individual answers are insufficient:

            A alone (client-side validation) is completely insufficient:
            ```javascript
            // Client-side validation is EASILY bypassed
            if (searchInput.length > 100) {
                alert("Too long!");
                return;  // User can just modify JavaScript!
            }

            // Attacker opens browser console:
            fetch('/api/products/search?name=' + 'x'.repeat(10000))
            // Bypasses client validation completely
            ```

            Client-side validation is UX, not security.

            B alone (server-side validation) is better but insufficient:
            ```java
            // Validates input length and characters
            @Size(min = 1, max = 100)
            @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$")

            // But what about:
            // - SQL injection with allowed characters
            // - NoSQL injection (if using MongoDB)
            // - LDAP injection
            // - Command injection
            // - Path traversal

            // Validation helps but isn't complete protection
            ```

            Validation is necessary but not sufficient.

            C alone (parameterized queries) prevents SQL injection but not other attacks:
            ```java
            // SQL injection: PREVENTED ✓
            String sql = "SELECT * FROM products WHERE name LIKE ?";

            // XSS: NOT prevented ✗
            // If attacker searches for: <script>alert('XSS')</script>
            // It's safely stored in database
            // But when displayed to users without encoding = XSS!

            // DoS: NOT prevented ✗
            // Attacker sends 10MB search string
            // Database tries to process
            // Application slows down or crashes
            ```

            **Security Checklist:**
            ```java
            public class SecureSearchEndpoint {

                // ✅ Input validation
                @Valid @RequestParam
                @Size(min = 1, max = 100)
                @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$")
                String name

                // ✅ Parameterized query
                @Query("... WHERE name LIKE :name")
                List<Product> search(@Param("name") String name)

                // ✅ Output encoding
                HtmlUtils.htmlEscape(name)

                // ✅ Rate limiting
                @RateLimiter(name = "search")

                // ✅ Logging
                log.info("Search query: {}", name)

                // ✅ CSP headers
                .contentSecurityPolicy("default-src 'self'")
            }
            ```

            **Key Takeaway:**

            Security requires defense in depth. No single technique prevents all attacks:
            - Validate input (reject bad data early)
            - Parameterize queries (prevent injection)
            - Encode output (prevent XSS)
            - Rate limit (prevent DoS)
            - Log activity (detect attacks)

            All layers together create robust security.
            """
        );

        question.addChoice("A", "Client-side JavaScript validation to prevent long strings or special characters");
        question.addChoice("B", "Server-side validation with @Size and @Pattern to restrict input format");
        question.addChoice("C", "Using parameterized queries or JPA repositories instead of raw SQL concatenation");
        question.addChoice("D", "All of the above - validation prevents various attacks, but parameterized queries are primary SQL injection defense");

        return question;
    }
}
