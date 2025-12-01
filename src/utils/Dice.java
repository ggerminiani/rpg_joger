package utils;
import java.util.Random;

public class Dice {
    private static final Random random = new Random();
    public static int roll(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}