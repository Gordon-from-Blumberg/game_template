package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gordonfromblumberg.games.core.common.world.WorldRenderer;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

public class SnakesScreen extends WorldScreen<SnakesWorld> {

    public SnakesScreen(SpriteBatch batch) {
        super(batch, new SnakesWorld(), "snakes");
    }

    @Override
    protected WorldUIRenderer<SnakesWorld> createUiRenderer() {
        return new SnakesUIRenderer(getInfo());
    }

    @Override
    protected WorldRenderer<SnakesWorld> createWorldRenderer() {
        return new WorldRenderer<>(world);
    }
}
