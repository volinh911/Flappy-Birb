package com.acesher.functionalities;

public class Point {

    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void updateX(int t) {
        x += t;
    }
    public void updateY(int t) {
        y += t;
    }

    public void setX(int t) {
        x = t;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
