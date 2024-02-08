package com.gordonfromblumberg.games.core.common.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SuperLabel extends Group {
    private Container<Label> innerContainer = new Container<>();
    private Label label;
    private int sign = -1;

    public SuperLabel(Skin skin) {
        setWidth(200);
        setHeight(50);

        innerContainer.setWidth(190);
        innerContainer.setHeight(40);
        innerContainer.setPosition(5, 5);
        addActor(innerContainer);

//        setZIndex(50);

        label = new Label("Some long, long, long... very long text", skin);
        innerContainer.setActor(label);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (sign < 0 && label.getX() < -100) {
            sign = 1;
        }
        if (sign > 0 && label.getX() > 100) {
            sign = -1;
        }
        label.moveBy(50 * sign * delta, 0);
    }
}
