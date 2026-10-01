# 📦 Order Service

A Spring Boot microservice that manages orders and communicates with the [Product Service](https://github.com/gmk-santhoshkumar/product-service) using both **Feign Client** and **WebClient**.

## 🏗️ Architecture

```
┌────────────────────────────────────────────────────────────────────┐
│                        Order Service                               │
│                         (Port: 8082)                               │
│                                                                    │
│  ┌──────────┐  ┌──────────┐  ┌───────────────┐  ┌─────────────┐  │
│  │Controller│→ │ Service  │→ │  Repository   │  │ H2 Database │  │
│  │  (REST)  │  │ (Logic)  │  │ (Spring JPA)  │→ │ (In-Memory) │  │
│  └──────────┘  └────┬─────┘  └───────────────┘  └─────────────┘  │
│                      │                                             │
│         ┌────────────┼────────────┐                                │
│         │            │            │                                │
│    ┌────▼─────┐ ┌────▼──────┐                                     │
│    │  Feign   │ │ WebClient │    ← Two ways to call Product Svc   │
│    │  Client  │ │  Service  │                                     │
│    └────┬─────┘ └─────┬─────┘                                     │
│         │             │                                            │
└─────────┼─────────────┼────────────────────────────────────────────┘
          │             │
          ▼             ▼
          
   ┌──────────────────────────┐
   │    Product Service       │
   │    (Port: 8081)          │
   │    GET  /api/products/id │
   │    PUT  /reduce-quantity │
   └──────────────────────────┘
```

## 🛠️ Tech Stack

| Technology              | Version  | Purpose                           |
|-------------------------|----------|-----------------------------------|
| Java                    | 21       | Programming Language              |
| Spring Boot             | 3.3.5    | Application Framework             |
| Spring Data JPA         | -        | Database ORM                      |
| H2 Database             | -        | In-Memory Database                |
| Spring Cloud OpenFeign  | 2023.0.4 | Declarative REST Client           |
| Spring WebFlux          | -        | WebClient (Programmatic REST)     |
| Gradle                  | 8.10     | Build Tool                        |
| Lombok                  | -        | Reduce Boilerplate Code           |

## 📁 Project Structure

```
order-service/
├── build.gradle                              # Gradle build config
├── settings.gradle                           # Gradle settings
├── README.md                                 # This file
├── src/
│   ├── main/
│   │   ├── java/com/example/orderservice/
│   │   │   ├── OrderServiceApplication.java          # Main class (@EnableFeignClients)
│   │   │   ├── controller/
│   │   │   │   └── OrderController.java              # REST endpoints
│   │   │   ├── model/
│   │   │   │   └── Order.java                        # JPA Entity
│   │   │   ├── dto/
│   │   │   │   ├── OrderRequest.java                 # Request body for placing order
│   │   │   │   ├── OrderResponse.java                # Response body
│   │   │   │   └── ProductDTO.java                   # Product data from Product Service
│   │   │   ├── repository/
│   │   │   │   └── OrderRepository.java              # Data Access Layer
│   │   │   ├── service/
│   │   │   │   └── OrderService.java                 # Business Logic
│   │   │   ├── client/
│   │   │   │   ├── ProductFeignClient.java           # ⭐ Feign Client (declarative)
│   │   │   │   └── ProductWebClientService.java      # ⭐ WebClient (programmatic)
│   │   │   ├── config/
│   │   │   │   └── WebClientConfig.java              # WebClient bean configuration
│   │   │   └── exception/
│   │   │       ├── OrderNotFoundException.java       # Custom Exception
│   │   │       └── GlobalExceptionHandler.java       # Error Handling
│   │   └── resources/
│   │       └── application.yml                       # App Configuration
│   └── test/
│       └── java/com/example/orderservice/
│           └── OrderServiceApplicationTests.java
```

## 🚀 How to Run

### Prerequisites
- Java 21 installed ([Download](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html))
- Git installed
- **Product Service must be running first** on port 8081

### Steps

1. **Start Product Service first** (see [product-service](https://github.com/gmk-santhoshkumar/product-service))

2. **Clone this repository**
   ```bash
   git clone https://github.com/gmk-santhoshkumar/order-service.git
   cd order-service
   ```

3. **Run the application**
   ```bash
   # On Windows
   gradlew.bat bootRun

   # On Mac/Linux
   ./gradlew bootRun
   ```

4. **Application starts on** → `http://localhost:8082`

5. **Access H2 Database Console** → `http://localhost:8082/h2-console`
   - JDBC URL: `jdbc:h2:mem:orderdb`
   - Username: `sa`
   - Password: *(leave empty)*

## 📡 API Endpoints

| Method | Endpoint                    | Description                        |
|--------|-----------------------------|------------------------------------|
| POST   | `/api/orders/feign`         | Place order using **Feign Client** |
| POST   | `/api/orders/webclient`     | Place order using **WebClient**    |
| GET    | `/api/orders`               | Get all orders                     |
| GET    | `/api/orders/{id}`          | Get order by ID                    |
| PUT    | `/api/orders/{id}/cancel`   | Cancel an order                    |
| DELETE | `/api/orders/{id}`          | Delete an order                    |

## 📝 Sample API Requests

### Place Order using Feign Client
```bash
curl -X POST http://localhost:8082/api/orders/feign \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

**Response:**
```json
{
  "orderId": 1,
  "productId": 1,
  "productName": "iPhone 15",
  "quantity": 2,
  "totalPrice": 1999.98,
  "orderStatus": "PLACED",
  "orderDate": "2024-01-15T10:30:00"
}
```

### Place Order using WebClient
```bash
curl -X POST http://localhost:8082/api/orders/webclient \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 2,
    "quantity": 1
  }'
```

### Get All Orders
```bash
curl http://localhost:8082/api/orders
```

### Get Order by ID
```bash
curl http://localhost:8082/api/orders/1
```

### Cancel an Order
```bash
curl -X PUT http://localhost:8082/api/orders/1/cancel
```

### Delete an Order
```bash
curl -X DELETE http://localhost:8082/api/orders/1
```

## 🔍 Feign Client vs WebClient

This project demonstrates **both** approaches for inter-service communication:

### Feign Client (`ProductFeignClient.java`)
```java
@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductFeignClient {

    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
```
- ✅ **Declarative** — Just define an interface, Spring generates the implementation
- ✅ **Easy to read** — Looks like a Spring MVC controller
- ✅ **Less code** — No manual HTTP client setup
- ❌ **Synchronous only** — Blocks the thread while waiting

### WebClient (`ProductWebClientService.java`)
```java
public ProductDTO getProductById(Long productId) {
    return webClient.get()
            .uri("/api/products/{id}", productId)
            .retrieve()
            .bodyToMono(ProductDTO.class)
            .block();
}
```
- ✅ **Non-blocking** — Can be used reactively with Mono/Flux
- ✅ **Flexible** — Full control over headers, timeouts, retries
- ✅ **Modern** — Recommended replacement for RestTemplate
- ❌ **More code** — Need to write the HTTP calls manually

### When to Use Which?

| Scenario                        | Use               |
|---------------------------------|-------------------|
| Simple REST calls               | Feign Client      |
| Need async/non-blocking         | WebClient          |
| Microservices with service discovery | Feign Client  |
| Need custom headers/retries     | WebClient          |
| Want minimal code               | Feign Client      |
| Full reactive application       | WebClient          |

## 🔗 Related Service

- **Product Service** → [https://github.com/gmk-santhoshkumar/product-service](https://github.com/gmk-santhoshkumar/product-service)

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
