package com.acesher.flappybirb;

import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import com.acesher.functionalities.Sprite;

public class Tubes extends Sprite {

    private int gap; //Gap between the top and bot (͠≖ ʖ͠≖)
    private int dist; //Distance between 2 sets of Tube
    private DisplayMetrics dM;

    public Tubes(@NonNull DisplayMetrics dM, int gap, double distRatio, double size) {
        super( 0, 0, size);
        this.dM = dM;
        this.gap = gap;

        dist = (int) Math.floor(dM.heightPixels * distRatio);
        pos.setX(dM.widthPixels);
    }

    public int getDist() {
        return dist;
    }

    public int getGap() {
        return gap;
    }
}
