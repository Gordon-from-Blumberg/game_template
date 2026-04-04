package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gordonfromblumberg.games.core.common.ui.IntChangeableLabel;
import com.gordonfromblumberg.games.core.common.ui.UpdatableLabel;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.common.world.WorldUIInfo;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

public class SnakesUIRenderer extends WorldUIRenderer<SnakesWorld> {

    public SnakesUIRenderer(WorldUIInfo<SnakesWorld> info) {
        super(info);

        final Skin skin = Assets.get("ui/uiskin.json", Skin.class);
        rootTable.add(worldTable(skin)).expand().fill();
//        rootTable.add(sideBar(skin)).width(200f);
        rootTable.add(sideBar(skin));
        rootTable.row();
        rootTable.add(footer(skin)).colspan(2);
    }

    private Table worldTable(Skin skin) {
        Table worldTable = new Table(skin);
        worldTable.defaults().fill().expand().pad(5f, 5f, 20f, 5f);

        for (int i = 0; i < 4; ++i) {
            worldTable.row();
            for (int j = 0; j < 4; ++j) {
                worldTable.add(new SnakesComponent(world.states[i * 4 + j], i * 4 + j, world::getFitness));
            }
        }
        return worldTable;
    }

    private Table sideBar(Skin skin) {
        Table sideBar = new Table(skin);
        return sideBar;
    }

    private Table footer(Skin skin) {
        float space = 20f;
        Table footer = new Table(skin);
        footer.add("Generation").align(Align.center).spaceRight(space);
        footer.add("Simulation").align(Align.center).colspan(4).space(0f, space, 0f, space);
        footer.add("Turn").align(Align.center).space(0f, space, 0f, space);
        footer.add("Speed").align(Align.center).spaceLeft(space);

        footer.row().padBottom(4f);
        IntChangeableLabel generationLbl = new IntChangeableLabel(skin, world::setGeneration);
        generationLbl.setMinValue(0);
        generationLbl.setMaxValue(200);
        generationLbl.setValue(0);
        generationLbl.setFieldWidth(40f);
        footer.add(generationLbl).spaceRight(space);

        TextButton oneTurnBtn = new TextButton("One turn", skin);
        oneTurnBtn.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                world.oneTurn();
            }
        });
        footer.add(oneTurnBtn).spaceLeft(space);

        TextButton runBtn = new TextButton("Run", skin);
        runBtn.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                world.resetPause();
            }
        });
        footer.add(runBtn);

        TextButton stopBtn = new TextButton("Stop", skin);
        stopBtn.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                world.pause();
            }
        });
        footer.add(stopBtn);

        TextButton resetBtn = new TextButton("Reset", skin);
        resetBtn.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                world.reset();
            }
        });
        footer.add(resetBtn).spaceRight(space);

        footer.add(new UpdatableLabel(skin, true, sb -> sb.append(world.getSimulationTurn())))
                .align(Align.center);

        IntChangeableLabel speedLbl = new IntChangeableLabel(skin, world::setSpeed);
        speedLbl.setMinValue(2);
        speedLbl.setMaxValue(60);
        speedLbl.setStep(2);
        speedLbl.setValue(6);
        speedLbl.setFieldWidth(30f);
        footer.add(speedLbl).spaceLeft(0);
        return footer;
    }
}
