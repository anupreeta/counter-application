## Write a simple rest-based java application (no frontend solution expected, only backend) that can do the following:


1. Create new Counter(s)

2. Increment a named counter by 1

3. Get a current value of a counter

4. Get a list of all counters and their current values


Topics to think about and discuss when we meet:


1. What structure would you choose for your project (which packages/classes) and why?
   1. Used spring initialzer to create springboot application 
   2. Used AtomicInteger for the functionality of incrementing counter to ensure thread-safety, non-blocking in nature 
   and thus highly usable in writing high throughput concurrent data structures
   3. Use @Transactional with default ISOLATION_LEVEL = READ_COMMITTED, prevents dirty-reads so that counter is 
   incremented on correct value.
   4. Use ModelMapper dependency to convert model to entity and vice-versa.
2. How would you bootstrap the project?
   1. Used spring initialzer to create the project structure for springboot application along with its dependencies
   2. Used docker and docker-compose to build and run the springboot application with persistence (postgresql database) 

3. What test would you write?
   1. Wrote unit tests at service and controller layers

4. What would you choose for persistence/database and why? (no implementation expected, only interface and hardcoded SQL queries)
   Depends on the use-case scenario for the application. 2 options:
* **NoSQL Databases**: (example: MongoDB)
    * Implementation idea: 
      * Add spring-boot-starter-data-mongodb dependency in pom.xml of springboot application
      * Create Counter class annotated with @Document 
      * CounterRepository should extend MongoRepository 
      * SQL query:
      ````````
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is("nails"));
        Update update = new Update();
        update.set("counter_value", 20);
        mongoTemplate.upsert(query, update, Counter.class);
      ````````
      * Update application.properties to contain MongoDB connection parameters
      * In docker-compose, setup mongodb database with init script to create database counters and counters table
      
    * Advantage -> high scalability, easy to scale horizontally as compared to scaling SQL databases.
    * Disadvantage -> BASE compliant and not completely ACID compliant resulting in write-write or read-write conflicts
      incrementing wrong values.
      This will make the application give unpredictable resulting in slight over-counting or under-counting
    * **SQL Databases** (example: PostGreSQL):
    * Implementation CounterRepository inherits CrudRepository interface, can use JpaRepository interface instead 
      for pagination and sorting of counters.
        * Advantage - > safer option, ACID compliant
        * Disadvantage -> hard to scale horizontally
    * Use-case scenario example:
        * NoSQL: for counting logged-in gameplayers, website-visitors
        * SQL : charging customers based on counter value, in that case, the count value has to accurate
  
5. How would you think around deployment? (no implementation expected)
   1. Implemented docker-compose to bring up application running in docker container
   2. Publish it to container registry of cloud provider(eg: AWS, GCP, etc)
   3. Create configmap.yaml, deployment.yaml and secrets.yaml requirement for deployment
   4. Deploy to kubernetes cluster (EKS or GKE) by setting up gitop actions using HelmCharts and values.yml for 
   different environments - dev, test and production
   5. One can also host the application on Heroku cloud platform. You need to add system.properties to configure Heroku
      to use Java 17 (same version as my app).


6. How would you choose to host this application? Describe infrastructure. (no implementation expected)
   1. Based on High Availability principle - a minimum of 2 instances of the app must be running and available.
     Database should also be replicated to cover fail-over scenario.
     Currently, this app is not being used by any other service, if in future it happens to be called by external services, we need to handle cascading failure
     by putting into place circuit breakers using Resilience4J, Spring retry, etc
   2. Scalability:   
      * Not vertical scaling(scale-up) because:
        * single point of failure 
        * soon reach the threshold limit 
      * Horizontal scaling(scale-out) of the application using load balancer in front of service instances. Load balancer will route
         the incoming request to the service which has the named counter.
         Load balancer will calculate `hashcode(counterId)%noOfServices` to decide which service the request should be routed to.
      * Databasewise, we need to scale them horizontally, scaling is easy for NoSQL database. 
      * SQL databases can be scaled by sharding, sharding by counterId.
   3. Authentication:
      * Basic authentication or cookie-based session auth. 
      * For distributed applications, OAuth or Okta and JWT tokens can be used

