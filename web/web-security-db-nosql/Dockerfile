FROM maven:3.6.3-openjdk-14 as build

COPY *.xml ./

# download the dependency if needed or if the pom file is changed
RUN mvn dependency:go-offline -B

COPY src src
RUN mvn spring-javaformat:apply
RUN mvn clean package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production Stage for Spring boot application image
FROM openjdk:14-jdk-alpine as production
ARG DEPENDENCY=/target/dependency

# Copy the dependency application file from build stage artifact
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Run the Spring boot application
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "app:app/lib/*","es.urjc.webcustomnosql.WebApplication"]
#ENTRYPOINT ["java", "-cp", "app:app/lib/*","es.urjc.webcustomnosql.WebApplication"]