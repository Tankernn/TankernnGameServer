package eu.tankernn.game.server.entities.player;

import org.lwjgl.util.vector.Vector3f;

public class ServerPlayer {
	private final Vector3f position;
	private String username;
	
	public ServerPlayer(String username) {
		this.username = username;
		this.position = new Vector3f();
	}

	public Vector3f getPosition() {
		return position;
	}

	public String getUsername() {
		return username;
	}
}
