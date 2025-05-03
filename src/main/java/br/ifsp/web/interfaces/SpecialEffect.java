package br.ifsp.web.interfaces;

import br.ifsp.web.model.RpgCharacter;

public interface SpecialEffect {
    int applyEffect(RpgCharacter character, int OriginalDamage);
}
