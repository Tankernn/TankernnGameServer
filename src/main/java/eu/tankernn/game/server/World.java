package eu.tankernn.game.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import eu.tankernn.game.server.entities.ServerEntity;
import eu.tankernn.game.server.entities.player.ServerPlayer;
import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;
import eu.tankernn.gameEngine.entities.ILight;
import eu.tankernn.gameEngine.multiplayer.WorldState;

public class World {
	private final int seed;
	private final List<ILight> lights = new ArrayList<>();
	private final Map<Integer, ServerEntity> entities = new ConcurrentHashMap<>();

	public World(int seed) {
		this.seed = seed;
	}

	public void update(GameContext ctx) {
		entities.values().forEach(e -> e.update(ctx));
		entities.values().removeIf(e -> e.getState().isDead());
	}

	public WorldState getState() {
		return new WorldState(seed, lights,
				entities.values().stream().map(ServerEntity::getState).collect(Collectors.toList()));
	}

	public Map<Integer, ServerEntity> getEntities() {
		return entities;
	}

	public void addPlayer(ServerPlayer player) {
		entities.put(player.getId(), player);
	}

	public void removePlayer(ServerPlayer player) {
		entities.remove(player.getId());
	}

	public void setEntityState(EntityState state) {
		if (entities.containsKey(state.getId()))
			entities.get(state.getId()).setState(state);
		else
			entities.put(state.getId(), new ServerEntity(state));
	}

	public float getTerrainHeight(float x, float z) {
		// TODO
		return 0;
	}

	public void addLight(ILight light) {
		lights.add(light);
	}

}
