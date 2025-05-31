package br.ifsp.web.model.actions;

import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.model.RpgCharacter;

public class AttackAction implements PlayerAction {
    @Override
    public void execute(RpgCharacter current, RpgCharacter opponent) {
        int d20 = current.rollHitDice();
        if (d20 >= opponent.getArmor()) {
            int damage = current.attack();
            opponent.defends(damage);
        }
    }
}
