#!/bin/bash
# wrapper to build a docker container from the app
# * run activator stage to prepare the app
# * copy docker file from deploy to target
# * copy wrapper script from deploy to target
# * run docker build

set -e

# prepare the app in target/stage:
/opt/activator-1.2.10-minimal/activator stage

# get the docker file
cp deploy/Dockerfile target/universal/stage/

# get the wrapper script from deploy
cp -p deploy/wrapper.sh target/universal/stage/bin/
cp -p deploy/bcrypt-tool target/universal/stage/bin/

# then run a docker build:
cd target/universal/stage/
docker build --tag koeb_me .

# tag the app version to the build:
VERSION=$(cat ../../../build.sbt | perl -ne 'if(/^version := "([\d\.]+)"/) {print $1;}')
docker tag koeb_me:latest koeb_me:$VERSION

# flatten the image:
# we do not do this regularly
# docker run -d --name web  --volumes-from data-container  koeb_me
# docker export web | bzip2 -c >  deploy/ansible/roles/app/files/koeb_me_dockerimage.tar.bz2
# docker stop web
# docker rm web

