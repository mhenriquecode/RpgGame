package br.ifsp.web.dto;

import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record CombatRequestDTO(
        @NotNull RpgCharacter player1,
        @NotBlank int strategy1,
        @NotNull RpgCharacter player2,
        @NotBlank int strategy2) {
}
