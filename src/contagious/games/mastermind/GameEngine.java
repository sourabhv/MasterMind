package contagious.games.mastermind;

public class GameEngine {

	public static boolean soundStatus;
	public static int MAX_GUESSES = 10;
	public static int COMBOLENGTH = 4;

	public int guessCount;
	public int[] combination;

	public GameEngine() {
		guessCount = 0;
		combination = new int[4];
		for (int i = 0; i < 4; i++)
			combination[i] = (int)(Math.random() * Peg.PEGSCOUNT);
	}
}
