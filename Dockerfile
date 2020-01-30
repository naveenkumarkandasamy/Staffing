FROM openjdk:8
ADD target/Staffing.jar Staffing.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "Staffing.jar"]