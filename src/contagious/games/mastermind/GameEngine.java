package contagious.games.mastermind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

    public static final String PREFFILENAME = "contagious.games.mastermind.PREFERENCES";
    public static final String PREFSOUND = "contagious.games.mastermind.SOUND";
    public static final String ISHIGHSCORE = "contagious.games.mastermind.HIGHSCORE";
    public static final String TIME = "contagious.games.mastermind.TIME";
    public static final String GUESSES = "contagious.games.mastermind.GUESSES";
    public static final String WIN = "contagious.games.mastermind.WIN";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";
    public static final String NONE = "NONE";

    public static boolean soundStatus = true;
    public static final int MAX_GUESSES = 10;
    public static final int COMBOLENGTH = 4;

    public int guessCount;
    public int[] combination;

    public GameEngine() {
        guessCount = 0;
        combination = new int[4];
        for (int i = 0; i < COMBOLENGTH; i++)
            combination[i] = (int)(Math.random() * Peg.PEGSCOUNT);
    }

    public int[] getFlagCombo(int[] pegCombo) {
        int[] flagCombo = {Flag.NULL, Flag.NULL, Flag.NULL, Flag.NULL};
        int flagComboCounter = 0;
        boolean[] pegComboReady = {true, true, true, true};
        boolean[] combinationReady = {true, true, true, true};

        //check for white flags
        for (int i = 0; i < COMBOLENGTH; i++) {
            if (pegCombo[i] == combination[i]) {
                pegComboReady[i] = false;
                combinationReady[i] = false;
                flagCombo[flagComboCounter] = Flag.WHITE;
                flagComboCounter++;
            }
        }

        // check for red flags
        for (int i = 0; i < COMBOLENGTH; i++)
            if (pegComboReady[i])
                for (int j = 0; j < COMBOLENGTH; j++)
                    if (combinationReady[j] && pegCombo[i] == combination[j]) {
                        combinationReady[j] = false;
                        flagCombo[flagComboCounter] = Flag.RED;
                        flagComboCounter++;
                    }

        return flagCombo;
    }

    public boolean getWinStatus(int[] flagCombo) {
        for (int i = 0; i < COMBOLENGTH; i++)
            if (flagCombo[i] == Flag.RED || flagCombo[i] == Flag.NULL)
                return false;
        return true;
    }

    public boolean checkHighScore(List<Map<String, Object>> highscores, long timeElapsed, int guessesTaken) {
        if (highscores.size() < 10)
            return true;
        int i;
        for (i = 0; i < highscores.size(); i++) {
            HashMap<String, Object> score = (HashMap<String, Object>) highscores.get(i);
            int time = (Integer) score.get(DataHandler.Highscores.COLUMN_TIME);
            int guesses = (Integer) score.get(DataHandler.Highscores.COLUMN_GUESSES);
            if (timeElapsed < time || (timeElapsed == time && guessesTaken < guesses))
                break;
        }

        if (i == highscores.size())
            return false;
        return true;
    }

}
