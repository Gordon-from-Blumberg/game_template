package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gordonfromblumberg.games.core.common.utils.Assets;
import com.gordonfromblumberg.games.core.common.world.WorldUIInfo;
import com.gordonfromblumberg.games.core.common.world.WorldUIRenderer;

public class SnakesUIRenderer extends WorldUIRenderer<SnakesWorld> {

    public SnakesUIRenderer(WorldUIInfo<SnakesWorld> info) {
        super(info);

        final Skin skin = Assets.get("ui/uiskin.json", Skin.class);
        rootTable.add(worldTable(skin)).expand();
        rootTable.add(sideBar(skin)).width(200f);
        rootTable.row();
        rootTable.add(footer(skin)).colspan(2).height(50f);
    }

    private Table worldTable(Skin skin) {
        Table worldTable = new Table(skin);
        return worldTable;
    }

    private Table sideBar(Skin skin) {
        Table sideBar = new Table(skin);
        return sideBar;
    }

    private Table footer(Skin skin) {
        Table footer = new Table(skin);
        return footer;
    }
}
