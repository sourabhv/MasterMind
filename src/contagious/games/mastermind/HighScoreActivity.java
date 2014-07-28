package contagious.games.mastermind;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HighScoreActivity extends Activity {

    private class PrintHighscores extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            highscoreTable.removeAllViewsInLayout();
            addHeaders();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            DataHandler dataHandler = DataHandler.getInstance(HighScoreActivity.this);
            return dataHandler.selectAll();
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> highscores) {
            super.onPostExecute(highscores);
            printHighscores(highscores);
        }
    }

    TableLayout highscoreTable;
    TableRow tr;
    TextView td;
    Typeface josefinSans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        setContentView(R.layout.highscore);
        
        TextView highscoreTitle = (TextView) findViewById(R.id.highscore_title);
        highscoreTitle.setTypeface(josefinSans);

        highscoreTable = (TableLayout) findViewById(R.id.highscore_table);
        new PrintHighscores().execute();
    }

    private void addHeaders() {
        tr = new TableRow(this);
        highscoreTable.addView(tr);

        td = new TextView(this);
        td.setText("Rank"); td.setTextColor(Color.WHITE); td.setGravity(Gravity.CENTER);
        td.setTypeface(josefinSans); td.setTextSize(20); tr.addView(td);

        td = new TextView(this);
        td.setText("Name"); td.setTextColor(Color.WHITE); td.setTypeface(josefinSans);
        tr.addView(td); td.setTextSize(20);

        td = new TextView(this);
        td.setText("Time"); td.setTextColor(Color.WHITE); td.setGravity(Gravity.CENTER);
        td.setTypeface(josefinSans); td.setTextSize(20); tr.addView(td);

        td = new TextView(this);
        td.setText("Guesses"); td.setTextColor(Color.WHITE); td.setGravity(Gravity.CENTER);
        td.setTypeface(josefinSans); td.setTextSize(20); tr.addView(td);
    }

    private void printHighscores(List<Map<String, Object>> highscores) {
        int rankCount = 1;
        for (Map<String, Object> score : highscores) {
            if (rankCount > 10) break;
            tr = new TableRow(this);
            highscoreTable.addView(tr);

            td = new TextView(this);
            td.setText(Integer.toString(rankCount)); td.setTextColor(Color.WHITE);
            td.setGravity(Gravity.CENTER); td.setTypeface(josefinSans); td.setTextSize(20);
            tr.addView(td); rankCount++;

            td = new TextView(this);
            td.setText(score.get(DataHandler.Highscores.COLUMN_NAME).toString());
            td.setTextColor(Color.WHITE); td.setTypeface(josefinSans);
            td.setTextSize(20); tr.addView(td);

            td = new TextView(this);
            td.setText(getTimeString(Integer.parseInt(score.get(DataHandler.Highscores.COLUMN_TIME).toString())));
            td.setTextColor(Color.WHITE); td.setGravity(Gravity.CENTER);
            td.setTypeface(josefinSans); td.setTextSize(20); tr.addView(td);

            td = new TextView(this);
            td.setText(score.get(DataHandler.Highscores.COLUMN_GUESSES).toString());
            td.setTextColor(Color.WHITE); td.setGravity(Gravity.CENTER);
            td.setTypeface(josefinSans); td.setTextSize(20); tr.addView(td);
        }
    }

    private static String getTimeString(int time) {
        final int minutes = time / 60;
        final int seconds = time % 60;
        final String secs = (seconds < 10) ? ("0" + seconds) : Integer.toString(seconds);
        final String mins = (minutes < 10) ? ("0" + minutes) : Integer.toString(minutes);
        return mins + ":" + secs;
    }
}
