package com.gordonfromblumberg.games.core.common.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gordonfromblumberg.games.core.common.Main;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.game_template.TemplateScreen;

public class MainMenuScreen extends AbstractScreen {
    private static final Logger log = LogManager.create(MainMenuScreen.class);

    TextButton textButton;

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

        final Skin uiSkin = Assets.get("ui/uiskin.json", Skin.class);

        textButton = new TextButton("PLAY", uiSkin);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                ConfigManager config = AbstractFactory.getInstance().configManager();
//                worldParams.save(config.getConfigPreferences());
//                config.flushPreferences();
                Main.getInstance().setScreen(new TemplateScreen(batch));
            }
        });

        uiRenderer.rootTable.add(textButton);
        return uiRenderer;
    }
}
