FROM maven:3.8.3-jdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn package

# Stage 2: Create the runtime container
FROM adoptopenjdk/openjdk11:jre-11.0.12_7-alpine
EXPOSE 8082
COPY --from=builder /app/target/app-0.0.1-SNAPSHOT.jar /app-1.0-SNAPSHOT.jar
ENV JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG"
ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "/app-1.0-SNAPSHOT.jar"]
