package contagious.games.mastermind;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

    Typeface josefinSans;
    Chronometer timer;
    GameEngine gameEngine;
    RelativeLayout hotbar;
    LinearLayout playarea;
    List<Map<String, Object>> highscores;
    OnClickListener onHotbarClick;
    int elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mastermind);

        new GetHighscores().execute();
        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        timer = ((Chronometer) findViewById(R.id.timer));
        timer.setBase(SystemClock.elapsedRealtime());
        hotbar = (RelativeLayout) findViewById(R.id.hotbar);
        playarea = (LinearLayout) findViewById(R.id.playarea);
        gameEngine = new GameEngine();
        elapsedTime = -1;

        timer.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer arg0) {
				elapsedTime += 1;
			}
		});

        onHotbarClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
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

        timer.setTypeface(josefinSans);
        makeHotbar(this);
        timer.start();
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

    public void onPegClick(View view) {
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

        peg.setDrawableID(active_peg);
    }

    public void onConfirmClick(View view) {
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

        int[] flagCombo = gameEngine.getFlagCombo(pegCombo);
        boolean win = gameEngine.getWinStatus(flagCombo);

        if (win) {
            timer.stop();
            // final long timeElapsed = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
            final int guesses = gameEngine.guessCount + 1;

            boolean ishighscore = gameEngine.checkHighScore(highscores, elapsedTime, guesses);

            Intent intent = new Intent(this, EndGameActivity.class);
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
            if (gameEngine.guessCount < 9)
                gameEngine.guessCount++;
            else {
                Intent intent = new Intent(this, EndGameActivity.class);
                intent.putExtra(GameEngine.WIN, GameEngine.FALSE);
                startActivity(intent);
            }

            for (int i = 0; i < 4; i++) {
                Flag flag = (Flag) parent.getChildAt(i);
                flag.setDrawableID(flagCombo[i]);
            }

            confirm.setImageResource(R.drawable.confirmblank);
            RelativeLayout r = (RelativeLayout) playarea.getChildAt(9 - playlevel - 1);
            ImageView nextConfirm = (ImageView) r.getChildAt(8);
            nextConfirm.setImageResource(R.drawable.confirm);
        }
    }

}
