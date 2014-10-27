#!/bin/bash
docker rmi nginx
docker build -t nginx .
docker create -v $(pwd)/log-dir:/var/log/nginx -v $(pwd)/tmp:/etc/nginx --name web nginx
docker export web | bzip2 -c > ~/projects/coreOS/myCoreOs-bootstrap/roles/nginx/files/nginx_dockerimage.tar.bz2 
docker rm web
