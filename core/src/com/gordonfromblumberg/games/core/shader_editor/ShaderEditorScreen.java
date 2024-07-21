package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.gordonfromblumberg.games.core.common.world.WorldScreen;

import static com.gordonfromblumberg.games.core.shader_editor.ShaderEditorWorld.DATA_DIR;

public class ShaderEditorScreen extends WorldScreen<ShaderEditorWorld> {

    public ShaderEditorScreen(SpriteBatch batch) {
        super(batch, initWorld());
    }

    @Override
    protected void initialize() {
        ShaderProgram.pedantic = false;

        super.initialize();

        uiRenderer.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.F5) {
                    FileHandle directory = Gdx.files.local(DATA_DIR + "/" + System.currentTimeMillis());
                    ((ShaderEditorRenderer) worldRenderer).saveImageAndShaders(directory);
                    return true;
                }
                return false;
            }
        });
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
        FileHandle vertex = Gdx.files.local(DATA_DIR + "/shader.vert");
        FileHandle fragment = Gdx.files.local(DATA_DIR + "/shader.frag");
        return new ShaderEditorWorld(vertex.readString(), fragment.readString());
    }
}
