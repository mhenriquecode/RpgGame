package br.ifsp.web.service;

import br.ifsp.web.dto.CombatHistoryDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.Turn;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.repository.CombatLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class CombatService {
    private final CombatLogRepository combatLogRepository;

    public Combat startCombat(RpgCharacter player1, ChooseAction strategy1, RpgCharacter player2, ChooseAction strategy2) {
        if (player1 == null) {throw new NullPointerException("player1 cannot be null");}
        if (player2 == null) {throw new NullPointerException("player2 cannot be null");}
        if (strategy1 == null) {throw new NullPointerException("player1 action cannot be null");}
        if (strategy2 == null) {throw new NullPointerException("player2 action cannot be null");}
        Combat combat = new Combat(player1, strategy1, player2, strategy2);
        RpgCharacter winner = executeCombat(player1, strategy1, player2, strategy2);
        combat.setWinner(winner);

        combat.setTimestamp(LocalDateTime.now());
        saveCombatLog(combat);

        return combat;
    }

    private RpgCharacter executeCombat(RpgCharacter player1, ChooseAction strategy1, RpgCharacter player2, ChooseAction strategy2) {
        RpgCharacter current = getFirstToPlay(player1, player2);
        RpgCharacter opponent = current == player1 ? player2 : player1;

        while (player1.getHealth() > 0 && player2.getHealth() > 0) {
            ChooseAction currentStrategy = (current == player1) ? strategy1 : strategy2;

            Turn turn = new Turn(current, opponent, currentStrategy);
            turn.execute();

            if (opponent.getHealth() <= 0) {
                return current;
            }

            RpgCharacter temp = current;
            current = opponent;
            opponent = temp;
        }

        return player1.getHealth() > 0 ? player1 : player2;
    }

    private void saveCombatLog(Combat combat) {
        CombatLog log = new CombatLog(
                combat.getPlayer1(),
                combat.getPlayer2(),
                combat.getWinner()
        );
        log.setTimestamp(combat.getTimestamp());
        combatLogRepository.save(log);
    }


    public List<CombatHistoryDTO> getCombatHistory() {
        return combatLogRepository.findAll().stream()
                .map(log -> new CombatHistoryDTO(
                        log.getPlayer1(),
                        log.getPlayer2(),
                        log.getWinner(),
                        log.getTimestamp()
                ))
                .toList();
    }

    public RpgCharacter getFirstToPlay(RpgCharacter player1, RpgCharacter player2) {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            return new Random().nextInt(2) == 0 ? player1 : player2;
        }
    }


    public RpgCharacter getFirstToPlay(RpgCharacter player1, RpgCharacter player2, Random random) {
        if (player1.getSpeed() > player2.getSpeed()) {
            return player1;
        } else if (player2.getSpeed() > player1.getSpeed()) {
            return player2;
        } else {
            return random.nextInt(2) == 0 ? player1 : player2;
        }
    }
}
