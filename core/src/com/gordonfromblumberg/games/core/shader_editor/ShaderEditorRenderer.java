package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    private float viewSize;
    private Texture texture;
    private final ShaderProgram defaultShader;
    private final Texture defaultTexture;

    private float time;

    public ShaderEditorRenderer(SpriteBatch batch, ShaderEditorWorld world) {
        super(world);

        this.centerCamera = true;
        this.batch = batch;
        this.defaultShader = batch.getShader();
        this.viewSize = AbstractFactory.getInstance().configManager().getFloat("shaderEditor.viewSize");
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        defaultTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        time += dt;

        if (world.needRecompile()) {
            ShaderProgram newShader = new ShaderProgram(world.getVertexShaderSource(), world.getFragmentShaderSource());
            if (newShader.isCompiled()) {
                if (shader != null && shader != defaultShader)
                    shader.dispose();
                shader = newShader;
                world.setError("");
            } else {
                world.setError(newShader.getLog());
            }
            world.reset();
        }

        batch.setShader(shader);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (shader != null) {
            shader.setUniformf("u_time", time);
            shader.setUniformf("u_resolution", viewSize, viewSize);
            shader.setUniformf("u_mouse",
                    Gdx.input.getX() / viewSize,
                    (Gdx.graphics.getHeight() - Gdx.input.getY()) / viewSize);
        }

        if (texture != null) {
            batch.draw(texture, 0, 0, viewSize, viewSize);
        } else {
            batch.draw(defaultTexture, 0, 0, viewSize, viewSize);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        ConfigManager config = AbstractFactory.getInstance().configManager();
        super.resize(width - config.getInteger("shaderEditor.uiWidth"), height);
    }

    @Override
    protected Viewport createViewport(Camera camera) {
        ConfigManager config = AbstractFactory.getInstance().configManager();
        // called before constructor, so do not use viewSize field
        float viewSize = config.getInteger("shaderEditor.viewSize");
        return new FitViewport(viewSize, viewSize, camera);
    }

    @Override
    public void dispose() {
        super.dispose();

        defaultTexture.dispose();
    }
}
