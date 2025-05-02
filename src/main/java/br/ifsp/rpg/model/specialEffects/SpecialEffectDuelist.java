package br.ifsp.rpg.model.specialEffects;

import br.ifsp.rpg.interfaces.SpecialEffect;
import br.ifsp.rpg.model.RpgCharacter;

public class SpecialEffectDuelist implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        int extraDamage = character.rollAttackDice();
        return originalDamage + extraDamage;
    }
}
