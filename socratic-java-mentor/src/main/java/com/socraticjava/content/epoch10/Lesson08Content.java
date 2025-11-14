package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Lesson 8: Performance Optimization and Profiling
 *
 * This lesson covers JVM tuning, database query optimization, profiling tools,
 * and strategies for identifying and fixing performance bottlenecks in production
 * Spring Boot applications.
 */
public class Lesson08Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Performance Optimization and Profiling",
            """
            # Performance Optimization and Profiling

            ## Why Performance Matters

            **Real-world scenario:** Your e-commerce site loads in 5 seconds. Amazon found that every 100ms of latency costs them 1% in sales. Your slow site is costing you customers and revenue every day.

            **Think of performance optimization like car maintenance:** You wouldn't wait for your car to break down before checking the oil. Similarly, you shouldn't wait for your application to crash before optimizing performance. Regular profiling and tuning prevent problems before they happen.

            **Production impact:**
            - 💰 **Revenue:** 1 second delay = 7% reduction in conversions (Google research)
            - 📈 **Scalability:** Well-optimized apps handle 10x more traffic with same infrastructure
            - 💵 **Cost savings:** Reduce cloud costs by 50% through efficient resource usage
            - 😊 **User retention:** 53% of mobile users abandon sites that take > 3 seconds to load

            ---

            ## The Performance Optimization Workflow

            **❌ Wrong approach:** Random optimization without measuring
            ```
            Developer: "Let me add caching everywhere!"
            Result: Increased complexity, minimal improvement
            ```

            **✅ Correct approach:** Measure, analyze, optimize, verify
            ```
            1. Measure baseline performance (metrics, profiling)
            2. Identify bottlenecks (use data, not guesses)
            3. Optimize the biggest bottleneck first
            4. Measure again to verify improvement
            5. Repeat
            ```

            **Golden rule:** "Premature optimization is the root of all evil" - Donald Knuth
            - Don't optimize before you have a performance problem
            - Always measure before and after optimization
            - Focus on bottlenecks, not micro-optimizations

            ---

            ## Profiling Tools for Spring Boot

            ### 1. VisualVM (Free, Open Source)

            **Best for:** Quick profiling during development, identifying memory leaks

            **Features:**
            - CPU profiling (method-level timing)
            - Memory profiling (heap dumps, object allocation)
            - Thread analysis (deadlock detection)
            - JMX monitoring

            **How to use:**
            ```bash
            # Download from https://visualvm.github.io/
            # Start your Spring Boot app with JMX enabled (default)
            java -jar myapp.jar

            # Launch VisualVM
            ./visualvm

            # Attach to your running Java process
            # Click "Sampler" tab → Start CPU/Memory sampling
            ```

            **Reading CPU samples:**
            ```
            Method                          Self Time    Total Time
            OrderService.processOrder()     5%           800ms
            ├─ InventoryService.check()     10%          600ms  ← BOTTLENECK!
            │  └─ Database.query()          90%          540ms  ← ROOT CAUSE!
            └─ PaymentService.charge()      2%           200ms
            ```

            **Interpretation:** 540ms spent in a single database query. Optimize that query first!

            ### 2. JProfiler (Commercial, $500+)

            **Best for:** Deep analysis, production profiling, enterprise applications

            **Advanced features:**
            - CPU hotspot analysis with call trees
            - Memory leak detection with retention paths
            - Database query profiling (JDBC)
            - Live memory analysis
            - Allocation call tree
            - Thread profiling with deadlock detection

            **Why it's worth the cost:**
            - Saves hours of debugging with precise call trees
            - Low overhead (can use in production)
            - JDBC profiling shows exact slow queries
            - Excellent UI for analyzing complex issues

            ### 3. async-profiler (Free, Low Overhead)

            **Best for:** Production profiling with minimal overhead

            **Unique advantage:** Uses native OS profiling (perf on Linux) for very low overhead (<1%)

            ```bash
            # Download from https://github.com/async-profiler/async-profiler
            # Profile running application
            ./profiler.sh -d 30 -f flamegraph.html <PID>

            # Generate flame graph showing CPU hotspots
            ```

            **Flame graph interpretation:**
            - Width = time spent (wider = slower)
            - Height = call stack depth
            - Color = CPU vs I/O (usually)

            ### 4. Spring Boot Actuator + Metrics (Production)

            **Best for:** Continuous monitoring in production (covered in Lesson 18)

            ```java
            @Timed(value = "orders.process", histogram = true)
            public Order processOrder(OrderRequest request) {
                // Automatically tracked by Micrometer
            }
            ```

            **Use Grafana to identify trends:**
            - Which endpoints are slow?
            - Is performance degrading over time?
            - Are there traffic spikes?

            ---

            ## JVM Tuning for Performance

            ### Understanding JVM Memory

            ```
            ┌─────────────────────────────────────────┐
            │         JVM Memory Layout               │
            ├─────────────────────────────────────────┤
            │  Heap (objects, garbage collected)      │
            │  ├─ Young Generation (new objects)      │
            │  │  ├─ Eden Space                       │
            │  │  └─ Survivor Spaces (S0, S1)         │
            │  └─ Old Generation (long-lived objects) │
            ├─────────────────────────────────────────┤
            │  Non-Heap (not garbage collected)       │
            │  ├─ Metaspace (class metadata)          │
            │  ├─ Code Cache (JIT compiled code)      │
            │  └─ Thread Stacks                       │
            └─────────────────────────────────────────┘
            ```

            ### Key JVM Parameters

            ```bash
            # Heap size (most important!)
            -Xms2g          # Initial heap size (2GB)
            -Xmx2g          # Maximum heap size (2GB)
            # ✅ Best practice: Set -Xms = -Xmx to avoid heap resizing

            # Garbage Collector selection
            -XX:+UseG1GC    # G1 Garbage Collector (default in Java 11+, best for most apps)
            # Alternatives:
            # -XX:+UseZGC           # ZGC (ultra-low pause times, Java 15+)
            # -XX:+UseShenandoahGC  # Shenandoah (low pause times)
            # -XX:+UseParallelGC    # Parallel GC (high throughput, longer pauses)

            # G1GC tuning
            -XX:MaxGCPauseMillis=200    # Target max GC pause (200ms)
            -XX:G1HeapRegionSize=16m    # Size of G1 regions

            # Metaspace (class metadata)
            -XX:MetaspaceSize=256m
            -XX:MaxMetaspaceSize=512m

            # GC logging (Java 9+)
            -Xlog:gc*:file=gc.log:time,uptime,level,tags

            # Enable JMX for monitoring
            -Dcom.sun.management.jmxremote
            -Dcom.sun.management.jmxremote.port=9010
            -Dcom.sun.management.jmxremote.authenticate=false
            -Dcom.sun.management.jmxremote.ssl=false
            ```

            ### Choosing Heap Size

            **How much heap do you need?**
            1. Start with 25-50% of available RAM
            2. Monitor heap usage in production
            3. Adjust based on actual usage

            **Example calculation:**
            - Server has 8GB RAM
            - OS uses ~1GB
            - Available for JVM: 7GB
            - Recommended heap: 4GB (`-Xms4g -Xmx4g`)
            - Remaining 3GB for non-heap, OS cache, other processes

            **Signs your heap is too small:**
            - Frequent full GC pauses
            - `OutOfMemoryError: Java heap space`
            - High GC overhead (>10% of CPU time)

            **Signs your heap is too large:**
            - Very long GC pauses (>1 second)
            - Wasted memory (heap 50% empty)
            - OS swapping (check with `free -h` on Linux)

            ### Garbage Collector Comparison

            | GC | Best For | Pause Times | Throughput | Java Version |
            |----|----------|-------------|------------|--------------|
            | **G1GC** | General purpose | 10-500ms | Good | 11+ (default) |
            | **ZGC** | Very low latency | <10ms | Good | 15+ |
            | **Shenandoah** | Low latency | <10ms | Good | 12+ |
            | **Parallel GC** | Batch processing | 100ms-1s+ | Excellent | All |
            | **Serial GC** | Small heaps (<100MB) | 10-100ms | Poor | All |

            **Recommendation for Spring Boot apps:**
            - **Default (G1GC):** Works well for 90% of applications
            - **Switch to ZGC if:** You need sub-10ms pause times and use Java 15+
            - **Parallel GC:** Only for batch jobs where throughput > latency

            ### Monitoring GC Performance

            ```bash
            # Enable GC logging
            java -Xlog:gc*:file=gc.log -jar myapp.jar

            # Analyze with GCViewer or GCeasy.io
            ```

            **GC metrics to monitor:**
            - **Pause time:** How long application stops (target: <100ms for web apps)
            - **Frequency:** How often GC runs (target: <1 per minute for full GC)
            - **Heap usage:** Average and peak (should have 30% headroom)
            - **GC overhead:** Percentage of time spent in GC (target: <5%)

            ---

            ## Database Query Optimization

            Database queries are the #1 performance bottleneck in most Spring Boot applications.

            ### 1. The N+1 Query Problem

            **❌ Problem: 1 query for users + N queries for their orders**
            ```java
            // Fetches users
            List<User> users = userRepository.findAll();  // 1 query

            // For each user, fetches orders separately
            for (User user : users) {
                List<Order> orders = user.getOrders();  // N queries! 😱
                // ... process orders ...
            }

            // Total: 1 + N queries
            // With 100 users: 101 queries!
            ```

            **✅ Solution: Eager fetching with JOIN FETCH**
            ```java
            public interface UserRepository extends JpaRepository<User, Long> {

                @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
                List<User> findAllWithOrders();  // Single query with JOIN
            }

            // Now only 1 query for everything!
            ```

            **EntityGraph alternative (Spring Data JPA):**
            ```java
            @EntityGraph(attributePaths = {"orders", "orders.items"})
            List<User> findAll();
            ```

            ### 2. Use Pagination for Large Results

            **❌ Fetching 100,000 rows:**
            ```java
            List<Product> allProducts = productRepository.findAll();  // OOM risk!
            ```

            **✅ Pagination:**
            ```java
            // Fetch 20 at a time
            Pageable pageable = PageRequest.of(0, 20, Sort.by("name"));
            Page<Product> page = productRepository.findAll(pageable);
            ```

            ### 3. Enable Query Logging and Analysis

            ```yaml
            # application.yml
            spring:
              jpa:
                show-sql: true
                properties:
                  hibernate:
                    format_sql: true
                    type: trace  # Show parameter values
                    use_sql_comments: true

            logging:
              level:
                org.hibernate.SQL: DEBUG
                org.hibernate.type.descriptor.sql.BasicBinder: TRACE
            ```

            **Example output:**
            ```sql
            /* load User */
            SELECT u.id, u.name, u.email
            FROM users u
            WHERE u.id = ? -- binding parameter [1] as [BIGINT] - 123

            Execution time: 450ms  ← SLOW QUERY!
            ```

            ### 4. Add Database Indices

            **❌ Without index (full table scan):**
            ```sql
            SELECT * FROM orders WHERE user_id = 123;
            -- Scans 1,000,000 rows → 800ms
            ```

            **✅ With index:**
            ```sql
            CREATE INDEX idx_orders_user_id ON orders(user_id);

            SELECT * FROM orders WHERE user_id = 123;
            -- Index lookup → 5ms (160x faster!)
            ```

            **When to add indices:**
            - Columns in WHERE clauses (frequently)
            - Columns in JOIN conditions
            - Foreign keys
            - Columns used in ORDER BY

            **When NOT to add indices:**
            - Rarely queried columns
            - Columns with low cardinality (e.g., boolean)
            - Tables with heavy writes (indices slow down INSERTs)

            ### 5. Optimize Connection Pooling (HikariCP)

            HikariCP is the default connection pool in Spring Boot (and the fastest).

            ```yaml
            spring:
              datasource:
                hikari:
                  maximum-pool-size: 10     # Max connections (default: 10)
                  minimum-idle: 5           # Min idle connections
                  connection-timeout: 30000 # 30 seconds
                  idle-timeout: 600000      # 10 minutes
                  max-lifetime: 1800000     # 30 minutes
                  pool-name: HikariPool
            ```

            **Formula for pool size:**
            ```
            connections = ((core_count) * 2) + effective_spindle_count

            Example:
            - 4-core CPU
            - 1 SSD (effective_spindle_count = 1)
            - Recommended: (4 * 2) + 1 = 9 connections
            ```

            **⚠️ More connections ≠ better performance!**
            - Too many connections → context switching, memory waste
            - Typical sweet spot: 10-20 connections per app instance

            ### 6. Use Query Caching (Second-Level Cache)

            ```java
            @Entity
            @Cacheable
            @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
            public class Product {
                @Id
                private Long id;
                private String name;
                private BigDecimal price;
            }
            ```

            ```yaml
            spring:
              jpa:
                properties:
                  hibernate:
                    cache:
                      use_second_level_cache: true
                      region:
                        factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
            ```

            **When to use:**
            - Read-heavy entities (products, categories)
            - Data that changes infrequently
            - High query volume

            **When NOT to use:**
            - Write-heavy entities (logs, events)
            - User-specific data
            - Real-time data

            ---

            ## Application-Level Optimizations

            ### 1. Lazy Initialization (Spring Boot 2.2+)

            ```yaml
            # application.yml
            spring:
              main:
                lazy-initialization: true  # Don't create beans until needed
            ```

            **Benefits:**
            - Faster startup (50% improvement possible)
            - Lower initial memory usage

            **Tradeoffs:**
            - First request slower (bean created on-demand)
            - Startup errors may be delayed

            **Best for:** Applications with many beans but limited initial traffic.

            ### 2. Async Processing (from Lesson 16)

            **❌ Blocking approach:**
            ```java
            public OrderResult processOrder(Order order) {
                sendEmail(order);           // 2 seconds
                updateInventory(order);     // 1 second
                chargePayment(order);       // 3 seconds
                return new OrderResult();   // Total: 6 seconds
            }
            ```

            **✅ Async approach:**
            ```java
            @Async
            public CompletableFuture<Void> sendEmailAsync(Order order) { ... }

            @Async
            public CompletableFuture<Void> updateInventoryAsync(Order order) { ... }

            public OrderResult processOrder(Order order) {
                chargePayment(order);  // 3 seconds (must be synchronous)

                // Fire and forget (async)
                sendEmailAsync(order);
                updateInventoryAsync(order);

                return new OrderResult();  // Total: 3 seconds (50% faster!)
            }
            ```

            ### 3. Caching (from Lesson 15)

            **❌ Without cache: Every request hits database**
            ```java
            public Product getProduct(Long id) {
                return productRepository.findById(id).orElseThrow();  // 50ms
            }
            ```

            **✅ With cache: Most requests hit cache**
            ```java
            @Cacheable("products")
            public Product getProduct(Long id) {
                return productRepository.findById(id).orElseThrow();  // 50ms first time, 1ms after
            }
            ```

            ### 4. Use Reactive Programming (Advanced)

            Spring WebFlux with Project Reactor for non-blocking I/O:

            ```java
            // Traditional (blocking)
            @GetMapping("/users/{id}")
            public User getUser(@PathVariable Long id) {
                return userService.findById(id);  // Thread blocked during DB call
            }

            // Reactive (non-blocking)
            @GetMapping("/users/{id}")
            public Mono<User> getUser(@PathVariable Long id) {
                return userService.findById(id);  // Thread released during DB call
            }
            ```

            **When to use WebFlux:**
            - High concurrency (1000+ concurrent requests)
            - I/O-heavy workloads (many external API calls)
            - Streaming data

            **When NOT to use WebFlux:**
            - Traditional CRUD apps (stick with Spring MVC)
            - Blocking dependencies (JDBC, most libraries)
            - Team unfamiliar with reactive programming (steep learning curve)

            ---

            ## Load Testing to Find Bottlenecks

            **Never deploy to production without load testing!**

            ### Using Apache Bench (ab)

            ```bash
            # 1000 requests, 10 concurrent
            ab -n 1000 -c 10 http://localhost:8080/api/products

            # Results:
            # Requests per second:    250.00 [#/sec]
            # Time per request:       40.000 [ms] (mean)
            # 95th percentile:        65 ms
            # Failed requests:        0
            ```

            ### Using JMeter (Better for Complex Scenarios)

            ```xml
            <!-- JMeter test plan -->
            <ThreadGroup>
              <numThreads>100</numThreads>     <!-- 100 concurrent users -->
              <rampTime>30</rampTime>          <!-- Ramp up over 30 seconds -->
              <duration>300</duration>         <!-- Run for 5 minutes -->
            </ThreadGroup>
            ```

            ### Using Gatling (Modern, Scala-based)

            ```scala
            // Gatling scenario
            scenario("Product API Load Test")
              .exec(http("Get Product")
                .get("/api/products/1"))
              .pause(1)
              .inject(
                rampUsersPerSec(1) to 100 during (2 minutes),
                constantUsersPerSec(100) during (5 minutes)
              )
            ```

            ### Load Testing Best Practices

            1. **Test in a production-like environment** (same CPU, memory, network)
            2. **Start with baseline:** Single user, no load
            3. **Gradually increase load:** 10 → 50 → 100 → 500 users
            4. **Monitor everything:** CPU, memory, database connections, response times
            5. **Find the breaking point:** When does it start failing?
            6. **Profile under load:** Use VisualVM/JProfiler during load test

            ---

            ## Spring Boot 3: Ahead-of-Time (AOT) Compilation

            Spring Boot 3 with GraalVM native images for faster startup and lower memory:

            ```xml
            <!-- pom.xml -->
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
            </plugin>
            ```

            ```bash
            # Build native image
            mvn -Pnative native:compile

            # Run native image
            ./target/myapp

            # Results:
            # Startup time: 0.05 seconds (vs 3 seconds for JVM)
            # Memory usage: 20 MB (vs 200 MB for JVM)
            ```

            **Benefits:**
            - ⚡ 60x faster startup
            - 💾 10x lower memory usage
            - 📦 Single executable (no JVM required)

            **Limitations:**
            - Not all libraries supported
            - Reflection requires configuration
            - Longer build times (2-5 minutes)
            - No runtime optimization (JIT disabled)

            **Best for:**
            - Serverless functions (AWS Lambda, Google Cloud Functions)
            - Microservices with frequent scaling
            - CLI tools

            ---

            ## Performance Optimization Checklist

            ✅ **Measurement:**
            - [ ] Baseline metrics established (response times, throughput)
            - [ ] Monitoring in place (Actuator, Prometheus, Grafana)
            - [ ] Load testing performed

            ✅ **JVM Tuning:**
            - [ ] Heap size configured (-Xms = -Xmx)
            - [ ] Appropriate GC selected (G1GC for most, ZGC for low latency)
            - [ ] GC logs enabled and analyzed
            - [ ] JVM metrics monitored (heap usage, GC pauses)

            ✅ **Database:**
            - [ ] N+1 queries eliminated (use JOIN FETCH or EntityGraph)
            - [ ] Indices added for frequently queried columns
            - [ ] Connection pool sized appropriately (10-20 connections)
            - [ ] Query logging enabled in development
            - [ ] Pagination used for large result sets
            - [ ] Second-level cache for read-heavy entities

            ✅ **Application:**
            - [ ] Caching enabled for expensive operations
            - [ ] Async processing for non-critical tasks
            - [ ] Lazy initialization considered for faster startup
            - [ ] Static resources served from CDN
            - [ ] Gzip compression enabled

            ✅ **Profiling:**
            - [ ] CPU profiling performed (VisualVM, JProfiler)
            - [ ] Memory profiling performed (heap dumps analyzed)
            - [ ] Bottlenecks identified and prioritized

            ---

            ## Common Performance Mistakes to Avoid

            ### 1. String Concatenation in Loops
            ```java
            // ❌ Creates 10,000 intermediate String objects
            String result = "";
            for (int i = 0; i < 10000; i++) {
                result += i + ",";
            }

            // ✅ Use StringBuilder
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                result.append(i).append(",");
            }
            ```

            ### 2. Unnecessary Object Creation
            ```java
            // ❌ Creates new BigDecimal on every call
            public BigDecimal calculateTax(BigDecimal amount) {
                return amount.multiply(new BigDecimal("0.08"));
            }

            // ✅ Reuse constant
            private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

            public BigDecimal calculateTax(BigDecimal amount) {
                return amount.multiply(TAX_RATE);
            }
            ```

            ### 3. Using Reflection in Hot Paths
            ```java
            // ❌ Reflection is slow (100x slower than direct call)
            for (Object obj : objects) {
                Method method = obj.getClass().getMethod("process");
                method.invoke(obj);
            }

            // ✅ Use polymorphism
            for (Processable obj : objects) {
                obj.process();
            }
            ```

            ### 4. Not Closing Resources
            ```java
            // ❌ Leaks database connections
            Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            // Forgot to close!

            // ✅ Use try-with-resources
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                // Automatically closed
            }
            ```

            ### 5. Synchronizing Too Much
            ```java
            // ❌ Everything synchronized (only 1 thread at a time)
            public synchronized void processOrder(Order order) {
                // Long operation...
            }

            // ✅ Synchronize only critical section
            public void processOrder(Order order) {
                // Non-critical work (parallel)
                validate(order);
                calculateTotals(order);

                // Only critical section synchronized
                synchronized(orderLock) {
                    orderRepository.save(order);
                }
            }
            ```

            ---

            ## Real-World Performance Optimization Case Study

            **Scenario:** E-commerce site experiencing slow response times during peak hours.

            **Step 1: Measure baseline**
            - Average response time: 3.5 seconds
            - 95th percentile: 8 seconds
            - Requests/second: 50

            **Step 2: Profile with VisualVM**
            - Found: `ProductService.getRelatedProducts()` taking 2.5 seconds per call

            **Step 3: Analyze the bottleneck**
            ```java
            // Original code (N+1 query problem)
            public List<Product> getRelatedProducts(Long productId) {
                Product product = productRepository.findById(productId).orElseThrow();

                // N queries for each category!
                return product.getCategories().stream()
                    .flatMap(category -> category.getProducts().stream())
                    .distinct()
                    .collect(Collectors.toList());
            }
            ```

            **Step 4: Optimize**
            ```java
            // Fixed with single query
            @Query("""
                SELECT DISTINCT p FROM Product p
                JOIN p.categories c
                WHERE c IN (
                    SELECT c2 FROM Product p2
                    JOIN p2.categories c2
                    WHERE p2.id = :productId
                )
                AND p.id != :productId
                """)
            List<Product> findRelatedProducts(Long productId);
            ```

            **Step 5: Add caching**
            ```java
            @Cacheable(value = "relatedProducts", key = "#productId")
            public List<Product> getRelatedProducts(Long productId) {
                return productRepository.findRelatedProducts(productId);
            }
            ```

            **Results:**
            - Average response time: **0.4 seconds** (88% improvement!)
            - 95th percentile: **0.8 seconds** (90% improvement!)
            - Requests/second: **400** (8x improvement!)
            - Database query time: 2500ms → 35ms (first hit) → 1ms (cached)

            **Cost savings:**
            - Before: Needed 10 servers to handle traffic
            - After: Needed 2 servers
            - **Saved $50,000/year in infrastructure costs**

            ---

            ## Summary

            In this lesson, you learned how to optimize Spring Boot applications for production performance:

            1. **Profiling tools:** VisualVM (free), JProfiler (commercial), async-profiler (low overhead)
            2. **JVM tuning:** Heap sizing, GC selection (G1GC, ZGC), monitoring GC metrics
            3. **Database optimization:** Eliminate N+1 queries, add indices, use pagination, optimize connection pools
            4. **Application optimizations:** Caching, async processing, lazy initialization
            5. **Load testing:** Apache Bench, JMeter, Gatling to find breaking points
            6. **Spring Boot 3 AOT:** Native images for faster startup and lower memory
            7. **Optimization workflow:** Measure → Analyze → Optimize → Verify
            8. **Avoid common mistakes:** String concatenation in loops, unnecessary object creation, excessive synchronization

            **Real-world impact:** Performance optimization can reduce response times by 80-90%, handle 10x more traffic, and cut infrastructure costs by 50% or more.

            **Remember:** Always measure before optimizing. Focus on bottlenecks, not micro-optimizations. Performance optimization is iterative—measure, optimize, repeat!

            **Next steps:** Final deployment and CI/CD to get your optimized application into production!
            """,
            65
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        return new QuizQuestion(
            "You're experiencing slow performance in production. VisualVM profiling shows `UserService.getUsers()` takes 80% of CPU time. The method has a for-loop that calls `userRepository.findById(id)` for each user ID. What is the problem and solution?",
            Arrays.asList(
                "The JVM heap is too small - increase -Xmx to 4GB",
                "N+1 query problem - replace individual findById() calls with a single findAllById() query",
                "GC pauses are too long - switch from G1GC to ZGC",
                "The database doesn't have enough connections - increase HikariCP pool size to 100"
            ),
            1, // N+1 query problem (index 1)
            """
            **Correct answer:** N+1 query problem - replace individual findById() calls with a single findAllById() query

            **What is the N+1 query problem?**
            When you execute 1 query to get a list of IDs, then N additional queries (one for each ID) to get the details.

            **The problematic code:**
            ```java
            public List<User> getUsers(List<Long> userIds) {
                List<User> users = new ArrayList<>();

                // This creates N database queries! 😱
                for (Long id : userIds) {
                    users.add(userRepository.findById(id).orElseThrow());
                }

                return users;  // Total: N queries (100 IDs = 100 queries!)
            }
            ```

            **Why is this slow?**
            - 100 user IDs = 100 separate database queries
            - Each query has network latency (~5ms)
            - Each query has database overhead (~10ms)
            - Total time: 100 × 15ms = **1,500ms (1.5 seconds)**

            **The solution:**
            ```java
            public List<User> getUsers(List<Long> userIds) {
                // Single query with WHERE id IN (1, 2, 3, ...)
                return userRepository.findAllById(userIds);  // 1 query!
            }
            ```

            **Performance improvement:**
            - Before: 100 queries, 1,500ms
            - After: 1 query, 50ms
            - **30x faster!**

            **Real-world example - JPA relationships:**
            ```java
            @Entity
            public class User {
                @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
                private List<Order> orders;  // LAZY by default
            }

            // ❌ N+1 problem
            List<User> users = userRepository.findAll();  // 1 query
            for (User user : users) {
                user.getOrders().size();  // N queries (one per user)!
            }

            // ✅ Solution: JOIN FETCH
            @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
            List<User> findAllWithOrders();  // Single query with JOIN

            // ✅ Alternative: EntityGraph
            @EntityGraph(attributePaths = "orders")
            List<User> findAll();
            ```

            **How to detect N+1 queries:**
            1. Enable query logging:
            ```yaml
            spring.jpa.show-sql: true
            logging.level.org.hibernate.SQL: DEBUG
            ```

            2. Look for repeated similar queries:
            ```sql
            SELECT * FROM users;
            SELECT * FROM orders WHERE user_id = 1;
            SELECT * FROM orders WHERE user_id = 2;
            SELECT * FROM orders WHERE user_id = 3;
            ...  ← N+1 DETECTED!
            ```

            3. Use profiling tools (VisualVM shows repository calls in hot paths)

            **Why the other answers are wrong:**

            **Increase JVM heap:**
            - Won't fix slow queries
            - Memory is not the bottleneck here (CPU time is 80%, not memory)
            - The problem is too many database calls, not memory

            **Switch to ZGC:**
            - GC pauses are not the issue
            - 80% of time in `getUsers()` means it's application code, not GC
            - ZGC helps with GC pauses, not query performance

            **Increase connection pool to 100:**
            - Sequential queries don't need more connections
            - The loop runs sequentially (one query at a time)
            - More connections won't speed up sequential operations
            - Typically 10-20 connections is optimal; 100 would waste resources
            """
        );
    }

    private static QuizQuestion createQuizQuestion2() {
        return new QuizQuestion(
            "Your Spring Boot app runs fine in development with -Xmx512m, but in production with -Xmx4g, you're seeing 5-second GC pauses every 2 minutes. What is the most likely cause and solution?",
            Arrays.asList(
                "4GB heap is too large for G1GC - reduce heap to 1GB",
                "The application has a memory leak - use heap dump analysis to find retained objects",
                "G1GC is wrong for this workload - switch to Parallel GC",
                "GC threads are competing with application threads - increase CPU cores"
            ),
            1, // Memory leak (index 1)
            """
            **Correct answer:** The application has a memory leak - use heap dump analysis to find retained objects

            **Why this is a memory leak:**

            **Expected behavior with larger heap:**
            - More heap = less frequent GC (good!)
            - More heap = longer GC pauses, but not 5 seconds

            **Abnormal symptoms:**
            - GC pauses are extremely long (5 seconds)
            - GC happens regularly (every 2 minutes)
            - Production uses more heap than development (expected), but GC shouldn't pause this long

            **What's happening:**
            ```
            Heap usage over time (with memory leak):

            4GB  ████████████████████████████████ ← Full! (GC runs)
            3GB  ██████████████████████████
            2GB  ████████████████
            1GB  ████████
            0GB
                 0min   2min   4min   6min   8min
                        ↑ GC                ↑ GC (5 second pause)
            ```

            The heap fills up completely, forcing a full GC. Full GC takes a long time (5 seconds) because it has to scan the entire 4GB heap and finds that most objects can't be freed (memory leak).

            **How to diagnose:**

            **1. Take a heap dump during/after GC:**
            ```bash
            # Manually trigger heap dump
            jmap -dump:live,format=b,file=heap.bin <PID>

            # Or configure automatic heap dump on OOM
            java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof -jar app.jar
            ```

            **2. Analyze with VisualVM or Eclipse MAT:**
            - Open heap dump in VisualVM
            - Look for "Dominator Tree" (largest objects)
            - Find "GC Roots" to see what's holding references

            **Common memory leak culprits:**
            ```java
            // ❌ Static collection that grows unbounded
            public class UserCache {
                private static Map<Long, User> cache = new HashMap<>();

                public void cacheUser(User user) {
                    cache.put(user.getId(), user);  // Never removed!
                }
            }

            // ❌ ThreadLocal not cleaned up
            private static ThreadLocal<List<String>> requestData = new ThreadLocal<>();

            public void handleRequest() {
                requestData.set(new ArrayList<>());
                // ... process request ...
                // Forgot to call requestData.remove()!
            }

            // ❌ Event listeners never unregistered
            public class OrderService {
                public void processOrder(Order order) {
                    eventBus.register(this);  // Registered but never unregistered!
                    // ...
                }
            }

            // ❌ Unclosed resources
            public void processFile(String path) {
                FileInputStream fis = new FileInputStream(path);
                // Forgot to close! Memory leak + file handle leak
            }
            ```

            **The fix depends on the leak:**
            ```java
            // ✅ Use bounded cache with eviction
            @Bean
            public CacheManager cacheManager() {
                return new CaffeineCacheManager("users") {
                    {
                        setCaffeine(Caffeine.newBuilder()
                            .maximumSize(10_000)  // Limit to 10K entries
                            .expireAfterWrite(Duration.ofMinutes(30)));
                    }
                };
            }

            // ✅ Always clean ThreadLocal
            public void handleRequest() {
                try {
                    requestData.set(new ArrayList<>());
                    // ... process request ...
                } finally {
                    requestData.remove();  // Clean up!
                }
            }

            // ✅ Use try-with-resources
            public void processFile(String path) throws IOException {
                try (FileInputStream fis = new FileInputStream(path)) {
                    // Automatically closed
                }
            }
            ```

            **Heap dump analysis example:**
            ```
            Dominator Tree (top memory consumers):

            1. HashMap<Long, User> (2.1 GB)  ← 52% of heap!
               - Path to GC Root: UserCache.cache (static field)
               - Contains: 5,000,000 User objects
               - Diagnosis: Unbounded static cache
               - Fix: Add eviction policy or use external cache (Redis)

            2. ArrayList<String> (800 MB)
               - Path to GC Root: ThreadLocal → Thread pool threads
               - Diagnosis: ThreadLocal not cleaned up
               - Fix: Call ThreadLocal.remove()
            ```

            **Why the other answers are less likely:**

            **4GB heap is too large:**
            - G1GC is designed for large heaps (up to 64GB+)
            - 4GB is not abnormally large
            - If heap was too large, you'd see longer but less frequent GC
            - The issue is objects can't be freed, not heap size

            **Switch to Parallel GC:**
            - Parallel GC has even longer pauses than G1GC (not shorter!)
            - Parallel GC is for throughput, not low latency
            - Wouldn't solve the 5-second pause problem

            **Increase CPU cores:**
            - G1GC already uses multiple threads
            - CPU is unlikely the bottleneck during GC
            - A memory leak can't be fixed by adding more CPU

            **Prevention:**
            - Monitor heap usage trends (should be sawtooth, not always climbing)
            - Set up alerts for high heap usage (>80%)
            - Use `-XX:+HeapDumpOnOutOfMemoryError` to catch leaks
            - Regular heap dump analysis in staging environments
            """
        );
    }

    private static QuizQuestion createQuizQuestion3() {
        return new QuizQuestion(
            "Load testing shows your Spring Boot app handles 100 requests/second fine, but at 150 requests/second, response times jump from 50ms to 2000ms. Database CPU is at 90%. What should you investigate first?",
            Arrays.asList(
                "Increase JVM heap size to reduce GC pressure",
                "Add more application server instances to distribute load",
                "Investigate database query performance and missing indices - the database is the bottleneck",
                "Switch to Spring WebFlux for non-blocking I/O"
            ),
            2, // Database bottleneck (index 2)
            """
            **Correct answer:** Investigate database query performance and missing indices - the database is the bottleneck

            **Why the database is the bottleneck:**

            **Key evidence:**
            1. **Database CPU at 90%:** The database is working extremely hard
            2. **Response time jump:** 50ms → 2000ms (40x slower)
            3. **Threshold behavior:** Works fine at 100 req/s, breaks at 150 req/s

            **What's happening:**
            ```
            Load: 100 req/s (works fine)
            ┌─────────┐     ┌──────────┐
            │  App    │────▶│ Database │ (CPU: 60%)
            │ (idle)  │◀────│  (fast)  │
            └─────────┘     └──────────┘
            Response: 50ms

            Load: 150 req/s (breaks)
            ┌─────────┐     ┌──────────┐
            │  App    │────▶│ Database │ (CPU: 90%!)
            │(waiting)│ ... │  (slow)  │ Queue builds up!
            └─────────┘     └──────────┘
            Response: 2000ms ← App threads waiting for DB
            ```

            The database can't process queries fast enough, so requests queue up. The app is idle, waiting for database responses.

            **How to diagnose database bottlenecks:**

            **1. Check database slow query log:**
            ```sql
            -- PostgreSQL: Find slow queries
            SELECT query, calls, total_time, mean_time
            FROM pg_stat_statements
            ORDER BY mean_time DESC
            LIMIT 10;

            -- Example output:
            -- Query: SELECT * FROM orders WHERE user_id = ?
            -- Calls: 15000
            -- Mean time: 850ms ← SLOW!
            ```

            **2. Check for missing indices:**
            ```sql
            -- PostgreSQL: Find tables without indices on foreign keys
            SELECT
                table_name,
                column_name
            FROM information_schema.columns
            WHERE column_name LIKE '%_id'
            AND table_name NOT IN (
                SELECT tablename FROM pg_indexes
            );

            -- Example result:
            -- orders.user_id ← No index! Full table scan on every query!
            ```

            **3. Enable JPA query logging:**
            ```yaml
            spring.jpa.show-sql: true
            logging.level.org.hibernate.SQL: DEBUG
            ```

            **Common database performance issues:**

            **Missing index (most common):**
            ```sql
            -- Without index: scans 1M rows
            SELECT * FROM orders WHERE user_id = 123;  -- 850ms

            -- Add index
            CREATE INDEX idx_orders_user_id ON orders(user_id);

            -- With index: seeks directly
            SELECT * FROM orders WHERE user_id = 123;  -- 5ms (170x faster!)
            ```

            **N+1 query problem:**
            ```java
            // ❌ 1 + N queries
            List<User> users = userRepository.findAll();  // 1 query
            for (User user : users) {
                user.getOrders().size();  // N queries!
            }

            // At 150 req/s × 100 users = 15,000 queries/second to DB! 😱

            // ✅ Single query with JOIN
            @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
            List<User> findAllWithOrders();  // 1 query total

            // Now: 150 req/s × 1 query = 150 queries/second ✓
            ```

            **Connection pool exhaustion:**
            ```yaml
            # Check HikariCP metrics
            spring.datasource.hikari.maximum-pool-size: 10

            # If all 10 connections busy:
            # - New requests wait for available connection
            # - Wait time adds to response time
            ```

            **The solution:**
            1. Add missing indices on frequently queried columns
            2. Optimize slow queries (eliminate N+1, use JOIN instead of subqueries)
            3. Add query caching for read-heavy queries
            4. If still not enough, scale the database (read replicas, sharding)

            **Why the other answers don't solve this:**

            **Increase JVM heap size:**
            - GC is not the problem (database CPU is 90%, not app CPU)
            - More heap won't make database queries faster
            - The app is idle, waiting for the database

            **Add more application servers:**
            - Won't help because the database is the bottleneck
            - More app servers = more concurrent queries to DB = worse database overload!
            - You'd need to scale the database, not the app

            **Example:**
            ```
            Before: 1 app server → 150 queries/s → DB at 90% CPU

            After adding app servers:
            3 app servers → 450 queries/s → DB at 300% CPU (impossible!)
            Result: Even worse performance!
            ```

            **Switch to Spring WebFlux:**
            - WebFlux makes the app non-blocking, but database queries are still blocking
            - JDBC (used by Spring Data JPA) is blocking
            - WebFlux with JDBC = no benefit
            - You'd need R2DBC (reactive database driver), but that doesn't fix slow queries

            **The correct scaling approach:**

            **If database is the bottleneck:**
            1. **First:** Optimize queries and add indices (cheapest, fastest improvement)
            2. **Then:** Add caching (Redis) to reduce database load
            3. **Finally:** Scale database (read replicas, vertical scaling, sharding)

            **If application is the bottleneck (not this scenario):**
            1. **First:** Optimize code (profiling, fix slow methods)
            2. **Then:** Add more app server instances (horizontal scaling)

            **Monitoring to verify:**
            ```yaml
            # After optimization, you should see:
            # - Database CPU: 90% → 30%
            # - Response time: 2000ms → 50ms
            # - Successful requests at 150 req/s
            ```
            """
        );
    }
}
