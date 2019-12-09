# java-senior-evandro-souza

A Spring Boot CRUD  example project.

It's a micro-services ready, CRUD example based on a employee/department use case.

# Business rules:

- A given department cannot have more than 20% of its people under the age of 18.
- A given department cannot have more than 20% of its people over the age of 65.

# Available API Operations:

- POST /colaboradores
- DELETE /colaboradores/{uuid}
- GET /colaboradores/{uuid}
- GET /departamentos/{department-id}/colaboradores

# Pre-included data:

- Departments 1, 2, 3 and 4;

# Stack:

 - Spring boot as application container 
 - Spring data/hibernate for persistence
 - Mapstruct for object mapping
 - Postgres database in production / H2 database for test
 - Swagger for documentation
 - Spring cache for cached operations
 
 # Live API:

 https://colaboradores-crud.herokuapp.com/swagger-ui.html
