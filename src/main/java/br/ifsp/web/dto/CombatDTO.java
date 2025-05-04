package br.ifsp.web.dto;

import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CombatDTO(
        @NotNull CharacterDTO player1,
        @NotNull CharacterDTO player2,
        CharacterDTO winner
) {
    public static CombatDTO from(Combat combat) {
        return new CombatDTO(
                CharacterDTO.from(combat.getPlayer1()),
                CharacterDTO.from(combat.getPlayer2()),
                CharacterDTO.from(combat.getWinner())
        );
    }
}
