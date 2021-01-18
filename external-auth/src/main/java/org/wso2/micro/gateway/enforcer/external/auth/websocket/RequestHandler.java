package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import io.jsonwebtoken.Claims;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;


import java.util.Map;

public class RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class.getName());

    public static ResponseObject handleRequest(RequestContext requestContext){
        String authHeader = getAuthHeader(requestContext.getHeaders());
        LOGGER.debug("auth header: "+authHeader);
        ResponseObject responseObject = new ResponseObject();
        try{
            String authToken = authHeader.split(" ")[1];
            Claims claims = TokenService.decodeJWT(authToken);
            JSONObject keys = new JSONObject();
            keys.put("apiKey", claims.get("apiKey"));
            keys.put("applicationKey", claims.get("applicationKey"));
            keys.put("subscriptionKey", claims.get("subscriptionKey"));
            responseObject.setKeys(keys);
            LOGGER.info("Claims: "+ claims.toString());
            responseObject.setStatusCode(200);
            LOGGER.info("Authentication successful");
        }catch (Exception e){
            LOGGER.info("Authentication failed");
            responseObject.setStatusCode(401);
            responseObject.setErrorCode("401");
            responseObject.setErrorDescription("Unauthorized");
            LOGGER.info("Authentication failed: ", e);
        }
        return responseObject;
    }

    public static String getAuthHeader(Map<String, String> headers){
        String authToken = null;
        for(Map.Entry<String, String> header : headers.entrySet()){
            System.out.println("Header :"+ header.toString());
            if (header.getKey().equals("authorization")){
                authToken = header.getValue();
                break;
            }
        }
        return authToken;
    }

}
