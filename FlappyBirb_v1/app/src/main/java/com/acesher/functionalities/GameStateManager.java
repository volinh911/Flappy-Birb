package com.acesher.functionalities;

public class GameStateManager {

    public static final int START = 0;
    public static final int PLAY = 1;
    public static final int LOSE = 2;

    private int state;

    public GameStateManager() {
        state = START;
    }

    public void updateState(int x) {
        state = x;
    }

    public boolean is(int state) {
        return this.state == state;
    }

}
