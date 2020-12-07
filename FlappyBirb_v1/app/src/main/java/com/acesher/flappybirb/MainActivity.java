package com.acesher.flappybirb;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import static com.acesher.functionalities.ResourcesManager.*;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout bg;

    private Resources res;
    private Button bg_btn, birb_btn, setTube;
    private TypedArray bg_arr, birb_arr, tube_arr;
    private int bg_index, birb_index, tube_index;
    private ImageView play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            res = getResources();

            bg = (ConstraintLayout) findViewById(R.id.back);

            bg_index = tube_index = 0;
            tube_index = 0;

            bg_btn = (Button) findViewById(R.id.change_btn);
            birb_btn = (Button) findViewById(R.id.set_bird_color);
            setTube= (Button) findViewById(R.id.set_tube_color);

            bg_arr = res.obtainTypedArray(R.array.background);
            tube_arr = res.obtainTypedArray(R.array.tube_color);
            birb_arr = res.obtainTypedArray(R.array.bird_color);

            bg_btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    if (bg_index == (bg_arr.length() - 1))
                        bg_index = DAY;
                    else
                        bg_index++;

                    bg.setBackgroundResource(bg_arr.getResourceId(bg_index, -1));
                }
            });

            birb_btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    if (birb_index == (birb_arr.length() - 1))
                        birb_index = YELLOWBIRD;
                    else
                        birb_index++;
                    Log.d("@@@@_", String.valueOf(birb_index));
                    birb_btn.setBackgroundResource(birb_arr.getResourceId(birb_index, -1));
                }
            });

            setTube.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    if (tube_index == (tube_arr.length() - 1))
                        tube_index = GREENTUBE;
                    else
                        tube_index++;

                    setTube.setBackgroundResource(tube_arr.getResourceId(tube_index, -1));
                }
            });

            play_btn = (ImageView) findViewById(R.id.play_button);
            play_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int resourceData = birb_index * 100 + bg_index * 10 + tube_index;
                    Intent i = new Intent(MainActivity.this, StartGame.class);
                    i.putExtra("AssetResources", resourceData);
                    startActivity(i);
                }
            });
        }
        catch (Exception e){
            Log.d("Debug Log: ", e.toString());
        }

    }
}