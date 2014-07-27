package contagious.games.mastermind;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;

public class MastermindActivity extends Activity {

    Typeface josefinSans;
    Chronometer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mastermind);
		
        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        timer = ((Chronometer) findViewById(R.id.timer));
        timer.setTypeface(josefinSans);
        makeHotbar();
		makePlayArea();
		timer.start();
	}

	private void makeHotbar() {
		
	}

	private void makePlayArea() {
		
	}
}
