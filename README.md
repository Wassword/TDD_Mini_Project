Getting Started
Java 22 or later
Maven 3.6+
An IDE (e.g., IntelliJ IDEA)


Build and run the application:
mvn clean install
mvn spring-boot:run
To run tests, use:
mvn test


API Endpoints
POST /orders - Create an order
GET /orders/{id} - Get order by ID
GET /orders - Get all orders
PUT /orders/{id} - Update an order
DELETE /orders/{id} - Delete an order