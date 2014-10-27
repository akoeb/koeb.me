FROM debian:stable
MAINTAINER Alexander Köb <nerdkram@koeb.me>
VOLUME ["/opt/docker/data", "/opt/docker/start-conf"]
EXPOSE 9000
WORKDIR /opt/docker
ADD bin /opt/docker/bin/
ADD lib /opt/docker/lib/
ADD conf /opt/docker/conf/
RUN apt-get update && apt-get install -y openjdk-7-jre-headless && apt-get clean
RUN chown -R daemon /opt/docker/
USER daemon
# we start the app from outside in the docker call
# this way, we have finer grained control over start params
ENTRYPOINT []
CMD []
