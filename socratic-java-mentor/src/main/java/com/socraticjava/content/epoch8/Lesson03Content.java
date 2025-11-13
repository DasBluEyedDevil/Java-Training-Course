package com.socraticjava.content.epoch8;

import com.socraticjava.model.Challenge;
import com.socraticjava.model.ChallengeType;
import com.socraticjava.model.Lesson;

/**
 * Lesson 8.3: REST API Design Best Practices
 */
public class Lesson03Content {

    public static Lesson create() {
        return new Lesson.Builder("epoch-8-lesson-3", "Lesson 8.3: REST API Design - Professional Standards")
            .addTheory("What Makes a Good REST API?",
                "REST = Representational State Transfer\n\n" +
                "A well-designed API should be:\n\n" +
                "✓ INTUITIVE - Easy to understand and use\n" +
                "✓ CONSISTENT - Follows predictable patterns\n" +
                "✓ DISCOVERABLE - Clear what each endpoint does\n" +
                "✓ SCALABLE - Can handle growth\n" +
                "✓ WELL-DOCUMENTED - Clear examples and responses\n\n" +
                "BAD API:\n" +
                "POST /createNewUser\n" +
                "GET /getUserById?id=123\n" +
                "POST /deleteUser\n\n" +
                "GOOD API:\n" +
                "POST /api/users\n" +
                "GET /api/users/123\n" +
                "DELETE /api/users/123\n\n" +
                "The difference? HTTP methods + resource-based URLs!")
            .addTheory("Resource Naming Conventions (2024-2025 Standards)",
                "RULE 1: Use PLURAL NOUNS for collections:\n" +
                "✓ /api/users (not /api/user)\n" +
                "✓ /api/orders (not /api/order)\n" +
                "✓ /api/products (not /api/product)\n\n" +
                "RULE 2: Use KEBAB-CASE for multi-word names:\n" +
                "✓ /api/shipping-addresses\n" +
                "✓ /api/order-items\n" +
                "❌ /api/shippingAddresses (camelCase)\n" +
                "❌ /api/shipping_addresses (snake_case)\n\n" +
                "RULE 3: NO VERBS in URLs:\n" +
                "❌ /api/createUser\n" +
                "❌ /api/getUsers\n" +
                "❌ /api/deleteOrder\n\n" +
                "Why? The HTTP METHOD is the verb!\n" +
                "✓ POST /api/users (create)\n" +
                "✓ GET /api/users (retrieve)\n" +
                "✓ DELETE /api/orders/123 (delete)\n\n" +
                "RULE 4: Nesting to ONE level for relationships:\n" +
                "✓ /api/users/123/orders (user's orders)\n" +
                "✓ /api/orders/456/items (order's items)\n" +
                "❌ /api/users/123/orders/456/items/789/details (too deep!)")
            .addAnalogy("HTTP Methods are Like Restaurant Actions",
                "Imagine managing a restaurant menu database:\n\n" +
                "📖 GET /api/menu-items\n" +
                "Action: Browse the menu\n" +
                "Like: Customer looking at menu\n" +
                "Changes nothing, just reads\n\n" +
                "➕ POST /api/menu-items\n" +
                "Action: Add new dish to menu\n" +
                "Like: Chef creates new recipe\n" +
                "Server generates ID, returns location\n\n" +
                "🔄 PUT /api/menu-items/123\n" +
                "Action: Replace entire menu item\n" +
                "Like: Completely redesign a dish\n" +
                "Must send ALL fields (name, price, ingredients)\n\n" +
                "✏️ PATCH /api/menu-items/123\n" +
                "Action: Update only price\n" +
                "Like: Just change the price tag\n" +
                "Send only changed fields: {\"price\": 15.99}\n\n" +
                "🗑️ DELETE /api/menu-items/123\n" +
                "Action: Remove from menu\n" +
                "Like: Discontinue a dish\n" +
                "Item gone, can't be retrieved")
            .addTheory("The 5 Essential HTTP Methods",
                "GET - Retrieve data:\n" +
                "@GetMapping(\"/api/products\")\n" +
                "public List<Product> getAll() { }\n\n" +
                "@GetMapping(\"/api/products/{id}\")\n" +
                "public Product getById(@PathVariable Long id) { }\n\n" +
                "POST - Create new resource:\n" +
                "@PostMapping(\"/api/products\")\n" +
                "public ResponseEntity<Product> create(@RequestBody Product product) {\n" +
                "    Product saved = productService.save(product);\n" +
                "    return ResponseEntity\n" +
                "        .created(URI.create(\"/api/products/\" + saved.getId()))\n" +
                "        .body(saved);\n" +
                "}\n\n" +
                "PUT - Replace entire resource:\n" +
                "@PutMapping(\"/api/products/{id}\")\n" +
                "public Product replace(@PathVariable Long id, @RequestBody Product product) {\n" +
                "    return productService.replace(id, product);\n" +
                "}\n\n" +
                "PATCH - Partial update:\n" +
                "@PatchMapping(\"/api/products/{id}\")\n" +
                "public Product update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {\n" +
                "    return productService.partialUpdate(id, updates);\n" +
                "}\n\n" +
                "DELETE - Remove resource:\n" +
                "@DeleteMapping(\"/api/products/{id}\")\n" +
                "public ResponseEntity<Void> delete(@PathVariable Long id) {\n" +
                "    productService.delete(id);\n" +
                "    return ResponseEntity.noContent().build();\n" +
                "}")
            .addTheory("HTTP Status Codes - The Response Language",
                "2xx - SUCCESS:\n" +
                "200 OK - Request succeeded (GET, PUT, PATCH)\n" +
                "201 Created - Resource created (POST)\n" +
                "204 No Content - Success but no data to return (DELETE)\n\n" +
                "4xx - CLIENT ERRORS (User's fault):\n" +
                "400 Bad Request - Invalid data sent\n" +
                "401 Unauthorized - Not authenticated (no login)\n" +
                "403 Forbidden - Authenticated but not authorized\n" +
                "404 Not Found - Resource doesn't exist\n" +
                "409 Conflict - Duplicate or conflicting data\n" +
                "422 Unprocessable Entity - Validation failed\n" +
                "429 Too Many Requests - Rate limit exceeded\n\n" +
                "5xx - SERVER ERRORS (Our fault):\n" +
                "500 Internal Server Error - Something broke\n" +
                "503 Service Unavailable - Server down/overloaded\n\n" +
                "Example in Spring Boot:\n\n" +
                "@PostMapping(\"/api/users\")\n" +
                "public ResponseEntity<User> create(@Valid @RequestBody User user) {\n" +
                "    if (userService.existsByEmail(user.getEmail())) {\n" +
                "        return ResponseEntity.status(HttpStatus.CONFLICT)\n" +
                "            .body(null);  // 409 Conflict\n" +
                "    }\n" +
                "    User saved = userService.save(user);\n" +
                "    return ResponseEntity\n" +
                "        .status(HttpStatus.CREATED)  // 201 Created\n" +
                "        .body(saved);\n" +
                "}")
            .addKeyPoint("Consistent Response Structure",
                "Always return JSON in a predictable format:\n\n" +
                "SUCCESS Response:\n" +
                "{\n" +
                "  \"data\": {\n" +
                "    \"id\": 123,\n" +
                "    \"name\": \"Product Name\",\n" +
                "    \"price\": 29.99\n" +
                "  },\n" +
                "  \"timestamp\": \"2025-01-15T10:30:00Z\"\n" +
                "}\n\n" +
                "ERROR Response:\n" +
                "{\n" +
                "  \"error\": {\n" +
                "    \"code\": 404,\n" +
                "    \"message\": \"Product not found\",\n" +
                "    \"details\": \"No product exists with ID 123\"\n" +
                "  },\n" +
                "  \"timestamp\": \"2025-01-15T10:30:00Z\",\n" +
                "  \"path\": \"/api/products/123\"\n" +
                "}\n\n" +
                "Spring Boot Implementation:\n\n" +
                "public record ApiResponse<T>(T data, LocalDateTime timestamp) {\n" +
                "    public ApiResponse(T data) {\n" +
                "        this(data, LocalDateTime.now());\n" +
                "    }\n" +
                "}\n\n" +
                "@GetMapping(\"/api/products/{id}\")\n" +
                "public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Long id) {\n" +
                "    Product product = productService.findById(id);\n" +
                "    return ResponseEntity.ok(new ApiResponse<>(product));\n" +
                "}")
            .addTheory("Pagination for Large Collections",
                "DON'T return thousands of items at once!\n\n" +
                "BAD:\n" +
                "GET /api/products\n" +
                "Returns: 50,000 products (crashes browser)\n\n" +
                "GOOD:\n" +
                "GET /api/products?page=0&size=20\n" +
                "Returns: First 20 products + metadata\n\n" +
                "Response format:\n" +
                "{\n" +
                "  \"content\": [\n" +
                "    {\"id\": 1, \"name\": \"Product 1\"},\n" +
                "    {\"id\": 2, \"name\": \"Product 2\"}\n" +
                "  ],\n" +
                "  \"page\": 0,\n" +
                "  \"size\": 20,\n" +
                "  \"totalElements\": 500,\n" +
                "  \"totalPages\": 25\n" +
                "}\n\n" +
                "Spring Data JPA makes this easy:\n\n" +
                "@GetMapping(\"/api/products\")\n" +
                "public Page<Product> getAll(\n" +
                "        @RequestParam(defaultValue = \"0\") int page,\n" +
                "        @RequestParam(defaultValue = \"20\") int size) {\n" +
                "    Pageable pageable = PageRequest.of(page, size);\n" +
                "    return productRepository.findAll(pageable);\n" +
                "}\n\n" +
                "FILTERING:\n" +
                "GET /api/products?category=electronics&minPrice=100\n\n" +
                "@GetMapping(\"/api/products\")\n" +
                "public List<Product> search(\n" +
                "        @RequestParam(required = false) String category,\n" +
                "        @RequestParam(required = false) Double minPrice) {\n" +
                "    return productService.search(category, minPrice);\n" +
                "}")
            .addTheory("API Versioning",
                "APIs evolve. Don't break existing clients!\n\n" +
                "VERSIONING STRATEGIES:\n\n" +
                "1. URL Path (Most common):\n" +
                "/api/v1/users\n" +
                "/api/v2/users\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/v1/users\")\n" +
                "public class UserControllerV1 { }\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/v2/users\")\n" +
                "public class UserControllerV2 { }\n\n" +
                "2. Header-based:\n" +
                "GET /api/users\n" +
                "Header: Accept: application/vnd.myapi.v1+json\n\n" +
                "When to version:\n" +
                "✓ Breaking changes (removed fields)\n" +
                "✓ Changed response structure\n" +
                "✓ Different authentication method\n\n" +
                "DON'T version for:\n" +
                "✓ Bug fixes\n" +
                "✓ Adding optional fields\n" +
                "✓ Performance improvements")
            .addWarning("Common REST API Mistakes to Avoid",
                "❌ MISTAKE 1: Verbs in URLs\n" +
                "POST /api/createUser\n" +
                "FIX: POST /api/users\n\n" +
                "❌ MISTAKE 2: Wrong HTTP methods\n" +
                "GET /api/users/delete/123\n" +
                "FIX: DELETE /api/users/123\n\n" +
                "❌ MISTAKE 3: Returning wrong status codes\n" +
                "return ResponseEntity.ok(null);  // 200 when nothing found\n" +
                "FIX: return ResponseEntity.notFound().build();  // 404\n\n" +
                "❌ MISTAKE 4: Exposing database structure\n" +
                "/api/users?query=SELECT * FROM users\n" +
                "FIX: /api/users?role=admin&status=active\n\n" +
                "❌ MISTAKE 5: No rate limiting\n" +
                "Anyone can call your API 1000000 times/second\n" +
                "FIX: Implement rate limiting (429 status code)\n\n" +
                "❌ MISTAKE 6: Returning passwords or sensitive data\n" +
                "{\"id\": 1, \"name\": \"Alice\", \"password\": \"secret123\"}\n" +
                "FIX: Use @JsonIgnore or DTOs to exclude sensitive fields")
            .addExample("Complete REST Controller with Best Practices",
                "@RestController\n" +
                "@RequestMapping(\"/api/v1/products\")\n" +
                "@Validated\n" +
                "public class ProductController {\n" +
                "    private final ProductService productService;\n" +
                "    \n" +
                "    public ProductController(ProductService productService) {\n" +
                "        this.productService = productService;\n" +
                "    }\n" +
                "    \n" +
                "    // GET all (with pagination)\n" +
                "    @GetMapping\n" +
                "    public ResponseEntity<Page<Product>> getAll(\n" +
                "            @RequestParam(defaultValue = \"0\") int page,\n" +
                "            @RequestParam(defaultValue = \"20\") int size) {\n" +
                "        Pageable pageable = PageRequest.of(page, size);\n" +
                "        Page<Product> products = productService.findAll(pageable);\n" +
                "        return ResponseEntity.ok(products);  // 200 OK\n" +
                "    }\n" +
                "    \n" +
                "    // GET one\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Product> getById(@PathVariable Long id) {\n" +
                "        return productService.findById(id)\n" +
                "            .map(ResponseEntity::ok)  // 200 OK\n" +
                "            .orElse(ResponseEntity.notFound().build());  // 404 Not Found\n" +
                "    }\n" +
                "    \n" +
                "    // POST (create)\n" +
                "    @PostMapping\n" +
                "    public ResponseEntity<Product> create(\n" +
                "            @Valid @RequestBody Product product) {\n" +
                "        Product saved = productService.save(product);\n" +
                "        URI location = URI.create(\"/api/v1/products/\" + saved.getId());\n" +
                "        return ResponseEntity.created(location).body(saved);  // 201 Created\n" +
                "    }\n" +
                "    \n" +
                "    // PUT (replace)\n" +
                "    @PutMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Product> replace(\n" +
                "            @PathVariable Long id,\n" +
                "            @Valid @RequestBody Product product) {\n" +
                "        if (!productService.exists(id)) {\n" +
                "            return ResponseEntity.notFound().build();  // 404\n" +
                "        }\n" +
                "        Product updated = productService.replace(id, product);\n" +
                "        return ResponseEntity.ok(updated);  // 200 OK\n" +
                "    }\n" +
                "    \n" +
                "    // PATCH (partial update)\n" +
                "    @PatchMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Product> update(\n" +
                "            @PathVariable Long id,\n" +
                "            @RequestBody Map<String, Object> updates) {\n" +
                "        return productService.partialUpdate(id, updates)\n" +
                "            .map(ResponseEntity::ok)  // 200 OK\n" +
                "            .orElse(ResponseEntity.notFound().build());  // 404\n" +
                "    }\n" +
                "    \n" +
                "    // DELETE\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public ResponseEntity<Void> delete(@PathVariable Long id) {\n" +
                "        if (!productService.exists(id)) {\n" +
                "            return ResponseEntity.notFound().build();  // 404\n" +
                "        }\n" +
                "        productService.delete(id);\n" +
                "        return ResponseEntity.noContent().build();  // 204 No Content\n" +
                "    }\n" +
                "}\n\n" +
                "This controller demonstrates:\n" +
                "✓ Correct HTTP methods\n" +
                "✓ Proper status codes\n" +
                "✓ Resource-based URLs (no verbs)\n" +
                "✓ Pagination support\n" +
                "✓ Validation with @Valid\n" +
                "✓ Constructor injection\n" +
                "✓ API versioning in path")
            .addChallenge(createNamingQuiz())
            .addChallenge(createStatusCodeQuiz())
            .addChallenge(createMethodQuiz())
            .estimatedMinutes(45)
            .build();
    }

