package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.SpecialEffect;

public class SpecialEffectDuelist implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        int extraDamage = character.rollAttackDice();
        return originalDamage + extraDamage;
    }
}
