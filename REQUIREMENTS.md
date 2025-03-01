**Task Overview**  
The goal is to create a **Java Spring Boot application** showcasing expertise in modern Java technologies and practices. The application will enable users to manage a list of image URLs and play a slideshow with transitions. Each image change should trigger a proof-of-play notification.

---

**Updated Requirements**

**Core Requirements**  
1. **Backend Development**
   - Implement a **Java Spring Boot** application with the following RESTful APIs:
     - **POST /addImage**: Add a new image URL with a specified duration. Validate that URL actually contains an image (JPEG, PNG, WEBP and other).
     - **DELETE /deleteImage/{id}**: Remove an image by its ID.
     - **POST /addSlideshow**: Add a new slideshow. The request should include array of images and their play duration.
     - **DELETE /deleteSlideshow/{id}**: Remove a slideshow by its ID.
     - **GET /images/search**: Search for images and their associated slideshows using keywords from the URL or duration.
     - **GET /slideShow/{id}/slideshowOrder**: Retrieve images in a slideshow ordered by image addition date.
     - **POST /slideShow/{id}/proof-of-play/{imageId}**: Record an event when an image is replaced by the next one.
   
2. **Data Storage**
   - Use **MySQL** or **PostgreSQL** for persistent data storage.

3. **Modern Java Techniques**
   - Apply the latest Java best practices wherever applicable.

4. **Error Handling**
   - Return user-friendly and standardized error responses.

5. **Testing**
   - Write unit tests using **JUnit 5**.

---

**Advanced Features**

1. **Containerization**
   - Create a **Dockerfile** to containerize the application.
   - Add a **docker-compose.yml** for local setup with the database.

2. **Event-Driven Architecture**
   - Use **Spring EventPublisher** or **Kafka** to log significant API actions (e.g., adding/deleting images).
  
2. **Reactive Programming**
   - Considering non-blocking request handling (try to use reactive stack: WebFlux/r2dbc etc.).

---

**Submission Guidelines**

1. **Code Submission**  
   - Upload the project to a public **GitHub repository**.  
   - Ensure the code is well-structured, follows best practices, and includes meaningful comments.

2. **Functional Requirements**  
   - Ensure APIs handle errors gracefully.  
   - Provide sample payloads for testing in the documentation.

3. **Evaluation Criteria**  
   - Quality of the code and adherence to best practices.  
   - Effective use of modern Java and Spring features.  
   - Completeness of the documentation and testing.
