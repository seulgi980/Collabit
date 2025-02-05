FROM bellsoft/liberica-openjdk-alpine:17
COPY build/libs/*SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev", "-Duser.timezone=Asia/Seoul", "/app.jar"]