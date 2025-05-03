package br.ifsp.rpg.model.actions;

import br.ifsp.rpg.interfaces.PlayerAction;
import br.ifsp.rpg.model.RpgCharacter;

public class AttackAction implements PlayerAction {
    @Override
    public void execute(RpgCharacter current, RpgCharacter opponent) {
        int d20 = current.rollHitDice();
        if (d20 >= opponent.getArmor()) {
            int damage = current.attack();
            opponent.defends(damage);
        } else {
            System.out.println(current.getName() + " missed the attack.");
        }
    }
}
