package com.acesher.flappybirb;

import com.acesher.functionalities.Sprite;

public class Birb extends Sprite {

    public Birb(int x, int y, double size) {
        super(x,y,size);
    }

    public int getPosX() {
        return pos.getX();
    }

    public int getPosY() {
        return pos.getY();
    }
    public void updatePosY(int posY) {
        pos.updateY(posY);
    }
}
