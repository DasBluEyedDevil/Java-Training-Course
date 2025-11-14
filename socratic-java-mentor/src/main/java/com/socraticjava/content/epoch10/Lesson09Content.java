package com.socraticjava.content.epoch10;

import com.socraticjava.model.Lesson;
import com.socraticjava.model.QuizQuestion;

import java.util.Arrays;

/**
 * Lesson 9: Deployment and CI/CD - The Final Journey
 *
 * This lesson covers containerization with Docker, Kubernetes deployment,
 * CI/CD pipelines with GitHub Actions and Jenkins, cloud deployment,
 * and completing your capstone project for your portfolio.
 */
public class Lesson09Content {

    public static Lesson create() {
        Lesson lesson = new Lesson(
            "Deployment and CI/CD - The Final Journey",
            """
            # Deployment and CI/CD - The Final Journey

            ## You've Come So Far

            **Congratulations!** You've journeyed from printing "Hello, World!" to building production-grade, full-stack applications with Spring Boot. This final lesson will take your capstone project from your local machine to the cloud, accessible to the world.

            **Think of deployment like launching a rocket:** You've built the spacecraft (your application), tested all systems (unit tests, integration tests), optimized fuel efficiency (performance tuning), and added safety systems (security, monitoring). Now it's time for liftoff!

            **What you'll achieve in this lesson:**
            - 🐳 **Containerize** your application with Docker
            - ☸️ **Orchestrate** with Kubernetes for scalability
            - 🔄 **Automate** deployments with CI/CD pipelines
            - ☁️ **Deploy** to the cloud (AWS, Azure, or GCP)
            - 📊 **Monitor** your production application
            - 🎓 **Complete** your portfolio-ready capstone project

            ---

            ## Containerization with Docker

            ### Why Docker?

            **The classic problem:** "It works on my machine!"
            ```
            Developer: "The app works perfectly on my laptop!"
            Ops: "It crashes on the server. Different Java version, missing dependencies..."
            ```

            **Docker's solution:** Package your app + dependencies + runtime into a single container that runs identically everywhere.

            **Benefits:**
            - ✅ **Consistency:** Same environment from dev → test → production
            - ✅ **Isolation:** Each app runs in its own container
            - ✅ **Portability:** Run on any OS (Windows, Mac, Linux, cloud)
            - ✅ **Scalability:** Start 10 containers as easily as 1
            - ✅ **Efficiency:** Share OS kernel, use less memory than VMs

            ### Creating a Dockerfile

            **Dockerfile** is the recipe for building your container image.

            ```dockerfile
            # Multi-stage build for smaller images

            # Stage 1: Build the application
            FROM eclipse-temurin:21-jdk-alpine AS builder
            WORKDIR /app

            # Copy Maven/Gradle files for dependency caching
            COPY pom.xml .
            COPY mvnw .
            COPY .mvn .mvn

            # Download dependencies (cached if pom.xml unchanged)
            RUN ./mvnw dependency:go-offline

            # Copy source code
            COPY src src

            # Build the application
            RUN ./mvnw package -DskipTests

            # Stage 2: Create runtime image (smaller!)
            FROM eclipse-temurin:21-jre-alpine
            WORKDIR /app

            # Create non-root user for security
            RUN addgroup -S spring && adduser -S spring -G spring
            USER spring:spring

            # Copy JAR from builder stage
            COPY --from=builder /app/target/*.jar app.jar

            # Expose port
            EXPOSE 8080

            # Health check
            HEALTHCHECK --interval=30s --timeout=3s --retries=3 \\
                CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

            # Run the application
            ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
            ```

            **Key features:**
            - **Multi-stage build:** Smaller final image (builder artifacts not included)
            - **JRE instead of JDK:** 50% smaller (JRE = 100MB vs JDK = 200MB)
            - **Alpine Linux:** Lightweight base image (5MB vs 100MB for Ubuntu)
            - **Non-root user:** Security best practice
            - **Health check:** Kubernetes uses this to monitor container health
            - **MaxRAMPercentage:** JVM uses 75% of container memory

            ### Building and Running with Docker

            ```bash
            # Build the image
            docker build -t my-spring-app:1.0.0 .

            # Run the container
            docker run -d \\
                --name my-app \\
                -p 8080:8080 \\
                -e SPRING_PROFILES_ACTIVE=prod \\
                -e DB_URL=jdbc:postgresql://db:5432/mydb \\
                -e DB_USERNAME=user \\
                -e DB_PASSWORD=secret \\
                my-spring-app:1.0.0

            # Check logs
            docker logs -f my-app

            # Check health
            curl http://localhost:8080/actuator/health

            # Stop and remove
            docker stop my-app
            docker rm my-app
            ```

            ### Docker Compose for Local Development

            **docker-compose.yml** defines multi-container applications:

            ```yaml
            version: '3.8'

            services:
              # Your Spring Boot application
              app:
                build: .
                ports:
                  - "8080:8080"
                environment:
                  SPRING_PROFILES_ACTIVE: dev
                  DB_URL: jdbc:postgresql://db:5432/mydb
                  DB_USERNAME: postgres
                  DB_PASSWORD: postgres
                  REDIS_HOST: redis
                depends_on:
                  db:
                    condition: service_healthy
                  redis:
                    condition: service_started

              # PostgreSQL database
              db:
                image: postgres:16-alpine
                ports:
                  - "5432:5432"
                environment:
                  POSTGRES_DB: mydb
                  POSTGRES_USER: postgres
                  POSTGRES_PASSWORD: postgres
                volumes:
                  - postgres_data:/var/lib/postgresql/data
                healthcheck:
                  test: ["CMD-SHELL", "pg_isready -U postgres"]
                  interval: 10s
                  timeout: 5s
                  retries: 5

              # Redis cache
              redis:
                image: redis:7-alpine
                ports:
                  - "6379:6379"

              # Prometheus monitoring
              prometheus:
                image: prom/prometheus
                ports:
                  - "9090:9090"
                volumes:
                  - ./prometheus.yml:/etc/prometheus/prometheus.yml

              # Grafana dashboards
              grafana:
                image: grafana/grafana
                ports:
                  - "3000:3000"
                environment:
                  GF_AUTH_ANONYMOUS_ENABLED: true

            volumes:
              postgres_data:
            ```

            **Usage:**
            ```bash
            # Start all services
            docker-compose up -d

            # View logs
            docker-compose logs -f app

            # Stop all services
            docker-compose down

            # Stop and remove volumes (clean slate)
            docker-compose down -v
            ```

            ---

            ## Kubernetes: Orchestrating at Scale

            ### Why Kubernetes?

            **Problem Docker doesn't solve:** How do you manage 100 containers across 10 servers?
            - Which server should run which container?
            - What if a container crashes?
            - How do you update containers without downtime?
            - How do you balance traffic across containers?

            **Kubernetes solves all of this:**
            - ✅ **Auto-scaling:** Add/remove containers based on load
            - ✅ **Self-healing:** Restart crashed containers automatically
            - ✅ **Load balancing:** Distribute traffic across containers
            - ✅ **Rolling updates:** Zero-downtime deployments
            - ✅ **Service discovery:** Containers find each other automatically

            ### Kubernetes Architecture Overview

            ```
            ┌─────────────────────────────────────────────────────┐
            │               Kubernetes Cluster                    │
            ├─────────────────────────────────────────────────────┤
            │  Control Plane (Master Node)                        │
            │  ├─ API Server (kubectl talks to this)             │
            │  ├─ Scheduler (decides where to run pods)           │
            │  ├─ Controller Manager (maintains desired state)    │
            │  └─ etcd (configuration database)                   │
            ├─────────────────────────────────────────────────────┤
            │  Worker Nodes                                       │
            │  ├─ Node 1                                          │
            │  │  ├─ Pod 1 (your-app:1.0.0)                      │
            │  │  └─ Pod 2 (your-app:1.0.0)                      │
            │  ├─ Node 2                                          │
            │  │  ├─ Pod 3 (your-app:1.0.0)                      │
            │  │  └─ Pod 4 (postgres)                            │
            │  └─ Node 3                                          │
            │     └─ Pod 5 (redis)                               │
            └─────────────────────────────────────────────────────┘
            ```

            **Key concepts:**
            - **Pod:** Smallest deployable unit (1+ containers)
            - **Deployment:** Manages replicas and updates
            - **Service:** Load balancer for pods
            - **ConfigMap:** Configuration data
            - **Secret:** Sensitive data (passwords, API keys)
            - **Ingress:** External HTTP/HTTPS routing

            ### Kubernetes Deployment Manifest

            **deployment.yaml:**
            ```yaml
            apiVersion: apps/v1
            kind: Deployment
            metadata:
              name: my-spring-app
              labels:
                app: my-spring-app
            spec:
              replicas: 3  # Run 3 instances
              selector:
                matchLabels:
                  app: my-spring-app
              strategy:
                type: RollingUpdate
                rollingUpdate:
                  maxSurge: 1        # Add 1 extra pod during update
                  maxUnavailable: 0  # Keep all pods running during update
              template:
                metadata:
                  labels:
                    app: my-spring-app
                spec:
                  containers:
                  - name: app
                    image: my-registry/my-spring-app:1.0.0
                    ports:
                    - containerPort: 8080
                    env:
                    - name: SPRING_PROFILES_ACTIVE
                      value: "prod"
                    - name: DB_URL
                      valueFrom:
                        configMapKeyRef:
                          name: app-config
                          key: database.url
                    - name: DB_PASSWORD
                      valueFrom:
                        secretKeyRef:
                          name: app-secrets
                          key: database.password
                    resources:
                      requests:
                        memory: "512Mi"
                        cpu: "500m"      # 0.5 CPU
                      limits:
                        memory: "1Gi"
                        cpu: "1000m"     # 1 CPU
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

            **service.yaml (Load Balancer):**
            ```yaml
            apiVersion: v1
            kind: Service
            metadata:
              name: my-spring-app-service
            spec:
              type: LoadBalancer
              selector:
                app: my-spring-app
              ports:
              - protocol: TCP
                port: 80
                targetPort: 8080
            ```

            **configmap.yaml (Non-sensitive config):**
            ```yaml
            apiVersion: v1
            kind: ConfigMap
            metadata:
              name: app-config
            data:
              database.url: "jdbc:postgresql://postgres:5432/mydb"
              redis.host: "redis"
              logging.level: "INFO"
            ```

            **secret.yaml (Sensitive data):**
            ```yaml
            apiVersion: v1
            kind: Secret
            metadata:
              name: app-secrets
            type: Opaque
            data:
              # Base64 encoded (use: echo -n 'password' | base64)
              database.password: cGFzc3dvcmQ=
              jwt.secret: c3VwZXJzZWNyZXRrZXk=
            ```

            **Deploy to Kubernetes:**
            ```bash
            # Apply configuration
            kubectl apply -f configmap.yaml
            kubectl apply -f secret.yaml
            kubectl apply -f deployment.yaml
            kubectl apply -f service.yaml

            # Check status
            kubectl get pods
            kubectl get services
            kubectl get deployments

            # View logs
            kubectl logs -f deployment/my-spring-app

            # Scale up
            kubectl scale deployment my-spring-app --replicas=5

            # Rolling update
            kubectl set image deployment/my-spring-app app=my-registry/my-spring-app:1.1.0

            # Rollback if needed
            kubectl rollout undo deployment/my-spring-app
            ```

            ---

            ## CI/CD Pipelines: Automate Everything

            ### CI/CD Workflow

            ```
            Developer pushes code to GitHub
                    ↓
            GitHub Actions / Jenkins triggers
                    ↓
            1. Checkout code
            2. Run tests
            3. Build JAR
            4. Build Docker image
            5. Push to Docker Hub / ECR
            6. Deploy to Kubernetes
                    ↓
            Application running in production!
            ```

            ### GitHub Actions Pipeline

            **.github/workflows/deploy.yml:**
            ```yaml
            name: Deploy to Production

            on:
              push:
                branches: [main]
              pull_request:
                branches: [main]

            env:
              DOCKER_IMAGE: myusername/my-spring-app
              KUBE_NAMESPACE: production

            jobs:
              test:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/checkout@v4

                  - name: Set up JDK 21
                    uses: actions/setup-java@v4
                    with:
                      java-version: '21'
                      distribution: 'temurin'
                      cache: maven

                  - name: Run tests
                    run: mvn test

                  - name: Run integration tests
                    run: mvn verify -P integration-tests

              build:
                needs: test
                runs-on: ubuntu-latest
                if: github.ref == 'refs/heads/main'
                steps:
                  - uses: actions/checkout@v4

                  - name: Set up JDK 21
                    uses: actions/setup-java@v4
                    with:
                      java-version: '21'
                      distribution: 'temurin'
                      cache: maven

                  - name: Build with Maven
                    run: mvn package -DskipTests

                  - name: Log in to Docker Hub
                    uses: docker/login-action@v3
                    with:
                      username: ${{ secrets.DOCKER_USERNAME }}
                      password: ${{ secrets.DOCKER_PASSWORD }}

                  - name: Build and push Docker image
                    uses: docker/build-push-action@v5
                    with:
                      context: .
                      push: true
                      tags: |
                        ${{ env.DOCKER_IMAGE }}:latest
                        ${{ env.DOCKER_IMAGE }}:${{ github.sha }}

              deploy:
                needs: build
                runs-on: ubuntu-latest
                if: github.ref == 'refs/heads/main'
                steps:
                  - uses: actions/checkout@v4

                  - name: Configure kubectl
                    uses: azure/k8s-set-context@v3
                    with:
                      method: kubeconfig
                      kubeconfig: ${{ secrets.KUBE_CONFIG }}

                  - name: Deploy to Kubernetes
                    run: |
                      kubectl set image deployment/my-spring-app \\
                        app=${{ env.DOCKER_IMAGE }}:${{ github.sha }} \\
                        -n ${{ env.KUBE_NAMESPACE }}

                      kubectl rollout status deployment/my-spring-app \\
                        -n ${{ env.KUBE_NAMESPACE }}

                  - name: Verify deployment
                    run: |
                      kubectl get pods -n ${{ env.KUBE_NAMESPACE }}
                      kubectl get services -n ${{ env.KUBE_NAMESPACE }}
            ```

            ### Jenkins Pipeline (Alternative)

            **Jenkinsfile:**
            ```groovy
            pipeline {
                agent any

                environment {
                    DOCKER_IMAGE = 'myusername/my-spring-app'
                    DOCKER_TAG = "${BUILD_NUMBER}"
                }

                stages {
                    stage('Checkout') {
                        steps {
                            git branch: 'main',
                                url: 'https://github.com/myuser/my-app.git'
                        }
                    }

                    stage('Test') {
                        steps {
                            sh 'mvn test'
                        }
                        post {
                            always {
                                junit 'target/surefire-reports/*.xml'
                            }
                        }
                    }

                    stage('Build') {
                        steps {
                            sh 'mvn package -DskipTests'
                        }
                    }

                    stage('Docker Build') {
                        steps {
                            script {
                                docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                                docker.build("${DOCKER_IMAGE}:latest")
                            }
                        }
                    }

                    stage('Docker Push') {
                        steps {
                            script {
                                docker.withRegistry('https://registry.hub.docker.com',
                                                    'docker-hub-credentials') {
                                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                                    docker.image("${DOCKER_IMAGE}:latest").push()
                                }
                            }
                        }
                    }

                    stage('Deploy to Kubernetes') {
                        steps {
                            sh """
                                kubectl set image deployment/my-spring-app \\
                                    app=${DOCKER_IMAGE}:${DOCKER_TAG} \\
                                    -n production

                                kubectl rollout status deployment/my-spring-app \\
                                    -n production
                            """
                        }
                    }
                }

                post {
                    success {
                        echo 'Deployment successful!'
                    }
                    failure {
                        echo 'Deployment failed!'
                    }
                }
            }
            ```

            ---

            ## Cloud Deployment

            ### Deploying to AWS EKS (Elastic Kubernetes Service)

            **1. Create EKS cluster:**
            ```bash
            # Install eksctl
            brew install eksctl  # macOS
            # Or download from https://eksctl.io/

            # Create cluster
            eksctl create cluster \\
                --name my-spring-app-cluster \\
                --region us-east-1 \\
                --nodegroup-name standard-workers \\
                --node-type t3.medium \\
                --nodes 3 \\
                --nodes-min 1 \\
                --nodes-max 5 \\
                --managed

            # Configure kubectl
            aws eks update-kubeconfig --region us-east-1 --name my-spring-app-cluster
            ```

            **2. Create ECR repository (Docker registry):**
            ```bash
            # Create repository
            aws ecr create-repository --repository-name my-spring-app

            # Login to ECR
            aws ecr get-login-password --region us-east-1 | \\
                docker login --username AWS --password-stdin \\
                123456789012.dkr.ecr.us-east-1.amazonaws.com

            # Tag and push image
            docker tag my-spring-app:1.0.0 \\
                123456789012.dkr.ecr.us-east-1.amazonaws.com/my-spring-app:1.0.0
            docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/my-spring-app:1.0.0
            ```

            **3. Deploy to EKS:**
            ```bash
            # Apply Kubernetes manifests
            kubectl apply -f k8s/

            # Check status
            kubectl get all

            # Get load balancer URL
            kubectl get service my-spring-app-service
            # EXTERNAL-IP: a1234567890.us-east-1.elb.amazonaws.com
            ```

            ### Deploying to Azure AKS (Alternative)

            ```bash
            # Create resource group
            az group create --name myResourceGroup --location eastus

            # Create AKS cluster
            az aks create \\
                --resource-group myResourceGroup \\
                --name myAKSCluster \\
                --node-count 3 \\
                --enable-addons monitoring \\
                --generate-ssh-keys

            # Connect to cluster
            az aks get-credentials --resource-group myResourceGroup --name myAKSCluster

            # Deploy
            kubectl apply -f k8s/
            ```

            ---

            ## Monitoring Your Production Application

            ### Application Performance Monitoring (APM)

            **1. Enable Spring Boot Actuator metrics (from Lesson 18):**
            ```yaml
            management:
              endpoints:
                web:
                  exposure:
                    include: health,info,metrics,prometheus
              metrics:
                export:
                  prometheus:
                    enabled: true
            ```

            **2. Deploy Prometheus and Grafana to Kubernetes:**
            ```bash
            # Add Helm repository
            helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
            helm repo update

            # Install Prometheus
            helm install prometheus prometheus-community/kube-prometheus-stack

            # Access Grafana
            kubectl port-forward svc/prometheus-grafana 3000:80

            # Login: admin / prom-operator (default)
            # Import Spring Boot dashboard: ID 12900
            ```

            **3. Set up alerts for critical issues:**
            ```yaml
            # prometheus-rules.yaml
            apiVersion: monitoring.coreos.com/v1
            kind: PrometheusRule
            metadata:
              name: app-alerts
            spec:
              groups:
              - name: application
                rules:
                - alert: HighErrorRate
                  expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.05
                  for: 5m
                  annotations:
                    summary: "Error rate above 5%"

                - alert: HighMemoryUsage
                  expr: jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} > 0.9
                  for: 10m
                  annotations:
                    summary: "JVM heap usage above 90%"
            ```

            ### Centralized Logging

            **Deploy ELK Stack (Elasticsearch, Logstash, Kibana):**
            ```bash
            # Using Helm
            helm install elasticsearch elastic/elasticsearch
            helm install kibana elastic/kibana

            # Configure Spring Boot to send logs
            # Use Logstash Logback Encoder
            ```

            ```xml
            <!-- pom.xml -->
            <dependency>
                <groupId>net.logstash.logback</groupId>
                <artifactId>logstash-logback-encoder</artifactId>
                <version>7.4</version>
            </dependency>
            ```

            ```xml
            <!-- logback-spring.xml -->
            <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
                <destination>logstash:5000</destination>
                <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
            </appender>
            ```

            ---

            ## Completing Your Capstone Project

            ### Final Checklist

            ✅ **Code Quality:**
            - [ ] All tests passing (unit + integration)
            - [ ] Code coverage > 80%
            - [ ] No critical security vulnerabilities (Snyk, OWASP Dependency-Check)
            - [ ] Code reviewed and refactored

            ✅ **Documentation:**
            - [ ] README.md with project description, setup, and usage
            - [ ] API documentation (Swagger/OpenAPI)
            - [ ] Architecture diagram
            - [ ] Database schema diagram

            ✅ **Deployment:**
            - [ ] Dockerfile created and tested
            - [ ] Kubernetes manifests created
            - [ ] CI/CD pipeline configured
            - [ ] Deployed to cloud (AWS, Azure, or GCP)
            - [ ] Custom domain configured (optional)

            ✅ **Monitoring:**
            - [ ] Health checks configured
            - [ ] Metrics exported to Prometheus
            - [ ] Grafana dashboard created
            - [ ] Alerts configured

            ✅ **Security:**
            - [ ] Authentication implemented (JWT or OAuth2)
            - [ ] HTTPS enabled
            - [ ] Secrets managed securely (not in Git!)
            - [ ] Input validation implemented
            - [ ] SQL injection prevention verified

            ✅ **Performance:**
            - [ ] Caching implemented
            - [ ] Database queries optimized
            - [ ] Load tested (handles expected traffic)

            ### Portfolio Presentation

            **Your README.md should include:**

            ```markdown
            # Task Manager Pro

            A production-grade task management application built with Spring Boot 3,
            React, PostgreSQL, Redis, and deployed to AWS EKS with CI/CD.

            ## 🚀 Features

            - User authentication with JWT
            - Real-time task updates with WebSockets
            - Advanced filtering and pagination
            - Email notifications
            - RESTful API with Swagger documentation
            - Monitoring with Prometheus and Grafana

            ## 🛠️ Tech Stack

            **Backend:**
            - Java 21
            - Spring Boot 3.2
            - Spring Security 6
            - Spring Data JPA
            - PostgreSQL 16
            - Redis 7
            - Flyway (database migrations)

            **Frontend:**
            - React 18
            - TypeScript
            - Tailwind CSS

            **DevOps:**
            - Docker
            - Kubernetes
            - GitHub Actions (CI/CD)
            - AWS EKS
            - Prometheus & Grafana

            ## 📊 Architecture

            [Include architecture diagram]

            ## 🚦 Getting Started

            ### Prerequisites
            - Java 21
            - Docker
            - Node.js 18+

            ### Local Development
            \\```bash
            # Start dependencies
            docker-compose up -d

            # Run backend
            ./mvnw spring-boot:run

            # Run frontend
            cd frontend && npm start
            \\```

            ## 📈 Monitoring

            - Grafana: http://your-domain.com/grafana
            - Prometheus: http://your-domain.com/prometheus

            ## 🧪 Testing

            \\```bash
            # Run all tests
            ./mvnw verify

            # Code coverage report
            ./mvnw jacoco:report
            \\```

            ## 📝 API Documentation

            Swagger UI: http://your-domain.com/swagger-ui.html

            ## 🎯 Performance

            - Response time (p95): 150ms
            - Throughput: 500 req/s
            - Uptime: 99.9%

            ## 📜 License

            MIT License
            ```

            ---

            ## The Journey Continues

            ### What You've Accomplished

            **From Epoch 0 to Epoch 9, you've learned:**

            **Epoch 0:** What is programming? What is Java?
            **Epoch 1:** Variables, loops, methods - the bare essentials
            **Epoch 2:** Object-oriented programming - thinking in objects
            **Epoch 3:** Data structures and collections
            **Epoch 4:** Testing, build tools, professional practices
            **Epoch 5:** Databases and SQL
            **Epoch 6:** HTTP, REST APIs, web services
            **Epoch 7:** Spring Boot and enterprise Java
            **Epoch 8:** Full-stack integration with React
            **Epoch 9:** Production-grade applications with security, monitoring, performance, and deployment

            **You're now a full-stack Java developer!**

            ### Next Steps in Your Journey

            **1. Keep Building**
            - Add features to your capstone project
            - Build new projects that solve real problems
            - Contribute to open-source projects

            **2. Keep Learning**
            - **Microservices:** Spring Cloud, service mesh (Istio)
            - **Event-driven architecture:** Apache Kafka, RabbitMQ
            - **Advanced cloud:** AWS Lambda, serverless, cloud-native patterns
            - **Machine learning:** Integrate ML models with Spring Boot
            - **Advanced Kubernetes:** Helm, Operators, GitOps (ArgoCD)

            **3. Get Involved**
            - Join local Java user groups
            - Attend conferences (SpringOne, Devoxx, JavaOne)
            - Follow thought leaders on Twitter/LinkedIn
            - Write blog posts about what you've learned

            **4. Prepare for Interviews**
            - Practice coding challenges (LeetCode, HackerRank)
            - Study system design (design Twitter, design URL shortener)
            - Review Spring Boot interview questions
            - Polish your GitHub profile with quality projects

            **5. Apply for Jobs**
            - Your capstone project is your portfolio
            - Highlight production-ready features (security, monitoring, CI/CD)
            - Contribute to open source to build your reputation
            - Network with developers in your area

            ---

            ## Final Words

            **From beginner to professional in one journey.**

            You started by printing "Hello, World!" and wondering what all those symbols meant. Now you're deploying production applications to the cloud, implementing security best practices, optimizing performance, and monitoring systems at scale.

            **This is not the end - it's the beginning.**

            The skills you've learned are the foundation. The real learning happens when you build real projects, make mistakes, debug production issues, and solve problems you've never seen before.

            **Remember:**
            - Every expert was once a beginner
            - Every production system was once "Hello, World!"
            - Every senior developer still Googles error messages
            - The best way to learn is to build

            **You have everything you need to succeed as a developer:**
            - ✅ Solid understanding of Java fundamentals
            - ✅ Experience with modern frameworks (Spring Boot, React)
            - ✅ Knowledge of databases, APIs, and full-stack development
            - ✅ Security, performance, and monitoring best practices
            - ✅ DevOps skills (Docker, Kubernetes, CI/CD)
            - ✅ A portfolio project showcasing all of the above

            **Now go build something amazing!**

            The world needs your applications. The problems you solve, the features you build, the bugs you fix - they all matter. You're ready.

            **Welcome to the community of professional developers. We're glad to have you here.**

            ---

            ## Summary

            In this final lesson, you learned how to deploy your application to production:

            1. **Docker:** Containerize your application for consistency and portability
            2. **Kubernetes:** Orchestrate containers at scale with auto-scaling and self-healing
            3. **CI/CD:** Automate testing, building, and deployment with GitHub Actions or Jenkins
            4. **Cloud deployment:** Deploy to AWS EKS or Azure AKS
            5. **Monitoring:** Track your application with Prometheus, Grafana, and ELK
            6. **Portfolio:** Complete your capstone project with documentation and deployment

            **You've completed the entire Java Training Course - from beginner to full-stack developer!**

            **Your journey as a developer is just beginning. Keep learning, keep building, and keep pushing forward. You've got this!** 🚀
            """,
            70
        );

        // Add quiz questions
        lesson.addQuizQuestion(createQuizQuestion1());
        lesson.addQuizQuestion(createQuizQuestion2());
        lesson.addQuizQuestion(createQuizQuestion3());

        return lesson;
    }

