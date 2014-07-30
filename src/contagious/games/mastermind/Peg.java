package contagious.games.mastermind;

import android.content.Context;
import android.widget.ImageView;

public class Peg extends ImageView {

    public static int NULL = -1, BLUE = 0, GREEN = 1, RED = 2, WHITE = 3,
            YELLOW = 4, PURPLE = 5, BASENORMAL = 0, BASESELECTED = 1;
    public static int[] PEGS = {R.drawable.bluepeg, R.drawable.greenpeg, R.drawable.redpeg,
            R.drawable.whitepeg, R.drawable.yellowpeg, R.drawable.purplepeg};
    public static int[] PEGSBASE = {R.drawable.basepeg, R.drawable.selectedbasepeg};

    private int drawableID;
    private boolean active;

    public Peg(Context context, int base, int peg) {
        super(context);
        drawableID = peg;
        active = true;
        setBackgroundResource(PEGSBASE[base]);
        if (peg >= 0)
            setImageResource(PEGS[peg]);
    }

    public Peg(Context context) {
        this(context, BASENORMAL, NULL);
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int id) {
        drawableID = id;
        if (id >= 0)
            setImageResource(PEGS[id]);
    }

    public void setBackground(int id) {
        setBackgroundResource(id);
    }

    public boolean getState() {
        return active;
    }

    public void setState(boolean _active) {
        active = _active;
    }

}
