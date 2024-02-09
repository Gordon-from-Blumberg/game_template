package com.gordonfromblumberg.games.core.mooncalendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gordonfromblumberg.games.core.common.log.LogManager;
import com.gordonfromblumberg.games.core.common.log.Logger;
import com.gordonfromblumberg.games.core.common.screens.AbstractScreen;

import java.time.LocalDate;
import java.time.Month;
import java.util.zip.Deflater;

import static com.gordonfromblumberg.games.core.common.utils.StringUtils.padLeft;

public class MoonScreen extends AbstractScreen {
    private static final Logger log = LogManager.create(MoonScreen.class);

    private static final int year = 2024;
    private final ObjectMap<LocalDate, MoonType> moonPhases = new ObjectMap<>();
    private final ObjectSet<LocalDate> eclipses = new ObjectSet<>();

    public MoonScreen(SpriteBatch batch) {

        super(batch);

        moonPhases.put(LocalDate.of(year, Month.JANUARY, 4), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 11), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 18), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.JANUARY, 25), MoonType.FULL);

        moonPhases.put(LocalDate.of(year, Month.FEBRUARY, 2), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.FEBRUARY, 9), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.FEBRUARY, 16), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.FEBRUARY, 24), MoonType.FULL);

        moonPhases.put(LocalDate.of(year, Month.MARCH, 3), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.MARCH, 10), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.MARCH, 17), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.MARCH, 25), MoonType.FULL);

        moonPhases.put(LocalDate.of(year, Month.APRIL, 2), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.APRIL, 8), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.APRIL, 15), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.APRIL, 23), MoonType.FULL);

        moonPhases.put(LocalDate.of(year, Month.MAY, 1), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.MAY, 8), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.MAY, 15), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.MAY, 23), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.MAY, 30), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.JUNE, 6), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.JUNE, 14), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.JUNE, 22), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.JUNE, 28), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.JULY, 5), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.JULY, 13), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.JULY, 21), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.JULY, 28), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.AUGUST, 4), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.AUGUST, 12), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.AUGUST, 19), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.AUGUST, 26), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.SEPTEMBER, 3), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.SEPTEMBER, 11), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.SEPTEMBER, 18), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.SEPTEMBER, 24), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.OCTOBER, 2), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.OCTOBER, 10), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.OCTOBER, 17), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.OCTOBER, 24), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.NOVEMBER, 1), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.NOVEMBER, 9), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.NOVEMBER, 15), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.NOVEMBER, 23), MoonType.QUARTER2);

        moonPhases.put(LocalDate.of(year, Month.DECEMBER, 1), MoonType.NEW);
        moonPhases.put(LocalDate.of(year, Month.DECEMBER, 8), MoonType.QUARTER1);
        moonPhases.put(LocalDate.of(year, Month.DECEMBER, 15), MoonType.FULL);
        moonPhases.put(LocalDate.of(year, Month.DECEMBER, 22), MoonType.QUARTER2);
        moonPhases.put(LocalDate.of(year, Month.DECEMBER, 30), MoonType.NEW);

        eclipses.add(LocalDate.of(year, Month.MARCH, 25));
        eclipses.add(LocalDate.of(year, Month.SEPTEMBER, 18));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected void initialize() {
        super.initialize();

        final Skin skin = assets.get("ui/uiskin.json", Skin.class);
        final TextureAtlas atlas = assets.get("image/texture_pack.atlas", TextureAtlas.class);
        Table rootTable = uiRenderer.getRootTable();
        rootTable.setBackground(new TextureRegionDrawable(atlas.findRegion("space")));
        rootTable.defaults().space(4f);
        rootTable.add(new Label(String.valueOf(year), skin)).colspan(32).align(Align.center);

        rootTable.row();
        rootTable.add(new Label("M", skin)).align(Align.center);
        for (int i = 0; i < 31; ++i) {
            rootTable.add(new Label(String.valueOf(i + 1), skin));
        }

        LocalDate date = LocalDate.of(year - 1, Month.DECEMBER, 31);
        Month month = null;
        MoonType moonType = MoonType.FULL.next();

        while (true) {
            date = date.plusDays(1);
            if (date.getYear() != year)
                break;

            if (date.getMonth() != month) {
                month = date.getMonth();
                rootTable.row();
                rootTable.add(new Label(padLeft(month.getValue(), 2), skin));
            }

            MoonType mt = moonPhases.get(date);
            if (mt != null) {
                moonType = mt;
            }

            TextureRegion region = atlas.findRegion(moonType.image);
            Image image = new Image(region);
            image.setOriginX(region.getRegionWidth() / 2f);
            image.setScaleX(moonType.flipped ? -1f : 1f);
            if (eclipses.contains(date)) {
                image.getColor().mul(0.65f);
            }
            rootTable.add(image);

            if (mt != null) {
                moonType = moonType.next();
            }
        }
        rootTable.layout();

//        log.info("Table size = " + rootTable.getPrefWidth() + ", " + rootTable.getPrefHeight());
//        FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888,
//                (int) rootTable.getPrefWidth(), (int) rootTable.getPrefHeight(),
//                false);
//        fbo.begin();
//        Gdx.gl.glClearColor(0, 0, 0, 0);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        uiRenderer.render(0);
//        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, fbo.getWidth(), fbo.getHeight());
//        PixmapIO.writePNG(Gdx.files.local("image/result.png"), pixmap, Deflater.DEFAULT_COMPRESSION, true);
//        pixmap.dispose();
//        fbo.end();
    }
}
