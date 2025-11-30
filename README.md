# 🎬 Movie Directors API

REST API that analyzes movies from an external source and identifies directors with significant filmographies.

![CI/CD](https://github.com/IancoRodriguez/take-home-challenge-eron/actions/workflows/ci.yml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/IancoRodriguez/take-home-challenge-eron/badge.svg?branch=develop)](https://coveralls.io/github/IancoRodriguez/take-home-challenge-eron?branch=develop)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green)

## ✨ Features

- 🔍 Analyzes movies from external API with automatic pagination
- 🎯 Filters directors by movie count (strictly greater than threshold)
- 📊 Returns results in alphabetical order
- ⚡ In-memory caching with Caffeine (5min TTL)
- ✅ Comprehensive validation and error handling
- 📖 Interactive API documentation (Swagger UI)
- 🧪 84% test coverage (Unit + Integration tests - JaCoCo ) + Coveralls integration
- 🐳 Docker ready with health checks
- 🚀 CI/CD with GitHub Actions

## 🚀 Quick Start

### Run Locally
```bash
./gradlew bootRun
```


## 🚀 Live Demo

**API en AWS:** http://54.92.249.194:8080

**Endpoints:**
- API: http://54.92.249.194:8080/api/directors?threshold=4
- Swagger UI: http://54.92.249.194:8080/swagger-ui/index.html
- Health Check: http://54.92.249.194:8080/actuator/health



### Run with Docker
```bash
docker-compose up
```

### Run JAR
```bash
./gradlew build
java -jar build/libs/challenge-0.0.1-SNAPSHOT.jar
```

## 📖 API Documentation

### Get Directors by Threshold

Returns directors who have directed strictly more than the specified number of movies.
```http
GET /api/directors?threshold={number}
```

**Parameters:**
| Parameter | Type | Required | Constraints | Description |
|-----------|------|----------|-------------|-------------|
| `threshold` | integer | Yes | >= 0 | Minimum number of movies (exclusive) |

**Example Request:**
```bash
curl "http://localhost:8080/api/directors?threshold=4"
```

**Example Response:**
```json
{
  "directors": [
    "Christopher Nolan",
    "Martin Scorsese",
    "Steven Spielberg"
  ]
}
```

**HTTP Status Codes:**
| Code | Description |
|------|-------------|
| 200 | Success - Directors retrieved |
| 400 | Bad Request - Invalid/missing threshold |
| 500 | Internal Server Error - External API unavailable |

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.2.1 |
| Language | Java | 17 |
| Build | Gradle | 8.5 |
| HTTP Client | WebFlux (WebClient) | 6.1.2 |
| Cache | Caffeine | 3.1.8 |
| Docs | SpringDoc OpenAPI | 2.3.0 |
| Testing | JUnit 5 + Mockito | - |
| Container | Docker | - |
| CI/CD | GitHub Actions | - |

## 🏗️ Architecture

The application follows a clean layered architecture:

**Controller Layer** → REST endpoints, validation, DTO conversion  
**Service Layer** → Business logic, caching, filtering  
**Client Layer** → External API integration with WebClient

**Key Design Decisions:**
- Interface-based services (SOLID principles)
- WebClient over RestTemplate (reactive, modern)
- Caffeine cache (high performance)
- Separate domain models (Clean Architecture)
- Records for DTOs (immutability)
- Global exception handler (consistent errors)

**View Coverage Reports:**
- 📊 **JaCoCo Report:** Run `./gradlew test jacocoTestReport` and open `build/reports/jacoco/test/html/index.html`
- 📈 **[Coveralls Dashboard](https://coveralls.io/github/IancoRodriguez/take-home-challenge-eron)** (live tracking)

**Coverage by Layer:**
- 100% Service layer
- 100% Controller layer
- 95% Domain layer
- 82% Client layer

**Coverage: 81%**
- 100% Service layer
- 100% Controller layer
- 95% Domain layer
- 82% Client layer

## 📦 Project Structure
```
src/
├── main/
│   ├── java/com/eron/challenge/
│   │   ├── client/          # External API calls
│   │   ├── config/          # Spring configuration
│   │   ├── controller/      # REST endpoints
│   │   ├── exception/       # Error handling
│   │   ├── model/
│   │   │   ├── domain/      # Business objects
│   │   │   └── dto/         # Data transfer objects
│   │   └── service/         # Business logic
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/com/eron/challenge/
        ├── client/          # Client tests
        ├── controller/      # Integration tests
        ├── model/domain/    # Domain tests
        └── service/         # Unit tests
```

## ⚙️ Configuration

**Profiles:**
- `dev` - Development (DEBUG logs, Swagger enabled)
- `prod` - Production (INFO logs, Swagger disabled)

**Cache Configuration:**
- Expiration: 5 minutes after write
- Max size: 100 entries
- Eviction policy: LRU (Least Recently Used)

## 🐳 Docker

**Build image:**
```bash
docker build -t movie-directors-api .
```

**Run container:**
```bash
docker run -p 8080:8080 movie-directors-api
```

**With Docker Compose:**
```bash
docker-compose up
```
## ☁️ AWS Deployment

**Live Production URL:** http://54.92.249.194:8080

### Endpoints
- **API:** http://54.92.249.194:8080/api/directors?threshold=4
- **Swagger UI:** http://54.92.249.194:8080/swagger-ui/index.html
- **Health Check:** http://54.92.249.194:8080/actuator/health

### Infrastructure
- **Platform:** AWS ECS Fargate (Serverless containers)
- **Region:** us-east-1 (N. Virginia)
- **Container Registry:** AWS ECR
- **Compute:** 256 CPU / 512 MB Memory
- **Networking:** VPC with public subnets, Security Group on port 8080
- **Logging:** CloudWatch Logs

### Deployment Architecture
```
GitHub → Docker Image → AWS ECR → ECS Fargate → Public IP
```

### CI/CD Pipeline
- Code push to `master` triggers GitHub Actions
- Automated testing and Docker build
- Manual deployment to AWS ECS (can be automated)

---

## 📝 Deployment Notes

**Cost:** Running on AWS Free Tier (first 12 months free)

**Scalability:** Can easily scale to multiple tasks with load balancer

**Monitoring:** Logs available in CloudWatch at `/ecs/movie-directors`




## 🔍 Monitoring

**Health Check:**
```bash
curl http://localhost:8080/actuator/health
```

**Metrics:**
```bash
curl http://localhost:8080/actuator/metrics
```

## 📝 Important Notes

- **Strictly greater than:** `threshold=4` returns directors with **5+ movies** (not 4)
- **Alphabetical order:** Results are always sorted A-Z
- **Caching:** Results cached by threshold for 5 minutes
- **Pagination:** Automatically fetches all pages from external API

## 🚢 CI/CD

GitHub Actions workflow runs on every push to `master` or `develop`:
- ✅ Compile code
- ✅ Run tests with coverage
- ✅ Build Docker image
- ✅ Test container health

## 👤 Author

**Ianco Rodríguez**

Built as part of a technical challenge for Eron.

## 📄 License

This project was created for evaluation purposes.