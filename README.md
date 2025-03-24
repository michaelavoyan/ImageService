# Image Service

This is the Image Service application, a Spring Boot-based service for managing images. The project is built using Gradle and supports Docker for containerized deployment.

## Prerequisites

Ensure you have the following installed:
- [JDK 17+](https://adoptopenjdk.net/)
- [Gradle](https://gradle.org/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

## Running the Application Locally

### 1. Clone the Repository
```sh
 git clone https://github.com/michaelavoyan/ImageService.git
 cd ImageService
```

### 2. Build the Project
Run the following command to build the project using Gradle:
```sh
 ./gradlew build
```

### 3. Run the Application
To run the application without Docker:
```sh
 ./gradlew bootRun
```

## Running with Docker

### 1. Build the Docker Image
```sh
 docker build -t image-service .
```

### 2. Run the Container
```sh
 docker run -p 8080:8080 image-service
```

### 3. Run with Docker Compose
To start the service along with any dependencies:
```sh
 docker-compose up -d
```

## API Testing

You can test the APIs using the provided `TestAPIs.http` file with an HTTP client like Postman or cURL.

## Project Structure
```
ImageService/
├── src/                 # Source code
├── build.gradle         # Gradle build configuration
├── Dockerfile           # Docker image definition
├── docker-compose.yml   # Docker Compose configuration
├── settings.gradle      # Gradle project settings
└── TestAPIs.http        # Sample API requests
```

## License
This project is licensed under the Apache 2.0 License. See the LICENSE file for details.
