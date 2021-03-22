package com.gordonfromblumberg.games.common.model;

import com.badlogic.gdx.math.Vector2;
import com.gordonfromblumberg.games.common.physics.MovingStrategy;

public class PhysicsGameObject extends GameObject {
    public final Vector2 velocity = new Vector2();
    public final Vector2 acceleration = new Vector2();

    protected MovingStrategy movingStrategy;

    {
        this.colliding = true;
    }

    @Override
    public void update(final float delta) {
        super.update(delta);

        if (movingStrategy != null)
            movingStrategy.update(position, velocity, acceleration, delta);

        adjustPosition();
        polygon.setPosition(position.x, position.y);
    }

    /**
     * Checks whether position out of limit bounds and handles such case
     */
    protected void adjustPosition() {}
}
