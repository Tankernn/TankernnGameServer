package eu.tankernn.game.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.entities.Light;
import eu.tankernn.gameEngine.loader.models.AABB;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GameServer {
	private ScheduledExecutorService executor;

	private int port;
	private World world;
	private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private long lastTickTimeStampNano;

	public GameServer(int port) {
		this.port = port;

		executor = Executors.newSingleThreadScheduledExecutor();

		this.world = new World(1337);
		world.addLight(new Light(new Vector3f(1000, 1000, 0), new Vector3f(1, 1, 1)));
		startListening();
		executor.scheduleAtFixedRate(this::update, 0, 1000 / 16, TimeUnit.MILLISECONDS); // 64
																							// tick
																							// XD
	}

	private void update() {
		try {
			double tickLengthSeconds = (System.nanoTime() - lastTickTimeStampNano) / 1000000000.0;
			lastTickTimeStampNano = System.nanoTime();
			GameContext ctx = new GameContext(false, (float) tickLengthSeconds, world.getState().getEntities()) {

				@Override
				public float getTerrainHeight(float x, float z) {
					return world.getTerrainHeight(x, z);
				}

				@Override
				public float getHeight(int entityId) {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public AABB getBoundingBox(int entityId) {
					// TODO Auto-generated method stub
					return new AABB(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)).updatePosition(world.getEntities().get(entityId).getState().getPosition());
				}

				@Override
				public EntityState getEntity(int id) {
					System.out.println("Request: " + id + ". Available: " + world.getEntities().values().toString());
					return world.getEntities().get(id).getState();
				}
			};
			world.update(ctx);
			channelGroup.flushAndWrite(world.getState()).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}

	public void startListening() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {

						ch.pipeline().addLast("objectDecoder", new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						ch.pipeline().addLast("objectEncoder", new ObjectEncoder());
						ch.pipeline().addLast("timeouthandler", new ReadTimeoutHandler(10));

						ch.pipeline().addLast(new GameServerHandler(channelGroup, world));
					}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		// Bind and start to accept incoming connections.
		try {
			b.bind(port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
			new GameServer(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
