package com.acesher.functionalities;

import com.acesher.flappybirb.R;

public class ResourcesManager {

    public final static int YELLOWBIRD = 0;
    public final static int REDBIRD = 1;
    public final static int BLUEBIRD = 2;

    public final static int DAY = 0;
    public final static int NIGHT = 1;

    public final static int GREENTUBE = 0;
    public final static int BROWNTUBE = 1;

    private int _cBg_id;
    private int[] _cBirb_id, _cTube_id;

    public int getBgResource() {
        return _cBg_id;
    }
    public void setBgResource(int bg_id) {
        switch (bg_id) {
            case DAY:
                _cBg_id = R.drawable.background_day;
                break;
            case NIGHT:
                _cBg_id = R.drawable.background_night;
                break;
        }
    }

    public int getBirbResource(int index) {
        return _cBirb_id[index];
    }
    public void setBirbResource(int birb_id) {
        switch (birb_id) {
            case YELLOWBIRD:
                _cBirb_id[0] = R.drawable.yellowbird_downflap;
                _cBirb_id[1] = R.drawable.yellowbird_midflap;
                _cBirb_id[2] = R.drawable.yellowbird_upflap;
                break;
            case REDBIRD:
                _cBirb_id[0] = R.drawable.redbird_downflap;
                _cBirb_id[1] = R.drawable.redbird_midflap;
                _cBirb_id[2] = R.drawable.redbird_upflap;
                break;
            case BLUEBIRD:
                _cBirb_id[0] = R.drawable.bluebird_downflap;
                _cBirb_id[1] = R.drawable.bluebird_midflap;
                _cBirb_id[2] = R.drawable.bluebird_upflap;
                break;
        }
    }

    public int getTubeResource(int index) {
        return _cTube_id[index];
    }
    public void setTubeResource(int tube_id) {
        switch (tube_id) {
            case GREENTUBE:
                _cTube_id[0] = R.drawable.pipe_green_up;
                _cTube_id[1] = R.drawable.pipe_green_down;
                break;
            case BROWNTUBE:
                _cTube_id[0] = R.drawable.pipe_red_up;
                _cTube_id[1] = R.drawable.pipe_red_down;
                break;
        }
    }

    public ResourcesManager(int _cBirb_id, int _cBg_id, int _cTube_id) {
        setBgResource(_cBg_id);

        this._cBirb_id = new int[3];
        setBirbResource(_cBirb_id);

        this._cTube_id = new int[2];
        setTubeResource(_cTube_id);
    }

}
    