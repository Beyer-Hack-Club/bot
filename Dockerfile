FROM gradle:8.14.4-jdk21-alpine AS builder

WORKDIR /jbuild

COPY . .

RUN gradle --no-daemon build


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=builder /jbuild/build/dist/HCBot-1.0.jar .

ENTRYPOINT [ "java", "-jar", "HCBot-1.0.jar" ]