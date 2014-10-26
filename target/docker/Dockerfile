FROM debian:stable
MAINTAINER Alexander Köb <nerdkram@koeb.me>
RUN ["mkdir", "-p", "/data"]
VOLUME ["/data"]
EXPOSE 9000
ADD files /
WORKDIR /opt/docker
RUN apt-get update && apt-get install -y openjdk-7-jre-headless && apt-get clean
RUN ["chown", "-R", "daemon", "."]
USER daemon
ENTRYPOINT []
CMD []