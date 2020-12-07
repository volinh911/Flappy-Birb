package com.acesher.functionalities;

public class Sprite {

    //Sprite technically holds the position (TOP-LEFT), size of the Sprite, everything behind an interact-able object
    protected Point pos; //Top-Left
    protected double size;

    public Sprite(int x, int y, double size) {
        pos = new Point(x, y);
        this.size = size;
    }

    public Point getPos() {
        return pos;
    }
    public void updatePos(int x, int y) {
        pos.updateX(x);
        pos.updateY(y);
    }

    public double getSize() {
        return size;
    }
}
