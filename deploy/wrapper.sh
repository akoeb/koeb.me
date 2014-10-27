#!/bin/bash
# simple wrapper script to start the application with the
# provided wrapper script koeb_me in the same dir
# I want to read configuration values before starting up the app

set -xe

CONFDIR="/opt/docker/start-conf"
APP_SECRET_FILE="${CONFDIR}/application.txt"
KEYSTORE_FILE="${CONFDIR}/mykeystore.keystore"


# are the files available?
if [ ! -d $CONFDIR ]
then
    echo  "No configuration directory /opt/docker/start-conf, can not load" 1>&2
    exit 1
fi

if [ ! -e $APP_SECRET_FILE ]
then
    echo  "No application secret file, can not load" 1>&2
    exit 1
fi

if [ ! -e $KEYSTORE_FILE ]
then
    echo  "No keystore file, can not load" 1>&2
    exit 1
fi


# ok, get the application secret from the provided file and write it into application conf:
app_secret=$( head -n1 $APP_SECRET_FILE )

if [ -z "$app_secret" ]
then
    echo "cannot parse application secret from file" 1>&2
    exit 1
fi

# remove application secret line from conf file
sed 's/^application.secret=.\+$//' -i /opt/docker/conf/application.conf

# put keystore to app:
cp $KEYSTORE_FILE /opt/docker/conf/

# and start the app:
./koeb_me -Dapplication.secret="$app_secret" -Dhttps.port=9000 -Dhttps.port=9443 -Dhttps.keyStore=/opt/docker/conf/mykeystore.keystore -Dhttps.keyStorePassword=changeme $@

