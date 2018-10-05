Example [Spring Boot 2](http://projects.spring.io/spring-boot) application with

* [Kotlin 1.2](https://kotlinlang.org)
* [Java 10](http://openjdk.java.net)
* [JUnit 5](http://junit.org/junit5)
* [TestContainers](https://www.testcontainers.org)

Gradle configuration improvements based on articles:
* [Improving the Performance of Gradle Builds](https://guides.gradle.org/performance/)
* [Organizing Gradle Projects](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html)

WebFlux CRUD with Reactive MongoDB client

### Run
```
gradle bootRun --spring.profiles.active=dev
```

### Build fat jar
```
gradle bootJar
```

### POST request example for httpie to create customer
```
http POST :8080/customer name="John Doe" balance="0.99" last_deposit="2018-08-14T16:55:30"
```