    private static QuizQuestion createQuizQuestion1() {
        return new QuizQuestion(
            "Your Docker image for a Spring Boot application is 800MB. You want to reduce the size for faster deployments. Which approach will have the MOST impact?",
            Arrays.asList(
                "Use a smaller base image (alpine instead of ubuntu) and multi-stage build with JRE instead of JDK",
                "Remove comments from your Java code before building",
                "Compress the JAR file with gzip",
                "Reduce the number of dependencies in pom.xml"
            ),
            0, // Alpine + multi-stage + JRE (index 0)
            """
            **Correct answer:** Use a smaller base image (alpine instead of ubuntu) and multi-stage build with JRE instead of JDK

            **Impact breakdown of different approaches:**

            **1. Base image + multi-stage + JRE (HUGE impact):**
            ```dockerfile
            # ❌ Large image (800MB+)
            FROM eclipse-temurin:21-jdk
            COPY target/*.jar app.jar
            ENTRYPOINT ["java", "-jar", "app.jar"]

            # Image size: ~450MB (JDK) + 50MB (JAR) + 300MB (Ubuntu) = 800MB

            # ✅ Optimized image (150MB)
            # Stage 1: Build
            FROM eclipse-temurin:21-jdk-alpine AS builder
            WORKDIR /app
            COPY pom.xml .
            COPY src src
            RUN ./mvnw package -DskipTests

            # Stage 2: Runtime (only this goes to final image)
            FROM eclipse-temurin:21-jre-alpine
            WORKDIR /app
            COPY --from=builder /app/target/*.jar app.jar
            ENTRYPOINT ["java", "-jar", "app.jar"]

            # Image size: ~100MB (JRE) + 50MB (JAR) + 5MB (Alpine) = 155MB
            ```

            **Size reduction: 800MB → 155MB (80% smaller!)**

            **Why this works:**
            - **Alpine vs Ubuntu:** 5MB vs 300MB (60x smaller base OS)
            - **JRE vs JDK:** 100MB vs 450MB (JRE = runtime only, no compiler/debugger)
            - **Multi-stage build:** Builder stage discarded (Maven cache, source code, build tools not in final image)

            **2. Remove comments (minimal impact):**
            ```java
            // Comments are not included in compiled .class files!
            // They're already removed during compilation
            // Impact: 0MB savings
            ```

            **3. Compress JAR with gzip (doesn't work):**
            - Docker images are already compressed with layers
            - JVM can't run a gzipped JAR directly
            - You'd need to decompress at runtime, adding overhead
            - **Impact: 0MB savings (or negative due to decompression step)**

            **4. Reduce dependencies (moderate impact):**
            ```xml
            <!-- Removing unused dependencies helps, but not as much as base image -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <!-- If unused: saves ~10-20MB from JAR size -->
            ```

            **Impact: 50MB → 30-40MB JAR (20-40MB savings, but JAR is only 6% of total image size)**

            **Advanced optimization - Spring Boot 3 native image:**
            ```dockerfile
            # GraalVM native image (smallest possible)
            FROM ghcr.io/graalvm/native-image:21 AS builder
            WORKDIR /app
            COPY . .
            RUN ./mvnw -Pnative native:compile

            FROM debian:bookworm-slim
            COPY --from=builder /app/target/my-app /app
            ENTRYPOINT ["/app"]

            # Image size: ~20MB (no JVM needed!)
            # Startup time: 0.05s vs 3s
            # Memory usage: 20MB vs 200MB
            ```

            **Real-world example:**
            ```
            Before optimization:
            - Base: ubuntu (300MB)
            - JDK: (450MB)
            - JAR: (50MB)
            - Total: 800MB
            - Pull time: 2 minutes on slow network
            - Disk usage: 800MB × 10 versions = 8GB

            After optimization:
            - Base: alpine (5MB)
            - JRE: (100MB)
            - JAR: (50MB)
            - Total: 155MB
            - Pull time: 15 seconds
            - Disk usage: 155MB × 10 versions = 1.55GB

            Benefits:
            - 5x faster image pulls
            - 5x less disk usage
            - 5x faster CI/CD pipelines
            - 5x less bandwidth costs
            ```

            **Best practices for small Docker images:**
            1. Use Alpine Linux base images
            2. Use JRE instead of JDK for runtime
            3. Multi-stage builds (builder + runtime)
            4. .dockerignore to exclude unnecessary files
            5. Combine RUN commands to reduce layers
            6. Remove package manager cache after install

            **Example .dockerignore:**
            ```
            target/
            .git/
            .idea/
            *.md
            .env
            docker-compose.yml
            ```

            **Layer optimization:**
            ```dockerfile
            # ❌ Many layers
            RUN apt-get update
            RUN apt-get install -y curl
            RUN apt-get install -y vim
            # 3 layers

            # ✅ Single layer
            RUN apt-get update && \\
                apt-get install -y curl vim && \\
                rm -rf /var/lib/apt/lists/*
            # 1 layer + cleaned cache
            ```
            """
        );
    }

