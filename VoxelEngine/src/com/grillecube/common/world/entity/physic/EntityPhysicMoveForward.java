package com.grillecube.common.world.entity.physic;

import com.grillecube.common.maths.Vector3f;
import com.grillecube.common.world.entity.Entity;

public class EntityPhysicMoveForward extends EntityPhysic {
	private Vector3f move;

	public EntityPhysicMoveForward() {
		this.move = new Vector3f();
	}

	@Override
	public void onEnable(Entity entity) {
	}

	@Override
	public void onDisable(Entity entity) {
		// if (entity instanceof Entity) {
		// Entity model = (Entity) entity;
		// ModelInstance instance = model.getModelInstance();
		// if (instance != null && instance.isPlayingAnyAnimations()) {
		// instance.getAnimationInstance(0).startPlay();
		// }
		// }
	}

	@Override
	public void onUpdate(Entity entity) {
		this.move.set(entity.getViewVector());
		this.move.y = 0;
		this.move.normalise();
		this.move.scale(entity.getSpeed());
		entity.move(this.move);

		// if (entity instanceof Entity) {
		// Entity model = (Entity) entity;
		// ModelInstance instance = model.getModelInstance();
		// if (instance != null && !instance.isPlayingAnyAnimations()) {
		// instance.getAnimationInstance(0).startPlay();
		// }
		// }
	}
}