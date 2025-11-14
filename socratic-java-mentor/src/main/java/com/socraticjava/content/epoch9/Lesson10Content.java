package com.socraticjava.content.epoch9;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 9, Lesson 10: Implementing JWT Authentication and Authorization
 *
 * This lesson teaches modern JWT-based authentication for stateless REST APIs
 * using Spring Security 6 and Spring Boot 3.
 */
public class Lesson10Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Implementing JWT Authentication and Authorization",
            """
            # Implementing JWT Authentication and Authorization

            ## Why JWT Authentication Matters

            Traditional session-based authentication stores user information on the server.
            This creates problems for modern applications:

            - **Scalability**: Each server needs access to session storage
            - **Mobile apps**: Cookies don't work well in mobile environments
            - **Microservices**: Session sharing across services is complex
            - **Cross-domain**: Sessions don't easily cross domains

            JWT (JSON Web Token) solves these problems with **stateless authentication**.
            The token contains all the information needed to identify the user—no server-side
            storage required.

            ## The Analogy: Digital Passport vs Hotel Key Card

            **Session-based authentication** is like a hotel key card:
            - Hotel keeps record of which rooms you can access
            - Key card is useless without hotel's database
            - If hotel system goes down, you're locked out
            - Can't use key card at different hotel chains

            **JWT authentication** is like a digital passport:
            - Contains all your information and permissions
            - Can be verified anywhere without calling home
            - Cryptographically signed so it can't be forged
            - Works across different services and domains
            - No need to check with issuing authority every time

            ## Understanding JWT Structure

            A JWT consists of three parts separated by dots: `header.payload.signature`

            ### 1. Header
            ```json
            {
              "alg": "HS256",
              "typ": "JWT"
            }
            ```
            Defines the signing algorithm and token type.

            ### 2. Payload (Claims)
            ```json
            {
              "sub": "user@example.com",
              "name": "John Doe",
              "roles": ["USER", "ADMIN"],
              "iat": 1709564800,
              "exp": 1709565700
            }
            ```
            Contains user information and metadata:
            - `sub` (subject): User identifier
            - `iat` (issued at): When token was created
            - `exp` (expiration): When token expires
            - Custom claims: roles, permissions, etc.

            ### 3. Signature
            ```
            HMACSHA256(
              base64UrlEncode(header) + "." + base64UrlEncode(payload),
              secret-key
            )
            ```
            Cryptographically signs the token to prevent tampering.

            ### Complete JWT Example
            ```
            eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
            eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MDk1NjQ4MDAsImV4cCI6MTcwOTU2NTcwMH0.
            xyz789signature
            ```

            ## Implementing JWT in Spring Boot 3

            ### Step 1: Add Dependencies

            Add to `pom.xml`:
            ```xml
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>0.12.5</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>0.12.5</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>0.12.5</version>
                <scope>runtime</scope>
            </dependency>
            ```

            ### Step 2: JWT Service

            Create a service to generate and validate tokens:

            ```java
            package com.example.security;

            import io.jsonwebtoken.Claims;
            import io.jsonwebtoken.Jwts;
            import io.jsonwebtoken.SignatureAlgorithm;
            import io.jsonwebtoken.security.Keys;
            import org.springframework.beans.factory.annotation.Value;
            import org.springframework.security.core.userdetails.UserDetails;
            import org.springframework.stereotype.Service;

            import javax.crypto.SecretKey;
            import java.util.Date;
            import java.util.HashMap;
            import java.util.Map;
            import java.util.function.Function;

            @Service
            public class JwtService {

                // Generate a secure key - in production, store in environment variable
                private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

                @Value("${jwt.expiration:900000}") // 15 minutes default
                private long jwtExpiration;

                @Value("${jwt.refresh-expiration:604800000}") // 7 days default
                private long refreshExpiration;

                /**
                 * Generate JWT token for user
                 */
                public String generateToken(UserDetails userDetails) {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("roles", userDetails.getAuthorities());
                    return createToken(claims, userDetails.getUsername(), jwtExpiration);
                }

                /**
                 * Generate refresh token
                 */
                public String generateRefreshToken(UserDetails userDetails) {
                    return createToken(new HashMap<>(), userDetails.getUsername(), refreshExpiration);
                }

                /**
                 * Create token with claims
                 */
                private String createToken(Map<String, Object> claims, String subject, long expiration) {
                    Date now = new Date();
                    Date expiryDate = new Date(now.getTime() + expiration);

                    return Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(now)
                        .setExpiration(expiryDate)
                        .signWith(secretKey)
                        .compact();
                }

                /**
                 * Extract username from token
                 */
                public String extractUsername(String token) {
                    return extractClaim(token, Claims::getSubject);
                }

                /**
                 * Extract expiration date from token
                 */
                public Date extractExpiration(String token) {
                    return extractClaim(token, Claims::getExpiration);
                }

                /**
                 * Extract specific claim from token
                 */
                public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                    final Claims claims = extractAllClaims(token);
                    return claimsResolver.apply(claims);
                }

                /**
                 * Extract all claims from token
                 */
                private Claims extractAllClaims(String token) {
                    return Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                }

                /**
                 * Check if token is expired
                 */
                private Boolean isTokenExpired(String token) {
                    return extractExpiration(token).before(new Date());
                }

                /**
                 * Validate token against user details
                 */
                public Boolean validateToken(String token, UserDetails userDetails) {
                    final String username = extractUsername(token);
                    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
                }
            }
            ```

            ### Step 3: JWT Authentication Filter

            Create a filter to intercept requests and validate JWT tokens:

            ```java
            package com.example.security;

            import jakarta.servlet.FilterChain;
            import jakarta.servlet.ServletException;
            import jakarta.servlet.http.HttpServletRequest;
            import jakarta.servlet.http.HttpServletResponse;
            import lombok.RequiredArgsConstructor;
            import org.springframework.lang.NonNull;
            import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
            import org.springframework.security.core.context.SecurityContextHolder;
            import org.springframework.security.core.userdetails.UserDetails;
            import org.springframework.security.core.userdetails.UserDetailsService;
            import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
            import org.springframework.stereotype.Component;
            import org.springframework.web.filter.OncePerRequestFilter;

            import java.io.IOException;

            @Component
            @RequiredArgsConstructor
            public class JwtAuthenticationFilter extends OncePerRequestFilter {

                private final JwtService jwtService;
                private final UserDetailsService userDetailsService;

                @Override
                protected void doFilterInternal(
                    @NonNull HttpServletRequest request,
                    @NonNull HttpServletResponse response,
                    @NonNull FilterChain filterChain
                ) throws ServletException, IOException {

                    // Extract Authorization header
                    final String authHeader = request.getHeader("Authorization");

                    // Check if header exists and starts with "Bearer "
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // Extract JWT token (remove "Bearer " prefix)
                    final String jwt = authHeader.substring(7);
                    final String username;

                    try {
                        // Extract username from token
                        username = jwtService.extractUsername(jwt);

                        // If username exists and user is not already authenticated
                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                            // Load user details from database
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            // Validate token
                            if (jwtService.validateToken(jwt, userDetails)) {
                                // Create authentication object
                                UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                    );

                                // Set additional details
                                authToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                                );

                                // Set authentication in security context
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                            }
                        }
                    } catch (Exception e) {
                        // Log error but don't stop the filter chain
                        logger.error("Cannot set user authentication: {}", e);
                    }

                    // Continue with the filter chain
                    filterChain.doFilter(request, response);
                }
            }
            ```

            **Key Points:**
            - Extends `OncePerRequestFilter` to ensure it runs once per request
            - Extracts token from `Authorization: Bearer <token>` header
            - Validates token and sets authentication in SecurityContext
            - Gracefully handles errors without blocking the request

            ### Step 4: Update Security Configuration

            Configure Spring Security to use JWT filter:

            ```java
            package com.example.config;

            import com.example.security.JwtAuthenticationFilter;
            import lombok.RequiredArgsConstructor;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.security.authentication.AuthenticationManager;
            import org.springframework.security.authentication.AuthenticationProvider;
            import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
            import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
            import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
            import org.springframework.security.config.annotation.web.builders.HttpSecurity;
            import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
            import org.springframework.security.config.http.SessionCreationPolicy;
            import org.springframework.security.core.userdetails.UserDetailsService;
            import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
            import org.springframework.security.crypto.password.PasswordEncoder;
            import org.springframework.security.web.SecurityFilterChain;
            import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

            @Configuration
            @EnableWebSecurity
            @EnableMethodSecurity // Enable @PreAuthorize, @PostAuthorize annotations
            @RequiredArgsConstructor
            public class SecurityConfig {

                private final JwtAuthenticationFilter jwtAuthFilter;
                private final UserDetailsService userDetailsService;

                @Bean
                public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                    http
                        .csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(auth -> auth
                            // Public endpoints
                            .requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/api/public/**").permitAll()
                            // Protected endpoints
                            .anyRequest().authenticated()
                        )
                        .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                    return http.build();
                }

                @Bean
                public AuthenticationProvider authenticationProvider() {
                    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                    authProvider.setUserDetailsService(userDetailsService);
                    authProvider.setPasswordEncoder(passwordEncoder());
                    return authProvider;
                }

                @Bean
                public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
                    throws Exception {
                    return config.getAuthenticationManager();
                }

                @Bean
                public PasswordEncoder passwordEncoder() {
                    return new BCryptPasswordEncoder();
                }
            }
            ```

            **Critical Configuration:**
            - `SessionCreationPolicy.STATELESS`: No sessions created
            - JWT filter added before `UsernamePasswordAuthenticationFilter`
            - Public endpoints allow unauthenticated access
            - `@EnableMethodSecurity` enables method-level security

            ### Step 5: Authentication Controller

            Create endpoints for login and token refresh:

            ```java
            package com.example.controller;

            import com.example.dto.AuthenticationRequest;
            import com.example.dto.AuthenticationResponse;
            import com.example.dto.RefreshTokenRequest;
            import com.example.security.JwtService;
            import lombok.RequiredArgsConstructor;
            import org.springframework.http.ResponseEntity;
            import org.springframework.security.authentication.AuthenticationManager;
            import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
            import org.springframework.security.core.userdetails.UserDetails;
            import org.springframework.security.core.userdetails.UserDetailsService;
            import org.springframework.web.bind.annotation.*;

            @RestController
            @RequestMapping("/api/auth")
            @RequiredArgsConstructor
            public class AuthenticationController {

                private final AuthenticationManager authenticationManager;
                private final JwtService jwtService;
                private final UserDetailsService userDetailsService;

                @PostMapping("/login")
                public ResponseEntity<AuthenticationResponse> authenticate(
                    @RequestBody AuthenticationRequest request
                ) {
                    // Authenticate user
                    authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                        )
                    );

                    // Load user details
                    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

                    // Generate tokens
                    String accessToken = jwtService.generateToken(userDetails);
                    String refreshToken = jwtService.generateRefreshToken(userDetails);

                    return ResponseEntity.ok(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tokenType("Bearer")
                        .expiresIn(900) // 15 minutes in seconds
                        .build());
                }

                @PostMapping("/refresh")
                public ResponseEntity<AuthenticationResponse> refreshToken(
                    @RequestBody RefreshTokenRequest request
                ) {
                    // Extract username from refresh token
                    String username = jwtService.extractUsername(request.getRefreshToken());

                    // Load user details
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Validate refresh token
                    if (!jwtService.validateToken(request.getRefreshToken(), userDetails)) {
                        return ResponseEntity.status(401).build();
                    }

                    // Generate new access token
                    String accessToken = jwtService.generateToken(userDetails);

                    return ResponseEntity.ok(AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(request.getRefreshToken())
                        .tokenType("Bearer")
                        .expiresIn(900)
                        .build());
                }
            }
            ```

            ### Step 6: DTOs

            ```java
            package com.example.dto;

            import lombok.Data;

            @Data
            public class AuthenticationRequest {
                private String email;
                private String password;
            }

            @Data
            @Builder
            public class AuthenticationResponse {
                private String accessToken;
                private String refreshToken;
                private String tokenType;
                private int expiresIn;
            }

            @Data
            public class RefreshTokenRequest {
                private String refreshToken;
            }
            ```

            ### Step 7: Method-Level Security

            Use `@PreAuthorize` for fine-grained access control:

            ```java
            package com.example.controller;

            import org.springframework.security.access.prepost.PreAuthorize;
            import org.springframework.web.bind.annotation.*;

            @RestController
            @RequestMapping("/api/admin")
            public class AdminController {

                @GetMapping("/users")
                @PreAuthorize("hasRole('ADMIN')")
                public ResponseEntity<List<User>> getAllUsers() {
                    // Only accessible to users with ADMIN role
                    return ResponseEntity.ok(userService.getAllUsers());
                }

                @DeleteMapping("/users/{id}")
                @PreAuthorize("hasRole('ADMIN') and #id != authentication.principal.id")
                public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
                    // Admin can delete users, but not themselves
                    userService.deleteUser(id);
                    return ResponseEntity.noContent().build();
                }

                @GetMapping("/users/{id}")
                @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
                public ResponseEntity<User> getUser(@PathVariable Long id) {
                    // Admin or the user themselves can view
                    return ResponseEntity.ok(userService.getUser(id));
                }
            }
            ```

            ## Frontend Integration

            ### Storing and Using JWT

            ```javascript
            // auth.js
            class AuthService {
                constructor() {
                    this.accessToken = localStorage.getItem('access_token');
                    this.refreshToken = localStorage.getItem('refresh_token');
                }

                async login(email, password) {
                    const response = await fetch('/api/auth/login', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ email, password })
                    });

                    if (!response.ok) throw new Error('Login failed');

                    const data = await response.json();
                    this.accessToken = data.accessToken;
                    this.refreshToken = data.refreshToken;

                    localStorage.setItem('access_token', data.accessToken);
                    localStorage.setItem('refresh_token', data.refreshToken);

                    return data;
                }

                async apiRequest(url, options = {}) {
                    // Add JWT to request
                    const config = {
                        ...options,
                        headers: {
                            ...options.headers,
                            'Authorization': `Bearer ${this.accessToken}`
                        }
                    };

                    let response = await fetch(url, config);

                    // If 401, try refreshing token
                    if (response.status === 401) {
                        const refreshed = await this.refreshAccessToken();
                        if (refreshed) {
                            // Retry request with new token
                            config.headers['Authorization'] = `Bearer ${this.accessToken}`;
                            response = await fetch(url, config);
                        } else {
                            // Refresh failed, redirect to login
                            this.logout();
                            window.location.href = '/login.html';
                        }
                    }

                    return response;
                }

                async refreshAccessToken() {
                    try {
                        const response = await fetch('/api/auth/refresh', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ refreshToken: this.refreshToken })
                        });

                        if (!response.ok) return false;

                        const data = await response.json();
                        this.accessToken = data.accessToken;
                        localStorage.setItem('access_token', data.accessToken);

                        return true;
                    } catch (error) {
                        return false;
                    }
                }

                logout() {
                    this.accessToken = null;
                    this.refreshToken = null;
                    localStorage.removeItem('access_token');
                    localStorage.removeItem('refresh_token');
                }

                isAuthenticated() {
                    return !!this.accessToken;
                }
            }

            const authService = new AuthService();
            export default authService;
            ```

            ### Using the Auth Service

            ```javascript
            // tasks.js
            import authService from './auth.js';

            async function loadTasks() {
                try {
                    const response = await authService.apiRequest('/api/tasks');
                    const tasks = await response.json();
                    displayTasks(tasks);
                } catch (error) {
                    console.error('Failed to load tasks:', error);
                }
            }

            async function createTask(taskData) {
                try {
                    const response = await authService.apiRequest('/api/tasks', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(taskData)
                    });

                    const task = await response.json();
                    return task;
                } catch (error) {
                    console.error('Failed to create task:', error);
                    throw error;
                }
            }
            ```

            ## Application Properties Configuration

            Add to `application.yml`:

            ```yaml
            jwt:
              expiration: 900000        # 15 minutes in milliseconds
              refresh-expiration: 604800000  # 7 days in milliseconds

            # For production, use environment variable:
            # JWT_SECRET_KEY should be a 256-bit key
            ```

            For production, generate and use a secure secret key:

            ```bash
            # Generate a secure key
            openssl rand -base64 32
            ```

            Then set as environment variable:
            ```bash
            export JWT_SECRET_KEY=your-generated-key-here
            ```

            ## Common Mistakes to Avoid

            ### ❌ Storing Sensitive Data in JWT

            **Wrong:**
            ```java
            claims.put("password", user.getPassword());
            claims.put("creditCard", user.getCreditCard());
            ```

            **Why:** JWT payload is Base64-encoded, not encrypted. Anyone can decode and read it.

            **Right:**
            ```java
            claims.put("userId", user.getId());
            claims.put("roles", user.getRoles());
            // Only include necessary, non-sensitive information
            ```

            ### ❌ Long Token Expiration

            **Wrong:**
            ```java
            @Value("${jwt.expiration:2592000000}") // 30 days!
            private long jwtExpiration;
            ```

            **Why:** If token is stolen, attacker has access for 30 days.

            **Right:**
            ```java
            @Value("${jwt.expiration:900000}") // 15 minutes
            private long jwtExpiration;

            @Value("${jwt.refresh-expiration:604800000}") // 7 days
            private long refreshExpiration;
            ```
            Use short-lived access tokens with longer-lived refresh tokens.

            ### ❌ Not Validating Token Signature

            **Wrong:**
            ```java
            public String extractUsername(String token) {
                // Directly decoding without verification
                String[] parts = token.split("\\.");
                String payload = new String(Base64.getDecoder().decode(parts[1]));
                // Parse and return username
            }
            ```

            **Why:** Attacker can modify the token payload to impersonate any user.

            **Right:**
            ```java
            public String extractUsername(String token) {
                // jjwt library verifies signature automatically
                return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)  // Validates signature
                    .getBody()
                    .getSubject();
            }
            ```

            ### ❌ Using Weak Secret Keys

            **Wrong:**
            ```java
            private final String secretKey = "mySecret123";
            ```

            **Why:** Weak keys can be brute-forced.

            **Right:**
            ```java
            // Generate cryptographically secure key
            private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            // Or load from environment (for production)
            @Value("${jwt.secret}")
            private String secretKeyString;

            private SecretKey getSigningKey() {
                byte[] keyBytes = Base64.getDecoder().decode(secretKeyString);
                return Keys.hmacShaKeyFor(keyBytes);
            }
            ```

            ### ❌ Not Handling Token Refresh

            **Wrong:**
            ```java
            // When token expires, user must log in again
            if (isTokenExpired(token)) {
                throw new UnauthorizedException("Please log in again");
            }
            ```

            **Why:** Poor user experience - users get logged out every 15 minutes.

            **Right:**
            ```java
            // Implement refresh token endpoint
            @PostMapping("/refresh")
            public ResponseEntity<AuthenticationResponse> refreshToken(
                @RequestBody RefreshTokenRequest request
            ) {
                String username = jwtService.extractUsername(request.getRefreshToken());
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(request.getRefreshToken(), userDetails)) {
                    String newAccessToken = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthenticationResponse(newAccessToken));
                }

                return ResponseEntity.status(401).build();
            }
            ```

            ## Best Practices for JWT Security

            ### 1. Use HTTPS Only

            Always transmit JWTs over HTTPS to prevent interception:

            ```yaml
            server:
              ssl:
                enabled: true
                key-store: classpath:keystore.p12
                key-store-password: ${KEYSTORE_PASSWORD}
                key-store-type: PKCS12
            ```

            ### 2. Implement Token Blacklisting

            For logout and security incidents, implement token revocation:

            ```java
            @Service
            public class TokenBlacklistService {

                private final RedisTemplate<String, String> redisTemplate;

                public void blacklistToken(String token, long expirationTime) {
                    redisTemplate.opsForValue().set(
                        "blacklist:" + token,
                        "revoked",
                        expirationTime,
                        TimeUnit.MILLISECONDS
                    );
                }

                public boolean isBlacklisted(String token) {
                    return redisTemplate.hasKey("blacklist:" + token);
                }
            }
            ```

            ### 3. Add Token Fingerprinting

            Bind token to user's device/IP for additional security:

            ```java
            public String generateToken(UserDetails userDetails, HttpServletRequest request) {
                Map<String, Object> claims = new HashMap<>();

                // Create fingerprint from user agent and IP
                String userAgent = request.getHeader("User-Agent");
                String ipAddress = request.getRemoteAddr();
                String fingerprint = DigestUtils.sha256Hex(userAgent + ipAddress);

                claims.put("fingerprint", fingerprint);
                claims.put("roles", userDetails.getAuthorities());

                return createToken(claims, userDetails.getUsername());
            }
            ```

            ### 4. Set Appropriate CORS Configuration

            ```java
            @Configuration
            public class CorsConfig implements WebMvcConfigurer {

                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/api/**")
                        .allowedOrigins("https://yourdomain.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Authorization", "Content-Type")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true)
                        .maxAge(3600);
                }
            }
            ```

            ### 5. Implement Rate Limiting

            Protect authentication endpoints from brute force:

            ```java
            @Component
            public class RateLimitingFilter extends OncePerRequestFilter {

                private final RateLimiter rateLimiter;

                @Override
                protected void doFilterInternal(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    FilterChain filterChain
                ) throws ServletException, IOException {

                    String clientId = request.getRemoteAddr();

                    if (request.getRequestURI().startsWith("/api/auth/")) {
                        if (!rateLimiter.tryAcquire(clientId)) {
                            response.setStatus(429); // Too Many Requests
                            response.getWriter().write("Rate limit exceeded");
                            return;
                        }
                    }

                    filterChain.doFilter(request, response);
                }
            }
            ```

            ## Testing JWT Authentication

            ### Unit Test for JWT Service

            ```java
            @ExtendWith(MockitoExtension.class)
            class JwtServiceTest {

                @InjectMocks
                private JwtService jwtService;

                private UserDetails userDetails;

                @BeforeEach
                void setUp() {
                    userDetails = User.builder()
                        .username("test@example.com")
                        .password("password")
                        .authorities("ROLE_USER")
                        .build();

                    // Set expiration via reflection for testing
                    ReflectionTestUtils.setField(jwtService, "jwtExpiration", 60000L);
                }

                @Test
                void shouldGenerateToken() {
                    String token = jwtService.generateToken(userDetails);

                    assertThat(token).isNotNull();
                    assertThat(token.split("\\.")).hasSize(3);
                }

                @Test
                void shouldExtractUsername() {
                    String token = jwtService.generateToken(userDetails);
                    String username = jwtService.extractUsername(token);

                    assertThat(username).isEqualTo("test@example.com");
                }

                @Test
                void shouldValidateToken() {
                    String token = jwtService.generateToken(userDetails);
                    boolean isValid = jwtService.validateToken(token, userDetails);

                    assertThat(isValid).isTrue();
                }

                @Test
                void shouldRejectExpiredToken() throws InterruptedException {
                    // Set very short expiration
                    ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);

                    String token = jwtService.generateToken(userDetails);
                    Thread.sleep(10); // Wait for token to expire

                    boolean isValid = jwtService.validateToken(token, userDetails);
                    assertThat(isValid).isFalse();
                }
            }
            ```

            ### Integration Test for Authentication

            ```java
            @SpringBootTest
            @AutoConfigureMockMvc
            class AuthenticationControllerTest {

                @Autowired
                private MockMvc mockMvc;

                @Autowired
                private ObjectMapper objectMapper;

                @Test
                void shouldAuthenticateAndReturnToken() throws Exception {
                    AuthenticationRequest request = new AuthenticationRequest();
                    request.setEmail("user@example.com");
                    request.setPassword("password123");

                    mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.accessToken").exists())
                        .andExpect(jsonPath("$.refreshToken").exists())
                        .andExpect(jsonPath("$.tokenType").value("Bearer"))
                        .andExpect(jsonPath("$.expiresIn").value(900));
                }

                @Test
                void shouldAccessProtectedEndpointWithValidToken() throws Exception {
                    // First, get token
                    String token = getValidToken();

                    // Then, access protected endpoint
                    mockMvc.perform(get("/api/tasks")
                            .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
                }

                @Test
                void shouldRejectInvalidToken() throws Exception {
                    mockMvc.perform(get("/api/tasks")
                            .header("Authorization", "Bearer invalid-token"))
                        .andExpect(status().isUnauthorized());
                }
            }
            ```

            ## Summary

            JWT authentication provides a modern, stateless approach to securing REST APIs:

            **Core Concepts:**
            - JWTs are self-contained tokens with header, payload, and signature
            - Stateless authentication eliminates server-side session storage
            - Token-based auth scales better for distributed systems

            **Implementation Steps:**
            1. Add jjwt dependencies
            2. Create JwtService for token generation and validation
            3. Implement JwtAuthenticationFilter extending OncePerRequestFilter
            4. Configure Security with stateless session management
            5. Create authentication endpoints (login, refresh)
            6. Use @PreAuthorize for method-level security

            **Security Best Practices:**
            - Use short-lived access tokens (15 minutes)
            - Implement refresh token rotation
            - Never store sensitive data in JWT payload
            - Always transmit tokens over HTTPS
            - Validate token signature on every request
            - Implement rate limiting on auth endpoints
            - Consider token blacklisting for logout

            **Frontend Integration:**
            - Add Authorization header to all protected requests
            - Implement automatic token refresh before expiration
            - Handle 401 errors by refreshing or redirecting to login

            **Token Storage: localStorage vs httpOnly Cookies**

            CRITICAL SECURITY DECISION: Where to store JWT tokens?

            **Option 1: localStorage (Common but Vulnerable to XSS)**
            ```javascript
            // Store token
            localStorage.setItem('access_token', token);

            // Send with requests
            fetch('/api/tasks', {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('access_token')}`
                }
            });
            ```

            ✅ Advantages:
            - Easy to implement
            - Works with CORS (cross-domain requests)
            - JavaScript has full access to token

            ❌ Disadvantages:
            - VULNERABLE TO XSS ATTACKS!
            - Any JavaScript code can read localStorage
            - If attacker injects <script>, they can steal tokens
            - Common attack: console.log(localStorage.getItem('access_token'))

            **Option 2: httpOnly Cookies (More Secure)**
            ```java
            // Backend sets cookie
            @PostMapping("/login")
            public ResponseEntity<?> login(@RequestBody LoginRequest request,
                                          HttpServletResponse response) {
                String token = jwtService.generateToken(user);

                // Create httpOnly cookie
                Cookie cookie = new Cookie("access_token", token);
                cookie.setHttpOnly(true);  // JavaScript CANNOT access this!
                cookie.setSecure(true);    // Only sent over HTTPS
                cookie.setPath("/");
                cookie.setMaxAge(900);     // 15 minutes

                response.addCookie(cookie);
                return ResponseEntity.ok(new LoginResponse(user.getEmail()));
            }
            ```

            ```javascript
            // Frontend - browser automatically sends cookie
            fetch('/api/tasks', {
                credentials: 'include'  // Include cookies in request
            });
            ```

            ✅ Advantages:
            - PROTECTED FROM XSS! JavaScript cannot access httpOnly cookies
            - Browser automatically handles cookie with each request
            - More secure by default

            ❌ Disadvantages:
            - VULNERABLE TO CSRF ATTACKS
            - Requires CSRF protection (CSRF tokens or SameSite attribute)
            - Doesn't work well with cross-domain requests (CORS)

            **RECOMMENDATION:**
            If frontend is on same domain as backend (e.g., www.myapp.com):
            → Use httpOnly cookies + SameSite=Strict + CSRF protection

            If frontend is on different domain (e.g., app.myapp.com + api.myapp.com):
            → Use localStorage + strict Content Security Policy (CSP) to prevent XSS

            **Best Practice for Production:**
            ```java
            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);   // Prevent XSS
            cookie.setSecure(true);     // HTTPS only
            cookie.setSameSite("Strict"); // Prevent CSRF
            cookie.setPath("/");
            ```

            This lesson uses localStorage for simplicity in learning, but httpOnly
            cookies are generally recommended for production when frontend and backend
            share the same domain.

            JWT authentication is now the industry standard for REST APIs and SPAs,
            providing security, scalability, and flexibility for modern applications.
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
            "Which of the following statements about JWT authentication is MOST accurate?",
            "B",
            """
            Understanding the fundamental difference between JWT and session-based authentication
            is critical for choosing the right approach and implementing it correctly.

            The correct answer is B. JWTs are stateless and self-contained, meaning all the information
            needed to identify and authenticate the user is contained within the token itself. The server
            doesn't need to query a database or session store on every request - it just validates the
            signature. This is the primary advantage of JWT authentication.

            Why other answers are incorrect:

            A is wrong because JWTs are NOT more secure by default. In fact, they introduce new
            security considerations like token storage, XSS attacks, and the inability to immediately
            revoke tokens. Security depends on implementation, not the authentication method.

            C is wrong because JWTs are NOT encrypted by default - they're Base64-encoded, which
            means anyone can decode and read the payload. You should never store sensitive data in
            a JWT. If encryption is needed, you must use JWE (JSON Web Encryption) explicitly.

            D is wrong because JWTs have significant security implications. Stolen tokens grant
            access until expiration, tokens can't be easily revoked, and improper implementation
            can lead to vulnerabilities like weak signing keys or accepting unsigned tokens.

            Key takeaway: JWT's main advantage is statelessness, not security. Security requires
            careful implementation with short expiration times, HTTPS, secure storage, and proper
            validation.
            """
        );

        question.addChoice("A", "JWTs are more secure than session-based authentication because they're stored on the client side");
        question.addChoice("B", "JWTs are stateless and self-contained, eliminating the need for server-side session storage");
        question.addChoice("C", "JWTs are encrypted by default, making them safe to store sensitive information like passwords");
        question.addChoice("D", "JWTs don't require HTTPS because the token signature provides sufficient security");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "Your JWT implementation is experiencing security issues. Users report that they can modify their JWT payload to grant themselves admin privileges. What is the MOST LIKELY cause?",
            "C",
            """
            This question tests understanding of JWT validation and the critical importance of
            signature verification in maintaining token integrity.

            The correct answer is C. The application is not properly validating the JWT signature
            before trusting the payload. An attacker can easily modify a Base64-encoded JWT payload
            (changing roles from USER to ADMIN), but they cannot create a valid signature without
            the secret key. If the application doesn't verify the signature, it will accept the
            modified token as valid.

            This is a critical security vulnerability:
            ```java
            // VULNERABLE CODE
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getDecoder().decode(parts[1]));
            // Using payload without verifying signature!

            // SECURE CODE
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)  // This validates the signature!
                .getBody();
            ```

            Why other answers are incorrect:

            A is wrong because token expiration time doesn't affect the ability to modify payloads.
            Even with a short expiration, an attacker can modify a valid token and use it until
            it expires if signatures aren't verified.

            B is wrong because storing tokens in localStorage vs cookies doesn't prevent payload
            modification. Both storage methods are vulnerable to XSS attacks, but neither makes
            it easier or harder to modify the token itself.

            D is wrong because HS256 (HMAC-SHA256) is a perfectly adequate signing algorithm for
            most applications. While RS256 (RSA) is sometimes preferred for certain use cases
            (like when public key distribution is needed), it wouldn't prevent this specific
            vulnerability. The problem is lack of validation, not choice of algorithm.

            Prevention checklist:
            ✓ Always use a JWT library (like jjwt) that validates signatures automatically
            ✓ Never manually decode and trust JWT payloads without verification
            ✓ Use cryptographically secure secret keys (minimum 256 bits for HS256)
            ✓ Test that your application rejects modified tokens
            ✓ Consider adding additional claims like token fingerprinting for defense in depth
            """
        );

        question.addChoice("A", "The JWT expiration time is set too long, allowing attackers time to modify tokens");
        question.addChoice("B", "The tokens are being stored in localStorage instead of httpOnly cookies");
        question.addChoice("C", "The application is not validating the JWT signature before trusting the payload");
        question.addChoice("D", "The secret key algorithm (HS256) is too weak and should be upgraded to RS256");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "You need to implement logout functionality in your JWT-based application. What is the BEST approach considering both security and user experience?",
            "D",
            """
            Implementing logout in stateless JWT systems is challenging because tokens remain valid
            until expiration. This question tests understanding of the tradeoffs between different
            logout strategies.

            The correct answer is D. Using short-lived access tokens (15 minutes) with longer-lived
            refresh tokens, and blacklisting the refresh token on logout, provides the best balance.

            Here's why this is optimal:

            1. Short access token lifespan (15 min) limits the window of vulnerability after logout
            2. Refresh token blacklisting provides true logout capability
            3. Most requests use access tokens (stateless), only refresh requests hit the blacklist
            4. User experience remains good with automatic token refresh

            Implementation:
            ```java
            @PostMapping("/logout")
            public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
                // Blacklist the refresh token
                tokenBlacklistService.blacklistToken(
                    request.getRefreshToken(),
                    refreshTokenExpiration
                );

                // Access token expires naturally in 15 minutes
                return ResponseEntity.noContent().build();
            }
            ```

            Why other answers are suboptimal:

            A is not ideal because maintaining a blacklist for all tokens defeats the purpose of
            stateless authentication. Every request would need to check the blacklist, which
            requires database/cache access and eliminates the scalability benefits of JWT.
            Additionally, the blacklist grows indefinitely until tokens expire.

            B is inadequate because client-side token deletion doesn't actually invalidate the token.
            If an attacker has copied the token (via XSS, network interception, or other means),
            they can continue using it until it expires. This provides a false sense of security.

            C is problematic because extremely short token lifespans (1-2 minutes) create poor UX.
            Users would need to refresh tokens very frequently, and any network issues during
            refresh could log them out unexpectedly. It also increases server load with constant
            refresh requests. The security benefit is minimal compared to 15-minute tokens with
            proper refresh token blacklisting.

            Architecture considerations:

            For the blacklist, use Redis or similar:
            ```java
            public void blacklistToken(String token, long ttl) {
                redisTemplate.opsForValue().set(
                    "blacklist:" + token,
                    "revoked",
                    ttl,
                    TimeUnit.MILLISECONDS
                );
            }
            ```

            The TTL ensures blacklist entries automatically expire when the token would have
            expired anyway, preventing unlimited growth.

            Additional security measures:
            - Implement refresh token rotation (new refresh token with each refresh)
            - Limit refresh tokens to specific devices/IPs
            - Implement suspicious activity detection
            - Consider token families for detecting token reuse
            """
        );

        question.addChoice("A", "Maintain a blacklist of all logged-out tokens in a database or Redis cache");
        question.addChoice("B", "Simply delete the token from client-side storage (localStorage or cookies)");
        question.addChoice("C", "Set token expiration to 1-2 minutes so tokens become invalid quickly after logout");
        question.addChoice("D", "Use short-lived access tokens (15 min) with refresh tokens, blacklist the refresh token on logout");

        return question;
    }
}
