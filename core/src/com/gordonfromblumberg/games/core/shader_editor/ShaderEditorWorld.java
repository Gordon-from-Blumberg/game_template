package com.gordonfromblumberg.games.core.shader_editor;

import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.world.World;

public class ShaderEditorWorld extends World {
    private String vertexShaderSource;
    private String fragmentShaderSource;
    private boolean wasChanged;
    private String error = "";

    private float time;

    public ShaderEditorWorld(String vertexShaderSource, String fragmentShaderSource) {
        this.vertexShaderSource = vertexShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
    }

    public void setVertexShaderSource(String vertexShaderSource) {
        this.vertexShaderSource = vertexShaderSource;
        this.wasChanged = true;
        this.time = 0;
    }

    public String getVertexShaderSource() {
        return vertexShaderSource;
    }

    public void setFragmentShaderSource(String fragmentShaderSource) {
        this.fragmentShaderSource = fragmentShaderSource;
        this.wasChanged = true;
        this.time = 0;
    }

    public String getFragmentShaderSource() {
        return fragmentShaderSource;
    }

    @Override
    public void update(float delta, float mouseX, float mouseY) {
        super.update(delta, mouseX, mouseY);

        if (wasChanged)
            time += delta;
    }

    public boolean needRecompile() {
        return wasChanged && time > AbstractFactory.getInstance().configManager().getFloat("shaderEditor.delaySec");
    }

    public void reset() {
        this.wasChanged = false;
        time = 0;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
