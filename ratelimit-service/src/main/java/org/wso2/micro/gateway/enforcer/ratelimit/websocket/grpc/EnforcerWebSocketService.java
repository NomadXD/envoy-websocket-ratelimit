package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.wasm.EnforcerWebSocketServiceGrpc;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.wasm.WebSocketFrameRequest;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.wasm.WebSocketFrameResponse;

public class EnforcerWebSocketService extends EnforcerWebSocketServiceGrpc.EnforcerWebSocketServiceImplBase {
    private static final Logger LOGGER = LogManager.getLogger(EnforcerWebSocketService.class.getName());
    @Override public StreamObserver<WebSocketFrameRequest> publishFrameData(
            StreamObserver<WebSocketFrameResponse> responseObserver) {
        LOGGER.info("publishFrameData called");
        return new WebSocketFrameServiceObserver(responseObserver);
    }
}
