package contagious.games.mastermind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class SettingsActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ImageButton view = (ImageButton) findViewById(R.id.settings_sound_toggle);
        view.setImageResource(R.drawable.sound_on);
        view.setTag(R.drawable.sound_on);
        GameEngine.soundStatus = true;
	}

	public void soundToggleClick(View view) {		
		if(((Integer)((ImageButton) view).getTag()) == R.drawable.sound_on) {
			((ImageButton) view).setImageResource(R.drawable.sound_off);
			((ImageButton) view).setTag(R.drawable.sound_off);
			GameEngine.soundStatus = false;
		}
		else {
			((ImageButton) view).setImageResource(R.drawable.sound_on);
			((ImageButton) view).setTag(R.drawable.sound_on);
			GameEngine.soundStatus = true;
		}
	}
	
	public void highscoreResetClick(View view) {
		DataHandler.getInstance(this).deleteAll();
	}

}
