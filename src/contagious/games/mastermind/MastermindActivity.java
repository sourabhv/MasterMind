package contagious.games.mastermind;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MastermindActivity extends Activity {

    private class GetHighscores extends AsyncTask<Void, Void, Void> {
        List<Map<String, Object>> list;
        @Override
        protected Void doInBackground(Void... params) {
            DataHandler dataHandler = DataHandler.getInstance(MastermindActivity.this);
            list = dataHandler.selectAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            highscores = list;
        }
    }

    private AdView adView;

    Typeface josefinSans;
    Chronometer timer;
    GameEngine gameEngine;
    RelativeLayout hotbar;
    LinearLayout playarea;
    List<Map<String, Object>> highscores;
    OnClickListener onHotbarClick;
    OnClickListener onPegClick;
    OnClickListener onConfirmClick;
    int elapsedTime;

    SoundPool soundPool;
    int mainClickID = -1;
    int pegClickID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mastermind);

        adView = (AdView) findViewById(R.id.mm_adView);
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .build();
        adView.loadAd(adRequest);

        new GetHighscores().execute();
        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        timer = ((Chronometer) findViewById(R.id.timer));
        timer.setBase(SystemClock.elapsedRealtime());
        hotbar = (RelativeLayout) findViewById(R.id.hotbar);
        playarea = (LinearLayout) findViewById(R.id.playarea);
        gameEngine = new GameEngine();
        elapsedTime = -1;

        // wake lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // music stream and sound pool
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor mainClickDescriptor = assetManager.openFd("sounds/main_click.ogg");
            AssetFileDescriptor pegClickDescriptor = assetManager.openFd("sounds/peg_click.ogg");
            mainClickID = soundPool.load(mainClickDescriptor, 1);
            pegClickID = soundPool.load(pegClickDescriptor, 1);

        } catch (IOException e) {
            // do nothing
        }

        /* --- listeners --- */

        timer.setOnChronometerTickListener(new OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                elapsedTime += 1;
            }
        });

        onHotbarClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                // play sound
                play(mainClickID);

                Peg peg = (Peg) view;
                if (peg.drawableID() >= Peg.PEGSCOUNT)
                    peg.setDrawableID(peg.drawableID() - Peg.PEGSCOUNT);
                else {
                    int len = hotbar.getChildCount();
                    for (int i = 0; i < len; i++) {
                        Peg cPeg = (Peg) hotbar.getChildAt(i);
                        if (cPeg.drawableID() >= Peg.PEGSCOUNT)
                            cPeg.setDrawableID(cPeg.drawableID() - Peg.PEGSCOUNT);
                    }

                    peg.setDrawableID(peg.drawableID() + Peg.PEGSCOUNT);
                }
            }
        };

        onPegClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Peg peg = (Peg) view;
                int active_peg = Peg.NULL, len = hotbar.getChildCount();
                int playlevel = Integer.parseInt(peg.getTag().toString().substring(8, 9));
                if (playlevel != gameEngine.guessCount)
                    return;

                for (int i = 0; i < len; i++) {
                    Peg cPeg = (Peg) hotbar.getChildAt(i);
                    if (cPeg.drawableID() >= Peg.PEGSCOUNT) {
                        active_peg = cPeg.drawableID();
                        break;
                    }
                }

                if (active_peg != Peg.NULL)
                    active_peg -= Peg.PEGSCOUNT;

                if (active_peg != peg.drawableID())
                    // play sound
                    play(pegClickID);

                peg.setDrawableID(active_peg);

            }
        };

        onConfirmClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView confirm = (ImageView) view;
                int playlevel = Integer.parseInt(confirm.getTag().toString().substring(8, 9));
                int[] pegCombo = new int[4];
                if (playlevel != gameEngine.guessCount)
                    return;

                RelativeLayout parent = (RelativeLayout) confirm.getParent();

                for (int i = 4; i < 8; i++) {
                    Peg peg = (Peg) parent.getChildAt(i);
                    pegCombo[i-4] = peg.drawableID();
                    if (peg.drawableID() == Peg.NULL)
                        return;
                }

                // play sound
                play(mainClickID);

                int[] flagCombo = gameEngine.getFlagCombo(pegCombo);
                boolean win = gameEngine.getWinStatus(flagCombo);

                if (win) {
                    timer.stop();
                    final int guesses = gameEngine.guessCount + 1;

                    boolean ishighscore = gameEngine.checkHighScore(highscores, elapsedTime, guesses);

                    Intent intent = new Intent(MastermindActivity.this, EndGameActivity.class);
                    intent.putExtra(GameEngine.WIN, GameEngine.TRUE);

                    if (ishighscore) {
                        intent.putExtra(GameEngine.ISHIGHSCORE, GameEngine.TRUE);
                        intent.putExtra(GameEngine.TIME, elapsedTime);
                        intent.putExtra(GameEngine.GUESSES, guesses);
                    } else {
                        intent.putExtra(GameEngine.ISHIGHSCORE, GameEngine.FALSE);
                    }

                    startActivity(intent);

                } else {
                    if (gameEngine.guessCount < 9) {
                        gameEngine.guessCount++;

                        for (int i = 0; i < 4; i++) {
                            Flag flag = (Flag) parent.getChildAt(i);
                            flag.setDrawableID(flagCombo[i]);
                        }

                        confirm.setImageResource(R.drawable.confirmblank);
                        RelativeLayout r = (RelativeLayout) playarea.getChildAt(9 - playlevel - 1);
                        ImageView nextConfirm = (ImageView) r.getChildAt(8);
                        nextConfirm.setImageResource(R.drawable.confirm);
                    }
                    else {
                        Intent intent = new Intent(MastermindActivity.this, EndGameActivity.class);
                        intent.putExtra(GameEngine.WIN, GameEngine.FALSE);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

        timer.setTypeface(josefinSans);
        makePlayarea(this);
        makeHotbar(this);
        timer.start();
    }

    private void makePlayarea(Context context) {
        for (int tagID = 9, ID = 2000; tagID >= 0; tagID--) {
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setPadding(0, 1, 0, 1);

            for (int i = 0; i < 4; i++) {
                RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                Flag flag = new Flag(context, null);
                flag.setId(ID);
                flag.setTag("playarea" + tagID + "_flag" + i);
                if (i == 1)
                    rParams.addRule(RelativeLayout.RIGHT_OF, ID - 1);
                else if (i == 2)
                    rParams.addRule(RelativeLayout.BELOW, ID - 2);
                else if (i == 3) {
                    rParams.addRule(RelativeLayout.RIGHT_OF, ID - 1);
                    rParams.addRule(RelativeLayout.BELOW, ID - 2);
                }
                relativeLayout.addView(flag, rParams);
                ID++;
            }

            for (int i = 0; i < 4; i++) {
                RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                Peg peg = new Peg(context, null);
                peg.setId(ID);
                peg.setTag("playarea" + tagID + "_peg" + i);
                peg.setOnClickListener(onPegClick);
                if (i == 0)
                    rParams.addRule(RelativeLayout.RIGHT_OF, ID - 3);
                else
                    rParams.addRule(RelativeLayout.RIGHT_OF, ID - 1);
                relativeLayout.addView(peg, rParams);
                ID++;
            }

            RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.RIGHT_OF, ID - 1);
            ImageView confirm = new ImageView(context);
            confirm.setId(ID);
            confirm.setTag("playarea" + tagID + "confirm" + tagID);
            confirm.setOnClickListener(onConfirmClick);
            if (tagID == 0)
                confirm.setImageResource(R.drawable.confirm);
            else
                confirm.setImageResource(R.drawable.confirmblank);
            confirm.setContentDescription(getString(R.string.confirm));
            relativeLayout.addView(confirm, rParams);

            ID++;
            playarea.addView(relativeLayout, lParams);
        }
    }

    private void makeHotbar(Context context) {
        RelativeLayout.LayoutParams params;
        int pegIds[] = {Peg.BLUE, Peg.GREEN, Peg.RED, Peg.WHITE, Peg.YELLOW, Peg.PURPLE};
        Peg peg;
        final int IDLB = 1000;

        for (int i = 0; i < pegIds.length; i++) {
            peg = new Peg(context, null);
            peg.setId(i + IDLB);
            peg.setTag("hotbar_" + i);
            peg.setDrawableID(pegIds[i]);
            peg.setOnClickListener(onHotbarClick);
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0)
                params.addRule(RelativeLayout.RIGHT_OF, (i + IDLB) - 1);
            hotbar.addView(peg, params);
        }
    }

    private void play(int id) {
        if (id != -1 && GameEngine.soundStatus)
            soundPool.play(id, 1.0f, 1.0f, 0, 0, 1);
    }
}
