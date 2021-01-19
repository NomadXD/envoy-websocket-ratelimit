FROM ubuntu:20.04

RUN apt-get update

COPY ./envoy/envoy /usr/local/bin/envoy

COPY ./envoy/envoy.yaml /etc/envoy/envoy.yaml

CMD /usr/local/bin/envoy -c /etc/envoy/envoy.yaml

EXPOSE 9095
EXPOSE 9000