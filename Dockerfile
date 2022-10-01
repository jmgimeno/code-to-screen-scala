FROM openjdk:17-jdk-slim

WORKDIR /home/app

COPY ./backend/target/scala-3.2.0/*standalone.jar ./code-to-screen.jar

EXPOSE 8080

CMD java $JAVA_OPTS -jar code-to-screen.jar