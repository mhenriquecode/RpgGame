package br.ifsp.web.model;

import br.ifsp.web.dto.TurnLogDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.model.actions.AttackAction;

import java.util.Random;

public class Turn {
    private final RpgCharacter current;
    private final RpgCharacter opponent;
    private final ChooseAction chooseAction;


    public Turn(RpgCharacter current, RpgCharacter opponent, ChooseAction chooseAction) {
        this.current = current;
        this.opponent = opponent;
        this.chooseAction = chooseAction;
    }

    public void execute() {
        current.onNewTurnStart();
        PlayerAction action = chooseAction.choose(current, opponent);
        action.execute(current, opponent);
    }
    public TurnLogDTO getTurnLog() {
        StringBuilder log = new StringBuilder();
        log.append("Turno de ").append(current.getName()).append(" contra ").append(opponent.getName()).append(":\n");

        int hitRoll = current.getHitDice().getLastRoll();
        int hitThreshold = opponent.getArmor();

        if (hitRoll >= hitThreshold) {
            int damage = current.getAttackDice().getLastRoll();
            log.append("→ Acertou o ataque! Dano causado: ").append(damage).append("\n");

            // Verifica se o efeito especial foi aplicado
            if (current.wasLastSpecialEffectUsed()) {
                log.append("  ✦ Efeito especial ").append(current.getSpecialEffect().getClass().getSimpleName()).append(" ativado!\n");
            }


        } else {
            log.append("→ Errou o ataque (rolagem de acerto: ").append(hitRoll)
                    .append(", necessário: ").append(hitThreshold).append(")\n");
        }

        return new TurnLogDTO(
                log.toString()
        );
    }
}
