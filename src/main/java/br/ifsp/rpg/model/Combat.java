package br.ifsp.rpg.model;

import lombok.Getter;

import java.util.Random;

public class Combat {
    private final RpgCharacter player1;
    private final RpgCharacter player2;
    private final Random random = new Random();

    public Combat(RpgCharacter player1, RpgCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public RpgCharacter getFirstToPlay() {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            int d2 = rollD2();
            return d2 == 1 ? player1 : player2;
        }
    }

    public int rollD2() {
        return random.nextInt(2) + 1;
    }

}
