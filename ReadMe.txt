# Spring Boot Backend for Ashok Dutta Project
## to improve
For the backend only configuration changes on the application.properties should alter to connect to the database.
 Note I used MySQL and port 8080. Also for the client he wants minimum costs so please use a serverless architecture.
## Overview

Welcome to the Spring Boot backend of the Ashok Dutta project, seamlessly connected to Amazon databases. This backend is designed to store models representing various aspects of the project, such as articles, conferences, and people. It acts as a robust bridge between the React front end and the Amazon databases, ensuring efficient communication and data management.

## Components

### 1. Controllers

The controllers serve as the communication layer between the React front end and the backend. They handle incoming requests, process them, and ensure appropriate responses. In this backend, controllers manage interactions related to articles, conferences, and people.

### 2. Repositories

Repositories are responsible for database communication. They encapsulate the logic for storing and retrieving data, ensuring a clean separation between the application's business logic and its data access code. In this backend, repositories interact with the Amazon databases to manage the persistence of models.

### 3. Services

Service files act as an additional layer between the controllers and repositories, enhancing security. They separate the business logic associated with an object's repository from its controller, providing a structured and secure approach to handle data operations. This modular design ensures better maintainability and scalability.

## Getting Started

To set up and run the Spring Boot backend for the Ashok Dutta project, follow these steps:

1. Clone the repository to your local machine.
2. Configure the Amazon database connection properties in the application.properties file.
3. Build the project using your preferred build tool (e.g., Maven or Gradle).
4. Run the application, and the backend will be accessible at the specified endpoint.

## Usage Guidelines

1. **React Front End Integration:**
   - Ensure that the React front end is configured to communicate with the backend's API endpoints.

2. **Model Management:**
   - Utilize the provided controllers to manage articles, conferences, and people models.

3. **Database Interaction:**
   - Leverage the repositories to handle interactions with the connected Amazon databases.

4. **Security Measures:**
   - Respect the separation of concerns implemented through service files for enhanced security.

5. **Contribution and Issues:**
   - Contributions and issue reports are welcome. Feel free to contribute enhancements or report any issues to improve the overall functionality of the backend.

