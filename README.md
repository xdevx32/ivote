# voting-e-gov

Spring Boot Application freshly initialized project with a set of dependencies.

For editing html templates and css without stopping the app

```mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.thymeleaf.prefix=file:src/main/resources/templates/ -Dspring.thymeleaf.cache=false -Dspring.resources.static-locations=file:src/main/resources/static/ -Dspring.resources.cache-period=0"```
