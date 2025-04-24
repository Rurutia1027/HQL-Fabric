FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/hql-fabric-routing-1.0-SNAPSHOT.jar  app.jar

# exposed ports, one for web, one for prometheus
EXPOSE 8082
EXPOSE 9103

ENTRYPOINT ["java", "-jar", "app.jar"]