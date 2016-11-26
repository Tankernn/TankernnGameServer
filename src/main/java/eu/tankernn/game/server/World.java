package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;

import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.Entity;
import eu.tankernn.gameEngine.entities.Light;

public class World {
	int seed;
	List<Light> lights;
	List<Entity> entities;
	List<ServerPlayer> players;
	
	public World(List<Light> lights, List<Entity> entities) {
		this.lights = lights;
		this.entities = entities;
		this.players = new ArrayList<ServerPlayer>();
	}
	
	
}
