package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;

import java.time.LocalDateTime;

public record CombatHistoryDTO(
        RpgCharacter player1,
        RpgCharacter player2,
        RpgCharacter winner,
        LocalDateTime timestamp
) {}

