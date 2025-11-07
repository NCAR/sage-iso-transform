# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY *.sh .
COPY examples ./examples

RUN mvn package spring-boot:repackage



# Stage 2: Run the application on some examples
RUN ./run_examples.sh


CMD ["/bin/bash"]
