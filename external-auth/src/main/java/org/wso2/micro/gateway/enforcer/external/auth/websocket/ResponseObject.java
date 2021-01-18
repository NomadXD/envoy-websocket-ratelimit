package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import org.json.simple.JSONObject;

import java.util.Map;

public class ResponseObject {
    private int statusCode;
    private String errorCode;
    private String errorDescription;
    private JSONObject keys;

    public JSONObject getKeys() {
        return keys;
    }

    public void setKeys(JSONObject keys) {
        this.keys = keys;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
