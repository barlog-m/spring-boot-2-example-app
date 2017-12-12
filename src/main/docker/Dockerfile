FROM anapsix/alpine-java:8_jdk_unlimited

MAINTAINER barlog@tanelorn.li

ARG JAVA_OPTS
ARG PORT=8080

ENV JAVA_OPTS \$JAVA_OPTS
ENV PORT \$PORT

EXPOSE \$PORT

COPY app.jar /

CMD java \$JAVA_OPTS \
	-Djava.security.egd=file:/dev/./urandom \
	-jar /app.jar \
	--port=\$PORT
