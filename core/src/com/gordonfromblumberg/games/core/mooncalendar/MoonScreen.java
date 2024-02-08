package com.gordonfromblumberg.games.core.mooncalendar;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.gordonfromblumberg.games.core.common.screens.AbstractScreen;

import java.time.LocalDate;
import java.time.Month;

public class MoonScreen extends AbstractScreen {
    private final ObjectMap<LocalDate, MoonType> moonPhases = new ObjectMap<>();

    public MoonScreen(SpriteBatch batch) {
        super(batch);

        final int year = 2024;
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 4), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 11), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 18), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 25), MoonType.FULL);
    }

    @Override
    protected void initialize() {
        super.initialize();

        final Skin skin = assets.get("ui/uiskin.json", Skin.class);
        Table rootTable = uiRenderer.getRootTable();
        rootTable.add(new Label("2024", skin)).colspan(31).align(Align.center);

        rootTable.row();
        for (int i = 0; i < 31; ++i) {
            rootTable.add(new Label(String.valueOf(i + 1), skin));
        }
    }
}
