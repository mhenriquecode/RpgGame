package br.ifsp.web.model.dice;

import br.ifsp.web.interfaces.DiceRoll;

import java.util.Random;

public class RollHitDice implements DiceRoll {
    private final Random random;
    private int lastRoll;

    public RollHitDice() {
        this.random = new Random();
    }

    public RollHitDice(Random random) {
        this.random = random;
    }


    @Override
    public int roll() {
        lastRoll = random.nextInt(20) + 1;
        return lastRoll;
    }
    public int getLastRoll() {
        return lastRoll;
    }
}
