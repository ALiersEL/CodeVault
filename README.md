# CodeVault
[中文版](README.zh.md)

CodeVault is a website based on a relational database designed to manage interview questions. It offers two types of questions: algorithmic and textual. Through this platform, users can easily organize, manage, and browse various interview questions. Whether you need challenging algorithmic problems or textual questions that demonstrate your thinking skills, CodeVault meets the needs of job seekers and interviewers alike. This powerful tool provides reliable support for interview preparation, enabling everyone to perform at their best during interviews.

## How to Use
Frontend port: 5172  
Backend port: 8765  
Set in vite.config.ts and application.yaml respectively  
Database connection information is set in application.yaml, default is
jdbc:postgresql://${POSTGRESQL_HOST:localhost}:5432/code_vault  
Username and password need to be set by the user  
DDL.sql must be executed to create the database tables  

## Project Structure
```
.
├── README.md
├── codevault-backend
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com.aliersel.codevaultbackend
│       │   │       ├── CodevaultBackendApplication.java
│       │   │       ├── controller
│       │   │       │   ├── UserController.java
│       │   │       │   └── ProblemController.java
│       │   │       |   └── FolderController.java
│       │   │       |   └── api
│       │   │       ├── mapper
│       │   │       │   ├── UserMapper.java
│       │   │       │   └── ProblemMapper.java
│       │   │       |   └── FolderMapper.java
│       │   │       |   └── CommentMapper.java
│       │   │       ├── service
│       │   │       │   ├── impl
│       │   │       │   │   ├── UserServiceImpl.java
│       │   │       │   │   ├── ProblemServiceImpl.java
│       │   │       │   │   └── FolderServiceImpl.java
│       │   │       │   ├── intf
│       │   │       │   │   ├── UserService.java
│       │   │       │   │   ├── ProblemService.java
│       │   │       │   │   └── FolderService.java
│       │   │       ├── config
│       │   │       ├── entity
│       │   │       ├── exception
│       │   │       ├── util
│       │   │       └── security
│       │   └── resources
│       │       ├── application.yaml
├── codevault-frontend
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── yarn.lock
│   ├── src
│   │   ├── App.vue
│   │   ├── main.ts
│   │   ├── style.css
│   │   ├── api
│   │   ├── assets
│   │   ├── components
│   │   ├── router
│   │   ├── store
│   │   ├── utils
│   │   └── views

```

## Project Tech Stack
Frontend: Vue3 + TypeScript + Vite + Naive UI  
Backend: Spring Boot + MyBatis + PostgreSQL

## Project Features
- Frontend and backend are separated, with Vue3 used on the frontend and Spring Boot on the backend
- Vite is used as the frontend build tool, which improves development efficiency
- TypeScript is used on the frontend, making the code more standardized and readable
- Naive UI is used as the UI framework, offering a beautiful interface and friendly user interactions
- MyBatis is used as the ORM framework on the backend, making the code clean and easy to maintain
- PostgreSQL is used as the database, supporting large-scale data storage
- Spring Security is used as the security framework to ensure the safety of user information
- JWT is used for authentication to ensure the security of user information
- Swagger is used for generating API documentation, facilitating frontend and backend integration
- Lombok is used to simplify the code and improve development efficiency

## Project Functions
- User registration and login
- CRUD operations for interview questions
- Categorization and management of interview questions
- Archiving of interview questions (creation, deletion, renaming, moving of folders)
- Adding notes and code to interview questions, supporting multimedia and multiple languages

## Project Screenshots
![image](./screenshots/login.png)
![image](./screenshots/register.png)
![image](./screenshots/problemset.png)
![image](./screenshots/problem-details.png)
![image](./screenshots/problem-edit.png)
![image](./screenshots/problem-add.png)
![image](./screenshots/code-display.png)

## Project Open Source License
MIT License

## Project References
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis](https://mybatis.org/mybatis-3/)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT](https://jwt.io/)
- [Swagger](https://swagger.io/)
- [Lombok](https://projectlombok.org/)
- [Vue3](https://v3.vuejs.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [Vite](https://vitejs.dev/)
- [Naive UI](https://www.naiveui.com/)
