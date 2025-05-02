package br.ifsp.rpg.model.specialEffects;

import br.ifsp.rpg.interfaces.SpecialEffect;
import br.ifsp.rpg.model.RpgCharacter;

public class SpecialEffectPaladin implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        character.setHealth(character.getHealth() + originalDamage);
        return originalDamage;
    }
}
