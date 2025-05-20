FROM amazoncorretto:21

WORKDIR /usr/local/app

COPY ./target/*.jar ./

EXPOSE 8080

RUN chmod 777 *.jar

CMD ["java", "-jar", "/usr/local/app/order-0.0.1-SNAPSHOT.jar"]

