FROM registry.connect.redhat.com/ibm/ibmjava8-jre-ubi8-minimal
COPY target/springboot-items-api-0.0.1-SNAPSHOT.jar /app.jar

USER 1001
EXPOSE 8080
EXPOSE 9001
ENTRYPOINT ["java","-Xmx308m","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]