package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Lesson 7: Monitoring and Observability with Spring Boot Actuator
 *
 * This lesson covers the three pillars of observability (logs, metrics, traces)
 * and how to implement production-grade monitoring using Spring Boot Actuator,
 * Micrometer, Prometheus, and distributed tracing.
 */
public class Lesson07Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Monitoring and Observability with Spring Boot Actuator",
            """
            # Monitoring and Observability with Spring Boot Actuator

            ## Why Observability Matters

            **Real-world scenario:** Your application crashes at 3 AM. Users report errors, but you have no idea what went wrong, which service failed, or how to reproduce the issue. Without observability, you're debugging in the dark.

            **Think of observability like a car dashboard:** Just as your car has gauges for speed, fuel, engine temperature, and warning lights, your application needs instruments to monitor health, performance, and issues in real-time.

            **Production impact:**
            - 🔍 **Faster incident detection:** Identify issues before users report them (reduce MTTR by 70%)
            - 🎯 **Root cause analysis:** Trace errors through distributed systems in minutes instead of hours
            - 📊 **Performance optimization:** Identify bottlenecks with data, not guesswork (improve response times by 50%)
            - 💰 **Cost savings:** Detect resource waste (unused caches, slow queries, memory leaks)

            ---

            ## The Three Pillars of Observability

            Modern observability is built on three complementary pillars:

            ### 1. Logs (What happened?)
            - **Purpose:** Detailed event records (errors, warnings, info)
            - **Tools:** Logback, SLF4J, Loki, ELK Stack
            - **Use case:** "Show me all errors in the payment service in the last hour"

            ### 2. Metrics (How much? How fast?)
            - **Purpose:** Quantitative measurements (counters, gauges, histograms)
            - **Tools:** Micrometer, Prometheus, Grafana
            - **Use case:** "What's the average response time of the checkout endpoint?"

            ### 3. Traces (Where did the request go?)
            - **Purpose:** Request flow through distributed systems
            - **Tools:** Micrometer Tracing, OpenTelemetry, Tempo, Zipkin
            - **Use case:** "Why is this specific API call slow? Which service is the bottleneck?"

            **Spring Boot 3 change:** Spring Cloud Sleuth was replaced by **Micrometer Tracing** with OpenTelemetry support.

            ---

            ## Spring Boot Actuator: Your Application's Health Dashboard

            Spring Boot Actuator provides production-ready features for monitoring and managing your application through HTTP or JMX endpoints.

            ### Adding Actuator

            ```xml
            <!-- Maven -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            ```

            ### Key Actuator Endpoints

            | Endpoint | Purpose | Example Use Case |
            |----------|---------|------------------|
            | `/actuator/health` | Application health status | Load balancer health checks |
            | `/actuator/metrics` | Available metric names | Browse available metrics |
            | `/actuator/metrics/{name}` | Specific metric value | Get current memory usage |
            | `/actuator/info` | Custom application info | Version, build info |
            | `/actuator/env` | Environment properties | Check active profiles |
            | `/actuator/loggers` | View/modify log levels | Enable DEBUG logging at runtime |
            | `/actuator/threaddump` | Thread dump | Diagnose deadlocks |
            | `/actuator/heapdump` | Heap dump | Analyze memory leaks |
            | `/actuator/prometheus` | Prometheus-formatted metrics | Scrape metrics for Prometheus |

            ### Enabling Actuator Endpoints

            ```yaml
            # application.yml
            management:
              endpoints:
                web:
                  exposure:
                    include: health,info,metrics,prometheus
              endpoint:
                health:
                  show-details: always
                  show-components: always

              # Customize the base path
              server:
                port: 8081  # Run management on different port
              # Or use: base-path: /manage
            ```

            **Security note:** Never expose sensitive endpoints like `/heapdump` or `/env` in production without authentication!

            ```java
            @Configuration
            public class ActuatorSecurityConfig {
                @Bean
                public SecurityFilterChain actuatorSecurity(HttpSecurity http) throws Exception {
                    http.securityMatcher("/actuator/**")
                        .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/actuator/health", "/actuator/info")
                                .permitAll()
                            .requestMatchers("/actuator/**")
                                .hasRole("ADMIN")
                        );
                    return http.build();
                }
            }
            ```

            ---

            ## Custom Health Indicators

            Create custom health checks to monitor critical dependencies:

            ```java
            import org.springframework.boot.actuate.health.Health;
            import org.springframework.boot.actuate.health.HealthIndicator;
            import org.springframework.stereotype.Component;

            @Component
            public class PaymentGatewayHealthIndicator implements HealthIndicator {

                private final PaymentGatewayClient paymentClient;

                public PaymentGatewayHealthIndicator(PaymentGatewayClient paymentClient) {
                    this.paymentClient = paymentClient;
                }

                @Override
                public Health health() {
                    try {
                        boolean isAvailable = paymentClient.ping();

                        if (isAvailable) {
                            return Health.up()
                                .withDetail("gateway", "Stripe")
                                .withDetail("status", "operational")
                                .withDetail("lastCheck", LocalDateTime.now())
                                .build();
                        } else {
                            return Health.down()
                                .withDetail("gateway", "Stripe")
                                .withDetail("error", "Ping failed")
                                .build();
                        }
                    } catch (Exception e) {
                        return Health.down()
                            .withDetail("gateway", "Stripe")
                            .withException(e)
                            .build();
                    }
                }
            }
            ```

            **Result at `/actuator/health`:**
            ```json
            {
              "status": "UP",
              "components": {
                "paymentGateway": {
                  "status": "UP",
                  "details": {
                    "gateway": "Stripe",
                    "status": "operational",
                    "lastCheck": "2025-01-15T10:30:45"
                  }
                },
                "db": {
                  "status": "UP",
                  "details": {
                    "database": "PostgreSQL",
                    "validationQuery": "isValid()"
                  }
                }
              }
            }
            ```

            ---

            ## Metrics with Micrometer

            Micrometer is to metrics what SLF4J is to logging—a vendor-neutral facade that lets you switch monitoring systems without changing your code.

            ### Adding Prometheus Support

            ```xml
            <dependency>
                <groupId>io.micrometer</groupId>
                <artifactId>micrometer-registry-prometheus</artifactId>
            </dependency>
            ```

            ```yaml
            # application.yml
            management:
              metrics:
                export:
                  prometheus:
                    enabled: true
                tags:
                  application: ${spring.application.name}
                  environment: ${spring.profiles.active}
            ```

            ### Custom Metrics

            ```java
            import io.micrometer.core.instrument.Counter;
            import io.micrometer.core.instrument.MeterRegistry;
            import io.micrometer.core.instrument.Timer;
            import org.springframework.stereotype.Service;

            @Service
            public class OrderService {

                private final OrderRepository orderRepository;
                private final Counter orderCreatedCounter;
                private final Counter orderFailedCounter;
                private final Timer orderProcessingTimer;

                public OrderService(
                    OrderRepository orderRepository,
                    MeterRegistry meterRegistry
                ) {
                    this.orderRepository = orderRepository;

                    // Counter: increments only
                    this.orderCreatedCounter = Counter.builder("orders.created")
                        .description("Total number of orders created")
                        .tag("type", "ecommerce")
                        .register(meterRegistry);

                    this.orderFailedCounter = Counter.builder("orders.failed")
                        .description("Total number of failed orders")
                        .tag("type", "ecommerce")
                        .register(meterRegistry);

                    // Timer: measures duration and count
                    this.orderProcessingTimer = Timer.builder("orders.processing.time")
                        .description("Time to process an order")
                        .tag("type", "ecommerce")
                        .register(meterRegistry);
                }

                public Order createOrder(OrderRequest request) {
                    return orderProcessingTimer.record(() -> {
                        try {
                            Order order = new Order(request);
                            Order saved = orderRepository.save(order);
                            orderCreatedCounter.increment();
                            return saved;
                        } catch (Exception e) {
                            orderFailedCounter.increment();
                            throw e;
                        }
                    });
                }
            }
            ```

            ### Metric Types

            **1. Counter** (only increases):
            ```java
            Counter.builder("api.calls")
                .tag("endpoint", "/api/users")
                .tag("method", "GET")
                .register(registry)
                .increment();
            ```

            **2. Gauge** (current value that can go up/down):
            ```java
            // Gauge that monitors a collection size
            Gauge.builder("queue.size", queue, Queue::size)
                .tag("queue", "order-processing")
                .register(registry);

            // Gauge for cache size
            Gauge.builder("cache.size", cache, Cache::estimatedSize)
                .register(registry);
            ```

            **3. Timer** (duration + count):
            ```java
            Timer timer = Timer.builder("api.response.time")
                .description("API response time")
                .tag("endpoint", "/api/orders")
                .register(registry);

            timer.record(() -> {
                // Your code here
            });

            // Or with Duration
            timer.record(Duration.ofMillis(150));
            ```

            **4. Distribution Summary** (distribution of values):
            ```java
            DistributionSummary.builder("order.amount")
                .description("Order amount distribution")
                .baseUnit("USD")
                .register(registry)
                .record(99.99);
            ```

            ### Using @Timed Annotation

            ```java
            @RestController
            @RequestMapping("/api/products")
            public class ProductController {

                @GetMapping("/{id}")
                @Timed(value = "products.get", description = "Get product by ID")
                public Product getProduct(@PathVariable Long id) {
                    return productService.getById(id);
                }
            }
            ```

            **Enable @Timed support:**
            ```java
            @Configuration
            public class MetricsConfig {
                @Bean
                public TimedAspect timedAspect(MeterRegistry registry) {
                    return new TimedAspect(registry);
                }
            }
            ```

            ---

            ## Distributed Tracing with Micrometer Tracing

            **Spring Boot 3 uses Micrometer Tracing** (replaced Spring Cloud Sleuth) with OpenTelemetry support.

            ### Adding Distributed Tracing

            ```xml
            <!-- Micrometer Tracing with OpenTelemetry -->
            <dependency>
                <groupId>io.micrometer</groupId>
                <artifactId>micrometer-tracing-bridge-otel</artifactId>
            </dependency>

            <!-- Exporter for Zipkin (or use Tempo) -->
            <dependency>
                <groupId>io.opentelemetry</groupId>
                <artifactId>opentelemetry-exporter-zipkin</artifactId>
            </dependency>
            ```

            ```yaml
            # application.yml
            management:
              tracing:
                sampling:
                  probability: 1.0  # 1.0 = 100% (use 0.1 = 10% in production)
              zipkin:
                tracing:
                  endpoint: http://localhost:9411/api/v2/spans

            logging:
              pattern:
                level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"
            ```

            ### How Tracing Works

            **Scenario:** User requests `/api/orders/123`:

            1. **API Gateway** receives request → Creates Trace ID `abc123`
            2. **Order Service** processes → Uses same Trace ID `abc123`, creates Span ID `span-1`
            3. **Order Service** calls **Inventory Service** → Propagates Trace ID `abc123`, new Span ID `span-2`
            4. **Inventory Service** queries **Database** → Same Trace ID, new Span ID `span-3`

            **Result:** You can view the entire request flow in Zipkin/Tempo with timing for each step.

            ### Custom Spans

            ```java
            import io.micrometer.tracing.Tracer;
            import io.micrometer.tracing.Span;
            import org.springframework.stereotype.Service;

            @Service
            public class PaymentService {

                private final Tracer tracer;

                public PaymentService(Tracer tracer) {
                    this.tracer = tracer;
                }

                public PaymentResult processPayment(PaymentRequest request) {
                    // Create custom span for payment processing
                    Span span = tracer.nextSpan().name("payment.process");

                    try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {
                        span.tag("payment.method", request.getMethod());
                        span.tag("payment.amount", request.getAmount().toString());

                        // Your payment logic
                        PaymentResult result = callPaymentGateway(request);

                        span.tag("payment.status", result.getStatus());
                        span.event("payment.completed");

                        return result;
                    } catch (Exception e) {
                        span.error(e);
                        throw e;
                    } finally {
                        span.end();
                    }
                }
            }
            ```

            ### Correlation IDs in Logs

            With tracing enabled, your logs automatically include trace and span IDs:

            ```
            2025-01-15 10:30:45 INFO  [order-service,abc123,span-1] Processing order 456
            2025-01-15 10:30:46 INFO  [inventory-service,abc123,span-2] Checking stock for product 789
            2025-01-15 10:30:47 ERROR [payment-service,abc123,span-3] Payment failed: insufficient funds
            ```

            **Benefit:** Given a trace ID, you can search logs across ALL services to see the complete request flow.

            ---

            ## Prometheus + Grafana Integration

            ### Prometheus Configuration

            ```yaml
            # prometheus.yml
            scrape_configs:
              - job_name: 'spring-boot-app'
                metrics_path: '/actuator/prometheus'
                static_configs:
                  - targets: ['localhost:8080']
            ```

            ### Prometheus Exemplars (New in Spring Boot 3)

            Exemplars link metrics to specific traces:

            ```yaml
            management:
              metrics:
                distribution:
                  percentiles-histogram:
                    http.server.requests: true
            ```

            **What this enables:** When viewing a metric spike in Grafana, you can click to see the actual trace that caused it!

            ### Common Prometheus Queries

            ```promql
            # Request rate (requests per second)
            rate(http_server_requests_seconds_count{uri="/api/orders"}[1m])

            # Average response time
            rate(http_server_requests_seconds_sum{uri="/api/orders"}[5m]) /
            rate(http_server_requests_seconds_count{uri="/api/orders"}[5m])

            # 95th percentile response time
            histogram_quantile(0.95,
              rate(http_server_requests_seconds_bucket{uri="/api/orders"}[5m])
            )

            # Error rate
            rate(http_server_requests_seconds_count{status=~"5.."}[1m])

            # JVM memory usage
            jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} * 100
            ```

            ### Grafana Dashboards

            **Pre-built Spring Boot 3 dashboards:**
            - Dashboard ID 19004: Spring Boot 3.x Statistics
            - Dashboard ID 12900: Spring Boot 2.1 System Monitor (works with SB3)

            **Key panels to include:**
            - Request rate (RPS)
            - Response time (p50, p95, p99)
            - Error rate (4xx, 5xx)
            - JVM memory (heap, non-heap)
            - GC pause time
            - Thread count
            - Database connection pool
            - Cache hit ratio

            ---

            ## Complete Observability Stack Example

            ```yaml
            # docker-compose.yml
            version: '3.8'
            services:
              # Your Spring Boot app
              app:
                image: my-spring-boot-app
                ports:
                  - "8080:8080"
                environment:
                  - MANAGEMENT_ZIPKIN_TRACING_ENDPOINT=http://tempo:9411

              # Metrics
              prometheus:
                image: prom/prometheus
                ports:
                  - "9090:9090"
                volumes:
                  - ./prometheus.yml:/etc/prometheus/prometheus.yml

              # Visualization
              grafana:
                image: grafana/grafana
                ports:
                  - "3000:3000"
                environment:
                  - GF_AUTH_ANONYMOUS_ENABLED=true

              # Traces
              tempo:
                image: grafana/tempo
                ports:
                  - "9411:9411"  # Zipkin compatibility
                  - "4317:4317"  # OpenTelemetry gRPC

              # Logs
              loki:
                image: grafana/loki
                ports:
                  - "3100:3100"
            ```

            ---

            ## Best Practices for Production Observability

            ### 1. Use Structured Logging
            ```java
            // ❌ Avoid string concatenation
            log.info("User " + userId + " created order " + orderId);

            // ✅ Use structured logging with MDC
            MDC.put("userId", userId.toString());
            MDC.put("orderId", orderId.toString());
            log.info("Order created");
            MDC.clear();

            // ✅ Or use parameterized logging
            log.info("User {} created order {}", userId, orderId);
            ```

            ### 2. Set Appropriate Trace Sampling
            ```yaml
            # Don't trace 100% in production (performance impact)
            management:
              tracing:
                sampling:
                  probability: 0.1  # 10% of requests
            ```

            ### 3. Add Business Metrics, Not Just Technical
            ```java
            // Technical metric (automatic)
            http.server.requests.seconds

            // Business metric (manual, more valuable!)
            registry.counter("business.revenue", "currency", "USD").increment(orderTotal);
            registry.counter("business.user.signups").increment();
            registry.gauge("business.inventory.critical", criticalStockItems);
            ```

            ### 4. Create SLO-Based Alerts
            ```yaml
            # Prometheus alert rules
            - alert: HighErrorRate
              expr: |
                rate(http_server_requests_seconds_count{status=~"5.."}[5m]) /
                rate(http_server_requests_seconds_count[5m]) > 0.05
              for: 5m
              annotations:
                summary: "Error rate above 5% for 5 minutes"

            - alert: SlowResponseTime
              expr: |
                histogram_quantile(0.95,
                  rate(http_server_requests_seconds_bucket[5m])
                ) > 1.0
              for: 5m
              annotations:
                summary: "95th percentile response time above 1 second"
            ```

            ### 5. Implement Health Check Hierarchy
            ```yaml
            management:
              endpoint:
                health:
                  group:
                    liveness:
                      include: ping
                    readiness:
                      include: db,redis,paymentGateway
            ```

            **Kubernetes integration:**
            ```yaml
            livenessProbe:
              httpGet:
                path: /actuator/health/liveness
                port: 8080
              initialDelaySeconds: 30
              periodSeconds: 10

            readinessProbe:
              httpGet:
                path: /actuator/health/readiness
                port: 8080
              initialDelaySeconds: 20
              periodSeconds: 5
            ```

            ---

            ## Common Mistakes to Avoid

            ### 1. Exposing Sensitive Endpoints Without Auth
            ```yaml
            # ❌ NEVER do this in production
            management:
              endpoints:
                web:
                  exposure:
                    include: "*"  # Exposes heapdump, env, etc.!

            # ✅ Be explicit
            management:
              endpoints:
                web:
                  exposure:
                    include: health,info,metrics,prometheus
            ```

            ### 2. Not Tagging Metrics
            ```java
            // ❌ Generic counter (can't filter by endpoint)
            Counter.builder("api.calls").register(registry).increment();

            // ✅ Tagged counter (can filter/aggregate)
            Counter.builder("api.calls")
                .tag("endpoint", "/api/users")
                .tag("method", "GET")
                .tag("status", "200")
                .register(registry)
                .increment();
            ```

            ### 3. Creating Too Many Unique Metric Names
            ```java
            // ❌ Creates unlimited metric names (cardinality explosion!)
            Counter.builder("user." + userId + ".logins")  // WRONG!

            // ✅ Use tags for dimensions
            Counter.builder("user.logins")
                .tag("userId", userId.toString())  // Still be careful with high-cardinality tags!
            ```

            ### 4. Ignoring Metric Cardinality
            **High cardinality = many unique tag combinations = memory problems**

            ```java
            // ❌ User ID as tag (millions of users = millions of metrics!)
            .tag("userId", userId.toString())

            // ✅ Use aggregated dimensions
            .tag("userType", user.getType())  // "free", "premium", "enterprise"
            .tag("region", user.getRegion())  // "us-east", "eu-west"
            ```

            ### 5. Not Monitoring the Monitors
            **Your observability stack needs monitoring too!**
            - Set up alerts if Prometheus scraping fails
            - Monitor Grafana uptime
            - Check trace sampling rate
            - Monitor disk usage for time-series data

            ---

            ## Observability Checklist for Production

            ✅ **Metrics:**
            - [ ] Spring Boot Actuator enabled
            - [ ] Prometheus endpoint exposed
            - [ ] Custom business metrics added
            - [ ] Metrics tagged appropriately
            - [ ] Grafana dashboards created

            ✅ **Traces:**
            - [ ] Micrometer Tracing configured
            - [ ] Trace sampling set (0.1 for production)
            - [ ] TraceId in log pattern
            - [ ] Custom spans for critical operations
            - [ ] Zipkin/Tempo collecting traces

            ✅ **Logs:**
            - [ ] Structured logging (JSON or ECS format)
            - [ ] Log levels appropriate (INFO default, DEBUG selectively)
            - [ ] Correlation IDs in logs
            - [ ] Centralized logging (Loki, ELK)
            - [ ] Log retention policy

            ✅ **Health Checks:**
            - [ ] Custom health indicators for dependencies
            - [ ] Liveness vs readiness probes
            - [ ] Health checks secured

            ✅ **Alerts:**
            - [ ] Error rate alerts
            - [ ] Response time alerts
            - [ ] Resource usage alerts (CPU, memory, disk)
            - [ ] Business metric alerts

            ✅ **Security:**
            - [ ] Actuator endpoints secured
            - [ ] Sensitive endpoints not exposed
            - [ ] Management port separate (optional)

            ---

            ## Summary

            In this lesson, you learned how to implement comprehensive observability for production Spring Boot applications:

            1. **Three pillars:** Logs (what happened), Metrics (how much/fast), Traces (request flow)
            2. **Spring Boot Actuator:** Production-ready endpoints for health, metrics, and management
            3. **Micrometer:** Vendor-neutral metrics facade with Prometheus integration
            4. **Custom metrics:** Counters, gauges, timers, and distribution summaries
            5. **Distributed tracing:** Micrometer Tracing with OpenTelemetry (replaced Sleuth)
            6. **Prometheus + Grafana:** Time-series metrics and visualization
            7. **Exemplars:** Link metrics to traces for faster debugging (Spring Boot 3 feature)
            8. **Best practices:** Structured logging, appropriate sampling, business metrics, SLO alerts

            **Real-world impact:** With proper observability, you can detect issues in seconds, debug across distributed services in minutes, and optimize performance with data instead of guesswork.

            **Next steps:** Performance optimization and profiling to make your application faster and more efficient using the insights from your observability stack!
            """,
            60
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        return new QuizQuestion(
            "Your Spring Boot application is experiencing slow response times, but you don't know which service in your microservices architecture is the bottleneck. Which observability pillar is MOST appropriate for diagnosing this issue?",
            Arrays.asList(
                "Logs - search for ERROR and WARN messages across all services",
                "Metrics - check the average response time metric for each service",
                "Traces - examine the distributed trace to see timing for each service in the request flow",
                "Health checks - verify all services are UP"
            ),
            2, // Traces (index 2)
            """
            **Correct answer:** Traces - examine the distributed trace to see timing for each service in the request flow

            **Why traces are best for this scenario:**

            A distributed trace shows the complete request flow with timing for each step:
            ```
            Request /api/orders/123 (total: 1200ms)
            ├─ API Gateway (50ms)
            ├─ Order Service (100ms)
            ├─ Inventory Service (900ms) ← BOTTLENECK!
            │  └─ Database query (850ms) ← ROOT CAUSE!
            └─ Payment Service (150ms)
            ```

            With a trace, you can immediately see that Inventory Service took 900ms out of 1200ms total, and 850ms was spent in a database query.

            **Why the other options are insufficient:**

            **Logs:** May show errors but won't show timing breakdown across services. You'd need to manually correlate timestamps across multiple services, which is error-prone.

            **Metrics:** Show average response time per service but don't show the relationship between services for a specific request. You'd see that Inventory Service is slow, but not *in the context of this specific request flow*.

            **Health checks:** Only show if services are UP/DOWN, not performance issues.

            **Best practice:** Use all three pillars together:
            1. **Traces** identify the slow service (Inventory)
            2. **Metrics** confirm it's a pattern (not a one-off)
            3. **Logs** provide detailed context (which query? what parameters?)

            **Micrometer Tracing configuration:**
            ```java
            // Automatically create spans for each service call
            @Bean
            public RestTemplate restTemplate(RestTemplateBuilder builder, Tracer tracer) {
                return builder
                    .interceptors(new TracingClientHttpRequestInterceptor(tracer))
                    .build();
            }
            ```

            Spring Boot 3 with Micrometer Tracing automatically propagates trace context across service boundaries using HTTP headers (W3C Trace Context standard).
            """
        );
    }

    private static QuizQuestion createQuizQuestion2() {
        return new QuizQuestion(
            "You create a custom metric to track user logins: `Counter.builder(\"user.\" + userId + \".logins\").register(registry).increment()`. In production with 1 million users, Prometheus crashes due to out-of-memory errors. What is the problem?",
            Arrays.asList(
                "Prometheus needs more memory allocation in the JVM",
                "The metric has too high cardinality - each unique userId creates a separate metric",
                "Counters should be Gauges for this use case",
                "The metric name should use dots instead of underscores"
            ),
            1, // High cardinality (index 1)
            """
            **Correct answer:** The metric has too high cardinality - each unique userId creates a separate metric

            **What is cardinality?**
            Cardinality is the number of unique time series created by a metric. Each unique combination of metric name + tags creates a separate time series.

            **The problem:**
            ```java
            // ❌ Creates 1 million separate metrics!
            Counter.builder("user.1.logins")     // Time series 1
            Counter.builder("user.2.logins")     // Time series 2
            Counter.builder("user.3.logins")     // Time series 3
            // ... 999,997 more time series ...
            ```

            Each time series consumes memory in Prometheus. With 1 million users, you're creating 1 million separate time series, which exhausts memory.

            **The solution - use tags instead:**
            ```java
            // ✅ Still creates 1 million time series (userId as tag)
            Counter.builder("user.logins")
                .tag("userId", userId.toString())  // Still problematic!
                .register(registry)
                .increment();

            // ✅✅ BEST: Use aggregated dimensions (low cardinality)
            Counter.builder("user.logins")
                .tag("userType", user.getType())      // "free", "premium", "enterprise" (3 values)
                .tag("region", user.getRegion())      // "us-east", "eu-west", etc. (10 values)
                .register(registry)                   // Total: 3 × 10 = 30 time series
                .increment();
            ```

            **Cardinality calculation:**
            - Metric with no tags: 1 time series
            - Metric with tag A (3 values) and tag B (10 values): 3 × 10 = 30 time series
            - Metric with userId tag (1M users): 1,000,000 time series ⚠️

            **Best practices:**
            1. **Avoid high-cardinality tags:** userId, requestId, timestamp, sessionId, IP address
            2. **Use aggregated dimensions:** userType, region, status, method
            3. **Limit tag values:** If a tag can have unlimited values, don't use it
            4. **Monitor cardinality:** `prometheus_tsdb_symbol_table_size_bytes`

            **When you need user-level data:**
            Use logs or traces, not metrics. Metrics are for aggregated data.

            ```java
            // For individual user tracking, use logs:
            log.info("User {} logged in", userId);

            // For aggregated insights, use metrics:
            meterRegistry.counter("user.logins",
                "userType", user.getType(),
                "region", user.getRegion()
            ).increment();
            ```

            **Why the other answers are wrong:**
            - **More JVM memory:** Just delays the problem; root cause is unbounded metric creation
            - **Use Gauges:** Gauge vs Counter doesn't solve cardinality; login count should be a Counter
            - **Dots vs underscores:** Naming convention doesn't affect cardinality or memory usage
            """
        );
    }

    private static QuizQuestion createQuizQuestion3() {
        return new QuizQuestion(
            "You're deploying your Spring Boot app to Kubernetes. The pod keeps restarting because the database connection takes 30 seconds to initialize, but the liveness probe fails after 10 seconds. What's the correct solution?",
            Arrays.asList(
                "Increase the liveness probe timeout to 60 seconds",
                "Use separate liveness and readiness probes - liveness checks if the app is alive, readiness checks if it's ready to serve traffic",
                "Disable health checks in Kubernetes",
                "Initialize the database connection asynchronously"
            ),
            1, // Separate liveness and readiness (index 1)
            """
            **Correct answer:** Use separate liveness and readiness probes - liveness checks if the app is alive, readiness checks if it's ready to serve traffic

            **Understanding Liveness vs Readiness:**

            **Liveness Probe:** "Is the application alive, or should Kubernetes restart it?"
            - **Purpose:** Detect deadlocks, infinite loops, crashes
            - **Action on failure:** Kubernetes RESTARTS the pod
            - **Check:** Basic ping, not dependent on external systems
            - **Example:** `/actuator/health/liveness`

            **Readiness Probe:** "Is the application ready to receive traffic?"
            - **Purpose:** Detect if dependencies (database, cache, etc.) are available
            - **Action on failure:** Kubernetes REMOVES from load balancer (but doesn't restart)
            - **Check:** Includes database, Redis, external APIs
            - **Example:** `/actuator/health/readiness`

            **Your scenario:**
            - Database takes 30 seconds to connect during startup
            - Liveness probe fails at 10 seconds → Kubernetes restarts pod
            - Pod restarts → Database connection starts over → Restarts again
            - **Result:** Infinite restart loop (CrashLoopBackOff)

            **The solution - Spring Boot health groups:**
            ```yaml
            # application.yml
            management:
              endpoint:
                health:
                  group:
                    liveness:
                      include: ping  # Simple, always succeeds
                    readiness:
                      include: db,redis,paymentGateway  # External dependencies
            ```

            **Kubernetes deployment:**
            ```yaml
            apiVersion: apps/v1
            kind: Deployment
            spec:
              template:
                spec:
                  containers:
                  - name: app
                    livenessProbe:
                      httpGet:
                        path: /actuator/health/liveness
                        port: 8080
                      initialDelaySeconds: 30   # Wait for JVM to start
                      periodSeconds: 10
                      failureThreshold: 3       # Restart after 3 failures

                    readinessProbe:
                      httpGet:
                        path: /actuator/health/readiness
                        port: 8080
                      initialDelaySeconds: 20
                      periodSeconds: 5
                      failureThreshold: 3       # Remove from LB after 3 failures
            ```

            **What happens now:**
            1. Pod starts, JVM initializes (30 seconds)
            2. Liveness probe starts after 30 seconds → SUCCESS (app is alive)
            3. Readiness probe starts after 20 seconds → FAILS (database not connected yet)
            4. Database connects at 30 seconds
            5. Readiness probe succeeds → Pod added to load balancer

            **No restart loop!** The pod is kept alive while waiting for dependencies.

            **Why the other answers are problematic:**

            **Increase liveness timeout to 60 seconds:**
            - Doesn't solve the root cause
            - What if database takes 90 seconds next time?
            - Liveness should be fast and simple, not dependent on slow external systems

            **Disable health checks:**
            - Kubernetes won't know when to restart crashed pods
            - Traffic will be sent to unhealthy pods
            - **Never disable health checks in production**

            **Initialize database asynchronously:**
            - Doesn't help - the app still can't serve requests without a database
            - Readiness probe would still fail until database is ready
            - This is a workaround, not a solution

            **Best practice - custom health indicator:**
            ```java
            @Component
            public class DatabaseHealthIndicator implements HealthIndicator {
                private final DataSource dataSource;

                @Override
                public Health health() {
                    try (Connection conn = dataSource.getConnection()) {
                        if (conn.isValid(2)) {
                            return Health.up().build();
                        }
                    } catch (Exception e) {
                        return Health.down(e).build();
                    }
                    return Health.down().build();
                }
            }
            ```

            This health indicator is automatically included in the readiness group, ensuring traffic isn't routed until the database is healthy.
            """
        );
    }
}
