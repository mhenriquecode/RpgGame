package br.ifsp.web.service;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;

public class CombatService {
        public Combat startCombat(RpgCharacter player1, RpgCharacter player2) {
            if(player1 == null || player2 == null)
                throw new NullPointerException("player1 and player2 cannot be null");

            ChooseAction neutralAction = new ChooseUserAction(1);
            return new Combat(player1, neutralAction, player2, neutralAction);
        }
}
