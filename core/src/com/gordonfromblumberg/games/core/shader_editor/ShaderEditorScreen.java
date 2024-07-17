package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;

public class ShaderEditorScreen extends WorldScreen<ShaderEditorWorld> {

    public ShaderEditorScreen(SpriteBatch batch) {
        super(batch, new ShaderEditorWorld());
    }

    @Override
    protected void createWorldRenderer() {
        worldRenderer = new ShaderEditorRenderer(batch, world);
    }

    @Override
    protected void createUiRenderer() {
        this.uiRenderer = new ShaderEditorUIRenderer(batch, world, this::getViewCoords3);
    }
}
