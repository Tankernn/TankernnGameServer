package eu.tankernn.game.server.entities;

import eu.tankernn.gameEngine.entities.EntityState;
import eu.tankernn.gameEngine.entities.GameContext;

public class ServerEntity {
	protected EntityState state;

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		//this.state = state;
		this.state.getVelocity().set(state.getVelocity());
		this.state.getPosition().set(state.getPosition());
		this.state.getRotation().set(state.getRotation());
		this.state.setBehaviors(state.getBehaviors());
	}
	
	public void update(GameContext ctx) {
		state.update(ctx);
	}
}
