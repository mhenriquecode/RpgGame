package br.ifsp.rpg.model;

import lombok.Getter;

import java.util.Random;

public class Combat {
    private final RpgCharacter player1;
    private final RpgCharacter player2;
    private final Random random;

    public Combat(RpgCharacter player1, RpgCharacter player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.random = new Random();
    }

    public Combat(RpgCharacter player1, RpgCharacter player2, Random random) {
        this.player1 = player1;
        this.player2 = player2;
        this.random = random;
    }

    public void start(){
        RpgCharacter faster = getFirstToPlay();
        RpgCharacter slower = faster == player1 ? player2 : player1;

        while(player1.getMaxHealth() > 0 && player2.getMaxHealth() > 0){
//            Turn turn = new Turn(faster, slower);

            if (slower.getMaxHealth() <= 0) {
                break;
            }

            RpgCharacter temp = faster;
            faster = slower;
            slower = temp;

        }
    }

    public RpgCharacter getFirstToPlay() {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            return random.nextInt(2) == 0 ? player1 : player2;
        }
    }

}
