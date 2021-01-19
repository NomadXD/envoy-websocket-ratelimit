package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.api.RateLimitRequest;
import org.wso2.micro.gateway.enforcer.ratelimit.websocket.api.RateLimitResponse;

public class MgwResponseObserver implements StreamObserver<RateLimitRequest>{
    private static final Logger LOGGER = LogManager.getLogger(RateLimitService.class.getName());
    private String subscriptionId;
    private StreamObserver<RateLimitResponse> responseStreamObserver;

    public MgwResponseObserver(StreamObserver<RateLimitResponse> responseStreamObserver) {
        this.responseStreamObserver = responseStreamObserver;
    }

    @Override
    public void onNext(RateLimitRequest rateLimitRequest) {
        LOGGER.info("metadata context :"+ rateLimitRequest.getMetadataContext().getFilterMetadataMap().get("envoy.filters.http.ext_authz"));
        Struct extAuthMetadata = rateLimitRequest.getMetadataContext().getFilterMetadataMap().get("envoy.filters.http.ext_authz");
        JsonFormat.Printer printer = JsonFormat.printer().includingDefaultValueFields();
        try{
            String jsonObject = printer.print(extAuthMetadata);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonObject);
            this.subscriptionId = (String) json.get("subscriptionKey");
            RateLimitService.addObserver(this.subscriptionId, this);
            LOGGER.info("ext auth :"+ json.toString());
        }catch (Exception e){
            LOGGER.error("Error:", new Error(e));
        }

        RateLimitResponse response = RateLimitResponse.newBuilder().setOverallCode(RateLimitResponse.Code.OK).build();
        this.responseStreamObserver.onNext(response);
    }

    @Override
    public void onError(Throwable throwable) {
        RateLimitService.removeObserver(this.subscriptionId);
        LOGGER.info("onError:", new Error(throwable));

    }

    @Override
    public void onCompleted() {
        RateLimitService.removeObserver(this.subscriptionId);
        LOGGER.info("onCompleted");
    }
}
