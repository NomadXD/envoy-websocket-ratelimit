package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RateLimitThreadPoolExecutor extends ThreadPoolExecutor{
    public RateLimitThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue){
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
}
