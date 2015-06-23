FROM java:8u45-jre

COPY target/game-of-life.jar /tmp/game-of-life.jar

CMD ["java", "-jar", "/tmp/game-of-life.jar"]
