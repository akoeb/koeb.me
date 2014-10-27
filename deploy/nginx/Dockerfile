#
# based on Nginx Dockerfile from https://github.com/dockerfile/nginx
# but with some modifications:
# use debian instead of ubuntu
# * baked in configuration
# * log dir is the only exposed volume
#

# Pull base image.
FROM debian:stable 

# Install Nginx.
RUN \
  apt-get update && \
  apt-get install -y nginx && \
  apt-get clean && \
  rm -rf /var/lib/apt/lists/* && \
  echo "\ndaemon off;" >> /etc/nginx/nginx.conf && \
  rm /etc/nginx/sites-enabled/default  && \
  chown -R www-data:www-data /var/lib/nginx


ADD sites-enabled-dir/* /etc/nginx/sites-enabled/
ADD conf.d-dir/*  /etc/nginx/conf.d/
ADD certs-dir/* /etc/nginx/certs/


# Define mountable directories.
VOLUME ["/var/log/nginx", "/etc/nginx/"]

# Define working directory.
WORKDIR /etc/nginx

# Define default command.
CMD ["/usr/sbin/nginx"]

# Expose ports.
EXPOSE 80
EXPOSE 443
