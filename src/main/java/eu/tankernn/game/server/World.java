package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;

import eu.tankernn.game.server.entities.ServerEntity;
import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.Light;

public class World {
	int seed;
	List<Light> lights;
	List<ServerEntity> entities;
	List<ServerPlayer> players;
	
	public World(List<Light> lights, List<ServerEntity> entities) {
		this.lights = lights;
		this.entities = entities;
		this.players = new ArrayList<ServerPlayer>();
	}
	
	
}
