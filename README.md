# Todo List API

## Technologies used in the project
- Java 17
- Spring Boot
- Postgres
- H2(tests)
- Docker

## How to run the application
The project uses a .env file to store your JWT key. There is a .env.example file in the repository:<br>
```
cp .env.example .env
```
And set your JWT 256 bits key.<br>

After that:

- Run:
  
  ```
  docker-compose up --build
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
- GET /todos?page=(page_number)&limit=(limit_number)
- POST -> /todos
- PUT and DELETE -> /todos/{id}

### BODY for POST and PUT operations example:
Available status: Pending, In Progress, Completed
{
        "title": "Buy groceries completed",
        "description": "Buy milk, eggs, bread, and cheese",
        "status": { "name": "Completed" }
}

  
