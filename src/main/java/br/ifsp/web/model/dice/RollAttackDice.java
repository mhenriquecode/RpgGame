package br.ifsp.web.model.dice;

import br.ifsp.web.interfaces.DiceRoll;
import br.ifsp.web.model.enums.Weapon;

import java.util.Random;

public class RollAttackDice implements DiceRoll {
    private final Random random;
    protected final Weapon weapon;
    private int lastRoll;

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

        for (int i = 0; i < this.weapon.getDice(); i++){
            int result = random.nextInt(this.weapon.getSides()) + 1;
            totalDamage += result;
        }
        this.lastRoll = totalDamage;
        return totalDamage;
    }
    public int getLastRoll() {
        return lastRoll;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
