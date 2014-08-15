package contagious.games.mastermind;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private class DeleteHighscores extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DataHandler dataHandler = DataHandler.getInstance(SettingsActivity.this);
            dataHandler.deleteAll();
            return null;
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

    public void highscoreResetClick(final View view) {
        new AlertDialog.Builder(this)
            .setTitle("Clear Highscores")
            .setMessage("Are you sure you want to clear highscores?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new DeleteHighscores().execute();
                    ((Button) view).setText("Cleared");
                    ((Button) view).setClickable(false);
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .show();
    }

}
