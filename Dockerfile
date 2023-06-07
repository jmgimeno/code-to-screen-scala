FROM eclipse-temurin:20-alpine

WORKDIR /home/app

COPY ./backend/target/scala-3.3.0/*standalone.jar ./code-to-screen.jar

EXPOSE 8080

CMD java $JAVA_OPTS -jar code-to-screen.jar