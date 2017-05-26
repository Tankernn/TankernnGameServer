package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;

import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.Entity3D;
import eu.tankernn.gameEngine.entities.Light;

public class World {
	public final int seed;
	List<Light> lights;
	List<Entity3D> entities;
	List<ServerPlayer> players;
	
	public World(int seed, List<Light> lights, List<Entity3D> entities) {
		this.seed = seed;
		this.lights = lights;
		this.entities = entities;
		this.players = new ArrayList<ServerPlayer>();
	}
	
	
}
