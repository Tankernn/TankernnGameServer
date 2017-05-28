package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.tankernn.game.server.entities.ServerEntity;
import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.multiplayer.WorldState;

public class World {
	private WorldState state;
	private final List<ServerEntity> entities = new ArrayList<>();
	private final List<ServerPlayer> players = new ArrayList<>();
	
	public World(int seed) {
		this.state = new WorldState(seed);
	}
	
	public void update() {
		state.getEntities().stream().forEach(EntityState::update);
	}
	
	public WorldState getState() {
		return new WorldState(state.getSeed(), state.getLights(), entities.stream().map(ServerEntity::getState).collect(Collectors.toList()));
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
