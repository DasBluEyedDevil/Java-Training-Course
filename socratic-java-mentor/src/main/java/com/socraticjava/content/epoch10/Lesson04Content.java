package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Epoch 10, Lesson 4: Caching Strategies with Spring Cache and Redis
 *
 * This lesson teaches professional caching strategies using Spring Cache
 * abstraction and Redis for high-performance applications.
 */
public class Lesson04Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Caching Strategies with Spring Cache and Redis",
            """
            # Caching Strategies with Spring Cache and Redis

            ## Why Caching Matters

            Imagine calling customer support every time you need your account number.
            The first time, they look it up (slow). The second time... they look it up
            again (still slow). The hundredth time... still looking it up!

            That's what applications do without caching. They repeatedly fetch the same
            data from slow sources (databases, external APIs) even though it hasn't changed.

            **Without caching:**
            - Every request hits the database
            - Response times: 200-500ms per request
            - Database gets overwhelmed under load
            - Expensive database resources wasted on repetitive queries
            - Application doesn't scale beyond a few hundred users

            **With caching:**
            - First request hits database (200ms)
            - Subsequent requests hit cache (2ms) - **100x faster!**
            - Database load reduced by 80-95%
            - Can handle 10x more users with same resources
            - Significant cost savings on infrastructure

            ## The Analogy: Library Desk Copy

            **Without cache (bad library):**
            - Librarian walks to archive for every book request
            - Even if 50 people want the same popular book
            - Each walk takes 5 minutes
            - Serves 12 people per hour

            **With cache (good library):**
            - Keeps popular books at front desk
            - First request: walk to archive (5 minutes)
            - Stores book at desk
            - Next 49 requests: grab from desk (5 seconds)
            - Serves 50 people in same time!

            Caching is keeping frequently accessed data "at the front desk."

            ## Understanding Cache-Aside Pattern

            The most common caching pattern in Spring applications:

            ```
            1. Application receives request
            2. Check cache - is data there?
               ├─ YES → Return cached data (fast! ✅)
               └─ NO  → Continue to step 3
            3. Fetch data from database (slow)
            4. Store data in cache for next time
            5. Return data to user
            ```

            **First Request (Cache Miss):**
            ```
            GET /api/users/123
            → Check cache: not found
            → Query database: SELECT * FROM users WHERE id = 123 (200ms)
            → Store in cache: users:123 = {user data}
            → Return to client
            Total: 200ms
            ```

            **Subsequent Requests (Cache Hit):**
            ```
            GET /api/users/123
            → Check cache: FOUND! ✅
            → Return from cache (2ms)
            Total: 2ms (100x faster!)
            ```

            ## Setting Up Redis Cache in Spring Boot

            ### Step 1: Add Dependencies

            ```xml
            <dependencies>
                <!-- Spring Cache Abstraction -->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-cache</artifactId>
                </dependency>

                <!-- Redis Cache Implementation -->
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-data-redis</artifactId>
                </dependency>
            </dependencies>
            ```

            ### Step 2: Configure Redis

            `application.yml`:
            ```yaml
            spring:
              cache:
                type: redis
                cache-names: users, tasks, products
                redis:
                  time-to-live: 600000  # 10 minutes in milliseconds
                  cache-null-values: false
                  use-key-prefix: true
                  key-prefix: "myapp:"

              data:
                redis:
                  host: localhost
                  port: 6379
                  password: ${REDIS_PASSWORD:}
                  timeout: 2000ms
                  lettuce:
                    pool:
                      max-active: 8
                      max-idle: 8
                      min-idle: 2
            ```

            ### Step 3: Enable Caching

            ```java
            package com.example.config;

            import org.springframework.cache.annotation.EnableCaching;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;
            import org.springframework.data.redis.cache.RedisCacheConfiguration;
            import org.springframework.data.redis.cache.RedisCacheManager;
            import org.springframework.data.redis.connection.RedisConnectionFactory;
            import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
            import org.springframework.data.redis.serializer.RedisSerializationContext;
            import org.springframework.data.redis.serializer.StringRedisSerializer;

            import java.time.Duration;
            import java.util.HashMap;
            import java.util.Map;

            @Configuration
            @EnableCaching  // Enable Spring's cache management
            public class CacheConfig {

                @Bean
                public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
                    // Default cache configuration
                    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))  // Default TTL: 10 minutes
                        .serializeKeysWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()
                            )
                        )
                        .serializeValuesWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                            )
                        )
                        .disableCachingNullValues();  // Don't cache null values

                    // Per-cache configurations
                    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

                    // Users cache: 5 minutes TTL
                    cacheConfigurations.put("users",
                        defaultConfig.entryTtl(Duration.ofMinutes(5)));

                    // Tasks cache: 2 minutes TTL (more volatile data)
                    cacheConfigurations.put("tasks",
                        defaultConfig.entryTtl(Duration.ofMinutes(2)));

                    // Products cache: 1 hour TTL (stable data)
                    cacheConfigurations.put("products",
                        defaultConfig.entryTtl(Duration.ofHours(1)));

                    return RedisCacheManager.builder(connectionFactory)
                        .cacheDefaults(defaultConfig)
                        .withInitialCacheConfigurations(cacheConfigurations)
                        .build();
                }
            }
            ```

            ## Using @Cacheable

            ### Basic Caching

            ```java
            package com.example.service;

            import lombok.RequiredArgsConstructor;
            import lombok.extern.slf4j.Slf4j;
            import org.springframework.cache.annotation.Cacheable;
            import org.springframework.stereotype.Service;

            @Service
            @RequiredArgsConstructor
            @Slf4j
            public class UserService {

                private final UserRepository userRepository;

                /**
                 * Cache user by ID
                 * Key: users::123
                 * Value: User object (serialized to JSON)
                 */
                @Cacheable(value = "users", key = "#userId")
                public User getUserById(Long userId) {
                    log.info("Fetching user {} from database", userId);
                    return userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", userId));
                }
            }
            ```

            **How it works:**
            ```java
            // First call
            userService.getUserById(123L);
            // Logs: "Fetching user 123 from database"
            // Returns from DB, stores in cache
            // Cache key: "users::123"

            // Second call (within TTL)
            userService.getUserById(123L);
            // No log message! (method not executed)
            // Returns directly from cache

            // After TTL expires (5 minutes)
            userService.getUserById(123L);
            // Logs: "Fetching user 123 from database"
            // Cache was empty, refetches from DB
            ```

            ### Custom Cache Keys

            ```java
            @Service
            public class TaskService {

                /**
                 * Cache tasks by user ID and status
                 * Key: tasks::userId:123:status:PENDING
                 */
                @Cacheable(
                    value = "tasks",
                    key = "'userId:' + #userId + ':status:' + #status"
                )
                public List<Task> getTasksByUserAndStatus(Long userId, TaskStatus status) {
                    return taskRepository.findByUserIdAndStatus(userId, status);
                }

                /**
                 * Cache method result using all parameters
                 * Key: tasks::UserFilters(userId=123, status=PENDING, priority=HIGH)
                 */
                @Cacheable(value = "tasks", key = "#filters")
                public List<Task> searchTasks(UserFilters filters) {
                    return taskRepository.findAll(buildSpecification(filters));
                }
            }
            ```

            ### Conditional Caching

            ```java
            @Service
            public class ProductService {

                /**
                 * Only cache if product is active
                 */
                @Cacheable(
                    value = "products",
                    key = "#productId",
                    condition = "#result != null && #result.isActive()"
                )
                public Product getProduct(Long productId) {
                    return productRepository.findById(productId).orElse(null);
                }

                /**
                 * Cache unless result is empty
                 */
                @Cacheable(
                    value = "products",
                    key = "'category:' + #category",
                    unless = "#result.isEmpty()"
                )
                public List<Product> getProductsByCategory(String category) {
                    return productRepository.findByCategory(category);
                }
            }
            ```

            ## Cache Synchronization

            Prevent cache stampede (multiple threads fetching same data):

            ```java
            @Service
            public class UserService {

                /**
                 * sync = true: Only one thread fetches data, others wait
                 * Prevents multiple simultaneous database hits for same key
                 */
                @Cacheable(value = "users", key = "#userId", sync = true)
                public User getUserById(Long userId) {
                    log.info("Fetching user {} from database", userId);
                    return userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", userId));
                }
            }
            ```

            **Without sync:**
            ```
            Time  Thread1       Thread2       Thread3       Database
            0ms   Check cache   Check cache   Check cache
            1ms   Miss!         Miss!         Miss!
            2ms   Query DB  →   Query DB  →   Query DB  →   3 queries!
            ```

            **With sync:**
            ```
            Time  Thread1       Thread2       Thread3       Database
            0ms   Check cache   Check cache   Check cache
            1ms   Miss!         Wait...       Wait...
            2ms   Query DB  →                               1 query
            3ms   Store cache   Wake up       Wake up
            4ms   Return        Read cache    Read cache
            ```

            ## Updating Cache with @CachePut

            ```java
            @Service
            public class UserService {

                /**
                 * Update user AND update cache
                 * Method always executes, result updates cache
                 */
                @CachePut(value = "users", key = "#userId")
                public User updateUser(Long userId, UserUpdateRequest request) {
                    User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User", userId));

                    user.setName(request.getName());
                    user.setEmail(request.getEmail());

                    User updated = userRepository.save(user);
                    log.info("Updated user {} in database and cache", userId);

                    return updated;
                }
            }
            ```

            **Flow:**
            ```
            1. updateUser(123, request) called
            2. Method executes (always)
            3. Database updated
            4. Return value stored in cache (key: "users::123")
            5. Next getUserById(123) hits cache with fresh data
            ```

            ## Evicting Cache with @CacheEvict

            ### Evict Specific Entry

            ```java
            @Service
            public class UserService {

                /**
                 * Delete user and remove from cache
                 */
                @CacheEvict(value = "users", key = "#userId")
                public void deleteUser(Long userId) {
                    userRepository.deleteById(userId);
                    log.info("Deleted user {} from database and cache", userId);
                }
            }
            ```

            ### Evict All Entries

            ```java
            @Service
            public class TaskService {

                /**
                 * Clear all tasks from cache
                 * Use when bulk operations affect many cached items
                 */
                @CacheEvict(value = "tasks", allEntries = true)
                public void bulkUpdateTasks(List<TaskUpdateRequest> updates) {
                    updates.forEach(update -> {
                        Task task = taskRepository.findById(update.getTaskId())
                            .orElseThrow();
                        task.setStatus(update.getStatus());
                        taskRepository.save(task);
                    });
                    log.info("Bulk updated {} tasks, cleared entire cache", updates.size());
                }
            }
            ```

            ### Multiple Cache Operations

            ```java
            @Service
            public class UserService {

                /**
                 * Update user: evict from users cache, evict related tasks cache
                 */
                @Caching(
                    put = @CachePut(value = "users", key = "#userId"),
                    evict = @CacheEvict(value = "tasks", key = "'userId:' + #userId + ':*'")
                )
                public User updateUser(Long userId, UserUpdateRequest request) {
                    // Update logic
                }

                /**
                 * Delete user: evict from multiple caches
                 */
                @Caching(evict = {
                    @CacheEvict(value = "users", key = "#userId"),
                    @CacheEvict(value = "tasks", allEntries = true)
                })
                public void deleteUser(Long userId) {
                    userRepository.deleteById(userId);
                }
            }
            ```

            ## Common Caching Patterns

            ### Pattern 1: Read-Heavy Data

            ```java
            @Service
            public class CategoryService {

                /**
                 * Categories rarely change - cache aggressively
                 * TTL: 1 hour
                 */
                @Cacheable(value = "categories", sync = true)
                public List<Category> getAllCategories() {
                    return categoryRepository.findAll();
                }

                /**
                 * Clear cache when admin updates categories
                 */
                @CacheEvict(value = "categories", allEntries = true)
                public Category updateCategory(Long id, CategoryRequest request) {
                    Category category = categoryRepository.findById(id).orElseThrow();
                    category.setName(request.getName());
                    return categoryRepository.save(category);
                }
            }
            ```

            ### Pattern 2: User-Specific Data

            ```java
            @Service
            public class UserProfileService {

                /**
                 * Cache user profile (changes infrequently)
                 */
                @Cacheable(value = "userProfiles", key = "#userId")
                public UserProfile getProfile(Long userId) {
                    return profileRepository.findByUserId(userId)
                        .orElseThrow();
                }

                /**
                 * Update profile and refresh cache
                 */
                @CachePut(value = "userProfiles", key = "#userId")
                public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                    UserProfile profile = getProfile(userId);
                    // Update fields
                    return profileRepository.save(profile);
                }
            }
            ```

            ### Pattern 3: Computed/Expensive Operations

            ```java
            @Service
            public class AnalyticsService {

                /**
                 * Expensive aggregation - cache result
                 * Recompute every 5 minutes
                 */
                @Cacheable(value = "analytics", key = "'dashboard:' + #userId")
                public DashboardStats getDashboardStats(Long userId) {
                    log.info("Computing dashboard stats for user {}", userId);

                    // Expensive calculations
                    long totalTasks = taskRepository.countByUserId(userId);
                    long completedTasks = taskRepository.countByUserIdAndStatus(
                        userId, TaskStatus.COMPLETED
                    );
                    double completionRate = (double) completedTasks / totalTasks * 100;

                    // More complex calculations...

                    return DashboardStats.builder()
                        .totalTasks(totalTasks)
                        .completedTasks(completedTasks)
                        .completionRate(completionRate)
                        .build();
                }
            }
            ```

            ## Cache Invalidation Strategies

            ### Strategy 1: Time-Based (TTL)

            ```java
            // Set in configuration
            cacheConfigurations.put("products",
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(30))
            );
            ```

            **Pros:** Simple, automatic
            **Cons:** Data might be stale for up to 30 minutes

            ### Strategy 2: Event-Based

            ```java
            @Service
            @RequiredArgsConstructor
            public class ProductService {

                private final CacheManager cacheManager;

                @CachePut(value = "products", key = "#productId")
                public Product updateProduct(Long productId, ProductRequest request) {
                    Product product = productRepository.findById(productId).orElseThrow();
                    // Update product
                    Product updated = productRepository.save(product);

                    // Invalidate related caches
                    evictRelatedCaches(updated);

                    return updated;
                }

                private void evictRelatedCaches(Product product) {
                    // Clear category cache (affected by product update)
                    Cache categoryCache = cacheManager.getCache("categories");
                    if (categoryCache != null) {
                        categoryCache.evict("category:" + product.getCategoryId());
                    }
                }
            }
            ```

            ### Strategy 3: Write-Through

            ```java
            @Service
            public class UserService {

                /**
                 * Update database AND cache simultaneously
                 */
                @CachePut(value = "users", key = "#userId")
                @Transactional
                public User updateUser(Long userId, UserUpdateRequest request) {
                    User user = userRepository.findById(userId).orElseThrow();
                    user.setName(request.getName());
                    User updated = userRepository.save(user);

                    // Cache is automatically updated with return value
                    return updated;
                }
            }
            ```

            ## Common Mistakes to Avoid

            ### ❌ Caching Mutable Objects

            **Wrong:**
            ```java
            @Cacheable("users")
            public User getUser(Long id) {
                return userRepository.findById(id).orElseThrow();
            }

            // Elsewhere in code:
            User user = userService.getUser(123L);
            user.setEmail("new@email.com");  // Modifies cached object!

            User user2 = userService.getUser(123L);
            // user2 has wrong email! Cache is corrupted!
            ```

            **Right:**
            ```java
            @Cacheable("users")
            public UserDTO getUser(Long id) {
                User user = userRepository.findById(id).orElseThrow();
                return convertToDTO(user);  // Return immutable DTO
            }
            ```

            ### ❌ Not Specifying Keys

            **Wrong:**
            ```java
            @Cacheable("tasks")
            public List<Task> getTasks(Long userId, TaskStatus status) {
                // Key will be based on all parameters: userId + status
                // This might work, but is unpredictable
            }
            ```

            **Right:**
            ```java
            @Cacheable(value = "tasks", key = "'user:' + #userId + ':status:' + #status")
            public List<Task> getTasks(Long userId, TaskStatus status) {
                // Explicit, readable cache key
            }
            ```

            ### ❌ Forgetting to Evict on Updates

            **Wrong:**
            ```java
            @CachePut(value = "users", key = "#userId")
            public User updateUser(Long userId, UserUpdateRequest request) {
                // Updates user cache...
                User user = userRepository.findById(userId).orElseThrow();
                user.setName(request.getName());
                User updated = userRepository.save(user);

                // But forgets to clear related caches!
                // User's tasks still show old user name in cache
                return updated;
            }
            ```

            **Right:**
            ```java
            @Caching(
                put = @CachePut(value = "users", key = "#userId"),
                evict = {
                    @CacheEvict(value = "tasks", key = "'userId:' + #userId"),
                    @CacheEvict(value = "userProfiles", key = "#userId")
                }
            )
            public User updateUser(Long userId, UserUpdateRequest request) {
                // Updates all related caches
            }
            ```

            ### ❌ Caching Too Aggressively

            **Wrong:**
            ```java
            // Cache EVERYTHING for 1 hour!
            @Cacheable(value = "data")
            public AccountBalance getBalance(Long accountId) {
                return balanceRepository.findByAccountId(accountId);
            }
            ```

            **Why:** Account balance changes frequently. Showing stale balance is dangerous.

            **Right:**
            ```java
            // Don't cache rapidly changing financial data
            // OR use very short TTL (30 seconds) with sync
            @Cacheable(value = "balances", key = "#accountId", sync = true)
            public AccountBalance getBalance(Long accountId) {
                // TTL: 30 seconds (configured in CacheConfig)
                return balanceRepository.findByAccountId(accountId);
            }
            ```

            ## Monitoring Cache Performance

            ### Enable Cache Statistics

            ```java
            @Configuration
            public class CacheConfig {

                @Bean
                public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
                    return (builder) -> builder
                        .withCacheConfiguration("users",
                            RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .enableStatistics()  // Enable statistics
                        );
                }
            }
            ```

            ### Cache Metrics Endpoint

            ```yaml
            management:
              endpoints:
                web:
                  exposure:
                    include: metrics, health, caches
              metrics:
                cache:
                  instrument-cache: true
            ```

            Access metrics:
            ```
            GET /actuator/metrics/cache.gets?tag=name:users
            GET /actuator/metrics/cache.hits?tag=name:users
            GET /actuator/metrics/cache.misses?tag=name:users
            ```

            ### Custom Logging

            ```java
            @Aspect
            @Component
            @Slf4j
            public class CacheLoggingAspect {

                @Around("@annotation(cacheable)")
                public Object logCacheable(ProceedingJoinPoint joinPoint, Cacheable cacheable)
                    throws Throwable {

                    String methodName = joinPoint.getSignature().getName();
                    Object[] args = joinPoint.getArgs();

                    long start = System.currentTimeMillis();
                    Object result = joinPoint.proceed();
                    long duration = System.currentTimeMillis() - start;

                    log.debug("Cache operation: {} with args {} took {}ms",
                        methodName, args, duration);

                    return result;
                }
            }
            ```

            ## Summary

            Caching dramatically improves application performance:

            **Key Concepts:**
            - Cache-aside pattern: Check cache first, fall back to database
            - TTL (Time To Live): How long data stays in cache
            - Cache hit: Data found in cache (fast)
            - Cache miss: Data not in cache, must fetch (slower)

            **Spring Cache Annotations:**
            - `@EnableCaching`: Enable caching in application
            - `@Cacheable`: Cache method result
            - `@CachePut`: Update cache
            - `@CacheEvict`: Remove from cache
            - `@Caching`: Multiple cache operations

            **Best Practices:**
            - Use sync=true to prevent cache stampede
            - Set appropriate TTL per cache
            - Evict related caches on updates
            - Cache immutable DTOs, not entities
            - Monitor cache hit rates
            - Don't cache rapidly changing data

            **Performance Impact:**
            - Typical database query: 50-200ms
            - Redis cache hit: 1-5ms
            - 10-100x faster response times
            - 80-95% reduction in database load

            Caching is one of the most effective ways to scale applications,
            reducing costs while improving user experience.
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
            "Your application caches User objects using @Cacheable. After retrieving a cached user, code modifies the user's email. The next cache hit returns the user with the modified email. What's the problem and solution?",
            "C",
            """
            This is a critical caching bug that many developers encounter: caching mutable
            objects allows modifications to corrupt the cache, affecting all subsequent reads.

            The correct answer is C. The cache stores object references, not copies. Modifying
            the object modifies the cached version. Solution: cache immutable DTOs instead.

            **Understanding the Problem:**

            ```java
            @Entity
            public class User {
                @Id private Long id;
                private String email;
                // Mutable - has setter!
                public void setEmail(String email) { this.email = email; }
            }

            @Service
            public class UserService {
                @Cacheable(value = "users", key = "#userId")
                public User getUser(Long userId) {
                    return userRepository.findById(userId).orElseThrow();
                }
            }
            ```

            **The Bug in Action:**
            ```java
            // Request 1: Loads from database
            User user1 = userService.getUser(123L);
            System.out.println(user1.getEmail());  // "john@example.com" ✓

            // Someone modifies the user object
            user1.setEmail("hacker@evil.com");

            // Request 2: Returns from cache
            User user2 = userService.getUser(123L);
            System.out.println(user2.getEmail());  // "hacker@evil.com" ✗✗✗

            // Request 3: Still corrupted!
            User user3 = userService.getUser(123L);
            System.out.println(user3.getEmail());  // "hacker@evil.com" ✗✗✗
            ```

            **Why This Happens:**

            Most cache implementations (including Spring Cache with Redis using default
            serialization) store references or serialize/deserialize to the same object
            structure. When you modify a cached object:

            1. Cache stores User object with email "john@example.com"
            2. Your code gets reference to that object
            3. Your code calls setEmail("hacker@evil.com")
            4. The cached object is now corrupted
            5. All subsequent cache hits return corrupted data

            **Real-World Impact:**

            This can cause:
            - Data corruption across user sessions
            - Security vulnerabilities (user A sees user B's data)
            - Difficult-to-diagnose bugs (works first time, breaks later)
            - Lost customer trust

            **The Solution: Cache Immutable DTOs**

            ```java
            // Immutable DTO (no setters!)
            @Value  // Lombok makes class immutable
            @Builder
            public class UserDTO {
                Long id;
                String name;
                String email;
                LocalDateTime createdAt;
                // No setters - cannot be modified after creation!
            }

            @Service
            public class UserService {
                @Cacheable(value = "users", key = "#userId")
                public UserDTO getUser(Long userId) {
                    User user = userRepository.findById(userId).orElseThrow();

                    // Convert entity to immutable DTO
                    return UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .createdAt(user.getCreatedAt())
                        .build();
                }
            }
            ```

            **Now it's safe:**
            ```java
            UserDTO user1 = userService.getUser(123L);
            System.out.println(user1.getEmail());  // "john@example.com"

            // Can't modify immutable DTO!
            // user1.setEmail("hacker@evil.com");  // Compilation error!

            UserDTO user2 = userService.getUser(123L);
            System.out.println(user2.getEmail());  // "john@example.com" ✓
            ```

            Why other answers are wrong:

            A is wrong because setting sync=true does NOT solve the mutability problem.
            sync=true only prevents multiple simultaneous cache misses:

            ```java
            @Cacheable(value = "users", key = "#userId", sync = true)
            public User getUser(Long userId) {
                // Still returns mutable User entity!
                return userRepository.findById(userId).orElseThrow();
            }
            ```

            With or without sync=true, if you cache a mutable object and modify it,
            the cache is corrupted. sync only affects cache population behavior, not
            what type of object you cache.

            B is wrong because lower TTL is a band-aid, not a fix:

            ```java
            // Shorter TTL doesn't prevent corruption!
            cacheConfig.entryTtl(Duration.ofSeconds(30));
            ```

            With 30-second TTL:
            ```
            0:00 - User loaded, email: "john@example.com"
            0:01 - User modified, email: "hacker@evil.com"
            0:02 - Cache hit: "hacker@evil.com" ✗ (still corrupted!)
            0:15 - Cache hit: "hacker@evil.com" ✗ (still corrupted!)
            0:29 - Cache hit: "hacker@evil.com" ✗ (still corrupted!)
            0:31 - Cache expired, reloads: "john@example.com" ✓
            ```

            The cache is still corrupted for 30 seconds! This doesn't fix the root cause,
            it just reduces how long the corruption lasts. Plus:
            - Defeats purpose of caching (frequent reloads)
            - Increases database load
            - Doesn't prevent the bug, just limits damage
            - Still a serious security issue

            D is wrong because @CacheEvict after every get defeats the entire purpose
            of caching:

            ```java
            @Cacheable(value = "users", key = "#userId")
            public User getUser(Long userId) {
                return userRepository.findById(userId).orElseThrow();
            }

            // After every get
            @CacheEvict(value = "users", key = "#userId")
            public void invalidateUser(Long userId) {
                // Evicts immediately after caching!
            }
            ```

            This means:
            ```
            Call 1: getUser(123) → Database query → Cache → Evict
            Call 2: getUser(123) → Database query → Cache → Evict
            Call 3: getUser(123) → Database query → Cache → Evict
            ```

            No cache hits! You're querying the database every time. Why have a cache at all?

            **Additional Best Practices:**

            1. **Use Records (Java 16+):**
            ```java
            public record UserDTO(
                Long id,
                String name,
                String email,
                LocalDateTime createdAt
            ) {
                // Records are immutable by default!
            }
            ```

            2. **Defensive Copying (if mutable entities required):**
            ```java
            @Cacheable(value = "users", key = "#userId")
            public User getUser(Long userId) {
                User original = userRepository.findById(userId).orElseThrow();
                // Return a copy, not the original
                return User.builder()
                    .id(original.getId())
                    .name(original.getName())
                    .email(original.getEmail())
                    .build();
            }
            ```

            3. **Consider Deep Copies for Complex Objects:**
            ```java
            public User getUser(Long userId) {
                User cached = userRepository.findById(userId).orElseThrow();
                // Serialize and deserialize to create deep copy
                return objectMapper.readValue(
                    objectMapper.writeValueAsString(cached),
                    User.class
                );
            }
            ```

            **Testing for Cache Corruption:**
            ```java
            @Test
            void shouldNotCorruptCacheOnModification() {
                // First call - loads from DB
                UserDTO user1 = userService.getUser(123L);
                String originalEmail = user1.getEmail();

                // Try to modify (shouldn't compile if truly immutable)
                // user1.setEmail("modified@example.com");

                // Second call - should return from cache
                UserDTO user2 = userService.getUser(123L);

                // Should still have original email
                assertEquals(originalEmail, user2.getEmail());

                // Should be different instances (if using defensive copies)
                // or same instance (if using immutable DTOs - both are fine)
            }
            ```

            The key principle: **Cache what you can afford to share unchanged**.
            Immutable objects are safe to share. Mutable objects are not.
            """
        );

        question.addChoice("A", "Redis caching isn't thread-safe - add sync=true to @Cacheable");
        question.addChoice("B", "Cache TTL is too long - reduce to 30 seconds to limit corruption window");
        question.addChoice("C", "Cache stores references to mutable objects - cache immutable DTOs instead of entities");
        question.addChoice("D", "Missing @CacheEvict - must evict cache after every read");

        return question;
    }

    private static QuizQuestion createQuizQuestion2() {
        QuizQuestion question = new QuizQuestion(
            "Your application experiences a sudden traffic spike. 1000 concurrent requests hit @Cacheable method for the same cache key (cache is empty). Without sync=true, what happens?",
            "B",
            """
            This question tests understanding of the "cache stampede" or "thundering herd"
            problem—a critical performance issue that occurs during cache misses under high load.

            The correct answer is B. All 1000 requests see cache miss and query the database
            simultaneously, causing a database overload. This is called cache stampede.

            **Understanding Cache Stampede:**

            **The Scenario:**
            ```java
            @Cacheable(value = "products", key = "#productId")
            // NO sync=true!
            public Product getProduct(Long productId) {
                log.info("Querying database for product {}", productId);
                return productRepository.findById(productId).orElseThrow();
            }
            ```

            **What Happens with 1000 Concurrent Requests:**

            ```
            Time    Thread 1     Thread 2     Thread 3     ... Thread 1000    Cache      Database
            0ms     Check cache  Check cache  Check cache      Check cache     EMPTY
            1ms     MISS!        MISS!        MISS!            MISS!           EMPTY
            2ms     Query DB →   Query DB →   Query DB →       Query DB →                1000 queries!
            150ms   ← Result     ← Result     ← Result         ← Result                  All complete
            151ms   Write cache  Write cache  Write cache      Write cache     FULL       (overwrites)
            152ms   Return       Return       Return           Return
            ```

            **Database Impact:**
            - 1000 simultaneous connections opened
            - 1000 identical SELECT queries executed
            - Database connection pool exhausted
            - Other queries blocked or timeout
            - Database CPU spikes to 100%
            - Response times: 50ms → 5000ms
            - Possible database crash

            **Log Output:**
            ```
            2025-01-15 10:30:00.001 Querying database for product 123
            2025-01-15 10:30:00.001 Querying database for product 123
            2025-01-15 10:30:00.001 Querying database for product 123
            ...
            (1000 identical log lines!)
            ```

            **The Solution: sync=true**

            ```java
            @Cacheable(value = "products", key = "#productId", sync = true)
            public Product getProduct(Long productId) {
                log.info("Querying database for product {}", productId);
                return productRepository.findById(productId).orElseThrow();
            }
            ```

            **With sync=true:**
            ```
            Time    Thread 1     Thread 2     Thread 3     ... Thread 1000    Cache      Database
            0ms     Check cache  Check cache  Check cache      Check cache     EMPTY
            1ms     MISS!        MISS!        MISS!            MISS!           EMPTY
            2ms     Acquire lock Wait...      Wait...          Wait...
            3ms     Query DB →                                                            1 query only!
            52ms    ← Result                                                              Complete
            53ms    Write cache                                                FULL
            54ms    Release lock Wake all     Wake all         Wake all
            55ms    Return       Read cache   Read cache       Read cache
            56ms                 Return       Return           Return
            ```

            **Log Output:**
            ```
            2025-01-15 10:30:00.001 Querying database for product 123
            (Only one log line!)
            ```

            **Benefits:**
            - Only 1 database query instead of 1000
            - Database load reduced 1000x
            - Connection pool not exhausted
            - Other operations continue normally
            - Total time: ~56ms vs ~5000ms

            Why other answers are incorrect:

            A is wrong because Spring Cache does NOT automatically queue requests without
            sync=true. The default behavior is that EVERY request that sees a cache miss
            will proceed to execute the method:

            ```java
            @Cacheable(value = "products", key = "#productId")
            // Default: sync = false
            public Product getProduct(Long productId) {
                // All 1000 requests execute this!
            }
            ```

            There's no implicit queueing. Each thread independently:
            1. Checks cache
            2. Sees miss
            3. Executes method
            4. Writes result to cache

            This is by design—without sync=true, Spring Cache assumes method execution
            is cheap enough that duplicate calls don't matter. This assumption is wrong
            for database queries.

            C is wrong because with 1000 concurrent requests hitting a cache miss, only
            ONE will successfully write to cache in practice due to Redis' atomic operations,
            but all 1000 will have already executed the database query:

            ```
            Database: 1000 queries (all executed!)
            Cache writes: 1000 attempts, but only last one matters

            Problem: Database still got hammered!
            ```

            The race condition on cache write is a minor issue compared to the database
            overload. The real problem is 1000 duplicate database queries, not cache writes.

            Also, multiple successful cache writes (even if they happened) wouldn't cause
            data corruption since they're all writing the same data. Redis SET is atomic—
            last write wins, but the value is the same.

            D is wrong for multiple reasons:

            1. **Not all 1000 requests would fail:**
            Assuming a reasonable timeout (2000ms), most queries would complete:
            ```
            Simple SELECT query: 50ms
            With DB load from 1000 queries: 500-1000ms
            Most would still complete within timeout
            ```

            2. **Even if some timeout, major damage done:**
            ```
            800 requests complete: Database overloaded, cache eventually populated
            200 requests timeout: Return errors to users

            Result: Database still overwhelmed + user errors + wasted resources
            ```

            3. **This doesn't describe the primary problem:**
            The primary problem is database overload, not request timeouts. Timeouts are
            a symptom, not the cause.

            **Real-World Scenario:**

            Popular e-commerce site during flash sale:
            ```
            10:00:00 - Flash sale starts
            10:00:00 - Everyone clicks "Hot Product"
            10:00:00 - Cache was cleared for sale
            10:00:00 - 10,000 requests hit cache miss
            10:00:00 - Without sync: 10,000 DB queries
            10:00:01 - Database crashes
            10:00:02 - Site goes down
            10:00:03 - Revenue loss: $100,000
            ```

            With sync=true:
            ```
            10:00:00 - Flash sale starts
            10:00:00 - 10,000 requests hit cache miss
            10:00:00 - With sync: 1 DB query, 9,999 wait
            10:00:01 - Cache populated
            10:00:01 - All requests complete successfully
            10:00:01 - Site stays up
            ```

            **Additional Mitigation Strategies:**

            1. **Pre-warm cache before traffic spike:**
            ```java
            @Scheduled(fixedRate = 60000)  // Every minute
            public void preWarmCache() {
                List<Long> popularProducts = getPopularProductIds();
                popularProducts.forEach(id -> getProduct(id));
                log.info("Pre-warmed cache with {} products", popularProducts.size());
            }
            ```

            2. **Circuit breaker pattern:**
            ```java
            @Cacheable(value = "products", key = "#productId", sync = true)
            @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
            public Product getProduct(Long productId) {
                return productRepository.findById(productId).orElseThrow();
            }

            public Product getProductFallback(Long productId, Exception e) {
                log.error("Failed to get product {}, returning cached or default", productId, e);
                return Product.builder().id(productId).name("Product Unavailable").build();
            }
            ```

            3. **Rate limiting:**
            ```java
            @RateLimiter(name = "productService")
            @Cacheable(value = "products", key = "#productId", sync = true)
            public Product getProduct(Long productId) {
                return productRepository.findById(productId).orElseThrow();
            }
            ```

            **Testing for Cache Stampede:**
            ```java
            @Test
            void shouldPreventCacheStampede() throws InterruptedException {
                Long productId = 123L;

                // Clear cache
                cacheManager.getCache("products").clear();

                // Simulate 100 concurrent requests
                ExecutorService executor = Executors.newFixedThreadPool(100);
                CountDownLatch latch = new CountDownLatch(100);

                AtomicInteger dbCallCount = new AtomicInteger(0);

                for (int i = 0; i < 100; i++) {
                    executor.submit(() -> {
                        try {
                            productService.getProduct(productId);
                            // Count would be tracked in aspect or mock
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await();

                // With sync=true: Should see only 1-2 DB calls
                // Without sync=true: Would see ~100 DB calls
                verify(productRepository, times(1)).findById(productId);
            }
            ```

            Cache stampede is one of the most dangerous caching problems under high load.
            Always use sync=true for database-backed cached methods.
            """
        );

        question.addChoice("A", "Spring Cache automatically queues the requests - only first request queries database");
        question.addChoice("B", "All 1000 requests see cache miss and query database simultaneously (cache stampede)");
        question.addChoice("C", "First request queries database, remaining 999 race to write cache causing corruption");
        question.addChoice("D", "Database connection pool exhausts, all 1000 requests timeout and fail");

        return question;
    }

    private static QuizQuestion createQuizQuestion3() {
        QuizQuestion question = new QuizQuestion(
            "You cache user profiles with 10-minute TTL. After a user updates their profile, you use @CachePut to update the cache. Users report seeing stale profile data. What's the MOST LIKELY cause?",
            "D",
            """
            This is a subtle but common caching bug related to cache invalidation in distributed
            systems or applications with multiple cache keys for related data.

            The correct answer is D. Multiple cache entries exist for the same user
            (different keys or related data), and @CachePut only updates one entry.

            **Understanding the Problem:**

            **Typical Profile Caching Setup:**
            ```java
            @Service
            public class ProfileService {

                // Cache by user ID
                @Cacheable(value = "profiles", key = "#userId")
                public UserProfile getProfileById(Long userId) {
                    return profileRepository.findByUserId(userId);
                }

                // Cache by username
                @Cacheable(value = "profiles", key = "'username:' + #username")
                public UserProfile getProfileByUsername(String username) {
                    return profileRepository.findByUsername(username);
                }

                // Cache user list (includes profile data)
                @Cacheable(value = "userLists", key = "'team:' + #teamId")
                public List<UserProfile> getTeamMembers(Long teamId) {
                    return profileRepository.findByTeamId(teamId);
                }
            }
            ```

            **The Update Method (BUGGY):**
            ```java
            @CachePut(value = "profiles", key = "#userId")
            public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                UserProfile profile = profileRepository.findByUserId(userId);
                profile.setName(request.getName());
                profile.setBio(request.getBio());
                return profileRepository.save(profile);
            }
            ```

            **What's Cached:**
            ```
            profiles::123 → {id: 123, name: "John Doe", bio: "Old bio"}
            profiles::username:johndoe → {id: 123, name: "John Doe", bio: "Old bio"}
            userLists::team:5 → [{id: 123, name: "John Doe", bio: "Old bio"}, ...]
            ```

            **After Update:**
            ```java
            updateProfile(123, new ProfileUpdateRequest("John Doe", "New bio"));
            ```

            **Cache State:**
            ```
            profiles::123 → {id: 123, name: "John Doe", bio: "New bio"}  ✓ Updated!
            profiles::username:johndoe → {id: 123, name: "John Doe", bio: "Old bio"}  ✗ Stale!
            userLists::team:5 → [{id: 123, name: "John Doe", bio: "Old bio"}, ...]  ✗ Stale!
            ```

            **User Experience:**
            ```
            // Request by ID - works!
            GET /api/profiles/123
            Response: {name: "John Doe", bio: "New bio"}  ✓

            // Request by username - stale!
            GET /api/profiles/username/johndoe
            Response: {name: "John Doe", bio: "Old bio"}  ✗

            // Team view - stale!
            GET /api/teams/5/members
            Response: [{name: "John Doe", bio: "Old bio"}, ...]  ✗
            ```

            **The Solution: Evict All Related Cache Entries**

            ```java
            @Caching(
                put = @CachePut(value = "profiles", key = "#userId"),
                evict = {
                    // Evict username-based cache
                    @CacheEvict(
                        value = "profiles",
                        key = "'username:' + #result.username"
                    ),
                    // Evict all team lists (user might be in multiple teams)
                    @CacheEvict(value = "userLists", allEntries = true)
                }
            )
            public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                UserProfile profile = profileRepository.findByUserId(userId);
                profile.setName(request.getName());
                profile.setBio(request.getBio());
                return profileRepository.save(profile);
            }
            ```

            **Better: Consistent Key Strategy**
            ```java
            // Always use user ID as the base key
            @Cacheable(value = "profiles", key = "#userId")
            public UserProfile getProfileById(Long userId) {
                return profileRepository.findByUserId(userId);
            }

            @Cacheable(value = "profiles", key = "#username.userId")  // Lookup ID first!
            public UserProfile getProfileByUsername(String username) {
                User user = userRepository.findByUsername(username);
                return getProfileById(user.getId());  // Reuse cached method
            }
            ```

            Why other answers are less likely:

            A is incorrect because @CachePut DOES update the database. The signature of
            @CachePut means:

            1. Execute method body
            2. Use return value to update cache

            ```java
            @CachePut(value = "profiles", key = "#userId")
            public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                // This code DOES execute!
                UserProfile profile = profileRepository.findByUserId(userId);
                profile.setName(request.getName());
                return profileRepository.save(profile);  // Database IS updated!
            }
            // Then Spring updates cache with return value
            ```

            If database weren't updated, ALL users would see stale data (not just some),
            and refreshing after TTL expiration wouldn't show new data. The fact that some
            paths show new data indicates database IS updated.

            B is unlikely because Redis serialization is battle-tested and rarely causes
            "partial updates." Redis operations are atomic—either a key is fully updated
            or it's not updated at all:

            ```
            Redis SET operation:
            BEFORE: profiles::123 = {old data}
            AFTER:  profiles::123 = {new data}

            No partial state like: profiles::123 = {name: new, bio: old}
            ```

            If Redis serialization were broken:
            - Would affect ALL cached data, not just profiles
            - Would cause exceptions and errors
            - Would be immediately obvious
            - Would be fixed by Redis/Spring quickly (not a common bug)

            The symptom described (some requests show new data, others show old) points to
            multiple cache keys, not serialization issues.

            C is possible but less likely than D. If @CachePut didn't specify the key parameter,
            Spring would generate one from all method parameters:

            ```java
            @CachePut(value = "profiles")  // No key specified
            public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                // Key would be: userId + request (both parameters)
                // Original cached key was just: userId
                // These don't match, so original isn't updated
            }
            ```

            However, most developers DO specify keys explicitly:
            ```java
            @CachePut(value = "profiles", key = "#userId")
            ```

            And if key were wrong, you'd see this consistently across ALL cache access
            patterns, not just some. The fact that some requests show fresh data suggests
            the key IS correct for that particular cache entry.

            **Complete Solution Pattern:**

            ```java
            @Service
            @RequiredArgsConstructor
            public class ProfileService {

                private final CacheManager cacheManager;

                @Caching(
                    put = @CachePut(value = "profiles", key = "#userId"),
                    evict = {
                        @CacheEvict(value = "profiles", key = "'username:' + #result.username"),
                        @CacheEvict(value = "userLists", allEntries = true),
                        @CacheEvict(value = "search", allEntries = true)  // Profile in search results
                    }
                )
                @Transactional
                public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
                    UserProfile profile = profileRepository.findByUserId(userId);
                    profile.setName(request.getName());
                    profile.setBio(request.getBio());
                    UserProfile updated = profileRepository.save(profile);

                    // Invalidate any other related caches programmatically
                    invalidateRelatedCaches(updated);

                    return updated;
                }

                private void invalidateRelatedCaches(UserProfile profile) {
                    // Clear any caches that might contain this profile
                    Cache notificationsCache = cacheManager.getCache("notifications");
                    if (notificationsCache != null) {
                        notificationsCache.evict("user:" + profile.getUserId());
                    }

                    // Clear friend lists
                    profile.getFriends().forEach(friendId -> {
                        Cache friendsCache = cacheManager.getCache("friends");
                        if (friendsCache != null) {
                            friendsCache.evict("user:" + friendId);
                        }
                    });
                }
            }
            ```

            **Testing Strategy:**
            ```java
            @Test
            void shouldInvalidateAllRelatedCachesOnUpdate() {
                Long userId = 123L;
                String username = "johndoe";

                // Populate multiple cache entries
                profileService.getProfileById(userId);
                profileService.getProfileByUsername(username);
                List<Long> teamIds = userService.getUserTeams(userId);

                // Update profile
                profileService.updateProfile(userId, new ProfileUpdateRequest(...));

                // Verify all paths show fresh data
                UserProfile byId = profileService.getProfileById(userId);
                UserProfile byUsername = profileService.getProfileByUsername(username);

                assertEquals("New bio", byId.getBio());
                assertEquals("New bio", byUsername.getBio());

                // Team lists should also be fresh
                for (Long teamId : teamIds) {
                    List<UserProfile> members = profileService.getTeamMembers(teamId);
                    UserProfile member = members.stream()
                        .filter(m -> m.getUserId().equals(userId))
                        .findFirst()
                        .orElseThrow();
                    assertEquals("New bio", member.getBio());
                }
            }
            ```

            The core lesson: When updating cached data, consider ALL cache entries that
            might contain that data, not just the primary cache key.
            """
        );

        question.addChoice("A", "@CachePut updates cache but doesn't update database - need @Transactional");
        question.addChoice("B", "Redis serialization partially updates the object, leaving some fields stale");
        question.addChoice("C", "@CachePut is using wrong cache key - doesn't match @Cacheable key");
        question.addChoice("D", "Multiple cache entries exist for same user (different keys), @CachePut only updates one");

        return question;
    }
}
