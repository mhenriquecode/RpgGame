package br.ifsp.web.model;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.actions.ChooseUserAction;
import lombok.Getter;

import java.util.Random;

@Getter
public class Combat {
    private final RpgCharacter player1;
    private final RpgCharacter player2;
    private final Random random;
    private RpgCharacter winner;

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
        RpgCharacter current = getFirstToPlay();
        RpgCharacter opponent = current == player1 ? player2 : player1;

        while (player1.getHealth() > 0 && player2.getHealth() > 0) {
            ChooseAction currentStrategy = new ChooseUserAction(1);

            Turn turn = new Turn(current, opponent, currentStrategy);
            turn.execute();

            if (opponent.getHealth() <= 0) {
                winner = current;
                break;
            }
            RpgCharacter temp = current;
            current = opponent;
            opponent = temp;
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
