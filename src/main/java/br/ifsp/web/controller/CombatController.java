package br.ifsp.web.controller;

import br.ifsp.web.model.Combat;
import br.ifsp.web.service.CombatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/combat")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Combat", description = "Combat Manager")
public class CombatController {
    private final CombatService combatService;

    @Operation(summary = "Start a combat")
    @PostMapping()
    public ResponseEntity<Combat> startCombat(@Valid @RequestBody Combat combatDTO) {
        Combat startedCombat = combatService.startCombat(
                combatDTO.getPlayer1(),
                combatDTO.getActionStrategy1(),
                combatDTO.getPlayer2(),
                combatDTO.getActionStrategy2()
        );

        return ResponseEntity.ok(startedCombat);
    }

}
