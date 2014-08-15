package contagious.games.mastermind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EndGameActivity extends Activity {

    private class AddHighscore extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DataHandler dataHandler = DataHandler.getInstance(EndGameActivity.this);
            List<Map<String, Object>> highscores = dataHandler.selectAll();
            dataHandler.deleteAll();

            String pName = pMap.get(DataHandler.Highscores.COLUMN_NAME).toString();
            int pTime = Integer.parseInt(pMap.get(DataHandler.Highscores.COLUMN_TIME).toString());
            int pGuesses = Integer.parseInt(pMap.get(DataHandler.Highscores.COLUMN_GUESSES).toString());
            boolean playerAdded = false;

            for(int i = 0, count = 0; i < highscores.size() && count < 10; ) {
                Map<String, Object> map = (HashMap<String, Object>) highscores.get(i);
                String name = map.get(DataHandler.Highscores.COLUMN_NAME).toString();
                int time = Integer.parseInt(map.get(DataHandler.Highscores.COLUMN_TIME).toString());
                int guesses = Integer.parseInt(map.get(DataHandler.Highscores.COLUMN_GUESSES).toString());

                if ((pTime < time || (pTime == time && pGuesses < guesses)) && !playerAdded) {
                    playerAdded = true;
                    dataHandler.insert(pName, pTime, pGuesses);
                    count++;
                } else {
                    dataHandler.insert(name, time, guesses);
                    i++;
                    count++;
                }
            }

            if (!playerAdded) {
                dataHandler.insert(pName, pTime, pGuesses);
            }

            return null;
        }
    }

    Typeface josefinSans;
    TextView message;
    TextView submessage;
    EditText name;
    Button submit;
    Button back;
    Map<String, Object> pMap;
    int time;
    int guesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame);

        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        message = (TextView) findViewById(R.id.endgame_message);
        submessage = (TextView) findViewById(R.id.endgame_submessage);
        name = (EditText) findViewById(R.id.endgame_name);
        submit = (Button) findViewById(R.id.endgame_submit);
        back = (Button) findViewById(R.id.endgame_back);

        message.setTypeface(josefinSans);
        submessage.setTypeface(josefinSans);
        name.setTypeface(josefinSans);
        submit.setTypeface(josefinSans);
        back.setTypeface(josefinSans);

        Intent intent = getIntent();
        String win = intent.getStringExtra(GameEngine.WIN);

        if (win.equals(GameEngine.TRUE)) {
            message.setText(R.string.youwin);
            String ishighscore = intent.getStringExtra(GameEngine.ISHIGHSCORE);
            if (ishighscore.equals(GameEngine.TRUE)) {
                submessage.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                time = intent.getIntExtra(GameEngine.TIME, 0);
                guesses = intent.getIntExtra(GameEngine.GUESSES, 0);
            } else {
                back.setVisibility(View.VISIBLE);
            }
        } else {
            message.setText(R.string.youlose);
            back.setVisibility(View.VISIBLE);
        }
    }

    public void onSubmit(View view) {
        if (name.length() > 0) {
            pMap = new HashMap<String, Object>();
            pMap.put(DataHandler.Highscores.COLUMN_NAME, name.getText().toString());
            pMap.put(DataHandler.Highscores.COLUMN_TIME, time);
            pMap.put(DataHandler.Highscores.COLUMN_GUESSES, guesses);
            (new AddHighscore()).execute();
            finish();
        }
    }

    public void onBack(View view) {
        finish();
    }

}
