package eu.tankernn.game.server.entities.player;

import eu.tankernn.game.server.entities.ServerEntity;
import eu.tankernn.gameEngine.entities.EntityState;
import io.netty.channel.Channel;

public class ServerPlayer extends ServerEntity {
	
	private final Channel channel;
	private String username;
	
	public ServerPlayer(Channel channel) {
		super(new EntityState());
		state.setModelId(0);
		this.channel = channel;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public Channel getChannel() {
		return channel;
	}

	public int getId() {
		return state.getId();
	}
}
