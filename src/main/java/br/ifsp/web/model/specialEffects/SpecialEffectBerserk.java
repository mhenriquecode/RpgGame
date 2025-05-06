package br.ifsp.web.model.specialEffects;

import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.RpgCharacter;

public class SpecialEffectBerserk implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        return originalDamage * 2;
    }
}
