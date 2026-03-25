package com.gordonfromblumberg.games.core.snakes;

import java.util.ArrayList;
import java.util.List;

public class SnakePart {
    static final List<SnakePart> pool = new ArrayList<>();

    int x;
    int y;

    static SnakePart instance() {
        return !pool.isEmpty() ? pool.remove(pool.size() - 1) : new SnakePart();
    }

    void set(String partCoords) {
        String[] coords = partCoords.split(",");
        this.x = Integer.parseInt(coords[0]);
        this.y = Integer.parseInt(coords[1]);
    }

    void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void free() {
        pool.add(this);
    }
}
