FROM tomcat:jre8-alpine

RUN rm -rf /usr/local/tomcat/webapps/*
ADD target/atvapi.war /usr/local/tomcat/webapps/atvapi.war

EXPOSE 8080
CMD ["catalina.sh", "run"]


# build:    sudo docker build -t atvapi . 
# start:    sudo docker run -p 80:8080 atvapi
# use:      http://localhost/atvpi
