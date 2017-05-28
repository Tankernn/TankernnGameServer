package eu.tankernn.game.server;

import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.multiplayer.LoginRequest;
import eu.tankernn.gameEngine.multiplayer.LoginResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

public class GameServerHandler extends ChannelInboundHandlerAdapter {

	private ServerPlayer player;
	private final World world;
	private final ChannelGroup group;

	public GameServerHandler(ChannelGroup group, World world) {
		this.group = group;
		this.world = world;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		player = new ServerPlayer(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof LoginRequest) {
			LoginRequest request = (LoginRequest) msg;
			player.setUsername(request.username);
			world.addPlayer(player);
			group.add(ctx.channel());
			System.out.println("Player connected with username: " + player.getUsername());
			ctx.writeAndFlush(new LoginResponse(true, player.getState())).sync();
		} else if (msg instanceof EntityState) {
			EntityState state = (EntityState) msg;
			player.setState(state);
		} else {
			System.err.println("Unknown message: " + msg.toString());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		world.removePlayer(player);
		group.remove(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
		e.printStackTrace();
		ctx.close();
	}
}
