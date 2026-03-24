package com.gordonfromblumberg.games.core.motion_animation.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gordonfromblumberg.games.core.common.ui.ZoomByScrollListener;
import com.gordonfromblumberg.games.core.common.world.WorldRenderer;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

public class GravityScreen extends WorldScreen<GravityWorld> {
    public GravityScreen(SpriteBatch batch) {
        super(batch, new GravityWorld());

        color = new Color(0.18f, 0.18f, 0.22f, 1f);
    }

    @Override
    protected WorldRenderer<GravityWorld> createWorldRenderer() {
        return new GravityWorldRenderer(world, batch);
    }

    @Override
    protected WorldUIRenderer<GravityWorld> createUiRenderer() {
        GravityUIRenderer uiRenderer = new GravityUIRenderer(getInfo());
        uiRenderer.addListener(new ZoomByScrollListener(worldRenderer.getCamera(), 1.2f, 0.1f, 10f));
        return uiRenderer;
    }
}
