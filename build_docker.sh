#!/bin/bash
# wrapper to build a docker container from the app
# uses partly thedocker task of the sbt plugin sbt-native-packager
# http://www.scala-sbt.org/sbt-native-packager/DetailedTopics/docker.html
# but adds additional RUN commands to docker file
# additionally, after creating the image, a container is started from that image and exported,
# to flatten the result


# create docker file with the app prepared:
sbt docker:stage

# now manipulate the docker file
cd target/docker
if ! grep -q openjdk-7-jre-headless Dockerfile
then
    dockerfile=$(awk '/^WORKDIR / { print; print "RUN apt-get update && apt-get install -y openjdk-7-jre-headless && apt-get clean"; next }1' Dockerfile)
    echo -en "$dockerfile" > Dockerfile
fi

# remove entrypoint to give more control to outside:
sed  's/ENTRYPOINT \["bin\/koeb_me"\]/ENTRYPOINT \[\]/' -i Dockerfile

# then run a docker build:
docker build --tag koeb_me .

# tag the app version to the build:
VERSION=$(cat ../../build.sbt | perl -ne 'if(/^version := "([\d\.]+)"/) {print $1;}')
docker tag koeb_me:latest koeb_me:$VERSION

# flatten the image:
docker run -d --name web  --volumes-from data-container  koeb_me
docker export web | bzip2 -c >  $HOME/projects/coreOS/myCoreOs-bootstrap/roles/app/files/koeb_me_dockerimage.tar.bz2
docker stop web
docker rm web

