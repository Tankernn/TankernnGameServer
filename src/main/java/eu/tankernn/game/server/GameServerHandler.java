package eu.tankernn.game.server;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

import eu.tankernn.game.server.entities.player.ServerPlayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class GameServerHandler extends ChannelInboundHandlerAdapter {

	private ServerPlayer player;
	private final World world;

	public GameServerHandler(World world) {
		this.world = world;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (player == null && msg instanceof String) {
			player = new ServerPlayer((String) msg);
			world.players.add(player);
			System.out.println("Player connected with username: " + player.getUsername());
			ctx.writeAndFlush(Integer.valueOf(world.seed));
		} else if (msg instanceof Vector3f) {
			player.getPosition().set((ReadableVector3f) msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.writeAndFlush(new Object());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		e.printStackTrace();
		ctx.close();
	}
}
