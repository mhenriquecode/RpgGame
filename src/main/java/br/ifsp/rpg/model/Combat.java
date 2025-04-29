package br.ifsp.rpg.model;

import java.util.Random;

public class Combat {
    private final RpgCharacter player1;
    private final RpgCharacter player2;

    public Combat(RpgCharacter player1, RpgCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public RpgCharacter getFirstToPlay() {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        }
        return player2;
    }

}
