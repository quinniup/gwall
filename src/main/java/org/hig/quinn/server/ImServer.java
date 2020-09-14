package org.hig.quinn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.hig.quinn.handler.ServerStringHandler;

/**
 * @author ZhihaoQuinn
 * @since 2020/9/14
 */
public class ImServer {

	EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
	EventLoopGroup workLoopGroup = new NioEventLoopGroup();

	public void start(int port) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 通过ServerBootstrap进行channel详细配置
		bootstrap.group(bossLoopGroup, workLoopGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("decoder", new StringDecoder());
						ch.pipeline().addLast("encoder", new StringEncoder());
						ch.pipeline().addLast(new ServerStringHandler());
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		try {
			// 绑定端口至channel
			ChannelFuture cf = bootstrap.bind(port).sync();
			cf.channel().closeFuture().sync();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		workLoopGroup.shutdownGracefully();
		bossLoopGroup.shutdownGracefully();
	}
}
