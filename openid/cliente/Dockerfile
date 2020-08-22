FROM maven:3.6.3-openjdk-14 as build

COPY *.xml ./

# download the dependency if needed or if the pom file is changed
RUN mvn dependency:go-offline -B

COPY src src
ARG googleclientsecret
ARG googleclientid
ARG oktaclientsecret
ARG oktaclientid
ARG oktadomain
RUN echo ${googleclientsecret} ${googleclientid} ${oktaclientsecret} ${oktaclientid} ${oktadomain}

RUN sed -ir "s/google_client_secret_value/${googleclientsecret}/g" /src/main/resources/application.yml
RUN sed -ir "s/google_client_id_value/${googleclientid}/g" /src/main/resources/application.yml
RUN sed -ir "s/okta_client_secret/${oktaclientsecret}/g" /src/main/resources/application.yml
RUN sed -ir "s/okta_clientid/${oktaclientid}/g" /src/main/resources/application.yml
RUN sed -ir "s,okta_domain,${oktadomain},g" /src/main/resources/application.yml
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

# Run the Spring boot application
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "app:app/lib/*","es.urjc.openid.oidcclient.ClientApplication"]
#ENTRYPOINT ["java", "-cp", "app:app/lib/*","es.urjc.openid.oidcclient.ClientApplication"]