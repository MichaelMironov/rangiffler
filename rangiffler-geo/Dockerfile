FROM openjdk:19-alpine
ARG APP_VER
ENV VER=${APP_VER}
ENV JAVA_OPTS=""
WORKDIR /application
COPY rangiffler-geo-$VER.jar ./

EXPOSE 8091
EXPOSE 8092
CMD java -jar -Dspring.profiles.active=docker $JAVA_OPTS rangiffler-geo-$VER.jar
