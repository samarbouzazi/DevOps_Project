# FROM maven:3.8.3-jdk-11 AS builder
# WORKDIR /app
# COPY pom.xml .
# RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
# COPY src/ src/
# RUN --mount=type=cache,target=/root/.m2 mvn package

# # Stage 2: Create the runtime container
# FROM adoptopenjdk/openjdk11:jre-11.0.12_7-alpine
# EXPOSE 8082
# COPY --from=builder /app/target/DevOps_Project-1.0.jar /DevOps_Project-1.0.jar
# ENV JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG"
# ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "/DevOps_Project-1.0.jar"]


# # Use an official OpenJDK runtime as a parent image
# FROM openjdk:11-jre-slim

# # Set the working directory in the container
# WORKDIR /app

# # Copy the compiled JAR file from your local machine into the container at /app
# COPY target/DevOps_Project-1.0.jar /app/DevOps_Project-1.0.jar

# # Expose the port that the application will run on
# EXPOSE 8082

# # Define the command to run your application
# CMD ["java", "-jar", "DevOps_Project-1.0.jar"]


# Stage 1: Build the application
FROM maven:3.8.3-openjdk-8 AS builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 mvn package

# Stage 2: Create the runtime container
FROM openjdk:8-jre-slim
EXPOSE 8082
# Install curl in the container

RUN apt-get update && apt-get install -y curl

# Download the .jar file from Nexus and copy it to the container

# ARG NEXUS_URL="http://192.168.153.128:8081/repository/maven-releases/"
# ARG ARTIFACT_PATH="tn/esprit/DevOps_Project/1.0/DevOps_Project-1.0.jar"

# RUN curl -o /DevOps_Project-1.0.jar ${NEXUS_URL}${ARTIFACT_PATH}

COPY --from=builder /app/target/DevOps_Project-1.0.jar /DevOps_Project-1.0.jar
ENV JAVA_OPTS="-Dlogging.level.org.springframework.security=DEBUG -Djdk.tls.client.protocols=TLSv1.2"
ENTRYPOINT ["java", "-jar", "/DevOps_Project-1.0.jar"]

