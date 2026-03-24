package com.gordonfromblumberg.games.core.motion_animation.gravity.ui;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.gordonfromblumberg.games.core.motion_animation.gravity.SpaceBody;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.gordonfromblumberg.games.core.common.utils.MathHelper.clampAngleDeg;
import static com.gordonfromblumberg.games.core.common.utils.StringUtils.floatToString;
import static com.gordonfromblumberg.games.core.common.utils.StringUtils.isBlank;

public class SpaceBodyInfo extends Window {
    private static final float duration = 0.2f;

    private final StringBuilder sb = new StringBuilder();
    private final Label massLbl;
    private final Label positionLbl;
    private final Label velocityLbl;
    private final Label velocityAngleLbl;

    private SpaceBody spaceBody;

    public SpaceBodyInfo(Skin skin) {
        super("", skin);

        defaults().space(5f);
        columnDefaults(0).align(Align.right);
        columnDefaults(1).align(Align.left).minWidth(120f);

        add("Mass");
        add(this.massLbl = new Label("", skin));

        row();
        add("Position");
        add(this.positionLbl = new Label("", skin));

        row();
        add("Velocity");
        add(this.velocityLbl = new Label("", skin));

        row();
        add("Vel angle");
        add(this.velocityAngleLbl = new Label("", skin));
    }

    @Override
    public void act(float delta) {
        sb.clear();
        floatToString(spaceBody.getMass(), 2, sb);
        massLbl.setText(sb);

        sb.clear();
        floatToString(spaceBody.position.x, 2, sb);
        sb.append("; ");
        floatToString(spaceBody.position.y, 2, sb);
        positionLbl.setText(sb);

        sb.clear();
        floatToString(spaceBody.velocity.len(), 2, sb);
        velocityLbl.setText(sb);

        sb.clear();
        floatToString(clampAngleDeg(spaceBody.velocity.angleDeg()), 2, sb);
        velocityAngleLbl.setText(sb);

        pack();

        super.act(delta);
    }

    public void hide(Action action) {
        clearActions();
        addAction(sequence(alpha(0f, duration), action));
    }

    public void hide() {
        hide(Actions.removeActor());
    }

    public void show(Stage stage) {
        clearActions();
        getColor().a = 0f;
        stage.addActor(this);
        addAction(showAction());
    }

    public SpaceBody getSpaceBody() {
        return spaceBody;
    }

    public void setSpaceBody(SpaceBody spaceBody) {
        this.spaceBody = spaceBody;

        sb.clear();
        if (!isBlank(spaceBody.getName())) sb.append(spaceBody.getName());
        sb.append('#').append(spaceBody.getId());
        getTitleLabel().setText(sb);
    }

    public static Action showAction() {
        return alpha(1f, duration);
    }
}
