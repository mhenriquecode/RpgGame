package br.ifsp.web.service;

import br.ifsp.web.dto.CombatHistoryDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.repository.CombatLogRepository;
import br.ifsp.web.repository.RpgCharacterEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CombatService {
    private final CombatLogRepository combatLogRepository;
    private final CharacterRepository characterRepository;
    public void saveCombatLog(Combat combat) {
        UUID player1Id = combat.getPlayer1().getId();
        UUID player2Id = combat.getPlayer2().getId();
        UUID winnerId = combat.getWinner().getId();

        RpgCharacterEntity player1Entity = characterRepository.findById(player1Id).orElseThrow();
        RpgCharacterEntity player2Entity = characterRepository.findById(player2Id).orElseThrow();
        RpgCharacterEntity winnerEntity = characterRepository.findById(winnerId).orElseThrow();

        CombatLog log = new CombatLog(player1Entity, player2Entity, winnerEntity);
        combatLogRepository.save(log);
    }

    public List<CombatHistoryDTO> getCombatHistory() {
        return combatLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")).stream()
                .map(CombatHistoryDTO::from)
                .toList();
    }
}
