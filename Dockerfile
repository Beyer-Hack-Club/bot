FROM gradle:8.14.4-jdk21-alpine AS builder

WORKDIR /jbuild

COPY . .

RUN gradle --no-daemon build


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# The code currently needs this (dummy) .env file here otherwise it'll crash
RUN touch .env

COPY --from=builder /jbuild/build/dist/SoltideBot-1.0.jar .

ENTRYPOINT [ "java", "-jar", "SoltideBot-1.0.jar" ]