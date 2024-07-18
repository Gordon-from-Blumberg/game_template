package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.utils.ConfigManager;
import com.gordonfromblumberg.games.core.common.world.WorldRenderer;

public class ShaderEditorRenderer extends WorldRenderer<ShaderEditorWorld> {
    private final SpriteBatch batch;
    private ShaderProgram shader;

    public ShaderEditorRenderer(SpriteBatch batch, ShaderEditorWorld world) {
        super(world);

        this.batch = batch;
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        if (world.needRecompile()) {
            ShaderProgram newShader = new ShaderProgram(world.getVertexShaderSource(), world.getFragmentShaderSource());
            if (newShader.isCompiled()) {
                shader.dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        ConfigManager config = AbstractFactory.getInstance().configManager();
        super.resize(width - config.getInteger("shaderEditor.uiWidth"), height);
    }

    @Override
    protected Viewport createViewport(Camera camera) {
        ConfigManager config = AbstractFactory.getInstance().configManager();
        float viewSize = config.getInteger("shaderEditor.viewSize");
        return new FitViewport(viewSize, viewSize, camera);
    }
}
