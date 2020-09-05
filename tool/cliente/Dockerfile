FROM maven:3.6.3-openjdk-14 as build

COPY *.xml ./

# download the dependency if needed or if the pom file is changed
#RUN mvn dependency:go-offline

COPY src src
ARG verifySign
ARG verifyExpired
ARG verifyClaims

RUN sed -ir "s/verifySign/${verifySign}/g" /src/main/resources/application.yml
RUN sed -ir "s/verifyExpired/${verifyExpired}/g" /src/main/resources/application.yml
RUN sed -ir "s/verifyClaims/${verifyClaims}/g" /src/main/resources/application.yml

RUN cat /src/main/resources/application.yml
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
ENV TZ=Europe/Madrid
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# Run the Spring boot application
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "app:app/lib/*","es.urjc.tool.oidcclient.ClientApplication"]
#ENTRYPOINT ["java", "-cp", "app:app/lib/*","es.urjc.tool.oidcclient.ClientApplication"]