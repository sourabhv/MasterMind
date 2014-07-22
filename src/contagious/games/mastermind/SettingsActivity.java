package contagious.games.mastermind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
	
	public void soundToggleClick(View view) {
		boolean soundOn = ((ToggleButton)view).isChecked();
//		GameEngine.soundSwitch(soundOn);
	}
	
	public void highscoreResetClick(View view) {
//		DataHandler.getInstance().deleteAll()
	}

}
