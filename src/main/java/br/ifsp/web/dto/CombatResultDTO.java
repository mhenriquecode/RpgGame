package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


public record CombatResultDTO(UUID winnerId, String winnerName, List<TurnLogDTO> turnLogs) {
}
