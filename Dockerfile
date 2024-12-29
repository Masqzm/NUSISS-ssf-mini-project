### DOCKERISATION - STAGE 1
#---------------------------
# Install JDK
FROM eclipse-temurin:22-jdk AS builder

LABEL maintainer="hazim"

# Set working dir
WORKDIR /compileDir

# Copy files & folders over
COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src    

# Build jar app
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true


### DOCKERISATION - STAGE 2
#---------------------------
FROM eclipse-temurin:22-jdk

# Set working dir
WORKDIR /app

# Copy over jar from first container (builder), rename to preferred app name
COPY --from=builder /compileDir/target/miniproject-0.0.1-SNAPSHOT.jar miniproject.jar

# Set environment variables
ENV SERVER_PORT=3000
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0
ENV SPRING_DATA_REDIS_USERNAME=""
ENV SPRING_DATA_REDIS_PASSWORD=""

ENV GOOGLE_API_KEY=""

EXPOSE ${SERVER_PORT}

# Run app
ENTRYPOINT java -jar miniproject.jar