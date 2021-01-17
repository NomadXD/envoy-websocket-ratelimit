package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AuthThreadPoolExecutor extends ThreadPoolExecutor{
    public AuthThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue){
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
}
