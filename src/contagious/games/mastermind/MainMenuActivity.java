package contagious.games.mastermind;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenuActivity extends Activity {

    SoundPool soundPool;
    int mainClickID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        SharedPreferences sharedPref = getSharedPreferences(GameEngine.PREFFILENAME, Context.MODE_PRIVATE);
        String soundPref = sharedPref.getString(GameEngine.PREFSOUND, GameEngine.NONE);

        if (soundPref.equals(GameEngine.NONE)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(GameEngine.PREFSOUND, GameEngine.TRUE);
            GameEngine.soundStatus = true;
            editor.commit();
        }
        else if (soundPref.equals(GameEngine.TRUE))
            GameEngine.soundStatus = true;
        else if (soundPref.equals(GameEngine.FALSE))
            GameEngine.soundStatus = false;

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
    }

    public void onPlayClick(View view) {
        if (mainClickID != -1 && GameEngine.soundStatus)
            soundPool.play(mainClickID, 1.0f, 1.0f, 0, 0, 1);
        startActivity(new Intent(this, MastermindActivity.class));
    }

    public void onInstnClick(View view) {
        if (mainClickID != -1 && GameEngine.soundStatus)
            soundPool.play(mainClickID, 1.0f, 1.0f, 0, 0, 1);
        startActivity(new Intent(this, InstructionsActivity.class));
    }

    public void onHighScoreClick(View view) {
        if (mainClickID != -1 && GameEngine.soundStatus)
            soundPool.play(mainClickID, 1.0f, 1.0f, 0, 0, 1);
        startActivity(new Intent(this, HighScoreActivity.class));
    }

    public void onSettingsClick(View view) {
        if (mainClickID != -1 && GameEngine.soundStatus)
            soundPool.play(mainClickID, 1.0f, 1.0f, 0, 0, 1);
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onExitClick(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
