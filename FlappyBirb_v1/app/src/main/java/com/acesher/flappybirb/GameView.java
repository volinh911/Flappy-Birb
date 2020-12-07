package com.acesher.flappybirb;

import android.content.Context;
import android.graphics.*;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import com.acesher.functionalities.GameStateManager;
import com.acesher.functionalities.ResourcesManager;

import java.util.Random;

import static com.acesher.functionalities.GameStateManager.*;

public class GameView extends SurfaceView { //The Game (that you lost)

    private final int UPDATE_MILLIS = 17;
    private final int gravity = 3;
    private final int tubeVelocity = 12;
    private SurfaceHolder holder;
    private DisplayMetrics dMetrics;
    private GameStateManager gameState;
    private boolean isGrounded = false; //Check if birb is on the ground (Base)
    private ResourcesManager resMng;

    //Score
    private Paint scorePaint;
    private int score;
    private int scoringTube;

    //SoundPlayer
    private int hitSound;
    private int overSound;
    private AudioAttributes audioAttributes;
    private SoundPool soundPool;

    //Background
    private Bitmap bg_d;

    //Base
    private Bitmap baseSprite;
    private Base base;
    private RectF baseHB;

    //BIRB
    private Birb birb;
    private Bitmap[] birbs;
    private RectF birbHB;

    //integer to keep birb image
    private int birbFrame;
    private int velocity = 0;

    //TUBE
    private Tubes tubes;
    private Bitmap topTube, bottomTube;
    private int minTubeOffset, maxTubeOffset;
    private int No_Tubes = 4;
    private int[] tubeX = new int[No_Tubes];
    private int[] topTubeY = new int[No_Tubes];
    private Random random;
    private RectF tubeTop_HB, tubeBot_HB;
    final private int hbScale = 15; //Reduces actual hitbox of Tubes for accuracy

    private void renderInit() {
        try {
            //Bg
            bg_d = BitmapFactory.decodeResource(getResources(), resMng.getBgResource());
            bg_d = Bitmap.createScaledBitmap(bg_d, dMetrics.widthPixels,dMetrics.heightPixels, false);

            //Score paint
            scorePaint.setColor(Color.WHITE);
            scorePaint.setTextSize(100);

            //Base
            baseSprite = BitmapFactory.decodeResource(getResources(), R.drawable.base);
            baseSprite = Bitmap.createScaledBitmap(baseSprite, dMetrics.widthPixels, (int) (baseSprite.getHeight() * base.getSize()), false);

            //Le Birb Sprite
            birbs = new Bitmap[3];
            for (int i = 0; i < 3; i++) {
                birbs[i] = BitmapFactory.decodeResource(getResources(), resMng.getBirbResource(i));
                birbs[i] = Bitmap.createScaledBitmap(birbs[i] , (int) (birbs[i] .getWidth() * birb.getSize()), (int) (birbs[i] .getHeight() * birb.getSize()),false);
            }

            //TUBES
            bottomTube = BitmapFactory.decodeResource(getResources(), resMng.getTubeResource(0));
            bottomTube = Bitmap.createScaledBitmap(bottomTube , (int) (bottomTube.getWidth() * tubes.getSize()), (int) (bottomTube .getHeight() * tubes.getSize()),false);
            topTube = BitmapFactory.decodeResource(getResources(), resMng.getTubeResource(1));
            topTube = Bitmap.createScaledBitmap(topTube , (int) (topTube.getWidth() * tubes.getSize()), (int) (topTube .getHeight() * tubes.getSize()),false);        }
        catch (Exception e) {
            Log.d("@@@@@@@@@@@@@@@", e.toString());
        }
    }
    private void hitBoxesInit() {
        birbHB = new RectF((float) birb.getPosX(), (float) birb.getPosY(), (float) birb.getPosX() + birbs[0].getWidth(), (float) birb.getPosY() + birbs[0].getHeight());
        baseHB = new RectF((float) base.getPos().getX(), (float) base.getPos().getY(), (float) base.getPos().getX() + baseSprite.getWidth(), (float) base.getPos().getY() + baseSprite.getHeight());
        tubeTop_HB = new RectF(0,0,0,0);
        tubeBot_HB = new RectF(0,0,0,0);
    }
    private void soundInit() {
        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(10).build();
    }
    private void init() {
        birb = new Birb(0, 0, 1.5f);
        base = new Base(0, 0, 1.5f);
        tubes = new Tubes(dMetrics, 350, 0.5f, 1.75f);

        //If just start game then initiates everything
        if (gameState.is(START)) {
            renderInit();
            soundInit();
        }
        else
            gameState.updateState(START);

        //Init base pos
        base.updatePos(0, dMetrics.heightPixels - (int) Math.floor(baseSprite.getHeight() * 0.8f));

        //Init birb position in the centre of the screen
        birb.updatePos(dMetrics.widthPixels/2 - birbs[0].getWidth()/2, dMetrics.heightPixels/2 - birbs[0].getHeight()/2);

        //Init Tubes variables stuff
        minTubeOffset = tubes.getGap() / 3;
        maxTubeOffset = dMetrics.heightPixels - minTubeOffset - tubes.getGap() - baseSprite.getHeight();

        random = new Random();
        for (int i = 0; i < No_Tubes; i++)
        {
            tubeX[i] = -i*tubes.getDist() - bottomTube.getWidth() * 2;
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }

        hitBoxesInit();

        //Set score
        score = scoringTube = 0;
    }

