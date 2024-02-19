FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY ./build/libs/Mour.jar /app/Mour.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "Mour.jar"]
