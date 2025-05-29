package br.ifsp.web.dto;

import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record CombatRequestDTO(
        @NotNull UUID player1,
        @NotNull
        @Min(1)
        @Max(3)
        Integer strategy1,
        @NotNull UUID player2,
        @NotNull
        @Min(1)
        @Max(3)
        Integer strategy2) {
}
