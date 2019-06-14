FROM azul/zulu-openjdk-alpine:12

LABEL maintainer="barlog@tanelorn.li"

ARG JAVA_OPTS="-XX:-TieredCompilation"
ARG PORT=8080

ENV JAVA_OPTS ${JAVA_OPTS}
ENV SERVER_PORT ${PORT}

EXPOSE ${PORT}

ARG JAR_FILE
COPY ${JAR_FILE} /app.jar

CMD java ${JAVA_OPTS} \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /app.jar
