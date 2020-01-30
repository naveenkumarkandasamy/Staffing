FROM openjdk:8
RUN sudo echo "Asia/Calcutta" > /etc/timezone
RUN sudo dpkg-reconfigure -f noninteractive tzdata
ADD target/Staffing.jar Staffing.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "Staffing.jar"]