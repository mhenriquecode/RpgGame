package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.DiceRoll;

import java.util.Random;

public class RollAttackDice implements DiceRoll {
    private final Random random;
    private final Weapon weapon;

    public RollAttackDice(Weapon weapon) {
        this.random = new Random();
        this.weapon = weapon;
    }

    public RollAttackDice(Weapon weapon, Random random) {
        this.random = random;
        this.weapon = weapon;
    }

    @Override
    public int roll() {
        int totalDamage = 0;

        for (int i = 0; i < this.weapon.dice; i++){
            int result = random.nextInt(this.weapon.sides) + 1;
            totalDamage += result;
        }
        return totalDamage;
    }
}
