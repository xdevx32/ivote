# voting-i-gov

Voting System providing UI for the end user and Administrative Panel for backend tasks.

First, create your MySQL database;

    create database i_vote_db character set cp1251;

Run Application.java and after loading you should reach the app at: http://localhost:8081 


For editing html templates and css without stopping the app

    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.thymeleaf.prefix=file:src/main/resources/templates/ -Dspring.thymeleaf.cache=false -Dspring.resources.static-locations=file:src/main/resources/static/ -Dspring.resources.cache-period=0"
