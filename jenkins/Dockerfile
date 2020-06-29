# Starting off with the Jenkins base Image
FROM jenkins/jenkins
RUN /usr/local/bin/install-plugins.sh sonar gitlab-plugin docker-plugin matrix-auth workflow-aggregator docker-workflow docker-compose-build-step blueocean credentials-binding dependency-check-jenkins-plugin
RUN ulimit -c unlimited

USER root
# Installing the plugins we need using the in-built install-plugins.sh script
RUN apt-get update
RUN apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common -y
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
RUN apt-get update
RUN apt-get install docker-ce docker-ce-cli containerd.io -y
RUN apt-get install docker.ce -y
RUN curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

# Setting up environment variables for Jenkins admin user
ENV JENKINS_USER admin
ENV JENKINS_PASS adminpwd
ENV JENKINS_DEV_USER=user1
ENV JENKINS_DEV_PASS=user1pwd
# Skip the initial setup wizard
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

# Start-up scripts to set number of executors and creating the admin user
COPY create_jenkins_admin.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY general_maven.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY general_url.groovy /usr/share/jenkins/ref/init.groovy.d/
COPY create_jenkins_admin.groovy /var/jenkins_home/init.groovy.d/
COPY general_maven.groovy /var/jenkins_home/init.groovy.d/
COPY general_url.groovy /var/jenkins_home/init.groovy.d/
COPY ./jobs /var/jenkins_home/jobs