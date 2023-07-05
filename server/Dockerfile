FROM mcr.microsoft.com/playwright/java
VOLUME /tmp
COPY build/libs/server-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]