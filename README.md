[![Build Status](https://travis-ci.com/pedrorenzo/time-clock-api.svg?branch=master)](https://travis-ci.com/pedrorenzo/time-clock-api)

# timeclock
This is a project to mark the time entries of employees of a company.

* In this project it is possible to register a company, its employees and the employees' time entries.
* Get companies by CNPJ, time entries by ID and time entries by employee ID.
* Update employee and time entries.
* Remove time entries.

I tried to use the best patterns for developing a RESTful API.

The technologies used were:
* Spring Boot for project creation
* TravisCI for continuous integration
* MySQL with JPA and Spring Data for data persistence
* Flyway for data migration
* Swagger for API documentation
* JWT and Spring Security for authentication
* Heroku to deploy
* JUnit and Mockito for unit and integration tests
* EhCache for cache data