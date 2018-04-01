FROM java:8
WORKDIR /
COPY src/KagelMarchMadness.jar /
COPY src/*.csv /input/
EXPOSE 8080
CMD ["java","-jar", "KagelMarchMadness.jar"]