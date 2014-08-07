package contagious.games.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Peg extends ImageView {

    public static int PEGSCOUNT = 6;
    public static int NULL = -1, BLUE = 0, GREEN = 1, RED = 2, WHITE = 3,
            YELLOW = 4, PURPLE = 5, BASENORMAL = 0, BASESELECTED = 1;
    public static int[] PEGS = {R.drawable.bluepeg, R.drawable.greenpeg, R.drawable.redpeg,
            R.drawable.whitepeg, R.drawable.yellowpeg, R.drawable.purplepeg};
    public static int[] PEGSBASE = {R.drawable.basepeg, R.drawable.selectedbasepeg};

    private int drawableID;
    private int backgroundID;
    private boolean ready;

    public Peg(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableID = NULL;
        backgroundID = BASENORMAL;
        ready = true;
        setBackgroundResource(PEGSBASE[BASENORMAL]);
    }

    public int drawableID() {
        return drawableID;
    }

    public void setDrawableID(int id) {
        drawableID = id;
        if (id >= 0)
            setImageResource(PEGS[id]);
    }

    public int backgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(int id) {
        backgroundID = id;
        setBackgroundResource(PEGSBASE[backgroundID]);
    }

    public boolean getState() {
        return ready;
    }

    public void setState(boolean _ready) {
        ready = _ready;
    }

}
