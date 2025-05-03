package br.ifsp.rpg.interfaces;

import br.ifsp.rpg.model.RpgCharacter;

public interface PlayerAction {
    void execute(RpgCharacter current, RpgCharacter opponent);
}