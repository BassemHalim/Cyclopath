FROM mcr.microsoft.com/playwright/java
WORKDIR /app
COPY build/libs/server-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]