package com.gordonfromblumberg.games.core.snakes;

import java.util.HashSet;
import java.util.Set;

import static com.gordonfromblumberg.games.core.snakes.SnakesWorld.*;

public class State {
    static final int bits16 = (1 << 16) - 1;

    int turn;
    final char[][] grid;
    final Snake[] snakeMap;
    final Set<Integer> moveTargets;
    final Set<Integer> powerSources = new HashSet<>();
    float myScoreSum;
    int outOfScreen;

    State(int width, int height, int snakeCount, boolean base) {
        this.grid = new char[width][height];
        this.snakeMap = new Snake[snakeCount];
        this.moveTargets = base ? null : new HashSet<>();
    }

    void newTurn(char[][] baseGrid) {
        ++turn;
        for (int i = 0, w = baseGrid.length, h = baseGrid[0].length; i < w; ++i) {
            System.arraycopy(baseGrid[i], 0, grid[i], 0, h);
        }
        powerSources.clear();
    }

    void updateSnakes(String[] snakeBodies) {
        for (int i = 0, n = snakeBodies.length; i < n; ++i) {
            Snake snake = snakeMap[i];

            String body = snakeBodies[i];
            if (body == null) {
                snake.reset();
            } else {
                snake.read(body);
                // render snake on grid
                char ch = snake.idToChar();
                for (SnakePart snakePart : snake.parts) {
                    if (snakePart == snake.head)
                        set(snakePart.x, snakePart.y, Character.toUpperCase(ch));
                    else
                        set(snakePart.x, snakePart.y, ch);
                }
            }
        }
    }

    // return true when finished
    boolean move() {
        final char[][] grid = this.grid;
        final int width = grid.length;
        final int height = grid[0].length;

        int mySnakeCount = 0, oppSnakeCount = 0;
        for (Snake snake : snakeMap) {
            if (snake.head != null) {
                if (snake.mine) ++mySnakeCount;
                else ++oppSnakeCount;
            }
        }

        if (mySnakeCount == 0 || oppSnakeCount == 0 || turn >= moveSequenceSize || powerSources.isEmpty())
            return true;

        // Calculate turn reward before moving
        float turnReward = 0;
        for (Snake snake : snakeMap) {
            if (snake.mine && snake.head != null) {
                turnReward += 1.0f;                          // reward for survival
                turnReward += snake.parts.size * 0.2f;       // reward for growth
            }
        }

        // fill moveTargets - set of cells where snakes are going to move (empty or with power source)
        for (Snake snake : snakeMap) {
            if (snake.head == null) continue;

            char target = snake.next(grid);
            if (target == emptyChar || target == appleChar) {
                int coords = packCoords(snake.head.x + snake.dir.x, snake.head.y + snake.dir.y);
                moveTargets.add(coords);
                if (target == appleChar) {
                    snake.grow = true;
                }
            }
        }

        // move snakes
        for (Snake snake : snakeMap) {
            if (snake.head == null) continue;

            int nextX = snake.head.x + snake.dir.x;
            int nextY = snake.head.y + snake.dir.y;
            char target = snake.next(grid);
            if (target == emptyChar || target == appleChar) {
                SnakePart newHead = SnakePart.instance();
                newHead.set(nextX, nextY);
                snake.parts.addFirst(newHead);
                char ch = snake.idToChar();
                set(snake.head.x, snake.head.y, ch);
                snake.head = newHead;
                set(snake.head.x, snake.head.y, Character.toUpperCase(ch));
                if (target == appleChar) {
                    powerSources.remove(packCoords(nextX, nextY));
                    if (snake.mine) {
                        turnReward += 5.0f;  // bonus for eating apple
                    }
                }
            }

            // head of another snake, but this cell was empty
            if (target >= 'A' && target <= 'Z' && moveTargets.contains(packCoords(nextX, nextY))) {
                snakeMap[Snake.charToId(target)].removeHead = true;
            }

            // when snake does not eat power source its tail is removed
            if (!snake.grow) {
                SnakePart tail = snake.parts.removeLast();
                set(tail.x, tail.y, emptyChar);
                tail.free();
            }

            snake.grow = false;
        }

        // remove heads
        for (Snake snake : snakeMap) {
            if (snake.head == null || !snake.removeHead) continue;

            snake.removeHead = false;
            set(snake.head.x, snake.head.y, emptyChar);
            snake.parts.removeFirst().free();
            snake.head = snake.parts.first();
            toUpperCase(snake.head.x, snake.head.y);
        }

        // check for out of screen and minimum length
        for (Snake snake : snakeMap) {
            if (snake.head == null) continue;

            int minX = snake.head.x;
            int maxX = minX;
            for (SnakePart part : snake.parts) {
                if (part.x < minX) minX = part.x;
                if (part.x > maxX) maxX = part.x;
            }

            // is snake length < 3 or snake out of screen
            if (snake.parts.size < 3 || maxX < 0 || minX >= width) {
                for (SnakePart part : snake.parts) {
                    set(part.x, part.y, emptyChar);
                }
                snake.reset();
            }
        }

        // move snakes down due to gravity
        for (Snake snake : snakeMap) {
            if (snake.head == null) continue;

            char snakeChar = snake.idToChar();
            int minFall = height;
            for (SnakePart part : snake.parts) {
                int x = part.x;
                if (x < 0 || x >= width) continue;

                int fall = 0;
                // find
                while (fall < minFall) {
                    char cell = get(x, part.y + fall + 1);
                    if (cell == emptyChar || Character.toLowerCase(cell) == snakeChar) {
                        ++fall;
                        continue;
                    }
                    break;
                }
                if (fall < minFall) minFall = fall;
            }

            if (minFall == 0) continue;

            // first remove
            for (SnakePart part : snake.parts) {
                set(part.x, part.y, emptyChar);
            }
            // then render
            for (SnakePart part : snake.parts) {
                part.y += minFall;
                set(part.x, part.y, part == snake.head ? Character.toUpperCase(snakeChar) : snakeChar);
            }
        }

        for (Snake snake : snakeMap) {
            if (snake.head != null && snake.mine) {
                for (SnakePart part : snake.parts) {
                    if (part.x < 0 || part.x >= width || part.y < 0 || part.y >= height) {
                        ++outOfScreen;
                        turnReward -= 1.0f;  // penalty for out of screen segment
                    }
                }
            }
        }

        myScoreSum += turnReward;
        ++turn;
        return false;
    }