    private static QuizQuestion createQuizQuestion2() {
        return new QuizQuestion(
            "Your Kubernetes deployment shows all 3 pods in 'Running' state, but the service returns 'Connection refused' errors. The deployment has a readiness probe checking `/actuator/health/readiness`. What is the most likely cause?",
            Arrays.asList(
                "The pods are running but failing the readiness probe, so they're not added to the service endpoints",
                "Kubernetes is still pulling the Docker image from the registry",
                "The service selector doesn't match the pod labels",
                "The load balancer is not configured correctly"
            ),
            0, // Failing readiness probe (index 0)
            """
            **Correct answer:** The pods are running but failing the readiness probe, so they're not added to the service endpoints

            **Understanding the problem:**

            **Kubernetes pod states:**
            - **Running:** Container started successfully (does NOT mean app is ready!)
            - **Ready:** Pod passed readiness probe (safe to send traffic)

            **Key insight:** Pods can be "Running" but not "Ready"!

            ```
            Pod Lifecycle:

            Pending → ContainerCreating → Running → Ready
                                            ↑         ↑
                                            |         |
                                      Liveness   Readiness
                                       passed     passed

            Your situation:
            Pending → ContainerCreating → Running (but NOT Ready)
                                            ↑
                                            Readiness probe FAILING
            ```

            **What's happening:**
            ```yaml
            # Your deployment
            readinessProbe:
              httpGet:
                path: /actuator/health/readiness
                port: 8080
              initialDelaySeconds: 20
              periodSeconds: 5

            # Kubernetes checks /actuator/health/readiness every 5 seconds
            # If it returns non-200 status, pod is NOT added to service endpoints
            ```

            **Check pod readiness:**
            ```bash
            # View pod status
            kubectl get pods

            # Output:
            NAME                         READY   STATUS    RESTARTS   AGE
            my-app-7d8f5c9b4-abc12      0/1     Running   0          2m
            my-app-7d8f5c9b4-def34      0/1     Running   0          2m
            my-app-7d8f5c9b4-ghi56      0/1     Running   0          2m
                                        ↑
                        0/1 = NOT READY (readiness probe failing)

            # Check endpoints (where service sends traffic)
            kubectl get endpoints my-app-service

            # Output:
            NAME              ENDPOINTS      AGE
            my-app-service    <none>         5m
                              ↑
                        NO ENDPOINTS! Service has no pods to route to!
            ```

            **Diagnose the readiness probe failure:**
            ```bash
            # View detailed pod status
            kubectl describe pod my-app-7d8f5c9b4-abc12

            # Output shows:
            Events:
              Type     Reason     Message
              ----     ------     -------
              Warning  Unhealthy  Readiness probe failed: HTTP probe failed with statuscode: 503
            ```

            **Common causes of readiness probe failure:**

            **1. Database not ready:**
            ```java
            // Readiness check includes database health
            @Component
            public class DatabaseHealthIndicator implements HealthIndicator {
                @Override
                public Health health() {
                    try {
                        dataSource.getConnection();
                        return Health.up().build();
                    } catch (Exception e) {
                        return Health.down(e).build();  // Probe fails!
                    }
                }
            }

            // Database not ready → health check DOWN → readiness probe fails
            ```

            **2. Missing dependency (Redis, external API):**
            ```yaml
            # application.yml includes Redis in readiness
            management:
              endpoint:
                health:
                  group:
                    readiness:
                      include: db,redis  # If Redis not available, probe fails
            ```

            **3. Wrong port in probe:**
            ```yaml
            # ❌ Wrong port
            readinessProbe:
              httpGet:
                path: /actuator/health/readiness
                port: 8080  # App runs on 8081!

            # ✅ Correct port
            readinessProbe:
              httpGet:
                path: /actuator/health/readiness
                port: 8081
            ```

            **How to fix:**

            **Solution 1: Wait for dependencies**
            ```yaml
            # Use initContainers to wait for database
            spec:
              initContainers:
              - name: wait-for-db
                image: busybox
                command: ['sh', '-c',
                  'until nc -z postgres 5432; do echo waiting for db; sleep 2; done']
              containers:
              - name: app
                # ... app container ...
            ```

            **Solution 2: Separate liveness and readiness**
            ```yaml
            management:
              endpoint:
                health:
                  group:
                    liveness:
                      include: ping  # Simple check (app alive?)
                    readiness:
                      include: db,redis,externalApi  # Dependencies ready?
            ```

            **Solution 3: Increase initialDelaySeconds**
            ```yaml
            readinessProbe:
              httpGet:
                path: /actuator/health/readiness
                port: 8080
              initialDelaySeconds: 60  # Give database time to connect
              periodSeconds: 5
            ```

            **Why the other answers are less likely:**

            **Still pulling image:**
            - If pulling image, status would be "ImagePullBackOff" or "ContainerCreating", not "Running"
            - Once status is "Running", image is already pulled

            **Service selector mismatch:**
            ```yaml
            # This would be a config error, but easy to check:
            kubectl get service my-app-service -o yaml

            # Shows selector:
            spec:
              selector:
                app: my-app  # Must match pod labels

            kubectl get pods --show-labels
            # Verify pods have label: app=my-app
            ```

            **Load balancer misconfigured:**
            - Load balancer is external (for exposing service to internet)
            - Internal traffic (pod → service → pod) doesn't use external LB
            - Error message would be different ("no route to host", not "connection refused")

            **Verification after fix:**
            ```bash
            # Pods should show 1/1 READY
            kubectl get pods
            NAME                         READY   STATUS    RESTARTS   AGE
            my-app-7d8f5c9b4-abc12      1/1     Running   0          5m
            my-app-7d8f5c9b4-def34      1/1     Running   0          5m
            my-app-7d8f5c9b4-ghi56      1/1     Running   0          5m

            # Endpoints should list pod IPs
            kubectl get endpoints my-app-service
            NAME              ENDPOINTS                                 AGE
            my-app-service    10.1.0.5:8080,10.1.0.6:8080,10.1.0.7:8080  10m

            # Service should respond
            kubectl port-forward svc/my-app-service 8080:80
            curl http://localhost:8080/actuator/health
            # {"status":"UP"}
            ```
            """
        );
    }

