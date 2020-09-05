# Starting off with the Jenkins base Image
FROM jboss/keycloak:11.0.1
RUN ulimit -c unlimited

# Setting up environment variables for Jenkins admin user
ENV KEYCLOAK_USER: admin
ENV KEYCLOAK_PASSWORD: admin

# Start-up scripts to set number of executors and creating the admin user
RUN ls -l /opt/jboss/keycloak/standalone/data
COPY ./data /opt/jboss/keycloak/standalone
USER root
RUN chown -R jboss:root /opt/jboss/keycloak/standalone/data
RUN chmod -R +rw /opt/jboss/keycloak/standalone/data
RUN ls -l /opt/jboss/keycloak/standalone/data
USER root
ENV TZ=Europe/Madrid
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone