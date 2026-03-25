package com.gordonfromblumberg.games.core.snakes;

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    ;

    static final Direction[] ALL = Direction.values();

    final int x;
    final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Direction next() {
        return ALL[(ordinal() + 1) % 4];
    }

    Direction prev() {
        return ALL[(ordinal() + 3) % 4];
    }

    Direction opposite() {
        return ALL[(ordinal() + 2) % 4];
    }

    char next(char[][] grid, int x, int y) {
        int nextX = x + this.x;
        if (nextX < 0 || nextX >= grid.length) return SnakesWorld.emptyChar;
        int nextY = y + this.y;
        if (nextY < 0 || nextY >= grid[0].length) return SnakesWorld.emptyChar;
        return grid[nextX][nextY];
    }
}
