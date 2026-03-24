package com.gordonfromblumberg.games.core.motion_animation.gravity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.world.WorldRenderer;

public class GravityWorldRenderer extends WorldRenderer<GravityWorld> {
    private static final float SCROLL_SPEED = AbstractFactory.getInstance().configManager().getFloat("gravity.scrollSpeed");

    private final Batch batch;

    public GravityWorldRenderer(GravityWorld world, Batch batch) {
        super(world);

        this.batch = batch;
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        updateCamera(camera.zoom * SCROLL_SPEED);
        final Batch batch = this.batch;
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        for (SpaceBody body : world.bodies) {
            body.render(batch);
        }
        batch.end();
    }
}
