package br.ifsp.rpg.model.actions;

import br.ifsp.rpg.interfaces.ChooseAction;
import br.ifsp.rpg.interfaces.PlayerAction;
import br.ifsp.rpg.model.RpgCharacter;

public class ChooseUserAction implements ChooseAction {
    private final int chosenAction;

    public ChooseUserAction(int chosenAction) {
        this.chosenAction = chosenAction;
    }

    @Override
    public PlayerAction choose(RpgCharacter current, RpgCharacter opponent) {
        return switch (chosenAction) {
            case 1 -> new AttackAction();
            case 2 -> new DefendingAction();
            case 3 -> new DodgeAction();
            default -> throw new IllegalArgumentException("Invalid action: " + chosenAction);
        };
    }
}
