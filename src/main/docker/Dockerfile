FROM openjdk:10-jre-slim

LABEL maintainer="barlog@tanelorn.li"

ARG JAVA_OPTS="-XX:-TieredCompilation -XX:MaxRAMFraction=1"
ARG PORT=8080

ENV JAVA_OPTS $JAVA_OPTS
ENV PORT $PORT

EXPOSE $PORT

COPY app.jar /

CMD java $JAVA_OPTS \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /app.jar \
	--server.port=$PORT
