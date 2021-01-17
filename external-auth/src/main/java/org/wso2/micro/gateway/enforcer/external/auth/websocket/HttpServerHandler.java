package org.wso2.micro.gateway.enforcer.external.auth.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger LOGGER = LogManager.getLogger(HttpServerHandler.class.getName());
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        LOGGER.info("JWT endpoint called");
        String authToken = TokenService.createJWT();
        ByteBuf content = Unpooled.copiedBuffer(authToken, CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        channelHandlerContext.write(response);
        channelHandlerContext.flush();
        LOGGER.info("JWT token successfully sent");
    }
}
