FROM openjdk:8

LABEL org.opencontainers.image.authors="jingxueru@tezign.com"

ENV TZ="Asia/Shanghai" HOME="/root" JVM_PARAMS=" " SPRING_PARAMS=" "

WORKDIR ${HOME}

ADD target/*.jar ${HOME}/ROOT.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JVM_PARAMS -Djava.security.egd=file:/dev/./urandom -jar ${HOME}/ROOT.jar $SPRING_PARAMS"]
