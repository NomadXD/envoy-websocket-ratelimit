package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import io.envoyproxy.envoy.service.auth.v3.CheckRequest;

import java.util.Map;

public class RequestContext {
    private String requestPath;
    private String requestMethod;
    private Map<String, String> headers;

    public RequestContext(CheckRequest checkRequest) {
        this.requestPath = checkRequest.getAttributes().getRequest().getHttp().getPath();
        this.requestMethod = checkRequest.getAttributes().getRequest().getHttp().getMethod();
        this.headers = checkRequest.getAttributes().getRequest().getHttp().getHeadersMap();
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
