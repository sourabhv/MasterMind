package contagious.games.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Peg extends ImageView {

	public static int PEGSCOUNT = 6;
	public static int BLUE = 0, GREEN = 1, RED = 2, WHITE = 3, YELLOW = 4, PURPLE = 5,
		BLUE_SELECTED = 6, GREEN_SELECTED = 7, RED_SELECTED = 8, WHITE_SELECTED = 9,
		YELLOW_SELECTED = 10, PURPLE_SELECTED = 11, NULL = 12;
	public static int[] PEGS = { R.drawable.peg_blue, R.drawable.peg_green, R.drawable.peg_red,
		R.drawable.peg_white, R.drawable.peg_yellow, R.drawable.peg_purple,
		R.drawable.peg_blue_selected, R.drawable.peg_green_selected, R.drawable.peg_red_selected,
		R.drawable.peg_white_selected, R.drawable.peg_yellow_selected, R.drawable.peg_purple_selected,
		R.drawable.peg_empty };

	private int drawableID;

    public Peg(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableID = NULL;
        setImageResource(PEGS[NULL]);
    }

    public int drawableID() {
        return drawableID;
    }

    public void setDrawableID(int id) {
        drawableID = id;
        setImageResource(PEGS[id]);
    }

}
