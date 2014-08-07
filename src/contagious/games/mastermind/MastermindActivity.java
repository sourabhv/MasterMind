package contagious.games.mastermind;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.RelativeLayout;

public class MastermindActivity extends Activity {

    Typeface josefinSans;
    Chronometer timer;
    GameEngine gameEngine;
    RelativeLayout hotbar;
    OnClickListener onHotbarClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mastermind);

        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        timer = ((Chronometer) findViewById(R.id.timer));
        hotbar = (RelativeLayout) findViewById(R.id.hotbar);
        gameEngine = new GameEngine();

        onHotbarClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Peg peg = (Peg) view;
                if (peg.backgroundID() == Peg.BASESELECTED)
                    peg.setBackgroundID(Peg.BASENORMAL);
                else {
                    for (int i = 0; i < hotbar.getChildCount(); i++)
                        ((Peg) hotbar.getChildAt(i)).setBackgroundID(Peg.BASENORMAL);
                    peg.setBackgroundID(Peg.BASESELECTED);
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
        timer.setText("Peg Clicked!");
    }

    public void onConfirmClick(View view) {
    }

}
