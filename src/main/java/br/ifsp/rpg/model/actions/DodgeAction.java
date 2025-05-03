package br.ifsp.rpg.model.actions;

import br.ifsp.rpg.interfaces.PlayerAction;
import br.ifsp.rpg.model.RpgCharacter;

public class DodgeAction implements PlayerAction {
    @Override
    public void execute(RpgCharacter current, RpgCharacter opponent) {
        current.dodge();
    }
}

