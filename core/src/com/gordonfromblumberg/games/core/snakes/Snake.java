package com.gordonfromblumberg.games.core.snakes;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Snake {
    int id;
    boolean mine;
    boolean grow;
    boolean removeHead;
    Direction dir = Direction.UP;
    SnakePart head;
    final Deque<SnakePart> parts = new ArrayDeque<>();

    Snake(int id, boolean mine) {
        this.id = id;
        this.mine = mine;
    }

    void read(String body) {
        String[] bodyParts = body.split(":");
        if (bodyParts.length < parts.size()) {
            parts.removeFirst().free();
        }
        int i = 0;
        for (SnakePart snakePart : parts) {
            snakePart.set(bodyParts[i++]);
        }
        while (i < bodyParts.length) {
            SnakePart snakePart = SnakePart.instance();
            snakePart.set(bodyParts[i++]);
            parts.add(snakePart);
        }
        head = parts.getFirst();
    }

    void set(Snake original) {
        if (original.head == null)
            return;
        dir = original.dir;
        int sizeDiff = parts.size() - original.parts.size();
        while (sizeDiff > 0) {
            parts.removeLast().free();
            --sizeDiff;
        }
        while (sizeDiff < 0) {
            parts.add(SnakePart.instance());
            ++sizeDiff;
        }

        Iterator<SnakePart> thisIt = parts.iterator();
        Iterator<SnakePart> origIt = original.parts.iterator();
        while (thisIt.hasNext()) {
            SnakePart origPart = origIt.next();
            thisIt.next().set(origPart.x, origPart.y);
        }
        head = parts.getFirst();
        grow = false;
    }

    void reset() {
        head = null;
        Iterator<SnakePart> it = parts.iterator();
        while (it.hasNext()) {
            SnakePart snakePart = it.next();
            snakePart.free();
            it.remove();
        }
    }

    char next(char[][] grid) {
        return dir.next(grid, head.x, head.y);
    }

    char idToChar() {
        return (char) ('a' + id);
    }

    static int charToId(char ch) {
        return Character.toLowerCase(ch) - 'a';
    }
}
