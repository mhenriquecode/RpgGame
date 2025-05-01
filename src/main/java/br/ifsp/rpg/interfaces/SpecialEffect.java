package br.ifsp.rpg.interfaces;

import br.ifsp.rpg.model.RpgCharacter;

public interface SpecialEffect {
    int applyEffect(RpgCharacter character, int OriginalDamage);
}
