package contagious.games.mastermind;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    SoundPool soundPool;
    int mainClickID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);



        // wake lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // music stream and sound pool
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor mainClickDescriptor = assetManager.openFd("sounds/main_click.ogg");
            mainClickID = soundPool.load(mainClickDescriptor, 1);

        } catch (IOException e) {
            // do nothing
        }

        if (GameEngine.soundStatus){
            ImageButton view = (ImageButton) findViewById(R.id.settings_sound_toggle);
            view.setImageResource(R.drawable.ic_sound_on);
            view.setTag(R.drawable.ic_sound_on);
        } else {
            ImageButton view = (ImageButton) findViewById(R.id.settings_sound_toggle);
            view.setImageResource(R.drawable.ic_sound_off);
            view.setTag(R.drawable.ic_sound_off);
        }

        Typeface josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        ((TextView) findViewById(R.id.settings_title)).setTypeface(josefinSans);
        ((Button) findViewById(R.id.settings_highscore_reset)).setTypeface(josefinSans);
    }

    public void soundToggleClick(View view) {
        Integer tag = (Integer)((ImageButton) view).getTag();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(tag == R.drawable.ic_sound_on) {
            GameEngine.soundStatus = false;
            ((ImageButton) view).setImageResource(R.drawable.ic_sound_off);
            ((ImageButton) view).setTag(R.drawable.ic_sound_off);
            editor.putString(GameEngine.PREFSOUND, GameEngine.FALSE);        }
        else {
            GameEngine.soundStatus = true;
            play(mainClickID);
            ((ImageButton) view).setImageResource(R.drawable.ic_sound_on);
            ((ImageButton) view).setTag(R.drawable.ic_sound_on);
            editor.putString(GameEngine.PREFSOUND, GameEngine.TRUE);
        }

        editor.commit();
    }

    public void highscoreResetClick(final View view) {
        play(mainClickID);
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

    private void play(int id) {
        if (id != -1 && GameEngine.soundStatus)
            soundPool.play(id, 1.0f, 1.0f, 0, 0, 1);
    }
}
