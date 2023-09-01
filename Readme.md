# Online shop

---
The goal of the application is to show and explain usage of various architectural and design patterns.

### Documentation
1. [Project description](./documentation/project-description.md) - brief description of the project and the way how the business wants to operate.
2. [Architecture Decision Record](./architecture-decision-record) - log of all architectural decisions made.
3. Event Storming Sessions:
   1. [Big Picture, Process Level and Design Level](https://miro.com/app/board/uXjVMzJI3n8=/?share_link_id=308586295561) 
4. [Solutions](./documentation/solutions.md) - list of used solutions, techniques and patterns with the reference to the example in the code.
5. [Articles](./documentation/articles.md) - references to blog articles that explains used solutions.

### How to run application
To run application you need to:
1. Have installed Docker with Docker Compose.
2. Run `docker-compose up --build` in main directory with `docker-compose.yml` file to build and run application.

To rebuild particular up you need to:
1. Run `docker-compose build <app-name>`.