package com.gordonfromblumberg.games.core.snakes;

public class Solution {
    final byte[] moveSequence;
    float fitness;

    Solution(int moveSequenceSize) {
        this.moveSequence = new byte[moveSequenceSize];
    }
}
