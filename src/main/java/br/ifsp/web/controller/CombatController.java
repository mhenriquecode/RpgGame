package br.ifsp.web.controller;

import br.ifsp.web.service.CombatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/combat")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Combat", description = "Combat Manager")
public class CombatController {
    private final CombatService combatService;

}
