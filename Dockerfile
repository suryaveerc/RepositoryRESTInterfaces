FROM tomcat:jre8
MAINTAINER suryaveer
cp PresenceRepository.war /usr/local/tomcat/webapps

EXPOSE 8080
CMD ["catalina.sh", "run"]
