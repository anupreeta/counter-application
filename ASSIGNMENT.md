# Apsis RnD Java assignment

Implement a simple backend application that satisfies the below specifications. You do not need to build any frontend, only an API and underlying system.

## Specifications

Your app should be implemented as a web service designed to be used by multiple clients.

It should expose a few methods: 
* one to increment a named counter by 1
* one to get the current value of a given counter
* one to get a list of all counters and their current value
* one to create new counters
 
## Notes

* Try to make the API nice to use for a hypothetical client developer.
* It’s ok if the counters lose state on app shutdown (you don’t need to implement persistent storage layer).
* For this test we ask you to use some version of Java.
* We recommend using a framework like Jersey, but you are free to choose other options.
* Bonus points if you can host a running app somewhere (for example Heroku or AWS) where we can play around with the app a bit


## Additional questions:
*(you do not need to actually implement support for the below items, just have an idea for how the app would be changed to support each one. We will discuss them during a subsequent code review session.)*


* **Persistence**. How would you  add a persistent storage layer such that the app could be restarted without losing counter states? What storage technology would you suggest?
  * Depends on the use-case scenario for the application. 2 options:
    * NoSQL Databases: Advantage -> high scalability, easy to scale horizontally as compared to scalign SQL databases.
      Disadvantage -> Not completely ACID compliant resulting in write-write or read-write conflicts incrementing wrong values. 
    This will make the application give unpredictable results - over-counting or under-counting
    * SQL Database: safer option, ACID compliant
    * Use-case scenario example:
      * NOSQL for counting logged-in gameplayers, website-visitors
      * SQL : where exact count value has to accurate and where there is financial implications
* **Fault tolerance**. How would you design the app in order to make the functionality be available even if some parts of the underlying hardware systems were to fail?
  * Based on High Availability principle - a minimum of 2 instances of the app must be running and available. 
    Database should also be replicated to cover fail-over scenario.
    Currently this app is not being used by any other service, if in future it happens to be called by external services, we need to handle cascading failure 
  by putting into place circuit breakers using Resilience4J, Spring retry, etc
* **Scalability**. How would you design the app need in order to ensure that it wouldn’t slow down or fail if usage increased by many orders of magnitude? Does your choice of persistence layer change in this scenario?
  * Horizontal scaling of the application using load balancer in front of services. Load balancer will route 
    the incoming request to the service which has the named counter. 
    Load balancer will calculate hashcode(counterId)%noOfServices to decide which service the request should be routed to. 
  * Databasewise, we need to scale them horizontally, scaling is easy for NoSQL db. SQL databases can be scaled by sharing, sharding by counterId.
* **Authentication**. How would you ensure that only authorised users can submit and retrieve data?
  * Basic authentication or cookie-based session auth. For distributed applications, OAuth or Okta and JWT tokens can be used
