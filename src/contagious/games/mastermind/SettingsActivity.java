package contagious.games.mastermind;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    private class DeleteHighscores extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            DataHandler dataHandler = DataHandler.getInstance(SettingsActivity.this);
            dataHandler.deleteAll();
            return Boolean.valueOf(true);
        }

        @Override
        protected void onPostExecute(Boolean deleted) {
            super.onPostExecute(deleted);
        }
    }


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        ImageButton view = (ImageButton) findViewById(R.id.settings_sound_toggle);
        view.setImageResource(R.drawable.ic_sound_on);
        view.setTag(R.drawable.ic_sound_on);
        GameEngine.soundStatus = true;
        
        Typeface josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        ((TextView) findViewById(R.id.settings_title)).setTypeface(josefinSans);
        ((Button) findViewById(R.id.settings_highscore_reset)).setTypeface(josefinSans);
	}

	public void soundToggleClick(View view) {		
		if(((Integer)((ImageButton) view).getTag()) == R.drawable.ic_sound_on) {
			((ImageButton) view).setImageResource(R.drawable.ic_sound_off);
			((ImageButton) view).setTag(R.drawable.ic_sound_off);
			GameEngine.soundStatus = false;
		}
		else {
			((ImageButton) view).setImageResource(R.drawable.ic_sound_on);
			((ImageButton) view).setTag(R.drawable.ic_sound_on);
			GameEngine.soundStatus = true;
		}
	}

	public void highscoreResetClick(View view) {
		new DeleteHighscores().execute();
		((Button) view).setText("Cleared");
		((Button) view).setClickable(false);
	}

}
