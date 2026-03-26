package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gordonfromblumberg.games.core.common.world.World;

import java.io.BufferedReader;
import java.io.IOException;

public class SnakesWorld extends World {
    static final int generationSize = 16;
    static final char emptyChar = '.';
    static final char appleChar = '$';
    static final char platformChar = '#';

    final State baseState;
    final State[] states = new State[generationSize];

    SnakesWorld() {
        Array<String> gridLines = read("snakes/grid.txt");
        Array<String> applesLines = read("snakes/power-sources.txt");
        Array<String> snakesLines = read("snakes/snakes.txt");
        final int width = gridLines.size;
        final int height = gridLines.get(0).length();
        final int allSnakeCount = snakesLines.size;
        baseState = new State(width, height, allSnakeCount, true);

        for (int i = 0, n = gridLines.size; i < n; ++i) {
            String gridLine = gridLines.get(i);
            gridLine.getChars(0, height, baseState.grid[i], 0);
        }

        for (int i = 0, n = allSnakeCount >> 1; i < n; ++i) {
            baseState.snakeMap[i] = new Snake(i, true);
            baseState.snakeMap[i + n] = new Snake(i + n, false);
        }

        for (String appleLine : applesLines) {
            String[] coords = appleLine.split(", ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            baseState.grid[x][y] = appleChar;
            baseState.powerSources.add(State.packCoords(x, y));
        }

        baseState.updateSnakes(snakesLines.toArray(String.class));

        for (int i = 0; i < generationSize; ++i) {
            states[i] = new State(width, height, snakesLines.size, false);
            states[i].set(baseState); // todo remove later
        }
    }

    private Array<String> read(String filePath) {
        Array<String> fileLines = new Array<>();
        try (BufferedReader br = Gdx.files.classpath(filePath).reader(1 << 13)) {
            String line;
            while ((line = br.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read grid.txt", e);
        }
        return fileLines;
    }
}
