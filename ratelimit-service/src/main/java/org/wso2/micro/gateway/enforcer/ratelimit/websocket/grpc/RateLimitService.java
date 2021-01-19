package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import com.google.protobuf.Struct;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.api.*;

import com.google.protobuf.util.JsonFormat;
import java.util.concurrent.ConcurrentHashMap;


public class RateLimitService extends RateLimitServiceGrpc.RateLimitServiceImplBase{
    private static final Logger LOGGER = LogManager.getLogger(RateLimitService.class.getName());
    private static ConcurrentHashMap<String, MgwResponseObserver> responseObserverArr = new ConcurrentHashMap<>();

    public RateLimitService() {
        LOGGER.info(">>>>>>>>>>>>>>>>> constructor invoked");
    }

    @Override
    public void shouldRateLimit(RateLimitRequest request, StreamObserver<RateLimitResponse> responseObserver) {
        super.shouldRateLimit(request, responseObserver);
    }

    @Override
    public StreamObserver<RateLimitRequest> shouldRateLimitStream(StreamObserver<RateLimitResponse> responseObserver) {
        LOGGER.info("shouldRateLimitStream method invoked");
        //MgwResponseObserver mgwResponseObserver = new MgwResponseObserver(responseObserver);


        return new MgwResponseObserver(responseObserver);
//        return new StreamObserver<RateLimitRequest>() {
//            @Override
//            public void onNext(RateLimitRequest rateLimitRequest) {
//                LOGGER.info("metadata context:"+ rateLimitRequest.getMetadataContext().getFilterMetadataMap().get("envoy.filters.http.ext_authz"));
//                Struct extAuthMetadata = rateLimitRequest.getMetadataContext().getFilterMetadataMap().get("envoy.filters.http.ext_authz");
//                JsonFormat.Printer printer = JsonFormat.printer().includingDefaultValueFields();
//                try{
//                    String jsonObject = printer.print(extAuthMetadata);
//                    LOGGER.info("ext auth :"+ jsonObject);
//                }catch (Exception e){
//                    LOGGER.error("Error:", new Error(e));
//                }
//
//                RateLimitResponse response = RateLimitResponse.newBuilder().setOverallCode(RateLimitResponse.Code.OK).build();
//                responseObserver.onNext(response);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                LOGGER.error("OnError:", throwable);
//            }
//
//            @Override
//            public void onCompleted() {
//                responseObserver.onCompleted();
//                LOGGER.info("onCompleted");
//            }
//        };
    }

    public static void addObserver(String subscriptionKey,MgwResponseObserver observer){
        responseObserverArr.put(subscriptionKey, observer);
        LOGGER.info(responseObserverArr.toString());
    }
    public static void removeObserver(String subscriptionKey){
        responseObserverArr.remove(subscriptionKey);
        LOGGER.info(responseObserverArr.toString());
    }
}




