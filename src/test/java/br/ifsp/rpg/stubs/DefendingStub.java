package br.ifsp.rpg.stubs;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.DefendingAction;

public class DefendingStub implements ChooseAction {
    @Override
    public PlayerAction choose(RpgCharacter atual, RpgCharacter oponente) {
        return new DefendingAction();
    }
}
