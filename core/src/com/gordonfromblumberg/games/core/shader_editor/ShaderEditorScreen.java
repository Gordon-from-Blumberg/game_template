package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;

public class ShaderEditorScreen extends WorldScreen<ShaderEditorWorld> {

    public ShaderEditorScreen(SpriteBatch batch) {
        super(batch, initWorld());
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

    protected static ShaderEditorWorld initWorld() {
        FileHandle vertex = Gdx.files.getFileHandle("shader/default.vert", Files.FileType.Internal);
        FileHandle fragment = Gdx.files.getFileHandle("shader/default.frag", Files.FileType.Internal);
        return new ShaderEditorWorld(vertex.readString(), fragment.readString());
    }
}
