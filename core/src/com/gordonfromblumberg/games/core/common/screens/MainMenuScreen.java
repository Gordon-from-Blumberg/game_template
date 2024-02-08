package com.gordonfromblumberg.games.core.common.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gordonfromblumberg.games.core.common.Main;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.ui.SuperLabel;
import com.gordonfromblumberg.games.core.common.utils.RandomGen;
import com.gordonfromblumberg.games.core.game_template.TemplateScreen;

public class MainMenuScreen extends AbstractScreen {
    private static final Logger log = LogManager.create(MainMenuScreen.class);

    TextButton textButton;
    SuperLabel sl;

    public MainMenuScreen(SpriteBatch batch) {
        super(batch);

        color = Color.FOREST;

        log.debug("Local storage path = " + Gdx.files.getLocalStoragePath());
        log.debug("External storage path = " + Gdx.files.getExternalStoragePath());
    }

    @Override
    protected void update(float delta) {
        sl.rotateBy(60 * delta);
    }

    @Override
    protected void createUiRenderer() {
        super.createUiRenderer();

        final Skin uiSkin = assets.get("ui/uiskin.json", Skin.class);

        textButton = new TextButton("PLAY", uiSkin);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getInstance().setScreen(new TemplateScreen(batch));
            }
        });
        uiRenderer.rootTable.add(textButton);

        sl = new SuperLabel(uiSkin);
        uiRenderer.addActor(sl);
        sl.addAction(new MyMoveAction());
    }

    static class MyMoveAction extends MoveToAction {
        MyMoveAction() {
            setPosition(RandomGen.INSTANCE.nextFloat(100, 500),
                    RandomGen.INSTANCE.nextFloat(50, 400));
            setDuration(1f);
        }

        @Override
        protected void end() {
            target.addAction(new MyMoveAction());
        }
    }
}
