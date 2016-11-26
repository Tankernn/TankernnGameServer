package eu.tankernn.game.server.entities.player;

import eu.tankernn.gameEngine.entities.Player;

public class ServerPlayer {
	private Player playerEntity;
	private String username;
	
	public ServerPlayer(String username, Player entity) {
		this.username = username;
		this.playerEntity = entity;
	}
}
