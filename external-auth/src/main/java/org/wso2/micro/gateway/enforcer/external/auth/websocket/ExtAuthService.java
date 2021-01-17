package org.wso2.micro.gateway.enforcer.external.auth.websocket;


import com.google.rpc.Code;
import com.google.rpc.Status;
import io.envoyproxy.envoy.config.core.v3.HeaderValue;
import io.envoyproxy.envoy.config.core.v3.HeaderValueOption;
import io.envoyproxy.envoy.service.auth.v3.*;
import io.envoyproxy.envoy.type.v3.HttpStatus;
import io.grpc.stub.StreamObserver;
import org.json.simple.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtAuthService extends AuthorizationGrpc.AuthorizationImplBase {

    private static final Logger LOGGER = LogManager.getLogger(ExtAuthService.class.getName());

    @Override
    public void check(CheckRequest request, StreamObserver<CheckResponse> responseObserver) {
        LOGGER.info("check method called, request id:"+ request.getAttributes().getRequest().getHttp().getId());
        RequestContext requestContext = new RequestContext(request);
        LOGGER.debug("request context created for request with id:"+request.getAttributes().getRequest().getHttp().getId());
        ResponseObject responseObject = RequestHandler.handleRequest(requestContext);
        LOGGER.debug("responseObject created for request with id:"+ request.getAttributes().getRequest().getHttp().getId());
        CheckResponse response = buildResponse(responseObject);
        LOGGER.debug("Checkresponse created for request with id:"+request.getAttributes().getRequest().getHttp().getId());
        responseObserver.onNext(response);

        responseObserver.onCompleted();
        LOGGER.info("checkresponse method invoked successfully for request id:"+request.getAttributes().getRequest().getHttp().getId());
    }

    private CheckResponse buildResponse(ResponseObject responseObject){
        if(responseObject.getStatusCode() != 200){
            LOGGER.debug("responseObject error code");
            String errorCode = responseObject.getErrorCode();
            String errorDescription = responseObject.getErrorDescription();
            JSONObject responseJson = new JSONObject();
            responseJson.put("errorCode", errorCode);
            responseJson.put("errorDescription", errorDescription);
            HeaderValueOption headerValueOption = HeaderValueOption.newBuilder()
                    .setHeader(HeaderValue.newBuilder().setKey("Content-type").setValue("application/json").build())
                    .build();
            HttpStatus status = HttpStatus.newBuilder().setCodeValue(responseObject.getStatusCode()).build();
            return CheckResponse.newBuilder().setStatus(Status.newBuilder().setCode(getCode(responseObject.getStatusCode())))
                        .setDeniedResponse(DeniedHttpResponse.newBuilder().setBody(responseJson.toString()).addHeaders(headerValueOption).setStatus(status).build()).build();


        }else {
            LOGGER.debug("responseObject status code: 200");
            OkHttpResponse.Builder okResponseBuilder = OkHttpResponse.newBuilder();
            return CheckResponse.newBuilder().setStatus(Status.newBuilder().setCode(Code.OK_VALUE).build())
                    .setOkResponse(okResponseBuilder.build()).build();
        }

    }

    private int getCode(int statusCode) {
        switch (statusCode) {
            case 200:
                LOGGER.debug("status code:"+Code.OK_VALUE);
                return Code.OK_VALUE;
            case 401:
                LOGGER.debug("status code:"+Code.UNAUTHENTICATED_VALUE);
                return Code.UNAUTHENTICATED_VALUE;
            case 403:
                LOGGER.debug("status code:"+Code.PERMISSION_DENIED_VALUE);
                return Code.PERMISSION_DENIED_VALUE;
            case 409:
                LOGGER.debug("status code:"+Code.RESOURCE_EXHAUSTED_VALUE);
                return Code.RESOURCE_EXHAUSTED_VALUE;
        }
        return Code.INTERNAL_VALUE;
    }


}