    private static QuizQuestion createQuizQuestion3() {
        return new QuizQuestion(
            "Your CI/CD pipeline successfully builds and pushes a Docker image with tag `myapp:${github.sha}`, but the Kubernetes deployment still runs the old version. The deployment manifest uses `image: myapp:latest`. What's the problem?",
            Arrays.asList(
                "Kubernetes caches the 'latest' tag and doesn't pull the new image - use the specific SHA tag in the deployment instead",
                "The Docker registry is down",
                "Kubernetes doesn't have permission to pull from the registry",
                "The CI/CD pipeline needs to restart the pods manually"
            ),
            0, // Image pull policy with 'latest' tag (index 0)
            """
            **Correct answer:** Kubernetes caches the 'latest' tag and doesn't pull the new image - use the specific SHA tag in the deployment instead

            **Understanding the problem:**

            **What happened:**
            1. CI/CD builds image: `myapp:abc123` (git commit SHA)
            2. CI/CD also tags as: `myapp:latest`
            3. Both pushed to Docker Hub
            4. Kubernetes deployment uses: `image: myapp:latest`
            5. Kubernetes sees "I already have `myapp:latest`" → doesn't pull new image
            6. Old version still running!

            **Why Kubernetes doesn't pull the new 'latest' image:**

            **Image Pull Policy:**
            ```yaml
            # Kubernetes default behavior
            spec:
              containers:
              - name: app
                image: myapp:latest
                imagePullPolicy: IfNotPresent  # Default for 'latest' tag!
                # IfNotPresent = only pull if not already on node
            ```

            **The problem with 'latest':**
            ```
            Timeline:

            Day 1:
            - Build: myapp:latest (v1.0)
            - Deploy to K8s
            - K8s pulls myapp:latest (v1.0) → Cached on node

            Day 2:
            - Build: myapp:latest (v2.0)  # Same tag, different image!
            - Push to Docker Hub
            - Trigger K8s deployment
            - K8s: "I have myapp:latest" → Uses cached v1.0 😱

            Result: Running old version!
            ```

            **The solution - use immutable tags:**

            **❌ Bad: Using 'latest' tag**
            ```yaml
            # deployment.yaml
            spec:
              containers:
              - name: app
                image: myapp:latest  # Ambiguous! Which version?
            ```

            **✅ Good: Using commit SHA**
            ```yaml
            # deployment.yaml (template)
            spec:
              containers:
              - name: app
                image: myapp:PLACEHOLDER_TAG
            ```

            ```yaml
            # GitHub Actions
            - name: Deploy to Kubernetes
              run: |
                # Replace placeholder with actual SHA
                sed -i "s|PLACEHOLDER_TAG|${{ github.sha }}|g" deployment.yaml
                kubectl apply -f deployment.yaml
            ```

            **✅ Better: Using kubectl set image**
            ```yaml
            # GitHub Actions
            - name: Deploy to Kubernetes
              run: |
                kubectl set image deployment/myapp \\
                  app=myapp:${{ github.sha }} \\
                  --record
                # --record saves the command in rollout history

                # Wait for rollout to complete
                kubectl rollout status deployment/myapp
            ```

            **Why this works:**
            ```
            Timeline with immutable tags:

            Day 1:
            - Build: myapp:abc123
            - Deploy: kubectl set image ... myapp:abc123
            - K8s pulls myapp:abc123 → Cached

            Day 2:
            - Build: myapp:def456  # Different tag!
            - Deploy: kubectl set image ... myapp:def456
            - K8s: "I don't have myapp:def456" → Pulls new image ✓

            Result: Running new version!
            ```

            **Best practices for image tagging:**

            **1. Semantic versioning + SHA**
            ```bash
            # Tag with both version and commit
            docker tag myapp myapp:1.2.3
            docker tag myapp myapp:1.2.3-abc123
            docker tag myapp myapp:latest

            docker push myapp:1.2.3
            docker push myapp:1.2.3-abc123
            docker push myapp:latest
            ```

            **2. Use imagePullPolicy: Always (last resort)**
            ```yaml
            # Forces pull on every pod restart (slower!)
            spec:
              containers:
              - name: app
                image: myapp:latest
                imagePullPolicy: Always  # Always pull, even if cached
            ```

            **⚠️ Downside:** Slower deployments, more registry bandwidth

            **3. Rolling update with unique tags**
            ```yaml
            # GitHub Actions complete pipeline
            name: Deploy

            on:
              push:
                branches: [main]

            env:
              IMAGE_TAG: ${{ github.sha }}
              REGISTRY: myregistry.azurecr.io
              IMAGE_NAME: myapp

            jobs:
              deploy:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/checkout@v4

                  - name: Build and push
                    run: |
                      docker build -t $REGISTRY/$IMAGE_NAME:$IMAGE_TAG .
                      docker push $REGISTRY/$IMAGE_NAME:$IMAGE_TAG

                  - name: Deploy to Kubernetes
                    run: |
                      # Update deployment with new image tag
                      kubectl set image deployment/myapp \\
                        app=$REGISTRY/$IMAGE_NAME:$IMAGE_TAG

                      # Verify rollout
                      kubectl rollout status deployment/myapp

                      # If rollout fails, automatic rollback
                      if [ $? -ne 0 ]; then
                        kubectl rollout undo deployment/myapp
                        exit 1
                      fi
            ```

            **Rollout history:**
            ```bash
            # View deployment history (with --record flag)
            kubectl rollout history deployment/myapp

            # Output:
            REVISION  CHANGE-CAUSE
            1         kubectl set image deployment/myapp app=myapp:abc123 --record=true
            2         kubectl set image deployment/myapp app=myapp:def456 --record=true
            3         kubectl set image deployment/myapp app=myapp:ghi789 --record=true

            # Rollback to previous version
            kubectl rollout undo deployment/myapp

            # Rollback to specific revision
            kubectl rollout undo deployment/myapp --to-revision=1
            ```

            **Why the other answers are less likely:**

            **Docker registry is down:**
            - If registry down, you'd see "ImagePullBackOff" error in pod status
            - The problem states deployment "still runs old version", implying no errors
            - Old version runs = image was pulled successfully at some point

            **No permission to pull:**
            - Would show "ErrImagePull" or "ImagePullBackOff" in pod status
            - Wouldn't run old version, would fail to start new pods
            - Need imagePullSecrets for private registries:

            ```yaml
            spec:
              imagePullSecrets:
              - name: registry-credentials
              containers:
              - name: app
                image: myregistry/myapp:latest
            ```

            **Need to restart pods manually:**
            - Not necessary with `kubectl set image` (triggers rolling update automatically)
            - Manual restart would be: `kubectl rollout restart deployment/myapp`
            - But this wouldn't help if using cached 'latest' tag

            **Complete CI/CD best practices:**
            1. ✅ Use immutable tags (commit SHA, semantic version)
            2. ✅ Avoid 'latest' tag in production deployments
            3. ✅ Use `kubectl set image` to trigger rolling updates
            4. ✅ Wait for rollout status before marking deployment successful
            5. ✅ Implement automatic rollback on failure
            6. ✅ Keep rollout history with --record flag
            """
        );
    }
}
