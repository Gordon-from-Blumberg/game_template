package com.gordonfromblumberg.games.core.motion_animation.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.ui.ButtonListWindow;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.common.world.WorldUIInfo;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;
import com.gordonfromblumberg.games.core.motion_animation.gravity.ui.SpaceBodyInfo;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class GravityUIRenderer extends WorldUIRenderer<GravityWorld> {
    private static final Logger log = LogManager.create(GravityUIRenderer.class);

    private final Vector3 coords = new Vector3();
    private final Vector3 screenCenter = new Vector3();
    private SpaceBodyInfo bodyInfo;

    public GravityUIRenderer(WorldUIInfo<GravityWorld> info) {
        super(info);

        addSaveLoadWindow(1 << 14, "saves", "sav");

        final Skin skin = Assets.get("ui/uiskin.json", Skin.class);
        String mainMenu = "main-menu";
        windowManager.register(mainMenu, createMainMenu(skin, mainMenu));
        windowManager.setDefaultWindow(mainMenu);

        bodyInfo = new SpaceBodyInfo(skin);
    }

    @Override
    public void render(float dt) {
        if (world.underPointerChanged) {
            if (world.underPointer == null) {
                bodyInfo.hide();
            } else {
                if (bodyInfo.getStage() == null) {
                    bodyInfo.setSpaceBody(world.underPointer);
                    bodyInfo.show(stage);
                } else {
                    bodyInfo.hide(sequence(
                            run(() -> bodyInfo.setSpaceBody(world.underPointer)),
                            SpaceBodyInfo.showAction()));
                }
            }
        }

        if (bodyInfo.getStage() != null) {
            screenCenter.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0).scl(0.5f);
            screenToViewport(screenCenter);
            coords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            screenToViewport(coords);
            float w = bodyInfo.getWidth() / 2;
            float h = bodyInfo.getHeight() / 2;
            screenCenter.sub(coords).setLength2(w*w + h*h).add(coords).sub(w, h, 0);
            bodyInfo.setPosition(screenCenter.x, screenCenter.y);
        }

        super.render(dt);
    }

    private ButtonListWindow createMainMenu(Skin skin, String name) {
        ButtonListWindow window = new ButtonListWindow("Main menu", skin);
        window.setName(name);
        window.getButtonTable().defaults().minWidth(200f);
        window.button("Continue", (Runnable) () -> {
            windowManager.close();
            world.resetPause();
        });
        window.button("Save", (Runnable) () -> windowManager.open("save-window", false));
        window.button("Load", (Runnable) () -> windowManager.open("load-window", false));

        return window;
    }
}
