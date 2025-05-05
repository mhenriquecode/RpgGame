package br.ifsp.web.controller;

import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.dto.CombatResultDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.actions.AttackAction;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.service.CombatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping
    public ResponseEntity<CombatResultDTO> startCombat(@RequestBody CombatRequestDTO request) {
        ChooseAction strategy1 = new ChooseUserAction(request.strategy1());
        ChooseAction strategy2 = new ChooseUserAction(request.strategy2());

        Combat combat = combatService.startCombat(request.player1(), strategy1,
                request.player2(), strategy2);

        return ResponseEntity.ok(new CombatResultDTO(combat.getWinner()));
    }
}
