# Tripr

This app was created with Bootify.io - tips on working with the code [can be found here](https://bootify.io/next-steps/).

## Development

When starting the application `docker compose up` is called and the app will connect to the contained services.
[Docker](https://www.docker.com/get-started/) must be available on the current system.

During development it is recommended to use the profile `local`. In IntelliJ `-Dspring.profiles.active=local` can be
added in the VM options of the Run Configuration after enabling this property in "Modify options". Create your own
`application-local.yml` file to override settings for development. For this multi-module
project you have to select the highest module `tripr-web.main` as the classpath.

In addition to the Spring Boot application, the development server must also be started - for this
[Node.js](https://nodejs.org/) version 22 is required. Angular CLI and required dependencies must be installed once:

```
npm install -g @angular/cli
npm install
```

The development server can be started as follows:

```
ng serve
```

Your application is now accessible under `localhost:4200`.

Add code using Angular schematics with `ng generate ...`.
Generate a messages.json for translation with `ng extract-i18n --format=json`.

## Testing requirements

Testcontainers is used for running the integration tests. Due
to the reuse flag, the container will not shut down after the tests. It can be stopped manually if needed.

Frontend unit tests can be executed with `ng test`.

## Build

The application can be tested and built using the following command:

```
gradlew clean build
```

Start your application with the following command - here with the profile `production`:

```
java -Dspring.profiles.active=production -jar ./tripr-web/build/libs/tripr-web-0.0.1-SNAPSHOT.jar
```

If required, a Docker image can be created with the Spring Boot plugin. Add `SPRING_PROFILES_ACTIVE=production` as
environment variable when running the container.

```
gradlew bootBuildImage --imageName=dev.vmillet/tripr
```

## Further readings

* [Gradle user manual](https://docs.gradle.org/)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Learn Angular](https://angular.dev/tutorials/learn-angular)  
* [Angular CLI](https://angular.dev/tools/cli)
* [Bootstrap docs](https://getbootstrap.com/docs/5.3/getting-started/introduction/)  
