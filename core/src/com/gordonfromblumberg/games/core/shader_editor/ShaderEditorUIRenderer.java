package com.gordonfromblumberg.games.core.shader_editor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    public ShaderEditorUIRenderer(SpriteBatch batch, ShaderEditorWorld world, Supplier<Vector3> viewCoords) {
        super(batch, world, viewCoords);

        ConfigManager config = AbstractFactory.getInstance().configManager();
        Skin skin = Assets.get("ui/uiskin.json", Skin.class);
        Table table = UIUtils.createTable(skin);
        table.setBackground(skin.getDrawable("default-round-large"));
        TextArea textArea = new TextArea("Hello, World!\n and Bye...", skin);
        table.add(textArea).height(100);

        rootTable.add().expand();
        rootTable.add(table).width(config.getFloat("shaderEditor.uiWidth")).fill();
    }


}
