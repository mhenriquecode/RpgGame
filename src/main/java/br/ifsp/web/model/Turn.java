package br.ifsp.web.model;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;

public class Turn {
    private final RpgCharacter current;
    private final RpgCharacter opponent;
    private final ChooseAction chooseAction;


    public Turn(RpgCharacter current, RpgCharacter opponent, ChooseAction chooseAction) {
        this.current = current;
        this.opponent = opponent;
        this.chooseAction = chooseAction;
    }

    public void execute() {
        current.onNewTurnStart();
        PlayerAction action = chooseAction.choose(current, opponent);
        action.execute(current, opponent);
    }
}
