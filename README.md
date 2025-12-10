

**HealthHub@UTM**

A full-stack web application for managing university health and fitness activities, developed as part of **Assignment #2** for internet programming project. This project upgrades the legacy Servlet/JSP system into a **Spring MVC** architecture using **Thymeleaf** for the view layer and **Hibernate** for the persistence layer.

## 📋 Project Overview

**HealthHub@UTM** is designed to manage interactions between three main actors: **Members** (Students/Staff), **Trainers**, and **Admins**. 

The primary objective of this project is to demonstrate the configuration and implementation of:
* **Spring MVC:** Controllers, Handler Mappings, Model Attributes.
* **Hibernate ORM:** Entity mapping, HQL, SessionFactory, and DAO Layer.
* **Thymeleaf:** Server-side templating for dynamic views.
* **MySQL:** Relational database storage.

## 🛠 Tech Stack

* **Language:** Java (JDK 8+)
* **Framework:** Spring MVC 5.x
* **ORM:** Hibernate 5.x
* **Template Engine:** Thymeleaf
* **Database:** MySQL
* **Build Tool:** Maven
* **Server:** Apache Tomcat 9/10

## ⚙️ Features & Modules

### 1. Common Module
* **Authentication:** Manual session-based Login/Logout.
* **Role Management:** Redirects users to their specific dashboards based on session role.

### 2. Member Module (Student/Staff)
* **BMI Calculator:** Input weight/height and view historical BMI results.
* **Program Management:** Browse available fitness programs.
* **Enrollment:** Enroll in specific fitness programs.
* **Workout Plan:** View and manage personal workout routines.

### 3. Trainer Module
* **Plan Creation:** Create new fitness plans.
* **Assignment:** Assign plans to specific members.
* **Monitoring:** Track member progress and schedule sessions.

### 4. Admin Module
* **CRUD Operations:** Manage Fitness Programs and Categories.
* **Reporting:** View system summaries and reports.

## 📂 Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.healthhub
│   │       ├── controller   # Spring MVC Controllers (Member, Trainer, Admin, Auth)
│   │       ├── dao          # Data Access Object Interfaces & Implementation
│   │       ├── entity       # Hibernate Entities (Person, Program, Enrollment, etc.)
│   │       └── service      # (Optional) Service layer logic
│   ├── resources
│   │   └── hibernate.cfg.xml # Hibernate Configuration
│   └── webapp
│       ├── WEB-INF
│       │   ├── dispatcher-servlet.xml # Spring Bean & View Resolver Config
│       │   ├── web.xml
│       │   └── views        # Thymeleaf .html templates
│       └── resources        # CSS, JS, Images
````

## 🚀 Setup & Installation

### Prerequisites

  * Java Development Kit (JDK)
  * Apache Maven
  * MySQL Server
  * IDE (IntelliJ IDEA or Eclipse or vscode + extentions)

### Step 1: Clone the Repository

```bash
git clone [https://github.com/YOUR_USERNAME/HealthHub-UTM-SpringMVC.git](https://github.com/YOUR_USERNAME/HealthHub-UTM-SpringMVC.git)
```

### Step 2: Database Configuration

1.  Create a schema in MySQL named `healthhubutm`.
    ```sql
    CREATE DATABASE healthhubutm;
    ```
2.  Open `src/main/resources/hibernate.properties` (or your datasource config inside `dispatcher-servlet.xml`) and update your MySQL credentials:
    ```properties
    jdbc.url=jdbc:mysql://localhost:3306/healthhubutm
    jdbc.username=your_root
    jdbc.password=your_password
    ```
3.  *Note:* Hibernate is configured to `update` the schema automatically. Tables will be generated upon the first run.

### Step 3: Build and Run

1.  Open the project in your IDE.
2.  Run `mvn clean install` to download dependencies.
3.  Configure your Tomcat Server in the IDE and deploy the artifact (`war` exploded).
4.  Access the application at: `http://localhost:8080/HealthHub` (or your configured context path).



## 📜 License

This project is for academic purposes under **Universiti Teknologi Malaysia (UTM)**.



***


