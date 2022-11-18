

FROM amazoncorretto:17
MAINTAINER github/gabrielamatheus
COPY ./target/locadora.jar /app/locadora.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "locadora.jar"]
EXPOSE 8080

#0.0.1-SNAPSHOT