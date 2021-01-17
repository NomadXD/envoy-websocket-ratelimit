package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.api.*;
//
public class RateLimitService extends RateLimitServiceGrpc.RateLimitServiceImplBase{
    private static final Logger LOGGER = LogManager.getLogger(RateLimitService.class.getName());
    @Override
    public void shouldRateLimit(RateLimitRequest request, StreamObserver<RateLimitResponse> responseObserver) {
        super.shouldRateLimit(request, responseObserver);
    }

    @Override
    public StreamObserver<RateLimitRequest> shouldRateLimitStream(StreamObserver<RateLimitResponse> responseObserver) {
        LOGGER.info("shouldRateLimitStream method invoked");
        return new StreamObserver<RateLimitRequest>() {
            @Override
            public void onNext(RateLimitRequest rateLimitRequest) {
                LOGGER.info("StreamObserver onNext()");
                RateLimitResponse response = RateLimitResponse.newBuilder().setOverallCode(RateLimitResponse.Code.OK).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.error("OnError:", throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
                LOGGER.info("onCompleted");
            }
        };
    }
}
