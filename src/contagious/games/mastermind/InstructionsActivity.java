package contagious.games.mastermind;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class InstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        Typeface josefinSans = Typeface.createFromAsset(getAssets(), "fonts/JosefinSans-SemiBold.ttf");

        ((TextView) findViewById(R.id.instructions_title)).setTypeface(josefinSans);
        ((TextView) findViewById(R.id.ins1)).setTypeface(josefinSans);
        ((TextView) findViewById(R.id.ins2)).setTypeface(josefinSans);
        ((TextView) findViewById(R.id.ins3)).setTypeface(josefinSans);
    }
}
