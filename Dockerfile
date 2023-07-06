FROM gradle:8-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

COPY --from=build /home/gradle/src/build/libs/financial-transaction-service-*.jar ./financial-transaction-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "financial-transaction-service.jar"]
