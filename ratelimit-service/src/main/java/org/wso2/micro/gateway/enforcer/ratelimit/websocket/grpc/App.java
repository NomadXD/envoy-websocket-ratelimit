package org.wso2.micro.gateway.enforcer.ratelimit.websocket.grpc;


import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static final Logger LOGGER = LogManager.getLogger(App.class.getName());
    private static final int GRPC_PORT = 50052;

    public static void main( String[] args ) throws IOException, InterruptedException
    {
        LOGGER.info("Starting RateLimit gRPC server"+GRPC_PORT+"...");
        final EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        final EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        int blockingQueueLength = 1000;
        final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue(blockingQueueLength);
        final Executor executor = new RateLimitThreadPoolExecutor(400, 500, 30, TimeUnit.SECONDS, blockingQueue);
        Server server = NettyServerBuilder.forPort(GRPC_PORT).maxConcurrentCallsPerConnection(50)
                .keepAliveTime(60, TimeUnit.SECONDS).maxInboundMessageSize(1000000000).bossEventLoopGroup(bossGroup)
                .workerEventLoopGroup(workerGroup).addService(new RateLimitService())
                .channelType(NioServerSocketChannel.class).executor(executor).build();
        server.start();
        LOGGER.info("Ratelimit gRPC server started on port :"+GRPC_PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down Ratelimit gRPC server");
        }));

        server.awaitTermination();
    }
}
