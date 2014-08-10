package contagious.games.mastermind;

public class GameEngine {

    public static boolean soundStatus;
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
                    if (combinationReady[j]) {
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

}
