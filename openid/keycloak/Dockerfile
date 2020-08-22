# Starting off with the Jenkins base Image
FROM jboss/keycloak
RUN ulimit -c unlimited

# Setting up environment variables for Jenkins admin user
ENV KEYCLOAK_USER: admin
ENV KEYCLOAK_PASSWORD: admin

# Start-up scripts to set number of executors and creating the admin user
COPY ./data /opt/jboss/keycloak/standalone
USER root
RUN chown -R jboss:root /opt/jboss/keycloak/standalone/data
RUN chmod -R +rw /opt/jboss/keycloak/standalone/data