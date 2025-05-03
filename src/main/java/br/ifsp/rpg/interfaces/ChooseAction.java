package br.ifsp.rpg.interfaces;

import br.ifsp.rpg.model.RpgCharacter;

public interface ChooseAction {
    PlayerAction choose(RpgCharacter current, RpgCharacter opponent);
}
