package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CombatResultDTO(@NotNull RpgCharacter winner) {
}
