package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.ui.UIUtils;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.common.utils.ConfigManager;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

import java.util.function.Supplier;

public class ShaderEditorUIRenderer extends WorldUIRenderer<ShaderEditorWorld> {
    private TextArea vertexShaderText;
    private TextArea fragmentShaderText;

    public ShaderEditorUIRenderer(SpriteBatch batch, ShaderEditorWorld world, Supplier<Vector3> viewCoords) {
        super(batch, world, viewCoords);

        ConfigManager config = AbstractFactory.getInstance().configManager();
        Skin skin = Assets.get("ui/uiskin.json", Skin.class);

        ShaderProgram defaultShader = batch.getShader();
        Table table = UIUtils.createTable(skin);
        table.setBackground(skin.getDrawable("default-round-large"));

        table.add("Vertex shader");
        defaultShader.setUniformf("foo", 5f);

        table.row();
        vertexShaderText = new TextArea(defaultShader.getVertexShaderSource(), skin);
        table.add(vertexShaderText).fill().expand();

        table.row();
        table.add("Fragment shader");

        table.row();
        fragmentShaderText = new TextArea(defaultShader.getFragmentShaderSource(), skin);
        table.add(fragmentShaderText).fill().expand();

        rootTable.add().expand();
        rootTable.add(table).width(config.getFloat("shaderEditor.uiWidth")).fill();
    }


}
