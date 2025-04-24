FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/hql-fabric-example-1.0-SNAPSHOT.jar  app.jar

# exposed ports, one for web, one for prometheus
EXPOSE 8080
EXPOSE 9101

ENTRYPOINT ["java", "-jar", "app.jar"]