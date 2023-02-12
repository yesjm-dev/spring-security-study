FROM amazoncorretto:17-alpine
COPY build/libs/*.jar security1.jar
EXPOSE 80:80
CMD ["java", "-jar", "security1.jar"]