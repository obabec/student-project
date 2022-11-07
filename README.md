# Project Spring Boot, Rest, PostgreSQL, Docker, Github

Project is specifically created to help solvers of this learn Java, Rest, Docker and other
technologies. Task assignment is situated as complete project which includes development, testing, dev ops, ...

In this project you should create Rest API for library. Via Rest API you should be able to create
Book, Authors and Libraries.

## Prerequisites
- You should get familiar with Git. Whole project should be versioned in GitHub. For that you should
  learn how to commit changes, work with branches, pull requests, rebases and proper commit messages
- Another important thing is that you should be familiar with [Rest API](https://aws.amazon.com/what-is/restful-api/) and [HTTP/S response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) 
- For those there is nice interactive [cousre](https://learngitbranching.js.org/) and [guideline](https://initialcommit.com/blog/git-commit-messages-best-practices) for writing commit messages
- All work should be done in separate pull requests, These requests will contain only work that is related to current task/part of the project (for example pull request - Create database entities will contain
  only entities under `model/entity` module).
- In order to work on this project you should have install following things:
    1. [Java 11](https://tecadmin.net/install-java-on-fedora/)
    2. [Maven](https://tecadmin.net/install-apache-maven-on-fedora/)
    3. [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/#section=linux) 
    4. [Docker](https://docs.docker.com/engine/install/fedora/)
    5. I personally recommend [DataGrip](https://www.jetbrains.com/datagrip/download/#section=linux) for databases. All IntelliJ products are free for education purposes (just need to create account
with ISIC card number) 
## Assignment

1. Create data model for your library Rest API
    - Deploy PostgreSQL database in container
        - To deploy this database you should get familiar with Docker and Postgres
        - These links should help you to get familiar with these technologies
            1. [Docker documentation](https://docs.docker.com/get-started/overview/)
            2. [PostgreSQL tutorial](https://www.postgresqltutorial.com/) 
            3. [Example project](https://github.com/obabec/iis-projekt/tree/main/docker/db)
        - All necessary files for the database deployment should live in docker/db folder. 
          sql_dump.sql script should contain database dump that will be used to initialize the database
          container.
        - Create `docker-compose.yml` file that will allow to deploy database using [docker-compose](https://learn.microsoft.com/cs-cz/dotnet/architecture/microservices/multi-container-microservice-net-applications/multi-container-applications-docker-compose).
          Later in this project you will add also this rest API to this compose.
    - Create database entries for Book, Author and Library. You can imagine all relations
      between these these tables. Author should have all necessary attributes (name, surname, date of
      birth and other attributes), Book (name, release date, isbn, author, ...), Library (name, address, 
      books, ...)
    - Create Java classes that should map these database entries. All these classes should live in `model`
      module where you can also find some examples.
2. Create Rest endpoints and database services
   - Once all database mapping is done you should create all necessary endpoints. How these endpoints
     will look like is entirely up to you. You should just keep in mind that you should allow only operations
     that are necessary for end user.
   - Also keep in mind that you should validate all the values that are handed to the application.
     (For example, forbidden characters should not be passed. Name has some rules (a-zA-Z),...)
   - You can find examples of services in `AuthorServiceImpl`. All endpoints that should exec some operation
     will receive object via [JSON](https://www.baeldung.com/spring-mvc-send-json-parameters) format.
3. Create testsuite for the API you created
    - First you should create unit tests for all the functionality
      - Here you can find short description about [unit testing](https://www.baeldung.com/java-unit-testing-best-practices)
      - Unit testing is basically testing one functionality alone. In this case you have several components: database service, rest endpoints. For unit testing you have to test functionality
        of rest endpoint alone and not together with database. For that you have to use [mocks](https://stackoverflow.com/questions/2665812/what-is-mocking). There is short overview of the unit testing
        in [spring boot](https://www.section.io/engineering-education/getting-started-with-unit-testing-with-spring-boot/)
    - Create integration tests for the Rest API
      - Some tests are already prepared in `test/java` module. You can use these as an example
      - Integration testing is basically opposite as unit testing. You are testing integration of the components (database services + rest endpoints)
    - You should aim to 100% coverage on both unit and integration tests. That means all functionality should be tested!
4. Create [GitHub Actions](https://docs.github.com/en/actions) workflows
    - This part of the assignment should introduce you to world of dev/ops.
    - You should prepare two separate workflows
        1. Pull request verify
            - This workflow should be triggered for all pull requests open against the repository
            - Workflow should run all tests in the repository, execute maven checkstyle and try building docker image
        2. Build and push image
            - Workflow should build the Docker image for this application and push image to quay.io image hub