    private static Challenge createNamingQuiz() {
        return new Challenge.Builder("epoch-8-lesson-3-naming", "Resource Naming", ChallengeType.MULTIPLE_CHOICE)
            .description("Which URL follows REST API naming conventions?")
            .addMultipleChoiceOption("A) POST /api/createNewUser")
            .addMultipleChoiceOption("B) POST /api/user")
            .addMultipleChoiceOption("C) POST /api/users")
            .addMultipleChoiceOption("D) POST /api/Users/Create")
            .correctAnswer("C")
            .build();
    }

    private static Challenge createStatusCodeQuiz() {
        return new Challenge.Builder("epoch-8-lesson-3-status", "HTTP Status Codes", ChallengeType.MULTIPLE_CHOICE)
            .description("What status code should be returned when a resource is successfully created?")
            .addMultipleChoiceOption("A) 200 OK")
            .addMultipleChoiceOption("B) 201 Created")
            .addMultipleChoiceOption("C) 204 No Content")
            .addMultipleChoiceOption("D) 202 Accepted")
            .correctAnswer("B")
            .build();
    }

    private static Challenge createMethodQuiz() {
        return new Challenge.Builder("epoch-8-lesson-3-methods", "HTTP Methods", ChallengeType.MULTIPLE_CHOICE)
            .description("Which HTTP method should be used to update only the price of a product?")
            .addMultipleChoiceOption("A) GET")
            .addMultipleChoiceOption("B) POST")
            .addMultipleChoiceOption("C) PUT")
            .addMultipleChoiceOption("D) PATCH")
            .correctAnswer("D")
            .build();
    }
}
