package com.gordonfromblumberg.games.core.common.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gordonfromblumberg.games.core.common.Main;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.ui.UIUtils;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.game_template.TemplateScreen;
import com.gordonfromblumberg.games.core.motion_animation.gravity.GravityScreen;
import com.gordonfromblumberg.games.core.shader_editor.ShaderEditorScreen;
import com.gordonfromblumberg.games.core.snakes.SnakesScreen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class MainMenuScreen extends AbstractScreen {
    private static final Logger log = LogManager.create(MainMenuScreen.class);

    public MainMenuScreen(SpriteBatch batch) {
        super(batch);

        color = Color.FOREST;

        log.debug("Local storage path = " + Gdx.files.getLocalStoragePath());
        log.debug("External storage path = " + Gdx.files.getExternalStoragePath());
    }

    @Override
    protected void update(float delta) {
    }

    @Override
    protected UIRenderer createUiRenderer() {
        UIRenderer uiRenderer = super.createUiRenderer();

        Map<String, Function<SpriteBatch, AbstractScreen>> projects = new LinkedHashMap<>();
        projects.put("Template", TemplateScreen::new);
        projects.put("Shader editor", ShaderEditorScreen::new);
        projects.put("Gravity", GravityScreen::new);
        projects.put("Snakes", SnakesScreen::new);

        final Skin uiSkin = Assets.get("ui/uiskin.json", Skin.class);

        Table buttonList = UIUtils.createTable(uiSkin);
        buttonList.defaults().fillX().space(5f);
        for (Map.Entry<String, Function<SpriteBatch, AbstractScreen>> e : projects.entrySet()) {
            TextButton button = new TextButton(e.getKey(), uiSkin);
            button.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Main.getInstance().setScreen(e.getValue().apply(batch));
                }
            });
            buttonList.row();
            buttonList.add(button);
        }
        uiRenderer.rootTable.add(buttonList);
        return uiRenderer;
    }
}
