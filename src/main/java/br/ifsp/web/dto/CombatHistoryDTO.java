package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;

import java.time.LocalDateTime;

public record CombatHistoryDTO(
        CharacterDTO player1,
        CharacterDTO player2,
        CharacterDTO winner,
        LocalDateTime timestamp
) {
    public static CombatHistoryDTO from(RpgCharacter p1, RpgCharacter p2, RpgCharacter winner, LocalDateTime timestamp) {
        return new CombatHistoryDTO(
                CharacterDTO.from(p1),
                CharacterDTO.from(p2),
                CharacterDTO.from(winner),
                timestamp
        );
    }
}

