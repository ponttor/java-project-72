FROM gradle:9.0.0-jdk21 AS build
WORKDIR /app
COPY app .
RUN ./gradlew installDist

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/install/app .
CMD ["bin/app"]
