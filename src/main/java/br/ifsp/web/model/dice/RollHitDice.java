package br.ifsp.web.model.dice;

import br.ifsp.web.interfaces.DiceRoll;

import java.util.Random;

public class RollHitDice implements DiceRoll {
    private final Random random;

    public RollHitDice() {
        this.random = new Random();
    }

    public RollHitDice(Random random) {
        this.random = random;
    }


    @Override
    public int roll() {
        return random.nextInt(20) + 1;
    }
}
