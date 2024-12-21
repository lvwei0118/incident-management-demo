FROM nginx

MAINTAINER yangao

RUN rm /etc/nginx/conf.d/nginx.conf

ADD nginx.conf /etc/nginx/conf.d/

COPY dist/ /usr/share/nginx/html/
