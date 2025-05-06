package br.ifsp.web.model.specialEffects;

import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.RpgCharacter;

public class SpecialEffectDuelist implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        int extraDamage = character.rollAttackDice();
        return originalDamage + extraDamage;
    }
}
