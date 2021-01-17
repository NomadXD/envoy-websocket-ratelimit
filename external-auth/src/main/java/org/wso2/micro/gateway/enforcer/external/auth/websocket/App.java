package org.wso2.micro.gateway.enforcer.external.auth.websocket;


import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App
{
    private static final int HTTP_PORT = 8080;
    private static final int GRPC_PORT = 50051;

    private static final Logger LOGGER = LogManager.getLogger(App.class.getName());

    Thread httpServerThread = new Thread(new Runnable() {
        @Override
        public void run(){
            LOGGER.info("http token server starting on port:"+HTTP_PORT+"....");
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try{
                ServerBootstrap httpBootstrap =  new ServerBootstrap();
                httpBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new HttpServerInitializer())
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                try {
                    ChannelFuture httpChannel = httpBootstrap.bind(HTTP_PORT).sync();
                    LOGGER.info("http token server started on port :" + HTTP_PORT);

                    httpChannel.channel().closeFuture().sync();
                }catch (Exception e){
                    LOGGER.error("Error occurred starting http token server on port : "+HTTP_PORT, new Error(e));
                }

            }finally {
                LOGGER.info("workerGroup & bossGroup shutting down gracefully");
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        }
    });

    Thread grpcServerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            LOGGER.info("gRPC auth server starting on port :"+GRPC_PORT+"....");
            final io.grpc.netty.shaded.io.netty.channel.EventLoopGroup bossGroup = new io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
            final io.grpc.netty.shaded.io.netty.channel.EventLoopGroup workerGroup = new io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
            int blockingQueueLength = 1000;
            final BlockingQueue blockingQueue = new LinkedBlockingQueue(blockingQueueLength);
            final Executor executor = new AuthThreadPoolExecutor(400, 500, 30, TimeUnit.SECONDS, blockingQueue);
            Server server = NettyServerBuilder.forPort(GRPC_PORT).maxConcurrentCallsPerConnection(50)
                    .keepAliveTime(60, TimeUnit.SECONDS).maxInboundMessageSize(1000000000).bossEventLoopGroup(bossGroup)
                    .workerEventLoopGroup(workerGroup).addService(new ExtAuthService())
                    .channelType(io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel.class).executor(executor).build();
            try{
                server.start();
                LOGGER.info("gRPC auth server started on port:" + GRPC_PORT);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    LOGGER.info("gRPC auth server shutdown hook");
                }));

                server.awaitTermination();
            }catch (Exception e){
                LOGGER.error("Error occurred starting gRPC auth server", new Error(e));
            }

        }
    });



    public static void main( String[] args ) throws Exception
    {
        App app = new App();
        LOGGER.debug("starting gRPC server thread...");
        app.grpcServerThread.start();
        LOGGER.debug("gRPC server thread started");
        LOGGER.debug("starting http server thread....");
        app.httpServerThread.start();
        LOGGER.debug("http server thread started");
    }


}
