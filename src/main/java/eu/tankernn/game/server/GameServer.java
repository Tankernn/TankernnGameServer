package eu.tankernn.game.server;

import java.util.ArrayList;

import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.Light;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class GameServer {
	private int port;
	private World world;

	public GameServer(int port) {
		this.port = port;
		this.world = new World(1337, new ArrayList<Light>(), new ArrayList<Entity3D>());
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						
						ch.pipeline().addLast("objectDecoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addLast("objectEncoder", new ObjectEncoder());
						ch.pipeline().addLast("timeouthandler", new ReadTimeoutHandler(30));
						ch.pipeline().addLast(new IdleStateHandler(0, 0, 29));

						ch.pipeline().addLast(new GameServerHandler(world));
					}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		// Bind and start to accept incoming connections.
		b.bind(port).sync();
		
		System.out.println("Server started.");
	}

	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 25566;
		}
		try {
			new GameServer(port).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