    void setDirections(byte move) {
        final int width = grid.length;
        int snakeInd = 0;
        for (Snake snake : snakeMap) {
            if (snake.mine) {
                snake.dir = Direction.ALL[(move >>> (2 * snakeInd++)) & 3];
            } else if (snake.head != null) {
                SnakePart head = snake.head;
                for (Direction d : Direction.ALL) {
                    int newX = head.x + d.x;
                    if (newX < 0 || newX >= width) continue;
                    char ch = get(newX, head.y + d.y);
                    if (ch == appleChar) {
                        snake.dir = d;
                        break;
                    }
                    if (ch == emptyChar) {
                        snake.dir = d;
                    }
                }
            }
        }
    }

    void set(State original) {
        turn = original.turn;
        for (int i = 0, w = grid.length, h = grid[0].length; i < w; ++i) {
            System.arraycopy(original.grid[i], 0, grid[i], 0, h);
        }
        myScoreSum = 0;
        outOfScreen = 0;
        for (int i = 0, n = snakeMap.length; i < n; ++i) {
            Snake origSnake = original.snakeMap[i];
            if (origSnake != null) {
                if (snakeMap[i] == null) {
                    snakeMap[i] = new Snake(origSnake.id, origSnake.mine);
                }
                snakeMap[i].set(origSnake);
            }
        }
        powerSources.clear();
        powerSources.addAll(original.powerSources);
    }

    char get(int x, int y) {
        return x < 0 || x >= grid.length || y < 0 || y >= grid[0].length ? emptyChar : grid[x][y];
    }

    void set(int x, int y, char ch) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            grid[x][y] = ch;
        }
    }

    void toUpperCase(int x, int y) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            grid[x][y] = Character.toUpperCase(grid[x][y]);
        }
    }

    String printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < grid[0].length; ++y) {
            for (char[] chars : grid) {
                sb.append(chars[y]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    static int packCoords(int x, int y) {
        return (x << 16) | (y & bits16);
    }

    static int getX(int coords) {
        return coords >> 16;
    }

    static int getY(int coords) {
        return (coords << 16) >> 16;
    }
}
