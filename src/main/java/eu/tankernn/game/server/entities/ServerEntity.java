package eu.tankernn.game.server.entities;

import eu.tankernn.gameEngine.entities.EntityState;

public class ServerEntity {
	protected EntityState state;

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		this.state = state;
	}
	
	public void update() {
		state.update();
	}
}
