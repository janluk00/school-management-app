# School Management System API

This project is a backend application built with Java and Spring Boot, designed to manage a school's administrative tasks. The API provides endpoints for managing students, teachers, courses, school subjects, and user accounts.

## Features

- **User Authentication:** Secure login for users through JWT tokens.
- **Account Management:** Allows users to confirm accounts and change passwords.
- **Teacher Management:** Manage teacher details, assign subjects and courses, and view reports.
- **Student Management:** Manage student details, view grades, and access course information.
- **Course Management:** Manage courses and their associations with teachers and students.
- **School Subject Management:** Manage school subjects and link them to classes.
- **Class Management:** Manage classes within the school.

## Technologies Used

- **Java** (JDK 17)
- **Spring Boot** (3.2.2)
- **PostgreSQL** (14.9)
- **Hibernate**
- **JUnit 5** 
- **AssertJ**
- **Testcontainers**
- **Docker**
- **Liquibase**


## Getting Started

### Prerequisites

- **Java 17**
- **Docker** and **Docker Compose** for containerized setup
- **Maven** for dependency management

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/janluk00/school-management-app.git
    cd school-management-app
    ```
   
2. Start the PostgreSQL database with Docker Compose:

    ```bash
    docker compose up -d
    ```

3. Install dependencies and run the application:

    ```bash
    mvn clean package
    mvn spring-boot:run
    ```

### API Documentation

- The API documentation is available via Swagger at: http://localhost:8080/swagger-ui/index.html

## API Endpoints

### Authentication

- **[POST]** `/api/v1/auth/login` - Login to receive an authentication token.

### Teacher Account

- **[POST]** `/api/v1/teachers/students/{studentId}/grades` - Assign grades to a student.
- **[GET]** `/api/v1/teachers/reports` - View teacher's reports.
- **[GET]** `/api/v1/teachers/courses` - View teacher's courses.

### Admin Teacher Management

- **[GET]** `/api/v1/admin/teachers` - Retrieve all teachers.
- **[POST]** `/api/v1/admin/teachers` - Add a new teacher.
- **[POST]** `/api/v1/admin/teachers/{id}/tutor` - Assign a tutor to a teacher.
- **[POST]** `/api/v1/admin/teachers/{id}/school-subjects` - Assign subjects to a teacher.
- **[DELETE]** `/api/v1/admin/teachers/{id}/school-subjects` - Remove subjects from a teacher.
- **[GET]** `/api/v1/admin/teachers/{id}` - Retrieve teacher by ID.
- **[GET]** `/api/v1/admin/teachers/{id}/courses` - View courses assigned to a teacher.

### Admin Student Management

- **[GET]** `/api/v1/admin/students` - Retrieve all students.
- **[POST]** `/api/v1/admin/students` - Add a new student.
- **[GET]** `/api/v1/admin/students/{id}` - Retrieve student by ID.
- **[GET]** `/api/v1/admin/students/school-classes/{schoolClass}` - View students in a class.

### Admin Course Management

- **[POST]** `/api/v1/admin/courses` - Add a new course.
- **[DELETE]** `/api/v1/admin/courses` - Delete a course.

### Account Management

- **[POST]** `/api/v1/account/confirm/{token}` - Confirm user account with token.
- **[POST]** `/api/v1/account/change-password` - Change user password.

### Student Account

- **[GET]** `/api/v1/students/grades` - View student's grades.
- **[GET]** `/api/v1/students/grades/averages` - View average grades.
- **[GET]** `/api/v1/students/courses` - View student's courses.

### Admin School Subject Management

- **[GET]** `/api/v1/admin/school-subjects` - Retrieve all school subjects.
- **[GET]** `/api/v1/admin/school-subjects/school-classes/{schoolClass}/courses` - View courses for a class.

### Admin School Class Management

- **[GET]** `/api/v1/admin/school-classes` - Retrieve all school classes.

![Screenshot 2024-10-26 at 15-37-08 Swagger UI](https://github.com/user-attachments/assets/57863414-bc03-4cf3-b300-7318d6fb2bb3)
