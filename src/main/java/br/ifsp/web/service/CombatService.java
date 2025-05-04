package br.ifsp.web.service;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CombatService {
        public Combat startCombat(RpgCharacter player1, ChooseAction strategy1, RpgCharacter player2, ChooseAction strategy2) {
            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.start();
            return combat;
        }
}
