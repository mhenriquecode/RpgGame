package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.SpecialEffect;

public class SpecialEffectWarrior implements SpecialEffect {
    @Override
    public int applyEffect(RpgCharacter character, int originalDamage) {
        character.setDefense(character.getDefense() + originalDamage);
        return originalDamage;
    }
}
