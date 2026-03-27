package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gordonfromblumberg.games.core.common.utils.Assets;

public class SnakesComponent extends Widget {
    private static final Drawable emptyCell;
    private static final Drawable platformCell;
    private static final Drawable appleCell;
    private static final Drawable mySnakeHead;
    private static final Drawable mySnakeBody;
    private static final Drawable mySnakeConnection;
    private static final Drawable oppSnakeHead;
    private static final Drawable oppSnakeBody;
    private static final Drawable oppSnakeConnection;

    static {
        TextureAtlas snakesAtlas = Assets.get("image/snakes.atlas", TextureAtlas.class);
        TextureRegionDrawable cellDrawable = new TextureRegionDrawable(snakesAtlas.findRegion("cell"));
        final Color color = new Color();
        emptyCell = cellDrawable.tint(color.set(0.8f, 0.8f, 0.8f, 1f));
        platformCell = cellDrawable.tint(color.set(0.2f, 0.2f, 0.2f, 1f));
        appleCell = cellDrawable.tint(color.set(1f, 0.2f, 0.2f, 1f));
        mySnakeBody = cellDrawable.tint(color.set(0.3f, 0.3f, 1f, 1f));
        oppSnakeBody = cellDrawable.tint(color.set(0.9f, 0.9f, 0.1f, 1f));

        TextureRegionDrawable headDrawable = new TextureRegionDrawable(snakesAtlas.findRegion("head"));
        mySnakeHead = headDrawable.tint(color.set(0.3f, 0.3f, 1f, 1f));
        oppSnakeHead = headDrawable.tint(color.set(0.9f, 0.9f, 0.1f, 1f));

        TextureRegionDrawable connectionDrawable = new TextureRegionDrawable(snakesAtlas.findRegion("connection"));
        mySnakeConnection = connectionDrawable.tint(color.set(0.3f, 0.3f, 1f, 1f));
        oppSnakeConnection = connectionDrawable.tint(color.set(0.9f, 0.9f, 0.1f, 1f));
    }

    private final State state;
    private final int number;
    private final Label label = new Label("", Assets.get("ui/uiskin.json", Skin.class));

    public SnakesComponent(State state, int number) {
        this.state = state;
        this.number = number;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        final char[][] grid = state.grid;
        float width = getWidth();
        float height = getHeight();
        int gridWidth = grid.length;
        int gridHeight = grid[0].length;
        float targetRatio = height / width;
        float sourceRatio = (float) gridHeight / gridWidth;
        float scale = targetRatio > sourceRatio ? width / gridWidth : height / gridHeight;
        width = gridWidth * scale;
        final float offsetX = getX();
        final float offsetY = gridHeight * scale + getY();
        final float cellSize = width / gridWidth;

        for (int x = 0; x < gridWidth; ++x) {
            char[] column = grid[x];
            for (int y = 0; y < gridHeight; ++y) {
                char cellChar = column[y];
                switch (cellChar) {
                    case SnakesWorld.emptyChar ->
                            emptyCell.draw(batch, offsetX + x * cellSize, offsetY - (y + 1) * cellSize,
                                           cellSize, cellSize);
                    case SnakesWorld.appleChar ->
                            appleCell.draw(batch, offsetX + x * cellSize, offsetY - (y + 1) * cellSize,
                                           cellSize, cellSize);
                    case SnakesWorld.platformChar ->
                            platformCell.draw(batch, offsetX + x * cellSize, offsetY - (y + 1) * cellSize,
                                              cellSize, cellSize);
                }
            }
        }

        for (Snake snake : state.snakeMap) {
            if (snake.head == null) continue;

            for (int i = 0, n = snake.parts.size; i < n; ++i) {
                SnakePart part = snake.parts.get(i);
                if (part == snake.head) {
                    (snake.mine ? mySnakeHead : oppSnakeHead)
                            .draw(batch, offsetX + part.x * cellSize, offsetY - (part.y + 1) * cellSize,
                                  cellSize, cellSize);
                } else {
                    (snake.mine ? mySnakeBody : oppSnakeBody)
                            .draw(batch, offsetX + part.x * cellSize, offsetY - (part.y + 1) * cellSize,
                                  cellSize, cellSize);
                }

                if (i + 1 < n) {
                    SnakePart next = snake.parts.get(i + 1);
                    float x = part.x + 0.5f * (next.x - part.x);
                    float y = part.y + 0.5f * (next.y - part.y);
                    (snake.mine ? mySnakeConnection : oppSnakeConnection)
                            .draw(batch, offsetX + x * cellSize, offsetY - (y + 1) * cellSize,
                                  cellSize, cellSize);
                }
            }
        }

        label.setX(offsetX);
        label.setY(getY() - cellSize);
        label.setText("#" + number + ", turn = " + state.turn + ", fitness = ");
        label.draw(batch, parentAlpha);
    }
}
