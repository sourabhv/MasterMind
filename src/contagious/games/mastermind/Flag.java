package contagious.games.mastermind;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Flag extends ImageView {

    // RED flag means color is correct, position is not
    // WHITE flag means that both position and color are correct.
    public static int NULL = 0, RED = 1, WHITE = 2;
    public static int FLAGS[] = {R.drawable.emptyflag, R.drawable.redflag, R.drawable.whiteflag};
    private int drawableID; // NULL or RED or WHITE

    public Flag(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawableID = NULL;
        setImageResource(FLAGS[NULL]);
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int id) {
        drawableID = id;
        setImageResource(FLAGS[drawableID]);
    }

}
