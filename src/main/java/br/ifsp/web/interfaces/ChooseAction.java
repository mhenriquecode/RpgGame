package br.ifsp.web.interfaces;

import br.ifsp.web.model.RpgCharacter;

public interface ChooseAction {
    PlayerAction choose(RpgCharacter current, RpgCharacter opponent);
}
