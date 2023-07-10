FROM mcr.microsoft.com/playwright/java
WORKDIR /app
COPY build/libs/server-0.0.1.jar app.jar
COPY ms-playwright /ms-playwright
ENTRYPOINT ["java","-jar","app.jar"]