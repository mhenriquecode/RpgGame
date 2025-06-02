package br.ifsp.web.dto;

import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.RpgCharacter;

import java.time.LocalDateTime;
import java.util.UUID;

public record CombatHistoryDTO(
        UUID logId, // ID do registro do CombatLog

        UUID player1Id,
        String player1Name,
        String player1ClassType, // Enviado como String para o frontend
        String player1Race,      // Enviado como String para o frontend

        UUID player2Id,
        String player2Name,
        String player2ClassType,
        String player2Race,

        UUID winnerId,
        String winnerName,
        String winnerClassType,
        String winnerRace,

        LocalDateTime timestamp
) {
    public static CombatHistoryDTO from(CombatLog log) {
        return new CombatHistoryDTO(
                log.getId(),

                log.getPlayer1Id(),
                log.getPlayer1Name(),
                log.getPlayer1ClassType().toString(), // Converte Enum para String
                log.getPlayer1Race().toString(),    // Converte Enum para String

                log.getPlayer2Id(),
                log.getPlayer2Name(),
                log.getPlayer2ClassType().toString(),
                log.getPlayer2Race().toString(),

                log.getWinnerId(),
                log.getWinnerName(),
                log.getWinnerClassType().toString(),
                log.getWinnerRace().toString(),

                log.getTimestamp()
        );
    }
}

