## Counter Application
A simple springboot java application to do the following:
  * Create new counter(s)
  * Increment a named counter by 1
  * Get current value of a counter
  * Get a list of all counters and their current values

## How to run the application locally
* Pre-requisite:
  * PostgreSql Database with `counters` database and `counters` table created. Refer to sql script under `sql/create_db.sql
* To run application locally (considering postgresql database is setup already locally):
  * To build and run tests: ``mvn clean package``
  * To run the application: ``mvn spring-boot:run``

## How to run the application in docker-compose
* Pre-requisite:
  * Docker
* To run application with `postgresql` database
  * docker-compose up
* To build and run application in dockerized container:
````
  * docker build --tag=counter-app:latest . 
  * docker run -it -p 8080:8080 counter-app
  * docker-compose up
````
* To shutdown application before a fresh start (will loose state): 
````
  * docker-compose down
  * docker rmi docker-spring-boot-postgres:latest
````

## Application details
* Java Version: 17
* Springboot3
* PostgreSql 14.6, in docker-compose used PostGreSQL 13-alpine version

### REST API endpoints
The endpoints can be tried out using POSTMAN or CURL commands. 
`
* **Create counter**`
  * POST http://localhost:8080/counters
Request Body:

```json
{
	"name": "nails",
	"counter" : "30"
}
```

Response:

```json
{
  "name": "trees",
  "counterValue": 30
}
```
* **Get counter value by name**
  * GET http://localhost:8080/counter?name={trees}
    Response:

```json
{
  "name": "trees",
  "counterValue": 30
}
```

* **Get all counters**
    * GET http://localhost:8080/counters
Response:
````json
[
    {
        "name": "trees",
        "counterValue": 30
    },
    {
        "name": "buildings",
        "counterValue": 60
    }
]
````
* **Increment counter by 1**
  * POST http://localhost:8080/counters/increment?name={buildings}
Response:
````json
{
    "name": "buildings",
    "counterValue": 61
}
````

## Notes
* In terms of persistence, initially used h2 in-memory database focussing on creating springboot application 
and adding REST API methods
* Later, changed to using PostGreSQL to provide persistence assuming integrity of the counter values is important 
to always get reliable results 
* Dockerized the application using docker-compose. See Dockerfile and docker-compose for details
* Depending on the use-case scenario of the application. 2 options:
  * **NoSQL Databases**: 
    * Advantage -> high scalability, easy to scale horizontally as compared to scaling SQL databases. 
    * Disadvantage -> BASE compliant and not completely ACID compliant resulting in write-write or read-write conflicts incrementing wrong values.
    This will make the application give unpredictable resulting in slight over-counting or under-counting
  * **SQL Databases** (example: PostGreSQL):
    * Advantage - > safer option, ACID compliant
    * Disadvantage -> hard to scale horizontally





