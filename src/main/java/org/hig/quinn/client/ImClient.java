package org.hig.quinn.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.hig.quinn.handler.ClientStringHandler;


/**
 * @author ZhihaoQuinn
 * @since 2020/9/14
 */
public class ImClient {
    private Channel channel;

    public Channel connect(String hosts, int port) {
        doConnect(hosts, port);
        return this.channel;
    }

    private void doConnect(String hosts, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast(new ClientStringHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.connect(hosts, port).sync();
            channel =  cf.channel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 2222;
        Channel channel = new ImClient().connect(host, port);
        channel.writeAndFlush("yinjihuan");
    }
}
