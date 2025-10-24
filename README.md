# Todo List API

## Technologies used in the project and requirements
- Java 17(OpenJDK 17.0.16 or higher)
- Maven 3.8.1 or higher
- Spring Boot
- Postgres(development)
- H2(tests)

## How to run the application
- Configure the properties files. (Examples in examples folder)
- Run:
  
  ```
  mvn spring-boot:run -Dspring-boot.run.profiles=dev
  ```
- In another terminal tab(here I'm using curl to make the requests examples):
  ```
  curl -X POST http://localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"name":"name","email":"email@example.com","password":"password"}'
  ```

  or (If you already have a user):

  ```
   curl -X POST http://localhost:8080/login \
   -H "Content-Type: application/json" \
   -d '{"email":"admin@mysite.com","password":"admin123"}'
  ```
  
- Store the token that the application will throw to be used in anothers requests.

### URL: 
- The token must be sent at Authorization: Bearer header.
- POST -> /todos
- PUT and DELETE -> /todos/{id}

### BODY for POST and PUT operations example:
{
        "title": "Buy groceries completed",
        "description": "Buy milk, eggs, bread, and cheese",
        "status": { "name": "Completed" }
}
  
## How to run the tests
- In a terminal run:
```
mvn test
```

  
