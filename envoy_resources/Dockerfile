FROM envoyproxy/envoy:v1.17.1

COPY ./envoy_resources/envoy.yaml /etc/envoy/envoy.yaml
COPY ./envoy_resources/mgw-websocket.wasm /etc/envoy/mgw-websocket.wasm

CMD /usr/local/bin/envoy -c /etc/envoy/envoy.yaml

EXPOSE 9095
EXPOSE 9000