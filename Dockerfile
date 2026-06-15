FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
RUN chmod +x gradlew
COPY src ./src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/build/libs/study-moim-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /data/uploads

ENV UPLOAD_DIR=/data/uploads
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
