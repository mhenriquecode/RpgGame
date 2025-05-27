package br.ifsp.web.controller;

import br.ifsp.web.dto.CombatHistoryDTO;
import br.ifsp.web.dto.CombatRequestDTO;
import br.ifsp.web.dto.CombatResultDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.AttackAction;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.service.CharacterService;
import br.ifsp.web.service.CombatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/combat")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Combat", description = "Combat Manager")
public class CombatController {
    private final CombatService combatService;
    private final CharacterService characterService;
    @Operation(summary = "Start a combat")
    @PostMapping
    public ResponseEntity<CombatResultDTO> startCombat(@RequestBody CombatRequestDTO request) {
        RpgCharacter player1 = characterService.getCharacter(request.player1())
                .orElseThrow(() -> new IllegalArgumentException("Player 1 not found"));
        RpgCharacter player2 = characterService.getCharacter(request.player2())
                .orElseThrow(() -> new IllegalArgumentException("Player 2 not found"));


        ChooseAction strategy1 = new ChooseUserAction(request.strategy1());
        ChooseAction strategy2 = new ChooseUserAction(request.strategy2());



        Combat combat = new Combat(player1, strategy1, player2, strategy2);
        combat.startCombat(player1, strategy1, player2, strategy2);

        return ResponseEntity.ok(new CombatResultDTO(combat.getWinner()));
    }

    @GetMapping("/history")
    @Operation(summary = "Get all past combats")
    public ResponseEntity<List<CombatHistoryDTO>> getCombatHistory() {
        List<CombatHistoryDTO> history = combatService.getCombatHistory();
        return ResponseEntity.ok(history);
    }
}
