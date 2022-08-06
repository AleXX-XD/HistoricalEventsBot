FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/HistoryEventsBot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "-Dfile.encoding=UTF-8", "app.jar"]