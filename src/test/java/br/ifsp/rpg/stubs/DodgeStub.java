package br.ifsp.rpg.stubs;

import br.ifsp.rpg.interfaces.ChooseAction;
import br.ifsp.rpg.interfaces.PlayerAction;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.actions.DefendingAction;
import br.ifsp.rpg.model.actions.DodgeAction;

public class DodgeStub implements ChooseAction {
    @Override
    public PlayerAction choose(RpgCharacter atual, RpgCharacter oponente) {
        return new DodgeAction();
    }
}
