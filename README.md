# 🛠️ MLOps MVP Platform – Java Spring Boot Implementation

A modular, microservices-based MLOps platform designed to simulate a production-ready pipeline for dataset handling, training orchestration, and model deployment. Built with Java 21, Spring Boot, and Docker to mimic scalable and loosely coupled architectures found in real-world systems.

---

## 🧩 Project Structure

```

mlops-platform/
├── docker-compose.yml
├── api-gateway/
├── data-service/
├── training-service/
└── deployment-service/

````

Each service is independently deployable and communicates via REST, forming the backbone of a minimal yet functional MLOps pipeline.

---

## 🚀 Features Overview

- 🔹 **Data Service**: Upload, store, and manage datasets.
- 🧠 **Training Service**: Submit and track training jobs, save models.
- 📦 **Deployment Service**: Deploy trained models and simulate predictions.
- 🌐 **API Gateway**: Route requests, perform basic load balancing, and expose a unified API interface.

---

## 🛠️ Prerequisites

Ensure you have the following installed:

```bash
- Java 21
- Maven 3.6+
- Docker (optional for containerized deployment)
````

---

## 🔧 Build & Run

### 🧪 Local Development

1. **Compile and Package Services**

```bash
cd data-service && mvn clean package
cd ../training-service && mvn clean package
cd ../deployment-service && mvn clean package
cd ../api-gateway && mvn clean package
```

2. **Run Services Individually**

```bash
# Terminal 1
cd data-service && mvn spring-boot:run

# Terminal 2
cd training-service && mvn spring-boot:run

# Terminal 3
cd deployment-service && mvn spring-boot:run

# Terminal 4
cd api-gateway && mvn spring-boot:run
```

### 🐳 Docker Compose

```bash
# Build all services
mvn clean package -f data-service/pom.xml
mvn clean package -f training-service/pom.xml
mvn clean package -f deployment-service/pom.xml
mvn clean package -f api-gateway/pom.xml

# Run with Docker Compose
docker-compose up --build
```

---

## 📡 API Examples

### 🗂️ Upload Dataset

```bash
curl -X POST http://localhost:8080/api/datasets \
  -F "file=@your-dataset.csv"
```

### 🧠 Create Training Job

```bash
curl -X POST http://localhost:8080/api/training/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Training Job",
    "datasetId": 1
  }'
```

### 🔍 Check Training Status

```bash
curl http://localhost:8080/api/training/jobs/1
```

### 📋 List Models

```bash
curl http://localhost:8080/api/models
```

### 🚀 Create Deployment

```bash
curl -X POST http://localhost:8080/api/deployments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Model Deployment",
    "modelId": 1,
    "modelPath": "./models/model_1.pkl"
  }'
```

### 📈 Make Prediction

```bash
curl -X POST http://localhost:8080/api/predict/1 \
  -H "Content-Type: application/json" \
  -d '{
    "feature1": 0.5,
    "feature2": 1.2,
    "feature3": "category_a"
  }'
```

---

## 📊 Monitoring & Health Checks

Every service exposes Spring Actuator endpoints:

* **Health**: `http://localhost:808x/actuator/health`
* **Metrics**: `http://localhost:808x/actuator/metrics`
* **Info**: `http://localhost:808x/actuator/info`

---

## ✨ Why This Project?

This MLOps MVP demonstrates the full lifecycle of an ML project in a modular, production-like setting — from data ingestion to model inference. Perfect for showcasing backend design skills, microservices orchestration, and real-world architecture understanding during technical interviews or hackathons.

---

## 🙌 Contributing

Feel free to fork the repo, report issues, or suggest improvements. This project is a great starting point for scaling to more complex MLOps workflows using Kubernetes, Kafka, or MLFlow in the future.

---

## 📜 License

This project is open-source and available under the [MIT License](LICENSE).

---

```

Let me know if you want to include diagrams, Swagger documentation, or even deploy it to a cloud platform like AWS/GCP as part of a showcase!
```
