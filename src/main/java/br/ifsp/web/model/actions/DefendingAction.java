package br.ifsp.web.model.actions;

import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.model.RpgCharacter;

public class DefendingAction implements PlayerAction {
    @Override
    public void execute(RpgCharacter current, RpgCharacter opponent) {
        current.setDefending(true);
    }
}