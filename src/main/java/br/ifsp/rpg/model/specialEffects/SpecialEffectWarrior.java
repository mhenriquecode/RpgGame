package br.ifsp.rpg.model.specialEffects;

import br.ifsp.rpg.interfaces.SpecialEffect;
import br.ifsp.rpg.model.RpgCharacter;

public class SpecialEffectWarrior implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        character.setDefense(character.getDefense() + originalDamage);
        return originalDamage;
    }
}
