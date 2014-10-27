#!/bin/bash
# simple wrapper script to start the application with the
# provided wrapper script koeb_me in the same dir
# I want to read configuration values before starting up the app

set -e



function start_app() {

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
    exec /opt/docker/bin/koeb_me \
        -Dapplication.secret="$app_secret" \
        -Dhttp.port=9000 -Dhttps.port=9443 \
        -Dhttps.keyStore=/opt/docker/conf/$(basename $KEYSTORE_FILE) \
        -Dhttps.keyStorePassword=changeit \
        $@

}

function stream_backup() {
    # create backup file:
    java -cp /opt/docker/lib/com.h2database.h2-1.3.175.jar \
        org.h2.tools.Script  \
        -url jdbc:h2:/opt/docker/data/koeb.me.db \
        -user sa -password oijd \
        -script /opt/docker/data/backup.sql 

    # stream to STDOUT for reading outside of the container
    cat /opt/docker/data/backup.sql
}

function reset_admin_pw() {
    local pass=$1
    # hash the password:
    crypt_pass=$(/opt/docker/bin/bcrypt-tool  hash $pass 13)
    
    echo "update users set password = '$crypt_pass' where username = 'koebi';"| java -cp /opt/docker/lib/com.h2database.h2-1.3.175.jar org.h2.tools.Shell -url jdbc:h2:/opt/docker/data/koeb.me.db  -user sa -password oijd
}

case $1 in 
    backup)
        stream_backup
        ;;
    passwd)
        reset_admin_pw $2
        ;;
    *)
        start_app $@
        ;;
esac

