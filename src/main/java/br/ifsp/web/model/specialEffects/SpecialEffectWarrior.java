package br.ifsp.web.model.specialEffects;

import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.RpgCharacter;

public class SpecialEffectWarrior implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        character.setDefense(character.getDefense() + originalDamage);
        return originalDamage;
    }
}
