package br.ifsp.web.model;

import br.ifsp.web.interfaces.ChooseAction;

import java.util.Random;
import java.util.UUID;

public class Combat {
    private final UUID id;
    private final RpgCharacter player1;
    private final RpgCharacter player2;
    private RpgCharacter winner;

    private ChooseAction actionStrategy1;
    private ChooseAction actionStrategy2;

    private RpgCharacter finalClone1;
    private RpgCharacter finalClone2;

    private Random random = new Random();

    public Combat(RpgCharacter player1, ChooseAction actionStrategy1,
                  RpgCharacter player2, ChooseAction actionStrategy2) {
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.actionStrategy1 = actionStrategy1;
        this.actionStrategy2 = actionStrategy2;
        this.random = new Random();
    }

    public void startCombat(RpgCharacter player1, ChooseAction strategy1, RpgCharacter player2, ChooseAction strategy2) {
        if (player1 == null) throw new NullPointerException("player1 cannot be null");
        if (player2 == null) throw new NullPointerException("player2 cannot be null");
        if (strategy1 == null) throw new NullPointerException("player1 action cannot be null");
        if (strategy2 == null) throw new NullPointerException("player2 action cannot be null");

        RpgCharacter clone1 = player1.cloneForCombat();
        RpgCharacter clone2 = player2.cloneForCombat();

        RpgCharacter combatWinner = executeCombat(clone1, strategy1, clone2, strategy2);
        RpgCharacter actualWinner = (combatWinner == clone1) ? player1 : player2;

        this.setFinalClone1(clone1);
        this.setFinalClone2(clone2);
        this.setWinner(actualWinner);
    }

    private RpgCharacter executeCombat(RpgCharacter player1, ChooseAction strategy1, RpgCharacter player2, ChooseAction strategy2) {
        RpgCharacter current = getFirstToPlay(player1, player2);
        RpgCharacter opponent = current == player1 ? player2 : player1;

        while (player1.getHealth() > 0 && player2.getHealth() > 0) {
            ChooseAction currentStrategy = (current == player1) ? strategy1 : strategy2;

            Turn turn = new Turn(current, opponent, currentStrategy);
            turn.execute();

            if (opponent.getHealth() <= 0) {
                return current;
            }

            RpgCharacter temp = current;
            current = opponent;
            opponent = temp;
        }

        return player1.getHealth() > 0 ? player1 : player2;
    }
    public void run() {
        startCombat(player1, actionStrategy1, player2, actionStrategy2);
    }

    public RpgCharacter getFirstToPlay(RpgCharacter player1, RpgCharacter player2) {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            return new Random().nextInt(2) == 0 ? player1 : player2;
        }
    }

    public RpgCharacter getFirstToPlay(RpgCharacter player1, RpgCharacter player2, Random random) {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            return random.nextInt(2) == 0 ? player1 : player2;
        }
    }

    public UUID getId() {
        return id;
    }

    public RpgCharacter getPlayer1() {
        return player1;
    }

    public RpgCharacter getPlayer2() {
        return player2;
    }

    public RpgCharacter getWinner() {
        return winner;
    }

    public void setWinner(RpgCharacter winner) {
        if(winner == null) throw new NullPointerException("winner cannot be null");
        this.winner = winner;
    }

    public RpgCharacter getFinalClone1() {
        return finalClone1;
    }
    public RpgCharacter getFinalClone2() {
        return finalClone2;
    }

    public void setFinalClone1(RpgCharacter finalClone1) {
        this.finalClone1 = finalClone1;
    }


    public void setFinalClone2(RpgCharacter finalClone2) {
        this.finalClone2 = finalClone2;
    }
}