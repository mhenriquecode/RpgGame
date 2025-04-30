package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.DiceRoll;

import java.util.Random;

public class RollHitDice implements DiceRoll {
    private final Random random = new Random();

    @Override
    public int roll() {
        return random.nextInt(20) + 1;
    }
}
