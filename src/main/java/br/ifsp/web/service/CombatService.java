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

    private void saveCombatLog(Combat combat) {
        CombatLog log = new CombatLog(
                combat.getPlayer1(),
                combat.getPlayer2(),
                combat.getWinner()
        );
        log.setTimestamp(LocalDateTime.now());
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
}
