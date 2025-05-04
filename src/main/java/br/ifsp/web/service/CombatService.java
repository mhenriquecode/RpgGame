package br.ifsp.web.service;

import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;

public class CombatService {
        public Combat startCombat(RpgCharacter player1, RpgCharacter player2) {
            if(player1 == null || player2 == null)
                throw new NullPointerException("player1 and player2 cannot be null");

            Combat combat = new Combat(player1, player2);
            combat.start();
            return combat;
        }
}
