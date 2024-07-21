package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.utils.ConfigManager;
import com.gordonfromblumberg.games.core.common.world.WorldRenderer;

import java.util.zip.Deflater;

import static com.badlogic.gdx.graphics.Pixmap.Format;
import static com.gordonfromblumberg.games.core.shader_editor.ShaderEditorWorld.DATA_DIR;

public class ShaderEditorRenderer extends WorldRenderer<ShaderEditorWorld> {
    private static final Logger log = LogManager.create(ShaderEditorRenderer.class);

    private final SpriteBatch batch;
    private final ShaderProgram defaultShader;
    private ShaderProgram shader;
    private final float viewSize;
    private final Texture defaultTexture;
    private Texture texture;
    private final FrameBuffer fbo;
    private final Viewport fboViewport;

    private final AutoSaveThread autoSaveThread;
    private final ShaderSourceHolder shaderSourceHolder = new ShaderSourceHolder();

    private float time;

    private final Vector3 vec = new Vector3();

    public ShaderEditorRenderer(SpriteBatch batch, ShaderEditorWorld world) {
        super(world);

        this.centerCamera = true;
        this.batch = batch;
        this.defaultShader = batch.getShader();
        this.viewSize = AbstractFactory.getInstance().configManager().getFloat("shaderEditor.viewSize");
        this.fbo = new FrameBuffer(Format.RGBA8888, (int) viewSize, (int) viewSize, false);
        this.fboViewport = new ScreenViewport();

        Pixmap pixmap = new Pixmap(2, 2, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        defaultTexture = new Texture(pixmap);
        pixmap.dispose();

        this.autoSaveThread = new AutoSaveThread();
        this.autoSaveThread.start();
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
                shaderSourceHolder.vertex = world.getVertexShaderSource();
                shaderSourceHolder.fragment = world.getFragmentShaderSource();
                autoSaveThread.sourceHolder = shaderSourceHolder;
            } else {
                world.setError(newShader.getLog());
            }
            world.reset();
        }

        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setShader(shader);
        fboViewport.update(fbo.getWidth(), fbo.getHeight(), true);
        batch.setProjectionMatrix(fboViewport.getCamera().combined);

//        Matrix4 mat = camera.combined;
//        vec.set(0, 0, 0).mul(mat);
//        log.debug("(0, 0, 0) -> " + vec);
//        vec.set(0, 0, 1).mul(mat);
//        log.debug("(0, 0, 1) -> " + vec);
//        vec.set(600, 0, 0).mul(mat);
//        log.debug("(600, 0, 0) -> " + vec);
//        vec.set(600, 600, 1).mul(mat);
//        log.debug("(600, 600, 1) -> " + vec);

        batch.begin();
        if (shader != null) {
            shader.setUniformf("u_time", time);
            shader.setUniformf("u_resolution", viewSize, viewSize);
            shader.setUniformf("u_mouse",
                    world.getMouseX() / viewSize,
                    world.getMouseY() / viewSize);
        }

        if (texture != null) {
            batch.draw(texture, 0, 0, viewSize, viewSize);
        } else {
            batch.draw(defaultTexture, 0, 0, viewSize, viewSize);
        }
        batch.end();

        fbo.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        batch.setShader(null);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(fbo.getColorBufferTexture(), 0, 0, viewSize, viewSize, 0, 0, 1, 1);
        batch.end();
    }

    public void saveImage(FileHandle fileHandle) {
        fbo.bind();
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, fbo.getWidth(), fbo.getHeight());
        PixmapIO.writePNG(fileHandle, pixmap, Deflater.DEFAULT_COMPRESSION, true);
        pixmap.dispose();
        fbo.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    public void saveImageAndShaders(FileHandle directory) {
        saveImage(directory.child("result.png"));
        directory.child("shader.vert").writeString(shader.getVertexShaderSource(), false);
        directory.child("shader.frag").writeString(shader.getFragmentShaderSource(), false);
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
        autoSaveThread.isRunning = false;
    }

    private static class AutoSaveThread extends Thread {
        private volatile boolean isRunning = true;
        private volatile ShaderSourceHolder sourceHolder;

        {
            setDaemon(true);
            setName("ShaderEditor_AutoSave");
        }

        @Override
        public void run() {
            while (isRunning) {
                final ShaderSourceHolder holder = sourceHolder;
                if (holder != null) {
                    FileHandle dataDir = Gdx.files.local(DATA_DIR);
                    dataDir.child("shader.vert").writeString(holder.vertex, false);
                    dataDir.child("shader.frag").writeString(holder.fragment, false);

                    if (holder == sourceHolder) {
                        sourceHolder = null;
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class ShaderSourceHolder {
        private volatile String vertex;
        private volatile String fragment;
    }
}
