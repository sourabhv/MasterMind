package contagious.games.mastermind;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MastermindActivity extends Activity {

    Typeface josefinSans;
    Chronometer timer;
    ScrollView playArea;
    LinearLayout hotbar;
    LinearLayout.LayoutParams wrapWidthHeightLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mastermind);

        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        timer = ((Chronometer) findViewById(R.id.timer));
        hotbar = (LinearLayout) findViewById(R.id.hotbar);
        playArea = (ScrollView) findViewById(R.id.playarea);
        wrapWidthHeightLinear = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        timer.setTypeface(josefinSans);
        makePlayArea(this);
        makeHotbar(this);
        timer.start();
	}

	private void makeHotbar(Context context) {

	}

	private void makePlayArea(Context context) {

	}

}
