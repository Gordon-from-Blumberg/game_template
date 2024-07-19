package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.ui.UIUtils;
import com.gordonfromblumberg.games.core.common.ui.UpdatableLabel;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.common.utils.ConfigManager;
import com.gordonfromblumberg.games.core.common.utils.StringUtils;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

import java.util.function.Supplier;

public class ShaderEditorUIRenderer extends WorldUIRenderer<ShaderEditorWorld> {
    private static final Logger log = LogManager.create(ShaderEditorUIRenderer.class);

    private TextArea vertexShaderText;
    private TextArea fragmentShaderText;

    private final TextField.TextFieldListener onTypeListener = (field, c) -> {
        if (c == '\n' || c == '\r') {
            String text = field.getText();
            int cursorPosition = field.getCursorPosition();
            int lastNewLine = text.lastIndexOf('\n', cursorPosition - 2);
            if (lastNewLine > -1) {
                int i = lastNewLine;
                int n = 0;
                while (text.charAt(++i) == ' ')
                    ++n;
                String indent = " ".repeat(n);
                text = text.substring(0, cursorPosition) + indent + text.substring(cursorPosition);
                field.setText(text);
                field.setCursorPosition(cursorPosition + n);
            }
        }

        if (field == vertexShaderText) {
            world.setVertexShaderSource(field.getText());
        } else {
            world.setFragmentShaderSource(field.getText());
        }
    };

    public ShaderEditorUIRenderer(SpriteBatch batch, ShaderEditorWorld world, Supplier<Vector3> viewCoords) {
        super(batch, world, viewCoords);

        ConfigManager config = AbstractFactory.getInstance().configManager();
        Skin skin = Assets.get("ui/uiskin.json", Skin.class);

        ShaderProgram defaultShader = batch.getShader();
        Table table = UIUtils.createTable(skin);
        table.setBackground(skin.getDrawable("default-round-large"));

        table.add("Vertex shader");

        table.row();
        vertexShaderText = new TextArea(defaultShader.getVertexShaderSource(), skin);
        vertexShaderText.setTextFieldListener(onTypeListener);
        table.add(vertexShaderText).fill().expand();

        table.row();
        table.add("Fragment shader");

        table.row();
        fragmentShaderText = new TextArea(defaultShader.getFragmentShaderSource(), skin);
        fragmentShaderText.setTextFieldListener(onTypeListener);
        table.add(fragmentShaderText).fill().expand();

        table.row();
        Label errorLabel = new UpdatableLabel(skin, () -> StringUtils.defaultIfBlank(world.getError(), "No error"));
        errorLabel.setWrap(true);
        table.add(errorLabel).fill();

        rootTable.add().expand();
        rootTable.add(table).width(config.getFloat("shaderEditor.uiWidth")).fill();

        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.F8) {
                    world.requestRecompile();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float dt) {
        stage.getBatch().setShader(null);

        super.render(dt);
    }
}
