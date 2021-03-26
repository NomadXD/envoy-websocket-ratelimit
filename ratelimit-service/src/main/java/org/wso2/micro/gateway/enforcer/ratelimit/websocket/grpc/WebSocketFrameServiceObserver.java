package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.api.RateLimitResponse;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.wasm.WebSocketFrameRequest;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.wasm.WebSocketFrameResponse;

public class WebSocketFrameServiceObserver implements StreamObserver<WebSocketFrameRequest> {
    private static final Logger LOGGER = LogManager.getLogger(WebSocketFrameServiceObserver.class.getName());
    private StreamObserver<WebSocketFrameResponse> responseStreamObserver;

    public WebSocketFrameServiceObserver(StreamObserver<WebSocketFrameResponse> responseStreamObserver) {
        this.responseStreamObserver = responseStreamObserver;
    }

    @Override public void onNext(WebSocketFrameRequest value) {
        //ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.info("websocket frame service onNext: "+ value.toString());
        WebSocketFrameResponse response = WebSocketFrameResponse.newBuilder().setMessage("Hello from server").build();
        this.responseStreamObserver.onNext(response);
    }

    @Override public void onError(Throwable t) {
        LOGGER.info("websocket frame service onError: "+ t);

    }

    @Override public void onCompleted() {
        LOGGER.info("websocket frame service onCompleted: ");

    }
}