    public GameView(final Context context, DisplayMetrics dMetrics, ResourcesManager resMng) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);

        //Initiate
        scorePaint = new Paint();
        gameState = new GameStateManager();
        this.dMetrics = dMetrics;
        this.resMng = resMng;
        init();

        hitSound= soundPool.load(context,R.raw.flap,1);
        overSound = soundPool.load(context, R.raw.fail, 1);

        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

        });

        play();
    }

    public void play() {
        try {
            final CountDownTimer countDownTimer = new CountDownTimer(10000, UPDATE_MILLIS) {
                @Override
                public void onTick(long l) {
                    if (!gameState.is(LOSE))
                        update.run();
                    else {
                        onLoseEvent();
                        this.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    this.start();
                }
            };

            if (!gameState.is(LOSE))
                countDownTimer.start();
        }
        catch (Error e) {
            Log.d(":3", e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (!gameState.is(LOSE) && action == MotionEvent.ACTION_DOWN) {
            //move the birb upward by some units
            soundPool.play(hitSound, 1, 1, 1, 0, 1.5f);
            birbFrame = 0;
            velocity = -30;
            gameState.updateState(1);
            isGrounded = false;
        }

        if (gameState.is(LOSE)) {
            birbFrame = 0;
            init();
            play();
        }
        return true;
    }

    private Runnable update = new Runnable() { //updating sprites
        @Override
        public void run() {
            try {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null)
                    draw(canvas);
                //Display updated frame
                canvas.drawBitmap(bg_d, 0, 0, null);

                //Draw starting birb
                if (!gameState.is(PLAY))
                    canvas.drawBitmap(birbs[birbFrame], birb.getPosX(), birb.getPosY(), null);

                //Birb animation
                if (birbFrame == 2)
                    birbFrame = 0;
                else
                    birbFrame++;

                //The birb should be on the screen
                if (gameState.is(PLAY)) {

                    if (!isGrounded && (birb.getPosY()< dMetrics.heightPixels - birbs[0].getHeight() || velocity < 0)) {//birb does not go beyond the bottom edge of the screen
                        //when the birb is falling, it gets faster and faster as the velocity value increments by gravity each time
                        velocity += gravity;

                        //Set not to go over rooftop
                        if (velocity + birb.getPosY() > 0)
                            birb.updatePosY(velocity);
                        else {
                            birb.updatePosY(-birb.getPosY());
                            velocity += gravity;
                        }
                    }

                    //Check if birb and base intersected, if so set birbSprite right on top of base and turn off velocity
                    if (baseHB.intersect(birbHB)) {
                        isGrounded = true;
                        birb.updatePosY((int) -((birb.getPosY() + birbs[0].getHeight()) - base.getPos().getY()));
                    }

                    //Set birb sprite and hitbox
                    canvas.drawBitmap(birbs[birbFrame], birb.getPosX(), birb.getPosY(), null);
                    birbHB.set((float) birb.getPosX(),
                            (float) birb.getPosY(),
                            (float) birb.getPosX() + birbs[0].getWidth(),
                            (float) birb.getPosY() + birbs[0].getHeight());

                    for (int i = 0; i < No_Tubes; i++)
                    {
                        tubeX[i] += tubeVelocity;
                        if (tubeX[i] > dMetrics.widthPixels) {
                            tubeX[i] -= No_Tubes * tubes.getDist();
                            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                        }
                        //Draw tubes and set their hitboxes
                        canvas.drawBitmap(topTube,
                                tubeX[i],
                                topTubeY[i] - topTube.getHeight(), null);
                        canvas.drawBitmap(bottomTube,
                                tubeX[i],
                                topTubeY[i] + tubes.getGap(), null);
                        tubeTop_HB.set((float) tubeX[i] + hbScale,
                                (float) 0,
                                (float) tubeX[i] + topTube.getWidth() - hbScale,
                                (float) topTubeY[i] - hbScale);
                        tubeBot_HB.set((float) tubeX[i] + hbScale,
                                (float) topTubeY[i] + tubes.getGap() + hbScale,
                                (float) tubeX[i] + topTube.getWidth() - hbScale,
                                (float) dMetrics.heightPixels);

                        //Check if birb and tubes intersected
                        if (birbHB.intersect(tubeTop_HB) || birbHB.intersect(tubeBot_HB)) {
                            soundPool.play(overSound, 1, 1, 1, 0, 1);
                            gameState.updateState(LOSE);
                        }
                    }
                }

                //Base drawing
                canvas.drawBitmap(baseSprite, 0, base.getPos().getY(), null);

                //Check score and displaying it
                if (tubeX[scoringTube] > (dMetrics.widthPixels - birbs[0].getWidth()) / 2) {
                    score++;
                    if (scoringTube < No_Tubes - 1) {
                        scoringTube++;
                    }
                    else
                        scoringTube = 0;
                }
                canvas.drawText(" " + score, 100, 200, scorePaint);

                holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                Log.d("@@@@@@@@@@@@@@@__", e.getMessage());
            }

        }
    };

    public void onLoseEvent() {
        try {
            Canvas canvas = holder.lockCanvas();

            //Draw GameOvew screen
            Bitmap loseScreen = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
            canvas.drawBitmap(bg_d, 0, 0, null);
            canvas.drawBitmap(baseSprite, 0, base.getPos().getY(), null);
            canvas.drawBitmap(loseScreen, (dMetrics.widthPixels - loseScreen.getWidth()) / 2, (dMetrics.heightPixels / 2 - loseScreen.getHeight()) / 2, null);

            holder.unlockCanvasAndPost(canvas);

            postInvalidate();
        }
        catch (Exception e) {
            Log.d(":3", e.toString());
        }
    }
}
