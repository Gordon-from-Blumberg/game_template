package com.gordonfromblumberg.games.core.snakes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.gordonfromblumberg.games.core.common.utils.RandomGen;
import com.gordonfromblumberg.games.core.common.world.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SnakesWorld extends World {
    static final int generationSize = 16;
    static final int moveSequenceSize = 32;
    static final char emptyChar = '.';
    static final char appleChar = '$';
    static final char platformChar = '#';
    static final Comparator<Solution> solutionComparator = (s1, s2) ->
            Float.compare(s2.fitness, s1.fitness);

    final State baseState;
    final State[] states = new State[generationSize];
    int generation;
    final Array<Solution[]> generations = new Array<>();
    int simulationTurn;

    float time;
    float turnsPerSecond = 6;

    SnakesWorld() {
        paused = true;
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
            State state = new State(width, height, snakesLines.size, false);
            states[i] = state;
        }
    }

    @Override
    public void initialize() {
        // generate first generation
        Solution[] firstGeneration = new Solution[generationSize];
        final int snakeCount = baseState.snakeMap.length >> 1;
        for (int i = 0; i < generationSize; ++i) {
            // copy best move sequence from previous turn
//            if (i == 0 && baseState.turn > 1) {
//                System.arraycopy(bestMoveSequence, 1, generation[0], 0, moveSequenceSize - 1);
//                generation[0][moveSequenceSize - 1] = randomMove(snakeCount);
//                continue;
//            }
            Solution solution = new Solution(moveSequenceSize);
            firstGeneration[i] = solution;
            for (int j = 0; j < moveSequenceSize; ++j) {
                solution.moveSequence[j] = randomMove(snakeCount);
            }
        }

        addGeneration(firstGeneration);
        simulateAndCalculateFitness();
    }

    @Override
    public void update(float delta, float mouseX, float mouseY) {
        super.update(delta, mouseX, mouseY);
        if (paused) return;

        time += delta;
        if (time >= 1f / turnsPerSecond) {
            time = 0;
            if (simulationTurn < moveSequenceSize - 1) {
                ++simulationTurn;
                move();
            } else {
                paused = true;
            }
        }
    }

    void oneTurn() {
        if (simulationTurn < moveSequenceSize - 1) {
            ++simulationTurn;
            move();
        }
    }

    void reset() {
        resetStates();
    }

    void setGeneration(int generationNumber) {
        if (generationNumber < 1)
            return;

        resetStates();
        if (generationNumber > generations.size) {
            generateNewGeneration();
        }
        generation = generations.size;
    }

    int getSimulationTurn() {
        return simulationTurn;
    }

    void setSpeed(int speed) {
        turnsPerSecond = speed;
    }

    float getFitness(int n) {
        return generations.get(generation - 1)[n].fitness;
    }

    private void move() {
        for (int i = 0; i < generationSize; ++i) {
            final State state = states[i];
            final byte move = generations.get(generation - 1)[i].moveSequence[simulationTurn];
            state.setDirections(move);
            state.move();
        }
    }

    private void simulateAndCalculateFitness() {
        int myBaseScore = 0, myBaseSnakeCount = 0;
        for (Snake snake : baseState.snakeMap) {
            if (snake.mine && snake.head != null) {
                myBaseScore += snake.parts.size;
                ++myBaseSnakeCount;
            }
        }
        final float loseScoreCoef = 0.5f / myBaseScore;
        final Solution[] curGeneration = generations.get(generation - 1);
        for (int g = 0; g < generationSize; ++g) {
            final State state = states[g];
            final Solution solution = curGeneration[g];
            for (int m = 0; m < moveSequenceSize; ++m) {
                final byte move = solution.moveSequence[m];
                state.setDirections(move);
                final boolean finished = state.move();
                int myScore = 0;
                for (Snake snake : state.snakeMap) {
                    if (snake.mine && snake.head != null)
                        ++myScore;
                }
                state.myScoreSum += myScore * (1 + 0.1f * (state.turn - baseState.turn));
                if (finished)
                    break;
            }

            int myScore = 0, mySnakeCount = 0;
            for (Snake snake : state.snakeMap) {
                if (snake.head != null) {
                    if (snake.mine) {
                        ++mySnakeCount;
                        myScore += snake.parts.size;
                    }
                }
            }

            float fit = state.myScoreSum / 10;
            if (mySnakeCount < myBaseSnakeCount) {
                fit *= 1 + 0.5f * (mySnakeCount - 1);
            } else {
                fit *= 5;
            }
            if (myScore < myBaseScore) {
                fit *= 1 - loseScoreCoef * (myBaseScore - myScore);
            } else if (myScore > myBaseScore) {
                float k = 1 + 0.5f * (myScore - myBaseScore);
                fit *= k * k;
            }
            fit /= 1 + 0.2f * state.outOfScreen;
//                if (myScore > oppScore) {
//                    fit *= 1 + 0.2f * (myScore - oppScore);
//                }
//                fit *= 1 + 0.8f * (state.myMaxScore - myBaseScore);
//                fit *= 1 + 0.1f * state.mySnakeCountSum / (state.turn - turn);

            solution.fitness = fit;
        }
        Arrays.sort(curGeneration, solutionComparator);
        resetStates();
    }

    private void addGeneration(Solution[] generation) {
        resetStates();
        generations.add(generation);
        this.generation = generations.size;
    }

    private void generateNewGeneration() {

    }

    private void resetStates() {
        for (int i = 0; i < generationSize; ++i) {
            states[i].set(baseState);
        }
        simulationTurn = 0;
    }

    private static Array<String> read(String filePath) {
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

    private byte randomMove(int snakeCount) {
        byte move = 0;
        for (int i = 0; i < snakeCount; ++i) {
            move |= (RandomGen.INSTANCE.nextInt(4) << (i * 2));
        }
        return move;
    }
}
