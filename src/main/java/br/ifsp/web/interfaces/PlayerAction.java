package br.ifsp.web.interfaces;

import br.ifsp.web.model.RpgCharacter;

public interface PlayerAction {
    void execute(RpgCharacter current, RpgCharacter opponent);
}