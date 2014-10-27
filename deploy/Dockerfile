FROM debian:stable
MAINTAINER Alexander Köb <nerdkram@koeb.me>
VOLUME ["/opt/docker/data", "/opt/docker/start-conf"]
EXPOSE 9000
WORKDIR /opt/docker
ADD bin /opt/docker/
ADD lib /opt/docker/
ADD conf /opt/docker/
RUN apt-get update && apt-get install -y openjdk-7-jre-headless && apt-get clean
RUN chown -R daemon /opt/docker/
USER daemon
ENTRYPOINT [bin/wrapper.sh]
CMD []
