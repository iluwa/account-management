FROM openjdk:11

ADD ./target/account-management-1.0.0-SNAPSHOT.jar /app/account-management.jar
CMD ["java", "-Xmx300m", "-jar", "/app/account-management.jar"]

EXPOSE 8080