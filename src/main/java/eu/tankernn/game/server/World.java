package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.tankernn.game.server.entities.ServerEntity;
import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.multiplayer.WorldState;

public class World {
	private final int seed;
	private final List<ILight> lights = new ArrayList<>();
	private final List<ServerEntity> entities = new ArrayList<>();
	private final List<ServerPlayer> players = new ArrayList<>();
	
	public World(int seed) {
		this.seed = seed;
	}
	
	public void update(GameContext ctx) {
		entities.stream().forEach(e -> e.update(ctx));
	}
	
	public WorldState getState() {
		return new WorldState(seed, lights, entities.stream().map(ServerEntity::getState).collect(Collectors.toList()));
	}
	
	public List<ServerEntity> getEntities() {
		return entities;
	}
	
	public List<ServerPlayer> getPlayers() {
		return players;
	}

	public void addPlayer(ServerPlayer player) {
		players.add(player);
		entities.add(player);
	}

	public void removePlayer(ServerPlayer player) {
		players.remove(player);
		entities.remove(player);
	}
	
	
}
