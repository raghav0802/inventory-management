# 📦 Inventory Management Service

The **Inventory Management Service** is a Spring Boot microservice responsible for managing product inventory, producing Kafka events, indexing/searching products in **Elasticsearch**, and securing access with JWT-based authentication. It integrates MongoDB for data persistence, Elasticsearch for fast product search, and supports password hashing for user security.

---

## 🚀 Features

* Manage inventory transactions: **CREATE, UPDATE, CANCEL**
* Produce transaction events to Kafka (`transactions` topic)
* **Elasticsearch integration** for full-text and category-based product search
* JWT authentication for secure access
* Password hashing with **BCrypt** before storing in MongoDB
* Dockerized setup with **Kafka**, **Kafka-UI**, and **Elasticsearch**

---

## 🛠️ Prerequisites

* Java 17+
* Maven 3.9+
* Docker & Docker Compose
* MongoDB Atlas (or local MongoDB)
* Elasticsearch 8.x (via Docker or local installation)

---

## ⚙️ Setup & Running

### 1️⃣ Clone the repository

```bash
git clone <your-repo-url>
cd inventory-management
```

### 2️⃣ Build the project

```bash
mvn clean install
```

### 3️⃣ Run locally

```bash
mvn spring-boot:run
```

By default, the service runs on **port 8080**.

---

## 🐳 Docker Setup

### docker-compose.yml (excerpt)

```yaml
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.15.0
    container_name: inventory-elasticsearch
    environment:
      - discovery.type=single-node
      - ES_JAVA_OPTS=-Xms512m -Xmx512m
    ports:
      - "9200:9200"
    networks:
      - inventory-net

  kafka:
    image: bitnami/kafka:3.7.0
    container_name: inventory-kafka
    ports:
      - "9092:9092"
      - "9094:9094"
    environment:
      - KAFKA_CFG_NODE_ID=1
      - KAFKA_CFG_PROCESS_ROLES=broker,controller
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,PLAINTEXT_HOST://:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://inventory-kafka:9092,PLAINTEXT_HOST://localhost:9094
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@inventory-kafka:9093
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - ALLOW_PLAINTEXT_LISTENER=yes
    volumes:
      - kafka_data:/bitnami/kafka
    networks:
      - inventory-net

  inventory-service:
    build: .
    container_name: inventory-service
    ports:
      - "8080:8080"
    environment:
      SPRING_DATA_MONGODB_URI: "<Your MongoDB URI>"
      SPRING_ELASTICSEARCH_URIS: "http://inventory-elasticsearch:9200"
      JWT_SECRET: "<your-secret-key>"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: "inventory-kafka:9092"
    depends_on:
      - kafka
      - elasticsearch
    networks:
      - inventory-net

volumes:
  kafka_data:

networks:
  inventory-net:
    driver: bridge
```

### Run containers

```bash
docker compose up -d
```

---

## 🔎 Elasticsearch Integration

* Products are indexed into the `products` index automatically.
* Example document:

```json
{
  "id": "12345",
  "name": "Sony Electronics 2428",
  "category": "Electronics",
  "price": 500,
  "description": "High-quality Electronics product #2428"
}
```

* Example search query (category = Electronics):

```bash
curl -X GET "http://localhost:9200/products/_search" -H 'Content-Type: application/json' -d '{"query":{"match":{"category":"Electronics"}}}'
```

* Fast keyword search (all fields):

```bash
curl -X GET "http://localhost:9200/products/_search" -H 'Content-Type: application/json' -d '{"query":{"multi_match":{"query":"Shoes","fields":["name","description","category"]}}}'
```

---

## 🔗 API Endpoints

| Method | Endpoint             | Description                       |
| ------ | -------------------- | --------------------------------- |
| POST   | `/transactions`      | Create new transaction            |
| GET    | `/transactions`      | List all transactions             |
| GET    | `/transactions/{id}` | Get transaction by ID             |
| PUT    | `/transactions/{id}` | Update a transaction              |
| DELETE | `/transactions/{id}` | Cancel a transaction              |
| POST   | `/auth/register`     | Register a new user               |
| POST   | `/auth/login`        | Login and receive JWT             |
| GET    | `/products/search`   | Search products via Elasticsearch |

---

## 🔐 Security

### JWT Authentication

* All endpoints (except `/auth/register` and `/auth/login`) require a valid JWT.
* Send token in the `Authorization` header:

```
Authorization: Bearer <token>
```

### Password Hashing

* User passwords are **never stored in plain text**.
* Stored securely using **BCrypt hashing**.

---

## ⚙️ Configuration

`application.properties` or environment variables:

```properties
spring.data.mongodb.uri=<MongoDB URI>
spring.kafka.bootstrap-servers=inventory-kafka:9092
spring.elasticsearch.uris=http://inventory-elasticsearch:9200
jwt.secret=<your-secret-key>
```

---

✅ The service now supports **real-time search with Elasticsearch**, making it easy to query products by name, category, or description.

