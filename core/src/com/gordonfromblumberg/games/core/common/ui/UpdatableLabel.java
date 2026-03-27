package com.gordonfromblumberg.games.core.common.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.function.Consumer;

public class UpdatableLabel extends Label {
    private final Consumer<StringBuilder> textUpdater;
    private boolean autoClear;

    public UpdatableLabel(Skin skin, boolean autoClear, Consumer<StringBuilder> textUpdater) {
        super(null, skin);

        this.autoClear = autoClear;
        this.textUpdater = textUpdater;
    }

    public UpdatableLabel(Skin skin, Consumer<StringBuilder> textUpdater) {
        super(null, skin);

        this.textUpdater = textUpdater;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        StringBuilder sb = getText();
        if (autoClear) sb.clear();
        textUpdater.accept(sb);
        invalidateHierarchy();
    }

    public UpdatableLabel center() {
        setAlignment(Align.center);
        return this;
    }

    public void setAutoClear(boolean autoClear) {
        this.autoClear = autoClear;
    }
}
