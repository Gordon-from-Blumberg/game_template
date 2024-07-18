package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;

public class ShaderEditorScreen extends WorldScreen<ShaderEditorWorld> {

    public ShaderEditorScreen(SpriteBatch batch) {
        super(batch, new ShaderEditorWorld());
    }

    @Override
    protected void initialize() {
        ShaderProgram.pedantic = false;

        super.initialize();
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
