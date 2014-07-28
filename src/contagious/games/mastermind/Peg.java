package contagious.games.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Peg extends ImageView {

    public static int NULL = 0, BLUE = 1, GREEN = 2, RED = 3,
                      WHITE = 4, YELLOW = 5, PURPLE = 6;
    public static int[] PEGS = {R.drawable.emptypeg, R.drawable.bluepeg,
        R.drawable.greenpeg, R.drawable.redpeg, R.drawable.whitepeg,
        R.drawable.yellowpeg, R.drawable.purplepeg};
    private int drawableID;
    private boolean active;

    public Peg(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableID = NULL;
        active = true;
        setImageResource(PEGS[NULL]);
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int id) {
        drawableID = id;
        setImageResource(PEGS[drawableID]);
    }

    public boolean getState() {
        return active;
    }

    public void setState(boolean _active) {
        active = _active;
    }

}
